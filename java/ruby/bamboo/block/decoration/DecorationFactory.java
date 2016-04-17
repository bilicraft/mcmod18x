package ruby.bamboo.block.decoration;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ruby.bamboo.core.init.EnumCreateTab;
import ruby.bamboo.item.itemblock.ItemDecorationSlab;

public class DecorationFactory {

    public void register() {
        for (EnumDecoration deco : EnumDecoration.values()) {
            if ((deco.getTypeFlg() & 1) != 0) {
                registerNormal(deco);
            }

            if ((deco.getTypeFlg() & 2) != 0) {
                registerDoubleSlab(deco);
                registerSlab(deco);
            }

            if ((deco.getTypeFlg() & 4) != 0) {
                registerStair(deco);
            }
        }
    }

    void registerNormal(EnumDecoration deco) {
        Block block = new DecorationBlock(deco.getMaterial()).setName(deco.getName());
        registerBlock(block, ItemBlock.class, deco.getName(), deco.getCreateTab());
    }

    void registerDoubleSlab(EnumDecoration deco) {
        Block doubleSlab = new DecorationDoubleSlab(deco.getMaterial(), deco.getModName() + EnumDecoration.SLAB).setName(deco.getName());
        registerBlock(doubleSlab, ItemBlock.class, deco.getName() + EnumDecoration.DOUBLE_SLAB, EnumCreateTab.NONE);
    }

    void registerSlab(EnumDecoration deco) {
        Block singleSlab = new DecorationSlab(deco.getMaterial()).setName(deco.getName());
        registerBlock(singleSlab, ItemDecorationSlab.class, deco.getName() + EnumDecoration.SLAB, deco.getCreateTab(), singleSlab, Block.getBlockFromName(deco.getModName() + EnumDecoration.DOUBLE_SLAB));
    }

    void registerStair(EnumDecoration deco) {
        Block block = new DecorationStairs(Block.getBlockFromName(deco.getModName()).getDefaultState()).setName(deco.getName());
        registerBlock(block, ItemBlock.class, deco.getName() + EnumDecoration.STAIRS, deco.getCreateTab());
    }

    private void registerBlock(Block block, Class<? extends ItemBlock> item, String name, EnumCreateTab tab, Object... obj) {
        if (tab != EnumCreateTab.NONE) {
            block.setCreativeTab(tab.getTabInstance());
        }
        block.setUnlocalizedName(name);
        if (obj.length == 0) {
            GameRegistry.registerBlock(block, item, name);
        } else {
            GameRegistry.registerBlock(block, item, name, obj);
        }
        FMLLog.info("BLOCK: %s to Registed", name);
    }

}
