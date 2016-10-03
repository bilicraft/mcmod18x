package ruby.bamboo.core.init;

import java.util.HashMap;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import ruby.bamboo.core.Constants;

public enum EnumCreateTab {
    NONE,
    TAB_BAMBOO;
    static HashMap<EnumCreateTab, CreativeTabs> map;

    static {
        map = new HashMap<EnumCreateTab, CreativeTabs>();
        map.put(NONE, null);
        map.put(TAB_BAMBOO, new CreativeTabs(Constants.MODID) {

            @Override
            public Item getTabIconItem() {
                return Item.getItemFromBlock(Blocks.STONE);//Block.getBlockFromName(BlockData.getModdedName(ItemBambooShoot.class)));
            }
        });
    }

    public CreativeTabs getTabInstance() {
        return map.get(this);
    }
};