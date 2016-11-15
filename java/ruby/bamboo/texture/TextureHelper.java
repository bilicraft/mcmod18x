package ruby.bamboo.texture;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ruby.bamboo.api.Constants;

public class TextureHelper {
    Set<String> texSet = Sets.newHashSet();
    final Map<ModelResourceLocation, Item> modelMap;

    public TextureHelper(Map<ModelResourceLocation, Item> modelMap) {
        MinecraftForge.EVENT_BUS.register(this);
        this.modelMap = ImmutableMap.copyOf(modelMap);
    }

    @SubscribeEvent
    public void modelBake(ModelBakeEvent event) {
        try {
            for (ModelResourceLocation ml : event.getModelRegistry().getKeys()) {

                if (ml.getResourceDomain().equals(Constants.MODID)) {
                    Block block = Block.getBlockFromName(Constants.RESOURCED_DOMAIN + ml.getResourcePath());
                    //MRLとItemを紐付ける逆引き手段が必要
                    //                    Item item = Item.getByNameOrId(Constants.RESOURCED_DOMAIN + ml.getResourcePath());
                    Item item = modelMap.get(ml);
                    if (ml.getVariant().equals("inventory")) {
                        if (item instanceof IMultiTextuerItem) {
                            //とりあえず普通にmodel対応。
                            //                            ResourceLocation roc = new ResourceLocation(Constants.RESOURCED_DOMAIN + ((IMultiTextuer) item).getTexname(ml.getVariant()));
                            //                            IModel rawModel = new ItemLayerModel(ImmutableList.of(roc));
                            //                            IBakedModel rawModel = event.getModelRegistry().getObject(ml);

                            //                            IModel retexedModel = rawModel.retexture(ImmutableMap.of("layer0", ((IMultiTextuer) item).getTexname(ml.getVariant())));
                            //                            IBakedModel bakedModel = retexedModel.bake(rawModel.getDefaultState(), Attributes.DEFAULT_BAKED_FORMAT, (rl) -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(rl.toString()));
                            //                            IModel rawModel = ModelLoaderRegistry.getModel(ml);
                            //                            IModel rawModel = ModelLoaderRegistry.getModel(ml);
                            //                            IBakedModel bakedModel = rawModel
                            //                                    .bake(rawModel.getDefaultState(), Attributes.DEFAULT_BAKED_FORMAT, (rl) -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(((IMultiTextuer) item).getTexname(ml.getVariant())));

                            //                            event.getModelRegistry().putObject(ml, rawModel.bake(rawModel.getDefaultState(), Attributes.DEFAULT_BAKED_FORMAT, (rl) -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(rl.toString())));
                            //                            event.getModelRegistry().putObject(ml, rawModel);
                            //                            IModel rawModel=new ItemLayerModel(ImmutableList.of());
                            //                            event.getModelRegistry().putObject(ml,rawModel.bake(rawModel.getDefaultState(), DefaultVertexFormats.ITEM,(rl) -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(rl.toString())));
//
//                            IBakedModel bakedModel = event.getModelRegistry().getObject(ml);
//
//                            event.getModelRegistry().putObject(ml, new ItemBakedWrapper(bakedModel));

                        }
                    } else {
                        // block
                        if (block instanceof IMultiTextuerBlock) {

                            // 生Modelを取得し、適当なスプライトをぶっこんで焼く。
                            IModel rawModel = ModelLoaderRegistry.getModel(ml);
                            event.getModelRegistry().putObject(ml, new BakedWrapper(rawModel));
                        }

                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Item用IBakedModelらっぱー
     */
    class ItemBakedWrapper implements IBakedModel {
        IBakedModel bakedModel;
        ItemOverrideList wrapedList;

        ItemBakedWrapper(IBakedModel bakedModel) {
            this.bakedModel = bakedModel;
        }

        @Override
        public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
//            IBakedModel newBakedModel = ((OverrideListWrapper) this.getOverrides()).newBakedModel;
//            if (bakedModel != newBakedModel) {
//                if (newBakedModel == null) {
//                    return ImmutableList.of();
//                }
//                bakedModel = newBakedModel;
//            }
            return bakedModel.getQuads(state, side, rand);
        }

        @Override
        public boolean isAmbientOcclusion() {
            if (bakedModel != null) {
                return bakedModel.isAmbientOcclusion();
            }
            return false;
        }

        @Override
        public boolean isGui3d() {
            if (bakedModel != null) {
                return bakedModel.isGui3d();
            }
            return false;
        }

        @Override
        public boolean isBuiltInRenderer() {
            if (bakedModel != null) {
                return bakedModel.isBuiltInRenderer();
            }
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            if (bakedModel != null) {
                return bakedModel.getParticleTexture();
            }
            return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            if (bakedModel != null) {
                return bakedModel.getItemCameraTransforms();
            }
            return ItemCameraTransforms.DEFAULT;
        }

        @Override
        public ItemOverrideList getOverrides() {
            if (wrapedList == null) {
                wrapedList = new OverrideListWrapper(bakedModel.getOverrides().getOverrides());
            }
            return wrapedList;
        }

    }

    /**
     * ItemLayer用
     */
    class OverrideListWrapper extends ItemOverrideList {
//        public IBakedModel newBakedModel;

        public OverrideListWrapper(List<ItemOverride> overridesIn) {
            super(overridesIn);
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
//            if (newBakedModel == null) {
//                if (stack.getItem() instanceof IMultiTextuerItem) {
//                    String texPath = ((IMultiTextuerItem) stack.getItem()).getTexName(stack);
//                    IModel rawModel = new ItemLayerModel(ImmutableList.of(new ResourceLocation(texPath)));
//                    newBakedModel = rawModel.bake(rawModel.getDefaultState(), DefaultVertexFormats.ITEM, (rl) -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(rl.toString()));
//                    newBakedModel=originalModel;
//                }
//            }
            return super.handleItemState(originalModel, stack, world, entity);
        }

    }

    /**
     * Block用IBakedModelらっぱー
     */
    class BakedWrapper implements IBakedModel {
        IBakedModel bakedModel;
        IModel rawModel;

        BakedWrapper(IModel rawModel) {
            this.rawModel = rawModel;
        }

        @Override
        public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
            if (bakedModel == null) {
                String texPath = ((IMultiTextuerBlock) state.getBlock()).getTexName(state, side);
                texSet.add(texPath);
                bakedModel = rawModel.bake(rawModel.getDefaultState(), Attributes.DEFAULT_BAKED_FORMAT, rl -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texPath));
            }
            return bakedModel.getQuads(state, side, rand);
        }

        @Override
        public boolean isAmbientOcclusion() {
            if (bakedModel != null) {
                return bakedModel.isAmbientOcclusion();
            }
            return false;
        }

        @Override
        public boolean isGui3d() {
            if (bakedModel != null) {
                return bakedModel.isGui3d();
            }
            return false;
        }

        @Override
        public boolean isBuiltInRenderer() {
            if (bakedModel != null) {
                return bakedModel.isBuiltInRenderer();
            }
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            if (bakedModel != null) {
                return bakedModel.getParticleTexture();
            }
            return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            if (bakedModel != null) {
                return bakedModel.getItemCameraTransforms();
            }
            return ItemCameraTransforms.DEFAULT;
        }

        @Override
        public ItemOverrideList getOverrides() {
            if (bakedModel != null) {
                return bakedModel.getOverrides();
            }
            return ItemOverrideList.NONE;
        }

    }

    @SubscribeEvent
    public void textureStitch(TextureStitchEvent.Pre event) {
        TextureMap textureMap = event.getMap();
        for (String s : texSet) {
            ResourceLocation ret = new ResourceLocation(s);
            textureMap.registerSprite(ret);
        }
        texSet.clear();
    }

}
