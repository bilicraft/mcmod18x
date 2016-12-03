package ruby.bamboo.texture;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
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
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ruby.bamboo.api.Constants;

public class TextureHelper {
    final Map<ModelResourceLocation, Item> modelLocMap;

    public TextureHelper(Map<ModelResourceLocation, Item> modelMap) {
        MinecraftForge.EVENT_BUS.register(this);
        this.modelLocMap = ImmutableMap.copyOf(modelMap);
    }

    @SubscribeEvent
    public void modelBake(ModelBakeEvent event) {
        try {
            for (ModelResourceLocation ml : event.getModelRegistry().getKeys()) {

                if (ml.getResourceDomain().equals(Constants.MODID)) {
                    Block block = Block.getBlockFromName(Constants.RESOURCED_DOMAIN + ml.getResourcePath());
                    //MRLとItemを紐付ける逆引き手段が必要
                    Item item = modelLocMap.get(ml);
                    if (ml.getVariant().equals("inventory")) {
                        if (item instanceof IMultiTextuerItem) {
                            IBakedModel bakedModel = event.getModelRegistry().getObject(ml);
                            //IBakedItemLayerModelは不可視
                            if (bakedModel instanceof IPerspectiveAwareModel) {

                                Map<Integer, String> texMap = ((IMultiTextuerItem) item).getTexName();
                                //jsonコード
                                String jsonCode = "{\'elements\':[{\'from\':[0,0,0],\'to\':[16,16,16],\'faces\':{\'down\':{\'uv\':[0,0,16,16],\'texture\':\'\'}}}],\'display\':{\'ground\':{\'rotation\':[0,0,0],\'translation\':[0,2,0],\'scale\':[0.5,0.5,0.5]},\'head\':{\'rotation\':[0,180,0],\'translation\':[0,13,7],\'scale\':[1,1,1]},\'thirdperson_righthand\':{\'rotation\':[0,0,0],\'translation\':[0,3,1],\'scale\':[0.55,0.55,0.55]},\'firstperson_righthand\':{\'rotation\':[0,-90,25],\'translation\':[1.13,3.2,1.13],\'scale\':[0.68,0.68,0.68]}}}";
                                ModelBlock model = ModelBlock.deserialize(jsonCode.replaceAll("\'", "\""));

                                Map<Integer, IBakedModel> modelMap = Maps.newHashMap();
                                OverrideListWrapper ovrList = new OverrideListWrapper(model.createOverrides().getOverrides()).setModelMap(modelMap);

                                ItemLayerModel rawModel = new ItemLayerModel(ImmutableList.of(), ovrList);
                                ItemCameraTransforms transforms = model.getAllTransforms();
                                Map<TransformType, TRSRTransformation> tMap = Maps.newHashMap();
                                tMap.putAll(IPerspectiveAwareModel.MapWrapper.getTransforms(transforms));
                                tMap.putAll(IPerspectiveAwareModel.MapWrapper.getTransforms(rawModel.getDefaultState()));
                                IModelState perState = new SimpleModelState(ImmutableMap.copyOf(tMap));
                                //retextureしつつ枚数分焼く
                                for (Entry<Integer, String> entry : texMap.entrySet()) {
                                    modelMap.put(entry.getKey(), rawModel.retexture(ImmutableMap.of("layer0", entry.getValue()))
                                            .bake(perState, DefaultVertexFormats.ITEM, (rl) -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(rl.toString())));
                                }

                                event.getModelRegistry().putObject(ml, rawModel.bake(perState, DefaultVertexFormats.ITEM, (rl) -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(rl.toString())));

                            }

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
     * ItemLayer用
     */
    class OverrideListWrapper extends ItemOverrideList {
        public Map<Integer, IBakedModel> modelMap;

        public OverrideListWrapper(List<ItemOverride> overridesIn) {
            super(overridesIn);
        }

        public OverrideListWrapper setModelMap(Map<Integer, IBakedModel> map) {
            this.modelMap = map;
            return this;
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
            if (modelMap != null && modelMap.containsKey(stack.getItemDamage())) {
                return modelMap.get(stack.getItemDamage());
            } else {
                return super.handleItemState(originalModel, stack, world, entity);
            }
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
        Set<String> texSet = Sets.newHashSet();
        for (Entry<ModelResourceLocation, Item> e : modelLocMap.entrySet()) {
            if (e.getValue() instanceof IMultiTextuerItem) {
                for (Entry<Integer, String> e2 : ((IMultiTextuerItem) e.getValue()).getTexName().entrySet()) {
                    texSet.add(e2.getValue());
                }
            }
        }
        TextureMap textureMap = event.getMap();
        for (String s : texSet) {
            ResourceLocation ret = new ResourceLocation(s);
            textureMap.registerSprite(ret);
        }
        texSet.clear();
    }

}
