package ruby.bamboo.model;

import java.io.IOException;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ruby.bamboo.core.Constants;

public class ModelFactory {

    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event) {
        String[] commonJsons = new String[] { "square", "stairs", "inner_stairs", "outer_stairs", "slab", "upper_slab" };

        for (String str : commonJsons) {
            String resouceName = Constants.getModDomain() + "block/common_" + str;
            ResourceLocation rawSided = new ResourceLocation(resouceName);
            try {
                IModel modelS = event.modelLoader.getModel(rawSided);
                if (modelS instanceof IRetexturableModel) {
                    IBakedModel bakedSided = updateTexture((IRetexturableModel) modelS);
                    event.modelRegistry.putObject(new ModelResourceLocation(resouceName, "normal"), bakedSided);
                    event.modelRegistry.putObject(new ModelResourceLocation(resouceName, "inventory"), bakedSided);
                }
            } catch (IOException e) {
                throw new IllegalArgumentException();
            }
        }
    }
    @SubscribeEvent
    public void textureStitch(TextureStitchEvent.Pre event) {
        String[] BlockTex=new String[]{"kawara"};
        TextureMap textureMap = event.map;
        // TEX.addAll(MainInit.logCont.getTexList());
        for (String s : BlockTex) {
            ResourceLocation ret = new ResourceLocation(Constants.getBlockTexPath()+s);
            textureMap.registerSprite(ret);
        }
    }

    //単純テクスチャーの張替え
    public IBakedModel updateTexture(IRetexturableModel model) {
        return new RetexModel(model);
    }

    static class RetexModel implements ISmartBlockModel, ISmartItemModel {

        IRetexturableModel retexturableModel;
        private Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>() {
            public TextureAtlasSprite apply(ResourceLocation location) {
                return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
            }
        };

        RetexModel(IRetexturableModel model) {
            retexturableModel = model;
        }

        @Override
        public IBakedModel handleItemState(ItemStack stack) {
            ImmutableMap map;
            if (stack.getItem() instanceof IRetexture) {
                // Itemパターン
                map = ((IRetexture) stack.getItem()).getTextureMap(stack.getItemDamage());
            } else if (stack.getItem() instanceof ItemBlock) {
                // Blockパターン
                Block block = ((ItemBlock) stack.getItem()).block;
                if (block instanceof IRetexture) {
                    map = ((IRetexture) block).getTextureMap(stack.getItemDamage());
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                throw new IllegalArgumentException();
            }
            return retexturableModel.retexture(map).bake(retexturableModel.getDefaultState(), Attributes.DEFAULT_BAKED_FORMAT, textureGetter);
        }

        @Override
        public IBakedModel handleBlockState(IBlockState state) {
            ImmutableMap map;
            if (state.getBlock() instanceof IRetexture) {
                map = ((IRetexture) state.getBlock()).getTextureMap(state.getBlock().getMetaFromState(state));
            } else {
                throw new IllegalArgumentException();
            }
            return retexturableModel.retexture(map).bake(retexturableModel.getDefaultState(), Attributes.DEFAULT_BAKED_FORMAT, textureGetter);
        }

        @Override
        public List<BakedQuad> getFaceQuads(EnumFacing p_177551_1_) {
            return null;
        }

        @Override
        public List<BakedQuad> getGeneralQuads() {
            return null;
        }

        @Override
        public boolean isAmbientOcclusion() {
            return false;
        }

        @Override
        public boolean isGui3d() {
            return false;
        }

        @Override
        public boolean isBuiltInRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return null;
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return null;
        }

    }


}
