package ruby.bamboo.crafting;

import static ruby.bamboo.api.BambooBlocks.*;
import static ruby.bamboo.api.BambooItems.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import ruby.bamboo.api.BambooBlocks;
import ruby.bamboo.block.decoration.EnumDecoration;
import ruby.bamboo.entity.EnumSlideDoor;
import ruby.bamboo.item.arrow.AntiArrow.AntiType;

public class BambooRecipes {
    private int WILD = Short.MAX_VALUE;
    private String bamboo = "bamboo";
    private String tudura = "tudura";
    private String straw = "straw";
    private String plankWood = "plankWood";

    /**
     * 鉱石辞書
     */
    public void oreDicRegist() {
        OreDictionary.registerOre("logWood", getIS(SAKURA_LOG));
        OreDictionary.registerOre("plankWood", getIS(SAKURA_PLANKS));
        OreDictionary.registerOre(bamboo, getIS(BAMBOO));
        OreDictionary.registerOre(tudura, getIS(TUDURA));
        OreDictionary.registerOre(straw, getIS(STRAW));
    }

    /**
     * クラフトテーブル
     */
    public void craftingTableRecipes() {
        // サクラ原木→木材
        addShapelessRecipe(getIS(SAKURA_PLANKS, 4, 0), getIS(SAKURA_LOG, 1, WILD));
        // 袋開放
        addShapelessRecipe(getIS(SACK), getIS(SACK, 1, WILD));
        // つづら
        addRecipe(getIS(TUDURA), " B ", "B B", " B ", 'B', bamboo);
        // 扇子
        addRecipe(getIS(FOLDING_FAN), "PPB", "PPB", "BBB", 'P', Items.PAPER, 'B', bamboo);
        // 袋
        addRecipe(getIS(SACK), "SSS", "WTW", "WWW", 'S', Items.STRING, 'T', tudura, 'W', getIS(Blocks.WOOL, 1, WILD));
        // たんす
        addCircleRecipe(getIS(JPCHEST), tudura, "logWood");
        // たたみ
        addRecipe(getIS(TATAMI), " S ", "STS", " S ", 'S', straw, 'T', tudura);
        // 引き戸類
        addRecipe(getIS(ITEM_SLIDE_DOOR, 2, EnumSlideDoor.HUSUMA.getId()), "XYX", "X#X", "XYX", 'X', Items.STICK, 'Y', Items.PAPER, '#', tudura);
        addRecipe(getIS(ITEM_SLIDE_DOOR, 2, EnumSlideDoor.SHOZI.getId()), "XYX", "Y#Y", "XYX", '#', tudura, 'X', Items.STICK, 'Y', Items.PAPER);
        addRecipe(getIS(ITEM_SLIDE_DOOR, 2, EnumSlideDoor.GLASS.getId()), "XYX", "X#X", "XYX", '#', tudura, 'X', Blocks.IRON_BARS, 'Y', Blocks.GLASS_PANE);
        addRecipe(getIS(ITEM_SLIDE_DOOR, 2, EnumSlideDoor.GGLASS.getId()), "XYX", "X#X", "XYX", '#', tudura, 'X', Items.STICK, 'Y', Blocks.GLASS_PANE);
        addRecipe(getIS(ITEM_SLIDE_DOOR, 2, EnumSlideDoor.YUKI.getId()), "XYX", "X#X", "XZX", '#', tudura, 'X', Items.STICK, 'Y', Items.PAPER, 'Z', Blocks.GLASS_PANE);
        addRecipe(getIS(ITEM_SLIDE_DOOR, 2, EnumSlideDoor.AMADO.getId()), "XYX", "X#X", "XYX", '#', tudura, 'X', Items.STICK, 'Y', "plankWood");
        // きつねび
        addRecipe(getIS(KITUNEBI, 6, 0), "XXX", "Y#Y", "XXX", 'X', "gemLapis", 'Y', tudura, '#', Blocks.LIT_PUMPKIN);
        addRecipe(getIS(KITUNEBI, 6, 0), "XXX", "Y#Y", "XXX", 'X', Items.ENDER_PEARL, 'Y', tudura, '#', Blocks.LIT_PUMPKIN);
        addRecipe(getIS(KITUNEBI, 6, 0), "XYX", "X#X", "XYX", 'X', "gemLapis", 'Y', tudura, '#', Blocks.LIT_PUMPKIN);
        addRecipe(getIS(KITUNEBI, 6, 0), "XYX", "X#X", "XYX", 'X', Items.ENDER_PEARL, 'Y', tudura, '#', Blocks.LIT_PUMPKIN);
        // 柵
        addRecipe(getIS(BAMBOO_PANE, 8, 0), "XXX", "XXX",  'X', bamboo);
        addRecipe(getIS(BAMBOO_PANE, 8, 1), "XYX", "XYX",  'X', bamboo, 'Y',getIS(BAMBOO_PANE, 8, 0));
        addRecipe(getIS(BAMBOO_PANE, 8, 2), "XYX", "XYX",  'X', bamboo, 'Y',getIS(BAMBOO_PANE, 8, 1));
        addRecipe(getIS(BAMBOO_PANE, 8, 3), "XYX", "XXX",  'X',plankWood , 'Y', tudura);
        // おんせん
        addRecipe(getIS(SPRING_BLOCK),"X#X","XYX","XZX",'X',Blocks.COBBLESTONE,'#',TUDURA,'Y',Items.WATER_BUCKET,'Z',Items.LAVA_BUCKET);

        //******デコレーション
        // 瓦
        addCircleRecipe(getIS(KAWARA, 8, 0), tudura, Items.BRICK);
        // 漆喰
        addCircleRecipe(getIS(PLASTER, 8, 0), tudura, Blocks.SAND);
        // なまこ
        addAltCircleRecipe(getIS(NAMAKO, 8, 0), tudura, getIS(PLASTER), getIS(KAWARA));
        // ワラ
        addRecipe(getIS(WARA, 4, 0), "XXX", "XXX", "XXX", 'X', getIS(STRAW));
        // かやぶき
        addCircleRecipe(getIS(KAYA, 8, 0), tudura, "cropWheat");
        // 市松各種
        addCircleRecipe(getIS(CBIRCH, 8, 0), tudura, new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
        addCircleRecipe(getIS(COAK, 8, 0), tudura, new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.OAK.getMetadata()));
        addCircleRecipe(getIS(CPINE, 8, 0), tudura, new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
        // デコレーション用半ブロと階段の登録
        registerDeco();

        //***弓とか槍とか
        addRecipe(getIS(BAMBOO_BOW), " BS", "T S", " BS", 'B', bamboo, 'T', tudura, 'S', Items.STRING);
        addRecipe(getIS(BAMBOO_ARROW, 8, 0), "B", "T", "T", 'B', bamboo, 'T', tudura);
        // アンチ系
        addAntiArrowsRecipe(AntiType.UNDEAD, 4, Items.ROTTEN_FLESH);
        addAntiArrowsRecipe(AntiType.MUTANT, 8, Items.GUNPOWDER);
        addAntiArrowsRecipe(AntiType.ENDERMAN, 8, Items.ENDER_PEARL);
        addAntiArrowsRecipe(AntiType.FLAME, 8, Items.FLINT);
        addAntiArrowsRecipe(AntiType.AIR, 8, Items.FEATHER);
        addAntiArrowsRecipe(AntiType.MULTILEG, 8, Items.STRING);
        addAntiArrowsRecipe(AntiType.HUMAN, 8, Items.EMERALD);
        addAntiArrowsRecipe(AntiType.NONLEG, 8, Items.BLAZE_ROD);
        addAntiArrowsRecipe(AntiType.HARD, 16, Items.IRON_INGOT);
        addAntiArrowsRecipe(AntiType.SOFT, 8, Items.SLIME_BALL);
        addAntiArrowsRecipe(AntiType.WATER, 8, Items.FISH);
        addShapelessRecipe(getIS(TORCH_ARROW), getIS(BAMBOO_ARROW), Blocks.TORCH);
        addShapelessRecipe(getIS(LIGHT_ARROW), getIS(BAMBOO_ARROW), Items.FEATHER);
        addShapelessRecipe(getIS(EXPLODE_ARROW), getIS(BAMBOO_ARROW), Items.GUNPOWDER);

    }

    private void addAntiArrowsRecipe(AntiType type, int ammo, Object material) {
        addRecipe(getIS(ANTI_ARROW, ammo, type.getID()), " B ", " T ", "MTM", 'B', bamboo, 'T', tudura, 'M', material);
    }

    private void registerDeco() {
        byte typeFlag;
        for (EnumDecoration deco : EnumDecoration.values()) {
            if (deco.isHalf()) {
                addSlabRecipe(getBlockIS(deco.getModName() + EnumDecoration.SLAB, 6, 0), getBlockIS(deco.getModName(), 1, 0));
            }
            if (deco.isStair()) {
                addStairsRecipe(getBlockIS(deco.getModName() + EnumDecoration.STAIRS, 4, 0), getBlockIS(deco.getModName(), 1, 0));
            }
        }
    }

    /**
     * 竈
     */
    public void smeltingRecipes() {
        GameRegistry.addSmelting(getIS(SAKURA_LOG), getIS(Items.COAL, 1, 1), 0.2F);
    }

    /**
     * 燃料
     */
    public void registFuel() {
        IFuelHandler handler = new IFuelHandler() {
            private Item sakuraLog = Item.getItemFromBlock(BambooBlocks.SAKURA_LOG);

            @Override
            public int getBurnTime(ItemStack fuel) {
                if (fuel.getItem() == sakuraLog) {
                    return 270;
                }
                return 0;
            }
        };
        GameRegistry.registerFuelHandler(handler);
    }

    /**
     * 種登録
     */
    public void registSeed() {
        MinecraftForge.addGrassSeed(getIS(RICE_SEED), 10);
    }

    /**
     * 囲みレシピ
     * XXX
     * X#X
     * XXX
     */
    private void addCircleRecipe(ItemStack out, Object center, Object outer) {
        addRecipe(out, "XXX", "X#X", "XXX", '#', center, 'X', outer);
    }

    /**
     * 交互囲みレシピ
     * XYX
     * Y#Y
     * XYX
     */
    private void addAltCircleRecipe(ItemStack out, Object center, Object one, Object two) {
        addRecipe(out, "XYX", "Y#Y", "XYX", '#', center, 'X', one, 'Y', two);
    }

    /**
     * 半ブロレシピ(復元含)
     * XXX
     */
    private void addSlabRecipe(ItemStack out, ItemStack itemStack) {
        addRecipe(out, "XXX", 'X', itemStack);
        addRecipe(new ItemStack(itemStack.getItem(), 1, 0), "X", "X", 'X', out);
    }

    /**
     * 階段レシピ(反転含)
     * X
     * XX
     * XXX
     */
    private void addStairsRecipe(ItemStack out, Object material) {
        addRecipe(out, "X  ", "XX ", "XXX", 'X', material);
        addRecipe(out, "  X", " XX", "XXX", 'X', material);
    }

    private void addRecipe(ItemStack output, Object... params) {
        GameRegistry.addRecipe(new ShapedOreRecipe(output, params));
    }

    private void addShapelessRecipe(Object... objArray) {
        ItemStack output;
        Object[] params = new Object[objArray.length - 1];

        output = (ItemStack) objArray[0];

        for (int i = 1; i < objArray.length; i++) {
            params[i - 1] = objArray[i];
        }
        GameRegistry.addRecipe(new ShapelessOreRecipe(output, params));
    }

    private ItemStack getIS(Block block) {
        return this.getIS(block, 0);
    }

    private ItemStack getIS(Block block, int meta) {
        return this.getIS(block, 1, meta);
    }

    private ItemStack getIS(Block block, int amo, int meta) {
        return new ItemStack(block, amo, meta);
    }

    private ItemStack getIS(Item item) {
        return this.getIS(item, 0);
    }

    private ItemStack getIS(Item item, int meta) {
        return this.getIS(item, 1, meta);
    }

    private ItemStack getIS(Item item, int amo, int meta) {
        return new ItemStack(item, amo, meta);
    }

    private ItemStack getBlockIS(String name) {
        return getBlockIS(name, 1, 0);
    }

    private ItemStack getBlockIS(String name, int amo, int meta) {
        return new ItemStack(Block.getBlockFromName(name), amo, meta);
    }

    private ItemStack getItemIS(String name, int amo, int meta) {
        return new ItemStack(Item.getByNameOrId(name), amo, meta);
    }
}
