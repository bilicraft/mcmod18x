package ruby.bamboo.item.arrow;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.core.init.BambooData.BambooItem;
import ruby.bamboo.core.init.EnumCreateTab;
import ruby.bamboo.entity.arrow.EntityBambooArrow;
import ruby.bamboo.util.ItemStackHelper;

@BambooItem(createiveTabs = EnumCreateTab.TAB_BAMBOO)
public class BambooArrow extends Item implements IBambooArrow {
    private int limit = 5;

    @Override
    public void execute(World world, ItemStack bow, ItemStack arrow, float power, int chargeFrame, EntityPlayer player) {
        EntityBambooArrow eba;
        int attackCount;
        int invStackSize = ItemStackHelper.getInventoryStackSize(player.inventory, arrow);
        attackCount = chargeFrame / 10 > limit ? limit : chargeFrame / 10;
        attackCount = attackCount < invStackSize ? attackCount : invStackSize;

        if (attackCount < 1) {
            attackCount = 1;
        }

        if (player.capabilities.isCreativeMode) {
            if (player.capabilities.isFlying) {
                attackCount = 12;
                power = 1;
            }
            eba = new EntityBambooArrow(world, player, power * 2.0f);
            eba.setMaxAge(60);
            eba.canBePickedUp = 0;
        } else {
            eba = new EntityBambooArrow(world, player, power * 2.0f);
        }

        eba.setBarrage(attackCount);

        if (power >= 1.0F) {
            eba.setIsCritical(true);
        }

        int enchantPower = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, bow);

        if (enchantPower > 0) {
            eba.setDamage(eba.getDamage() + enchantPower * 0.15D);
        }

        int enchantPunch = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, bow);

        if (enchantPunch > 0) {
            eba.setKnockbackStrength(enchantPunch);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, bow) > 0) {
            eba.setFire(100);
        }
        if (!world.isRemote) {
            world.spawnEntityInWorld(eba);
        }
        if (isNoResources(player)) {
            ItemStackHelper.decrStackSize(player.inventory, arrow, attackCount);
        }

    }

}
