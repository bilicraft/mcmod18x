package ruby.bamboo.util;

import java.util.Objects;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemStackHelper {
    /**
     * インベントリ内部で、itemとdamageが等しい物の合算から減算可能か、NBTは考慮しない
     */
    public static boolean hasDecrStackSize(IInventory inv, ItemStack is, int count) {
        int invCount = 0;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (is.isItemEqual(stack)) {
                invCount += stack.stackSize;
            }
        }
        return invCount >= count;
    }

    /**
     * インベントリ内部で、itemとdamageが等しい物から減算する、NBTは考慮しない
     * インベントリの後ろから順に消化する
     */
    public static boolean decrStackSize(IInventory inv, ItemStack is, int count) {

        if (!hasDecrStackSize(inv, is, count)) {
            return false;
        }

        for (int i = inv.getSizeInventory(); 0 < i; i--) {
            if (count <= 0) {
                break;
            }
            ItemStack stack = inv.getStackInSlot(i);
            if (is.isItemEqual(stack)) {
                count -= stack.stackSize;
                if (count >= 0) {
                    inv.setInventorySlotContents(i, null);
                } else {
                    inv.decrStackSize(i, Math.abs(count));
                }
            }
        }
        return true;
    }

    public static class HashedStack {
        public final Item item;
        public final int damage;
        public final int stackSize;

        public HashedStack(ItemStack stack) {
            item = stack.getItem();
            damage = stack.getItemDamage();
            stackSize = stack.stackSize;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (obj instanceof HashedStack) {
                return super.equals(obj) || this.hashCode() == obj.hashCode();
            }
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return Objects.hash(item, damage);
        }

        public ItemStack getItemStack() {
            return new ItemStack(item, stackSize, damage);
        }
    }
}
