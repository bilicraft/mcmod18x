package ruby.bamboo.enchant.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IInteractionForEntity {
    public void onInteract(ItemStack stack, EntityLivingBase target, EntityLivingBase user);
}
