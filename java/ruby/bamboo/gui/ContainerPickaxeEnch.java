package ruby.bamboo.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ruby.bamboo.core.DataManager;
import ruby.bamboo.item.BambooPickaxe;

public class ContainerPickaxeEnch extends Container {

    public ContainerPickaxeEnch(InventoryPlayer par1InventoryPlayer, ItemStack stack) {

        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return playerIn.getCurrentEquippedItem() != null && playerIn.getCurrentEquippedItem().getItem() == DataManager.getItem(BambooPickaxe.class);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemstack = null;
        //        Slot slot = (Slot) this.inventorySlots.get(par2);
        //        if (slot != null && slot.getHasStack()) {
        //            ItemStack itemstack1 = slot.getStack();
        //            itemstack = itemstack1.copy();
        //
        //
        //                if (!this.mergeItemStack(itemstack1, 0, 36, true)) {
        //                    return null;
        //                }
        //
        //            slot.onSlotChanged();
        //            if (itemstack1.stackSize == 0) {
        //                slot.putStack((ItemStack) null);
        //            }
        //
        //            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        //        }

        return null;
    }

    @Override
    public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer playerIn) {
        return super.slotClick(slotId, clickedButton, mode, playerIn);
    }

    @Override
    public void onContainerClosed(EntityPlayer par1EntityPlayer) {
        //        if (par1EntityPlayer.getCurrentEquippedItem() != null && par1EntityPlayer.getCurrentEquippedItem().getItem() == DataManager.getItem(Sack.class)) {
        //            ItemStack slot0 = ((Slot) this.inventorySlots.get(0)).getStack();
        //            if (itemStack != null && slot0 != null) {
        //                itemStack.setTagCompound(new NBTTagCompound());
        //                NBTTagCompound var4 = itemStack.getTagCompound();
        //                var4.setString("type", String.valueOf(Item.itemRegistry.getNameForObject(slot0.getItem())));
        //                var4.setShort("count", (short) slot0.stackSize);
        //                var4.setShort("meta", (short) slot0.getItemDamage());
        //                itemStack.setItemDamage(itemStack.getMaxDamage() - 1);
        //            }
        //
        //            ItemStack item = itemStack;
        //
        //            if (item != null && item.getTagCompound() != null) {
        //                if (isStorage(Item.itemRegistry.getObject(new ResourceLocation(item.getTagCompound().getString("type"))))) {
        //                    par1EntityPlayer.getCurrentEquippedItem().setTagCompound(item.getTagCompound());
        //                } else {
        //                    if (!par1EntityPlayer.worldObj.isRemote) {
        //                        par1EntityPlayer.worldObj.spawnEntityInWorld(new EntityItem(par1EntityPlayer.worldObj, par1EntityPlayer.posX, par1EntityPlayer.posY + 0.5, par1EntityPlayer.posZ, inventry.slot0));
        //                    }
        //                }
        //            }
        //        } else {
        //            if (inventry.slot0 != null) {
        //                if (!par1EntityPlayer.worldObj.isRemote) {
        //                    par1EntityPlayer.worldObj.spawnEntityInWorld(new EntityItem(par1EntityPlayer.worldObj, par1EntityPlayer.posX, par1EntityPlayer.posY + 0.5, par1EntityPlayer.posZ, inventry.slot0));
        //                }
        //            }
        //        }

        super.onContainerClosed(par1EntityPlayer);
    }

}
