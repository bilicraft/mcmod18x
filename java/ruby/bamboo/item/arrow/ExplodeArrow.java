package ruby.bamboo.item.arrow;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.core.init.BambooData.BambooItem;
import ruby.bamboo.core.init.EnumCreateTab;

@BambooItem(createiveTabs = EnumCreateTab.TAB_BAMBOO)
public class ExplodeArrow extends ArrowBase{
    @Override
    public void execute(World world, ItemStack bow, ItemStack arrow, float power, int chargeFrame, EntityPlayer player) {

    }

    @Override
    public ModelResourceLocation getBowModel(int useRemaining) {
        return null;
    }

    @Override
    public Class<? extends EntityArrow> getArrowClass() {
        return null;
    }

}
