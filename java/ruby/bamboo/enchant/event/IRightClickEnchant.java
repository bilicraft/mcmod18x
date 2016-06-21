package ruby.bamboo.enchant.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IRightClickEnchant {
    public void onRichtClick(ItemStack stack, EntityPlayer clicker);
}
