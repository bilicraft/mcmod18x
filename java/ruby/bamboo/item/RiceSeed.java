package ruby.bamboo.item;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;
import ruby.bamboo.api.BambooBlocks;
import ruby.bamboo.core.init.BambooData.BambooItem;
import ruby.bamboo.core.init.EnumCreateTab;

@BambooItem(createiveTabs = EnumCreateTab.TAB_BAMBOO)
public class RiceSeed extends ItemSeeds {

    public RiceSeed() {
        super(BambooBlocks.RICEPLANT, Blocks.FARMLAND);
    }

}
