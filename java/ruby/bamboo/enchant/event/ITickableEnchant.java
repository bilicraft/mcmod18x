package ruby.bamboo.enchant.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface ITickableEnchant {
    public void onTick(ItemStack stack, EntityLivingBase ticker);
}
