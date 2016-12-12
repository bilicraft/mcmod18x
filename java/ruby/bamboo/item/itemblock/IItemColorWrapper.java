package ruby.bamboo.item.itemblock;

import net.minecraft.item.ItemStack;

public interface IItemColorWrapper {
    public int getColorFromItemstack(ItemStack stack, int tintIndex);
}
