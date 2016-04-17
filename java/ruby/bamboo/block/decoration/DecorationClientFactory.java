package ruby.bamboo.block.decoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import ruby.bamboo.core.Constants;

public class DecorationClientFactory extends DecorationFactory {
    String[] commonJsons = new String[] { "square", "stairs", "inner_stairs", "outer_stairs", "slab", "upper_slab" };

    @Override
    void registerNormal(EnumDecoration deco) {
        String jsonName = "common_square";
        StateModMapper state = new StateModMapper(jsonName);
        registerJson(deco.getModName(), jsonName, state);
    }

    @Override
    void registerDoubleSlab(EnumDecoration deco) {
        //ModelLoader.setCustomStateMapper(Block.getBlockFromName(deco.getModName() + EnumDecoration.DOUBLE_SLAB), (new StateMap.Builder()).ignore(DecorationSlab.SEAMLESS).build());
        String jsonName = "common" + EnumDecoration.DOUBLE_SLAB;
        StateModMapper state = new StateModMapper(jsonName);
        state.ignore(DecorationSlab.SEAMLESS);
        registerJson(deco.getModName() + EnumDecoration.DOUBLE_SLAB, jsonName, state);
    }

    @Override
    void registerSlab(EnumDecoration deco) {
        //ModelLoader.setCustomStateMapper(Block.getBlockFromName(deco.getModName() + deco.SLAB), (new StateMap.Builder()).addPropertiesToIgnore(DecorationSlab.HALF).build());
        String jsonName = "common" + EnumDecoration.SLAB;
        StateModMapper state = new StateModMapper(jsonName);
        registerJson(deco.getModName() + EnumDecoration.SLAB, jsonName, state);
    }

    @Override
    void registerStair(EnumDecoration deco) {
        String jsonName = "common" + EnumDecoration.STAIRS;
        StateModMapper state = new StateModMapper(jsonName);
        registerJson(deco.getModName() + EnumDecoration.STAIRS, jsonName, state);
    }

    private void registerJson(String name, final String jsonName, StateMapperBase state) {
        Item item = Item.getByNameOrId(name);
        for (int i = 0; i < 16; i++) {
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(Constants.getModDomain() + jsonName, "inventory"));
        }
        if (item instanceof ItemBlock) {
            ModelLoader.setCustomStateMapper(((ItemBlock) item).block, state);
        }

    }

    class StateModMapper extends StateMapperBase {
        private String jsonName;
        private List<IProperty<?>> ignored;

        public StateModMapper(String jsonName) {
            ignored = new ArrayList<IProperty<?>>();
            this.jsonName = Constants.getModDomain() + jsonName;
        }

        public StateModMapper ignore(IProperty... prop) {
            for (IProperty p : prop) {
                ignored.add(p);
            }
            return this;
        }

        @Override
        protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            Map<IProperty, Comparable> map = Maps.<IProperty, Comparable> newLinkedHashMap(state.getProperties());
            for (IProperty<?> iproperty : this.ignored) {
                map.remove(iproperty);
            }
            return new ModelResourceLocation(jsonName, this.getPropertyString(map));
        }

    }

}
