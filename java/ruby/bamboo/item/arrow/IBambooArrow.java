package ruby.bamboo.item.arrow;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.entity.arrow.BaseArrow;

public interface IBambooArrow {
    public BaseArrow createArrowIn(World world, ItemStack bow, ItemStack arrow, float power, int chargeFrame, EntityPlayer player);

    public default void onHitEntity(World world, Entity hitEntity, Entity shooter, ItemStack arrow) {
    }

    public default boolean isNoResources(EntityPlayer player, ItemStack bowStack) {
        return player.capabilities.isCreativeMode|| EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, bowStack) > 0;
    }

    public default float getBowModel(int chargeFrame) {
        return chargeFrame >= 40 ? 1 : chargeFrame > 25 ? 0.7F : 0;
    }
}
