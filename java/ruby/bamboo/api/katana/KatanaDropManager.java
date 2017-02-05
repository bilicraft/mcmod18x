package ruby.bamboo.api.katana;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ArrayListMultimap;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class KatanaDropManager {

    private KatanaDropManager() {}

    private static final ArrayListMultimap<Class<? extends Entity>, KatanaDropItem> dropTable = ArrayListMultimap.create();

    public static void addDrop(Class<? extends Entity> entityCls, KatanaDropItem item) {
        if (!dropTable.containsEntry(entityCls, item)) {
            dropTable.put(entityCls, item);
        }
    }

    public static boolean isDropableEntity(Class<? extends Entity> entityCls) {
        return dropTable.containsKey(entityCls);
    }

    public static ItemStack getRandomDropItem(Class<? extends Entity> entityCls, Random rand, float dropRate) {
        if (dropTable.containsKey(entityCls)) {
            List<KatanaDropItem> list = dropTable.get(entityCls);
            KatanaDropItem drop = list.get(rand.nextInt(list.size()));
            return drop.getRandomDrop(rand, dropRate);
        }
        return null;
    }

    public static class KatanaDropItem {
        private ItemStack dropItem;
        private float reality;
        private int randomAmount;

        public KatanaDropItem(Item item, float reality) {
            this(new ItemStack(item), reality);
        }

        public KatanaDropItem(Block block, float reality) {
            this(new ItemStack(block), reality);
        }

        public KatanaDropItem(ItemStack dropItem, float reality) {
            this.dropItem = dropItem;
            this.reality = reality;
            this.randomAmount = 0;
        }

        public KatanaDropItem setRandomAddAmount(int amount) {
            this.randomAmount = amount;
            return this;
        }

        public float getReality() {
            return reality;
        }

        public ItemStack getDropItem() {
            return dropItem;
        }

        public int getRandomAmount() {
            return randomAmount;
        }

        public ItemStack getRandomDrop(Random rand, float dropRate) {
            if (rand.nextFloat() < getReality() + dropRate) {
                ItemStack result = getDropItem().copy();
                if (getRandomAmount() != 0) {
                    result.stackSize += rand.nextInt(getRandomAmount() + 1);
                }
                return result;
            }
            return null;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof KatanaDropItem) {
                return dropItem.isItemEqual(((KatanaDropItem) obj).getDropItem());
            }
            return false;
        }
    }
}
