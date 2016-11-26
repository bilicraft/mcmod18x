package ruby.bamboo.core.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import ruby.bamboo.api.BambooBlocks;
import ruby.bamboo.api.Constants;

public enum EnumCreateTab {
    NONE(null),
    TAB_BAMBOO( new CreativeTabs(Constants.MODID) {

        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(BambooBlocks.BAMBOOSHOOT);//Block.getBlockFromName(BlockData.getModdedName(ItemBambooShoot.class)));
        }
    });

    EnumCreateTab(CreativeTabs tab){
        this.tab=tab;
    }
    CreativeTabs tab;
    public CreativeTabs getTabInstance() {
        return tab;
    }
};