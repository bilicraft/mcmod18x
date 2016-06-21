package ruby.bamboo.gui;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import ruby.bamboo.core.BambooCore;

public class GuiHandler implements IGuiHandler {

    public static final int GUI_SACK = 0;
    private static final int GUI_MILLSTONE = 1;
    private static final int GUI_CAMPFIRE = 2;
    public static final int GUI_JPCHEST = 3;
    public static final int GUI_PICKAXE_LV = 4;
    public static final int GUI_PICKAXE_NAME = 5;
    public static final int GUI_PICKAXE_ENCH = 6;

    public static void openGui(EntityPlayer player, int guiId) {
        openGui(player.worldObj, player, guiId);
    }

    public static void openGui(World world, EntityPlayer player, int guiId) {
        BlockPos pos = player.getPosition();
        openGui(world, player, guiId, pos);
    }

    public static void openGui(World world, EntityPlayer player, int guiId, BlockPos pos) {
        openGui(world, player, guiId, pos.getX(), pos.getY(), pos.getZ());
    }

    public static void openGui(World world, EntityPlayer player, int guiId, int x, int y, int z) {
        player.openGui(BambooCore.instance, guiId, world, x, y, z);
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case GUI_SACK:
                return new ContainerSack(player.inventory, player.getCurrentEquippedItem());
            case GUI_JPCHEST:
                return new ContainerChest(player.inventory, (IInventory) world.getTileEntity(new BlockPos(x, y, z)), player);
            case GUI_PICKAXE_LV:
                return new ContainerPickaxeLV(player.inventory, player.getCurrentEquippedItem());
            case GUI_PICKAXE_NAME:
                return new ContainerPickaxeName(player, player.getCurrentEquippedItem());
            case GUI_PICKAXE_ENCH:
                return new ContainerPickaxeEnch(player.inventory, player.getCurrentEquippedItem());
            // case GUI_MILLSTONE:
            // return new ContainerMillStone(player.inventory, (TileEntityMillStone) world.getTileEntity(x, y, z));
            // case GUI_CAMPFIRE:
            // return new ContainerCampfire(player.inventory, (TileEntityCampfire) world.getTileEntity(x, y, z));
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case GUI_SACK:
                return new GuiSack(player.inventory, player.getCurrentEquippedItem());
            case GUI_JPCHEST:
                return new GuiChest(player.inventory, (IInventory) world.getTileEntity(new BlockPos(x, y, z)));
            case GUI_PICKAXE_LV:
                return new GuiPickaxeLV(player, player.getCurrentEquippedItem());
            case GUI_PICKAXE_NAME:
                return new GuiPickaxeName(player, player.getCurrentEquippedItem());
            case GUI_PICKAXE_ENCH:
                return new GuiPickaxeEnch(player, player.getCurrentEquippedItem());
            // case GUI_MILLSTONE:
            // return new GuiMillStone(player.inventory, world.getTileEntity(x, y, z));
            //
            // case GUI_CAMPFIRE:
            // return new GuiCampfire(player.inventory, (TileEntityCampfire) world.getTileEntity(x, y, z));
            default:
                return null;
        }
    }

}
