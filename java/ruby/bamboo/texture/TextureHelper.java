package ruby.bamboo.texture;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ruby.bamboo.api.Constants;

public class TextureHelper {

    public TextureHelper() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void modelBake(ModelBakeEvent event) {
        try {
            for (ModelResourceLocation ml : event.getModelRegistry().getKeys()) {
                if (ml.getResourceDomain().equals(Constants.MODID)) {
                    Block block = Block.getBlockFromName(Constants.RESOURCED_DOMAIN + ml.getResourcePath());
                    Item item = Item.getByNameOrId(Constants.RESOURCED_DOMAIN + ml.getResourcePath());

                    if (ml.getVariant().equals("inventory")) {
                        if (item instanceof IMultiTextuer) {
                            //ItemLayerModelrの取得法がわからない上に、メタ別の判別不可？、普通にmodel対応。
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
                        }
                    } else {
                        // block
                        if (block instanceof IMultiTextuer) {
                            // 生Modelを取得し、適当なスプライトをぶっこんで焼く。
                            IModel rawModel = ModelLoaderRegistry.getModel(ml);
                            IBakedModel bakedModel = rawModel
                                    .bake(rawModel.getDefaultState(), Attributes.DEFAULT_BAKED_FORMAT, (rl) -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(((IMultiTextuer) block).getTexName(ml.getVariant())));
                            event.getModelRegistry().putObject(ml, bakedModel);
                        }

                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void textureStitch(TextureStitchEvent.Pre event) {
        String[] TEX = new String[] { "bamboopane2", "bamboopane3", "ranma" };
        TextureMap textureMap = event.getMap();
        for (String s : TEX) {
            ResourceLocation ret = new ResourceLocation(Constants.RESOURCED_DOMAIN + "blocks/" + s);
            textureMap.registerSprite(ret);
        }
    }

}
