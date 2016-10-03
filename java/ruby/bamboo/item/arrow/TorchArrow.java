package ruby.bamboo.item.arrow;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.core.init.BambooData.BambooItem;
import ruby.bamboo.core.init.EnumCreateTab;
import ruby.bamboo.entity.arrow.BaseArrow;
import ruby.bamboo.entity.arrow.EntityTorchArrow;
import ruby.bamboo.item.BambooBow;
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

        int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, bow);

        if (j > 0) {
            entityArrow.setDamage(entityArrow.getDamage() + j * 0.5D + 0.5D);
        }

        int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, bow);

        if (k > 0) {
            entityArrow.setKnockbackStrength(k);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, bow) > 0) {
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
            entityArrow.canBePickedUp = 0;
        }
    }

    @Override
    public ModelResourceLocation getBowModel(int chargeFrame) {
        String jsonName = BambooBow.ICON_PULL_NAMES[chargeFrame >= 5 ? 3 : chargeFrame > 2 ? 2 : 1];
        return new ModelResourceLocation(jsonName, "inventory");
    }

    @Override
    public Class<? extends BaseArrow> getArrowClass() {
        return EntityTorchArrow.class;
    }

}
