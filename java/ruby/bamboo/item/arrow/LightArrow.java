package ruby.bamboo.item.arrow;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.core.init.BambooData.BambooItem;
import ruby.bamboo.core.init.EnumCreateTab;

@BambooItem(createiveTabs = EnumCreateTab.TAB_BAMBOO)
public class LightArrow extends Item implements IBambooArrow{

    @Override
    public void execute(World world, ItemStack bow, ItemStack arrow, float power) {

    }

    @Override
    public ModelResourceLocation getBowModel(int useRemaining) {
        return null;
    }

}
