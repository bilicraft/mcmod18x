package ruby.bamboo.item.itemblock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import ruby.bamboo.block.IndLight;

public class ItemIndLight extends ItemBlock implements IItemColorWrapper{

    public ItemIndLight(Block block) {
        super(block);
    }

    @Override
    public int getColorFromItemstack(ItemStack stack, int tintIndex) {
        return ((IndLight)block).color.getMapColor().colorValue;
    }

}
