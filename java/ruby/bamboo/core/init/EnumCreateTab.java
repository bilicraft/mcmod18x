package ruby.bamboo.core.init;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruby.bamboo.api.BambooBlocks;
import ruby.bamboo.api.Constants;

public enum EnumCreateTab {
    NONE(null),
    TAB_BAMBOO(new CreativeTabs(Constants.MODID) {

        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(BambooBlocks.BAMBOOSHOOT);//Block.getBlockFromName(BlockData.getModdedName(ItemBambooShoot.class)));
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void displayAllRelevantItems(List<ItemStack> baseList) {
            super.displayAllRelevantItems(baseList);
            Comparator<ItemStack> comp = (is1, is2) -> (is1.getItem() instanceof ICreativeSoatName ? ((ICreativeSoatName) is1.getItem()).getSortName(is1) : is1.getUnlocalizedName())
                    .compareTo(is2.getItem() instanceof ICreativeSoatName ? ((ICreativeSoatName) is2.getItem()).getSortName(is2) : is2.getUnlocalizedName());
            //Block系ソート
            List<ItemStack> blockList = baseList.stream().filter(is -> is.getItem() instanceof ItemBlock).sorted(comp).collect(Collectors.toList());
            //Item系ソート
            List<ItemStack> itemList = baseList.stream().filter(is -> !blockList.contains(is)).sorted(comp).collect(Collectors.toList());

            baseList.clear();
            baseList.addAll(blockList);
            baseList.addAll(itemList);
        }
    });

    EnumCreateTab(CreativeTabs tab) {
        this.tab = tab;
    }

    CreativeTabs tab;

    public CreativeTabs getTabInstance() {
        return tab;
    }

    public interface ICreativeSoatName {
        public String getSortName(ItemStack is);
    }
};