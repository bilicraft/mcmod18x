package ruby.bamboo.block.decoration;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;


public class DecorationClientFactory extends DecorationFactory {

    @Override
    void registerNormal(EnumDecoration deco) {
        registerJson(deco.getModName());
    }

    @Override
    void registerDoubleSlab(EnumDecoration deco) {
        ModelLoader.setCustomStateMapper(Block.getBlockFromName(deco.getModName() + EnumDecoration.DOUBLE_SLAB), (new StateMap.Builder()).ignore(DecorationSlab.SEAMLESS).build());
        registerJson(deco.getModName() + EnumDecoration.DOUBLE_SLAB);
    }

    @Override
    void registerSlab(EnumDecoration deco) {
        //ModelLoader.setCustomStateMapper(Block.getBlockFromName(deco.getModName() + deco.SLAB), (new StateMap.Builder()).addPropertiesToIgnore(DecorationSlab.HALF).build());
        registerJson(deco.getModName() + EnumDecoration.SLAB);
    }

    @Override
    void registerStair(EnumDecoration deco) {
        registerJson(deco.getModName() + EnumDecoration.STAIRS);
    }

    private void registerJson(String name) {
        Item item = Item.getByNameOrId(name);
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(name, "inventory"));
    }

}
