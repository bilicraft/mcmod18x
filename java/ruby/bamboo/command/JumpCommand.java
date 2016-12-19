package ruby.bamboo.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class JumpCommand extends CommandBase {
    public JumpCommand(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandName() {
        return "bamboo_jump";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Do not use this command";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            return;
        }
        int dim = Integer.parseInt(args[0]);
        EntityPlayerMP player = (EntityPlayerMP) sender.getCommandSenderEntity();
        NBTTagCompound nbt = player.getEntityData();
        NBTTagCompound dimtag = new NBTTagCompound();
        NBTTagCompound postag = new NBTTagCompound();
        nbt.setTag("bamboojump", dimtag);
        dimtag.setTag("dim" + player.dimension, postag);
        postag.setLong("pos", player.getPosition().toLong());
        if (nbt.hasKey("bamboojump")) {
            dimtag = (NBTTagCompound) nbt.getTag("bamboojump");
            if (dimtag.hasKey("dim" + dim)) {
                postag=(NBTTagCompound) dimtag.getTag("dim" + dim);
                BlockPos pos = BlockPos.fromLong(postag.getLong("pos"));
                player.setPosition(pos.getX(), pos.getY(), pos.getZ());
            }
        }
        server.getPlayerList().transferPlayerToDimension(player, dim, new BambooTeleport(server.worldServerForDimension(dim)));

    }

    private class BambooTeleport extends Teleporter {

        public BambooTeleport(WorldServer worldIn) {
            super(worldIn);
        }

        @Override
        public void placeInPortal(Entity entityIn, float rotationYaw) {

        }

        @Override
        public void removeStalePortalLocations(long worldTime) {

        }
    }

    @SubscribeEvent
    public void capEvent(PlayerEvent.Clone e) {
        if (e.getEntity() instanceof EntityPlayer) {
            NBTTagCompound nbt= e.getOriginal().getEntityData();
            if(nbt.hasKey("bamboojump")){
                NBTTagCompound jump=(NBTTagCompound) nbt.getTag("bamboojump");
                e.getEntityPlayer().getEntityData().setTag("bamboojump", jump);
            }
        }
    }
}
