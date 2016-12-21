package ruby.bamboo.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import ruby.bamboo.core.Config;

public class JumpCommand extends CommandBase {
    public JumpCommand() {}

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
        if (dim == Config.DIMID.get()) {
            player.setSpawnChunk(new BlockPos(0, 4, 0), true, dim);
        }
        server.getPlayerList().transferPlayerToDimension(player, dim, new BambooTeleport(server.worldServerForDimension(dim)));

    }

    private class BambooTeleport extends Teleporter {

        public BambooTeleport(WorldServer worldIn) {
            super(worldIn);
        }

        @Override
        public void placeInPortal(Entity entityIn, float rotationYaw) {
            EntityPlayer player = (EntityPlayer) entityIn;
            BlockPos pos = player.getBedLocation();
            if (pos == null) {
                pos = player.getServer().worldServerForDimension(player.dimension).getSpawnPoint();
            }
            entityIn.setPosition(pos.getX(), pos.getY(), pos.getZ());

        }

        @Override
        public void removeStalePortalLocations(long worldTime) {

        }
    }

}
