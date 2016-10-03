package ruby.bamboo.enchant;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import ruby.bamboo.enchant.event.ITickableEnchant;

public class Floating extends EnchantBase implements ITickableEnchant {

    public Floating(int id, String name) {
        super(id, name);
    }

    @Override
    public void onTick(ItemStack stack, EntityLivingBase ticker, int slotNum, boolean isSelected) {

    }

    @Override
    public int getRarity() {
        return 10;
    }

}
