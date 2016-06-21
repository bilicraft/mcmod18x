package ruby.bamboo.enchant.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IAttackEnchant {
    public void onEntityAttack(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker);
}
