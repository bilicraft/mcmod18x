package ruby.bamboo.command;

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ATCommand extends CommandBase {

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandName() {
        return "autotorch";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "auto building torch from args chunks";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            sender.addChatMessage(new TextComponentString("args1:chunk range number. args2: thread num"));
            return;
        }
        if (!(sender.getCommandSenderEntity() instanceof EntityPlayer)) {
            sender.addChatMessage(new TextComponentString("this command is EntityPlayer and CreativeMode only"));
            return;
        }
        if (!((EntityPlayer) sender.getCommandSenderEntity()).capabilities.isCreativeMode) {
            sender.addChatMessage(new TextComponentString("this command is CreativeMode only"));
            return;
        }
        MinecraftForge.EVENT_BUS.register(new ATCommand.AutoTorchScan(sender, Integer.parseInt(args[0]), Integer.parseInt(args[1])));
    }

    public class AutoTorchScan {

        private LinkedList<BlockPos> posList = Lists.newLinkedList();
        private Searcher[] searches;
        private final ICommandSender sender;
        private final World startWorld;

        public AutoTorchScan(ICommandSender sender, int chunkRange, int threadNum) {
            this.sender = sender;
            this.startWorld = sender.getEntityWorld();
            this.searches = new Searcher[threadNum];
            for (int i = 0; i < searches.length; i++) {
                searches[i] = new Searcher(this, sender.getEntityWorld());
            }
            BlockPos pos = sender.getPosition();
            List<Chunk> chunkList = Lists.newArrayList();
            for (int x = -chunkRange; x <= chunkRange; x++) {
                for (int z = -chunkRange; z <= chunkRange; z++) {
                    chunkList.add(startWorld.getChunkFromChunkCoords((pos.getX() >> 4) + x, (pos.getZ() >> 4) + z));
                }
            }

            for (int i = 0; i < chunkList.size(); i++) {
                searches[i % searches.length].addTargetChunk(chunkList.get(i));
            }

            for (Searcher s : searches) {
                s.start();
            }
        }

        public synchronized void addQueue(List<BlockPos> pos) {
            posList.addAll(pos);
        }

        @SubscribeEvent
        public void tick(TickEvent.WorldTickEvent e) {
            if (e.world == startWorld) {
                boolean isSearchEnd = true;
                for (Searcher s : searches) {
                    if (s.isAlive()) {
                        isSearchEnd = false;
                        break;
                    }
                }
                if (isSearchEnd) {
                    MinecraftForge.EVENT_BUS.unregister(this);
                    this.addChat("Scan end,start torch add");
                    MinecraftForge.EVENT_BUS.register(new ATCommand.AutoTorcPlacer(posList, sender));
                }
            }
        }

        private void addChat(String str) {
            sender.addChatMessage(new TextComponentString(str));
        }

        private class Searcher extends Thread {
            final AutoTorchScan at;
            final World world;
            private List<Chunk> chunkList = Lists.newArrayList();

            Searcher(AutoTorchScan at, World world) {
                this.world = world;
                this.at = at;
            }

            public void addTargetChunk(Chunk chunk) {
                chunkList.add(chunk);
            }

            @Override
            public void run() {
                List<BlockPos> posList = Lists.newArrayListWithCapacity(4096);
                for (Chunk c : chunkList) {
                    for (int i = 0; i < c.getBlockStorageArray().length; i++) {
                        ExtendedBlockStorage e = c.getBlockStorageArray()[i];
                        if (e != c.NULL_BLOCK_STORAGE) {
                            for (int x = 0; x < 16; ++x) {
                                for (int y = 0; y < 16; ++y) {
                                    for (int z = 0; z < 16; ++z) {
                                        BlockPos pos = new BlockPos((c.xPosition << 4) + x, (i << 4) + y, (c.zPosition << 4) + z);
                                        if (startWorld.getBlockState(pos.down()).getMaterial().isReplaceable()) {
                                            continue;
                                        }
                                        IBlockState state = e.get(x, y, z);
                                        if (state.getMaterial() != Material.WATER && state.getMaterial().isReplaceable()) {
                                            if (Blocks.TORCH.canPlaceBlockAt(startWorld, pos)) {
                                                posList.add(pos);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                at.addQueue(posList);
            }

        }
    }

    public class AutoTorcPlacer {

        private ICommandSender sender;
        private final World startWorld;
        LinkedList<BlockPos> posList;
        private int totalBlock;
        private int totalTorch;
        private long stateTime;

        public AutoTorcPlacer(LinkedList<BlockPos> posList, ICommandSender sender) {
            this.startWorld = sender.getEntityWorld();
            this.sender = sender;
            this.posList = posList;
            this.totalBlock = posList.size();
            this.stateTime = System.currentTimeMillis();
        }

        @SubscribeEvent
        public void tick(TickEvent.ServerTickEvent e) {
            int i = 0;
            for (;;) {
                if (!posList.isEmpty()) {
                    BlockPos pos = posList.pollFirst();
                    if (startWorld.getLightFor(EnumSkyBlock.BLOCK, pos) < 8) {
                        startWorld.setBlockState(pos, Blocks.TORCH.getDefaultState(), 2);
                        totalTorch++;
                        if (i++ > (totalBlock / 100)) {
                            this.addChat("remaining:" + posList.size());
                            break;
                        }
                    }
                } else {
                    this.addChat("torch add end");
                    this.addChat("Check:" + totalBlock + "Block.  " + totalTorch + " torch added");
                    this.addChat("time:" + (System.currentTimeMillis() - stateTime) + "ms");
                    MinecraftForge.EVENT_BUS.unregister(this);
                    return;
                }
            }
        }

        private void addChat(String str) {
            sender.addChatMessage(new TextComponentString(str));
        }
    }
}
