package ruby.bamboo.item.arrow;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.core.init.BambooData.BambooItem;
import ruby.bamboo.core.init.EnumCreateTab;
import ruby.bamboo.entity.arrow.EntityTouchArrow;
import ruby.bamboo.item.BambooBow;
import ruby.bamboo.util.ItemStackHelper;

@BambooItem(createiveTabs = EnumCreateTab.TAB_BAMBOO)
public class TouchArrow extends Item implements IBambooArrow {

    @Override
    public void execute(World world, ItemStack bow, ItemStack arrow, float power, int chargeFrame, EntityPlayer player) {
        if ((power *= 2) > 1.0F) {
            power = 1.0F;
        }

        EntityTouchArrow eta = new EntityTouchArrow(world, player, power * 2.0f);
        eta.setDamage(0.25);
        if (world.rand.nextFloat() < 0.1f) {
            eta.setFire(100);
        }
        if (!world.isRemote) {
            world.spawnEntityInWorld(eta);
        }
        if (isNoResources(player)) {
            ItemStackHelper.decrStackSize(player.inventory, arrow, 1);
        } else {
            eta.canBePickedUp = 0;
        }
    }

    @Override
    public ModelResourceLocation getBowModel(int chargeFrame) {
        String jsonName = BambooBow.ICON_PULL_NAMES[chargeFrame >= 5 ? 3 : chargeFrame > 2 ? 2 : 1];
        return new ModelResourceLocation(jsonName, "inventory");
    }

}
