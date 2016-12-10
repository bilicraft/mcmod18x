package ruby.bamboo.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;

/*
 * InventoryHelperになってきてる気がする
 */
public class ItemStackHelper {
    private final static int armorSlotCount = 0;

    /**
     * インベントリ内部で、itemとdamageが等しい物の合算から減算可能か、NBTは考慮しない
     */
    public static boolean hasDecrStackSize(IInventory inv, ItemStack stack, int count) {
        return getInventoryStackSize(inv, stack) >= count;
    }

    /**
     * 指定ItemStackのインベントリ内での合算
     */
    public static int getInventoryStackSize(IInventory inv, ItemStack stack) {
        int invCount = 0;
        for (int i = 0; i < inv.getSizeInventory() - armorSlotCount; i++) {
            ItemStack is = inv.getStackInSlot(i);
            if (stack.isItemEqual(is)) {
                invCount += is.stackSize;
            }
        }
        return invCount;
    }

    public static int getInventoryStackSize(ItemStack[] inv, ItemStack stack) {
        return (int) Stream.of(inv).map(is->stack.isItemEqual(is)).count();
    }

    /**
     * インベントリ内部で、itemとdamageが等しい物から減算する、NBTは考慮しない
     * インベントリの後ろから順に消化する
     */
    public static boolean decrStackSize(IInventory inv, ItemStack stack, int count) {

        if (!hasDecrStackSize(inv, stack, count)) {
            return false;
        }

        for (int i = inv.getSizeInventory() - armorSlotCount; 0 <= i; i--) {
            if (count <= 0) {
                break;
            }
            ItemStack is = inv.getStackInSlot(i);
            if (stack.isItemEqual(is)) {
                count -= is.stackSize;
                if (count >= 0) {
                    inv.setInventorySlotContents(i, null);
                } else {
                    inv.decrStackSize(i, count + is.stackSize);
                }
            }
        }
        return true;
    }

    /**
     * 後方からItemとダメージが等しい物を検索
     */
    public static int getSlotNum(IInventory inv, ItemStack stack) {
        for (int i = inv.getSizeInventory() - armorSlotCount; 0 <= i; i--) {
            ItemStack is = inv.getStackInSlot(i);
            if (stack.isItemEqual(is)) {
                return i;
            }
        }
        return -1;
    }

    public static class HashedStack {
        public final Item item;
        public final int damage;
        public final int stackSize;

        public HashedStack(Item item) {
            this(new ItemStack(item));
        }
        public HashedStack(Block block) {
            this(new ItemStack(block));
        }
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

    public static <T extends NBTBase> Iterable<T> getNBTTagListIte(final NBTTagList tagList) {
        return () -> {
            return new Iterator<T>() {
                int cursor;

                @Override
                public boolean hasNext() {
                    return cursor != tagList.tagCount();
                }

                @Override
                public T next() {
                    int i = cursor;
                    if (i >= tagList.tagCount()) {
                        throw new NoSuchElementException();
                    }
                    cursor = i + 1;
                    return (T) tagList.get(i);
                }

                @Override
                public void remove() {
                    int i = cursor;
                    if (i >= tagList.tagCount()) {
                        throw new NoSuchElementException();
                    }
                    cursor = i - 1;
                    tagList.removeTag(i);
                }
            };
        };
    }

    public static EnumHand getHandItemEq(EntityPlayer player, Item target) {
        return player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == target ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
    }
}
