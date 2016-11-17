package ruby.bamboo.item.arrow;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.core.init.BambooData.BambooItem;
import ruby.bamboo.core.init.EnumCreateTab;
import ruby.bamboo.entity.arrow.BaseArrow;
import ruby.bamboo.entity.arrow.EntityTorchArrow;
import ruby.bamboo.util.ItemStackHelper;

@BambooItem(createiveTabs = EnumCreateTab.TAB_BAMBOO)
public class TorchArrow extends ArrowBase {

    @Override
    public void execute(World world, ItemStack bow, ItemStack arrow, float power, int chargeFrame, EntityPlayer player) {
        if ((power *= 2) > 1.0F) {
            power = 1.0F;
        }

        EntityTorchArrow entityArrow = new EntityTorchArrow(world, player, power * 2.0f);
        entityArrow.setDamage(0.25);

        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, bow);

        if (j > 0) {
            entityArrow.setDamage(entityArrow.getDamage() + j * 0.5D + 0.5D);
        }

        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, bow);

        if (k > 0) {
            entityArrow.setKnockbackStrength(k);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, bow) > 0) {
            entityArrow.setFire(100);
        }

        if (world.rand.nextFloat() < 0.1f) {
            entityArrow.setFire(100);
        }
        if (!world.isRemote) {
            world.spawnEntityInWorld(entityArrow);
        }
        if (!isNoResources(player)) {
            ItemStackHelper.decrStackSize(player.inventory, arrow, 1);
        } else {
            entityArrow.setNoPick();
        }
    }

    @Override
    public float getBowModel(int chargeFrame) {
        return chargeFrame >= 5 ? 1 : chargeFrame > 2 ? 0.7F : 0;
    }

    @Override
    public Class<? extends BaseArrow> getArrowClass() {
        return EntityTorchArrow.class;
    }

}
