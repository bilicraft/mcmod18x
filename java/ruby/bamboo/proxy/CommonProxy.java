package ruby.bamboo.proxy;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ruby.bamboo.api.Constants;
import ruby.bamboo.block.decoration.DecorationFactory;
import ruby.bamboo.block.tile.TileJPChest;
import ruby.bamboo.core.BambooCore;
import ruby.bamboo.core.PacketDispatcher;
import ruby.bamboo.core.init.DataLoader;
import ruby.bamboo.core.init.EntityRegister;
import ruby.bamboo.crafting.BambooRecipes;
import ruby.bamboo.crafting.CraftingHandler;
import ruby.bamboo.generate.GenerateHandler;
import ruby.bamboo.gui.GuiHandler;

/**
 * サーバープロクシ
 *
 * @author Ruby
 *
 */
public class CommonProxy {
    List<String> registedList = Lists.newArrayList();

    public void preInit() {
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
        GameRegistry.registerTileEntity(TileJPChest.class, "jpchest");
        // えんてぃてぃ
        new EntityRegister().entityRegist();
        PacketDispatcher.init();
    }

    public void init() {
        this.registRecipe();
    }

    public void postInit() {
        NetworkRegistry.INSTANCE.registerGuiHandler(BambooCore.instance, new GuiHandler());
    }

    // 鉱石名等登録
    private void registRecipe() {
        BambooRecipes recipeIns = new BambooRecipes();
        recipeIns.oreDicRegist();
        recipeIns.craftingTableRecipes();
        recipeIns.smeltingRecipes();
        recipeIns.registFuel();
        recipeIns.registSeed();
    }

    public EntityPlayer getPlayer(){
        return null;
    }

}
