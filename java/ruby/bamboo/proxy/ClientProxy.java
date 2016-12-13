package ruby.bamboo.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLLog;
import ruby.bamboo.api.BambooBlocks;
import ruby.bamboo.api.BambooItems;
import ruby.bamboo.api.Constants;
import ruby.bamboo.block.IBlockColorWrapper;
import ruby.bamboo.block.ICustomState;
import ruby.bamboo.block.decoration.DecorationClientFactory;
import ruby.bamboo.core.client.KeyBindFactory;
import ruby.bamboo.core.init.BambooData.BambooBlock;
import ruby.bamboo.core.init.BambooData.BambooBlock.StateIgnore;
import ruby.bamboo.core.init.EntityRegister;
import ruby.bamboo.item.itemblock.IEnumTex;
import ruby.bamboo.item.itemblock.IItemColorWrapper;
import ruby.bamboo.item.itemblock.ISubTexture;
import ruby.bamboo.texture.TextureHelper;
import ruby.bamboo.tileentity.TileCampfire;
import ruby.bamboo.tileentity.TileMillStone;
import ruby.bamboo.tileentity.render.RenderCampfire;
import ruby.bamboo.tileentity.render.RenderMillStone;

/**
 * クライアントプロクシ
 *
 * @author Ruby
 *
 */
public class ClientProxy extends CommonProxy {

    Map<ModelResourceLocation, Item> modelMap = Maps.newHashMap();

    @Override
    public void preInit() {
        super.preInit();
        this.registJson();
        new DecorationClientFactory().register();
        // えんてぃてぃれんだー
        new EntityRegister().renderRegist();
        this.registTileRender();
        KeyBindFactory.preInit();
    }

    @Override
    public void init() {
        super.init();
        KeyBindFactory.init();
        MinecraftForge.EVENT_BUS.register(BambooItems.BAMBOO_BOW);
        registColors();

        new TextureHelper(modelMap);
    }

    @Override
    public void postInit() {
        super.postInit();
        //多分使わないのでクリア
        registedList = null;
    }

    /**
     * json登録の自動化
     *
     * setCustomModelResourceLocationの登録ファイル名を変更
     *
     * thx PR defeatedcrow
     */
    private void registJson() {
        List<ItemStack> isList = new ArrayList<ItemStack>();
        List<String> tmpNameList = new ArrayList<String>();
        for (String name : registedList) {
            Block block = Block.getBlockFromName(name);
            Item item = Item.getByNameOrId(name);
            isList.clear();
            item.getSubItems(item, item.getCreativeTab(), isList);
            this.setIgnoreState(block);
            IStateMapper state = this.setCustomState(block);

            if (block instanceof BlockFluidBase) {
                // Fluid系ブロック用処理
                ModelResourceLocation mrl = new ModelResourceLocation(name, "fluid");
                ModelLoader.setCustomMeshDefinition(item, is -> mrl);
                ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
                    @Override
                    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                        return mrl;
                    }
                });
                ModelBakery.registerItemVariants(item, mrl);
            } else if (block != null && state != null) {
                String jsonName = name;
                if (block != null) {
                    BambooBlock anoData = block.getClass().getAnnotation(BambooBlock.class);
                    if (anoData != null && !anoData.jsonName().isEmpty()) {
                        jsonName = anoData.jsonName();
                        ModelResourceLocation mrl = new ModelResourceLocation(jsonName, "inventory");

                        ModelLoader.setCustomModelResourceLocation(item, 0, mrl);
                    }
                }
                Map<IBlockState, ModelResourceLocation> map = state.putStateModelLocations(block);
                for (ModelResourceLocation mrl : map.values()) {
                    modelMap.put(mrl, item);
                }
            }else {
                if (item instanceof ISubTexture) {

                    //                List<ResourceLocation> locList = Lists.newArrayList();
                    for (IEnumTex tex : ((ISubTexture) item).getName()) {
                        String jsonName = tex.getJsonName();
                        //ModelBakery.addVariantName(item, jsonName);
                        //locList.add(new ResourceLocation(jsonName));
                        ModelResourceLocation mrl = new ModelResourceLocation(jsonName, "inventory");

                        ModelLoader.setCustomModelResourceLocation(item, tex.getId(), mrl);
                        modelMap.put(mrl, item);
                    }
                    //                ModelBakery.registerItemVariants(item, locList.toArray(new ResourceLocation[0]));
                } else {
                    for (int i = 0; i < isList.size(); i++) {
                        String jsonName = name;
                        if (block != null) {
                            BambooBlock anoData = block.getClass().getAnnotation(BambooBlock.class);
                            if (anoData != null && !anoData.jsonName().isEmpty()) {
                                jsonName = Constants.RESOURCED_DOMAIN + anoData.jsonName();
                            }
                        }
                        ModelResourceLocation mrl = new ModelResourceLocation(jsonName, "inventory");
                        ModelLoader.setCustomModelResourceLocation(item, i, mrl);
                        modelMap.put(mrl, item);
                    }
                }
            }

            //            new ModelResourceLocation((ResourceLocation)Block.REGISTRY.getNameForObject(state.getBlock()), this.getPropertyString(state.getProperties()));

        }
    }

    public String getPropertyString(Map<IProperty<?>, Comparable<?>> values) {
        StringBuilder stringbuilder = new StringBuilder();

        for (Entry<IProperty<?>, Comparable<?>> entry : values.entrySet()) {
            if (stringbuilder.length() != 0) {
                stringbuilder.append(",");
            }

            IProperty<?> iproperty = (IProperty) entry.getKey();
            stringbuilder.append(iproperty.getName());
            stringbuilder.append("=");
            stringbuilder.append(this.getPropertyName(iproperty, (Comparable) entry.getValue()));
        }

        if (stringbuilder.length() == 0) {
            stringbuilder.append("normal");
        }

        return stringbuilder.toString();
    }

    private <T extends Comparable<T>> String getPropertyName(IProperty<T> property, Comparable<?> value) {
        return property.getName((T) value);
    }

    /**
     * カスタムstate設定
     *
     * @param block
     */
    private IStateMapper setCustomState(Block block) {
        if (block instanceof ICustomState) {
            try {
                IStateMapper state = (IStateMapper) ((ICustomState) block).getCustomState();
                ModelLoader.setCustomStateMapper(block, state);
                return state;
            } catch (Exception e) {
                FMLLog.warning(block.getClass().getName() + ": Custom State Error");
            }
        }
        return null;
    }

    /**
     * stateをmodel参照時無視する
     *
     * @param <T>
     */
    private <T> void setIgnoreState(T obj) {
        Method method = this.getMethod(obj, StateIgnore.class);

        if (method != null) {
            try {
                IProperty[] prop = (IProperty[]) method.invoke(obj);
                if (prop != null) {
                    ModelLoader.setCustomStateMapper((Block) obj, (new StateMap.Builder()).ignore(prop).build());
                }
            } catch (Exception e) {
                FMLLog.warning(obj.getClass().getName() + "Ignore State Error");
            }
        }
    }

    /**
     * アノテーション付きメソッド探索
     *
     * @param obj
     * @param ano
     * @return
     */
    private <T> Method getMethod(T obj, Class<? extends Annotation> ano) {
        if (obj == null) {
            return null;
        }
        Method method = null;
        for (Method e : obj.getClass().getDeclaredMethods()) {
            if (e.getAnnotation(ano) != null) {
                method = e;
                break;
            }
        }
        return method;
    }

    private void registColors() {
        List<Block> colorBlockList = registedList.stream().map(Block::getBlockFromName).filter(ins -> ins instanceof IBlockColorWrapper).collect(Collectors.toList());
        List<Item> colorItemList = registedList.stream().map(Item::getByNameOrId).filter(ins -> ins instanceof IItemColorWrapper).collect(Collectors.toList());

        //Block用色乗算
        colorBlockList
                .forEach(colorBlock -> Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((state, worldIn, pos, tintIndex) -> ((IBlockColorWrapper) colorBlock).colorMultiplier(state, worldIn, pos, tintIndex), colorBlock));
        //Item用
        colorItemList.forEach(colorItem -> Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> ((IItemColorWrapper) colorItem).getColorFromItemstack(stack, tintIndex), colorItem));

    }

    private void registTileRender() {
        registTESR(Item.getItemFromBlock(BambooBlocks.CAMPFIRE), TileCampfire.class, new RenderCampfire());
        registTESR(Item.getItemFromBlock(BambooBlocks.MILLSTONE), TileMillStone.class, new RenderMillStone());
    }

    private void registTESR(Item item, Class<? extends TileEntity> cls, TileEntitySpecialRenderer render) {
        ClientRegistry.bindTileEntitySpecialRenderer(cls, render);
        ForgeHooksClient.registerTESRItemStack(item, 0, cls);
    }

    @Override
    public EntityPlayer getPlayer() {
        return FMLClientHandler.instance().getClient().thePlayer;
    }
}
