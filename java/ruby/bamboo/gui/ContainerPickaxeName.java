package ruby.bamboo.gui;

import static ruby.bamboo.enchant.EnchantConstants.*;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import ruby.bamboo.core.DataManager;
import ruby.bamboo.item.BambooPickaxe;

public class ContainerPickaxeName extends Container {

    private ItemStack pickAxe;
    private String repairedItemName;
    private EntityPlayer player;

    private static final int TAG_SLOT_ID = 0;
    private IInventory inventry;

    public ContainerPickaxeName(EntityPlayer player, ItemStack stack) {
        this.pickAxe = stack;
        this.player = player;

        inventry = new InventoryBasic("nameTag", false, 1) {
            @Override
            public int getInventoryStackLimit() {
                return 1;
            }
        };
        int i;

        this.addSlotToContainer(new Slot(inventry, TAG_SLOT_ID, 27, 20) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack != null ? stack.getItem() instanceof ItemNameTag : false;
            }
        });

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return playerIn.getCurrentEquippedItem() != null && playerIn.getCurrentEquippedItem().getItem() == DataManager.getItem(BambooPickaxe.class);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);
        Slot tagSlot = (Slot) this.inventorySlots.get(TAG_SLOT_ID);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1;
            if (par2 == TAG_SLOT_ID) {
                itemstack1 = slot.getStack();
                itemstack = itemstack1.copy();
                // tag用スロット→プレイヤーインベントリ
                if (tagSlot.getHasStack()) {
                    if (!this.mergeItemStack(itemstack1, 1, 37, true)) {
                        return null;
                    }
                    tagSlot.onSlotChanged();
                    if (itemstack1.stackSize == 0) {
                        tagSlot.putStack((ItemStack) null);
                    }

                    tagSlot.onPickupFromSlot(par1EntityPlayer, itemstack1);
                }
            } else {
                int splitNum = 1;
                //プレイヤーインベントリ→tag用スロット
                //itemstack = null;
                // tagslotが埋まっているときは何もしない
                if (tagSlot.getHasStack()) {
                    return null;
                }

                itemstack1 = slot.getStack();
                // あらかじめ分けておかないと、getInventoryStackLimitの制限にかかわらず全てセットされてしまう。
                ItemStack itemstack2 = slot.getStack().copy().splitStack(splitNum);
                itemstack = itemstack1.copy();
                // マージ可能ではない場合、何もしない
                if (!this.mergeItemStack(itemstack2, 0, 1, true)) {
                    return null;
                }
                itemstack1.stackSize -= splitNum;
                slot.onSlotChanged();
                if (itemstack1.stackSize == 0) {
                    slot.putStack((ItemStack) null);
                }

                slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
            }
        }

        return itemstack;
    }

    @Override
    public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer playerIn) {
        return super.slotClick(slotId, clickedButton, mode, playerIn);
    }

    @Override
    public void onContainerClosed(EntityPlayer par1EntityPlayer) {

        Slot tagSlot = (Slot) this.inventorySlots.get(TAG_SLOT_ID);

        if (tagSlot != null && tagSlot.getHasStack()) {
            if (!par1EntityPlayer.worldObj.isRemote) {
                par1EntityPlayer.worldObj.spawnEntityInWorld(new EntityItem(par1EntityPlayer.worldObj, par1EntityPlayer.posX, par1EntityPlayer.posY + 0.5, par1EntityPlayer.posZ, tagSlot.getStack()));
            }
        }

        super.onContainerClosed(par1EntityPlayer);
    }

    public void updateItemName(String newName) {

        ItemStack itemstack = pickAxe;

        if (StringUtils.isBlank(newName)) {
            pickAxe.clearCustomName();
        } else {
            pickAxe.setStackDisplayName(newName);
        }
        if (!player.capabilities.isCreativeMode) {
            player.removeExperienceLevel(getCost());
            this.inventorySlots.get(0).putStack((ItemStack) null);
        }

    }

    private int getCost() {
        return this.inventorySlots.get(0).getHasStack() ? 0 : COST;
    }

}
