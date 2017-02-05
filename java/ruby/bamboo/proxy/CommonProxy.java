package ruby.bamboo.proxy;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ruby.bamboo.api.Constants;
import ruby.bamboo.block.decoration.DecorationFactory;
import ruby.bamboo.core.BambooCore;
import ruby.bamboo.core.PacketDispatcher;
import ruby.bamboo.core.init.DataLoader;
import ruby.bamboo.core.init.EntityRegister;
import ruby.bamboo.crafting.BambooRecipes;
import ruby.bamboo.crafting.CraftingHandler;
import ruby.bamboo.fluid.HotSpring;
import ruby.bamboo.generate.GenerateHandler;
import ruby.bamboo.gui.GuiHandler;
import ruby.bamboo.item.katana.CounterManager;
import ruby.bamboo.item.katana.KatanaDrops;
import ruby.bamboo.tileentity.TileCampfire;
import ruby.bamboo.tileentity.TileJPChest;
import ruby.bamboo.tileentity.TileMillStone;
import ruby.bamboo.tileentity.TileSpringWater;

/**
 * サーバープロクシ
 *
 * @author Ruby
 *
 */
public class CommonProxy {
    List<String> registedList = Lists.newArrayList();

    public void preInit() {
        // ブロック登録前に登録が必要？
        registerFluid();
        // ブロックアイテム初期化
        try {
            FMLLog.info("********** BambooMod Data Init Start **********");
            // アノテーション付きブロック
            DataLoader loader = new DataLoader();
            registedList.addAll(loader.init(Constants.BLOCK_PACKAGE));

            // デコレーチョンブロック
            new DecorationFactory().register();

            // あいてむ
            registedList.addAll(loader.init(Constants.ITEM_PACKAGE));

            FMLLog.info("********** BambooMod Data Init END **********");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // じぇねれーた
        GenerateHandler gen = new GenerateHandler();
        GameRegistry.registerWorldGenerator(gen, 1);
        // クラフトハンドラ
        MinecraftForge.EVENT_BUS.register(new CraftingHandler());
        registTileEntity();
        // えんてぃてぃ
        new EntityRegister().entityRegist();
        PacketDispatcher.init();
        new KatanaDrops().regist();
    }

    public void init() {
        this.registRecipe();
        MinecraftForge.EVENT_BUS.register(new CounterManager());
    }

    public void postInit() {
        NetworkRegistry.INSTANCE.registerGuiHandler(BambooCore.instance, new GuiHandler());
    }

    // 鉱石名等登録
    private void registRecipe() {
        BambooRecipes recipeIns = new BambooRecipes();
        recipeIns.addRecipes();
    }

    private void registerFluid() {
        FluidRegistry.registerFluid(new HotSpring());
    }

    private void registTileEntity() {
        GameRegistry.registerTileEntity(TileJPChest.class, "jpchest");
        GameRegistry.registerTileEntity(TileSpringWater.class, "spring_water");
        GameRegistry.registerTileEntity(TileCampfire.class, "campfire");
        GameRegistry.registerTileEntity(TileMillStone.class, "millstone");
    }

    public EntityPlayer getPlayer() {
        return null;
    }

}
