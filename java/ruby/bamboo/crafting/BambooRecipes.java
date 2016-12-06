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
    public final String bamboo = "bamboo";
    public final String tudura = "tudura";
    public final String plankWood = "plankWood";

    public final String cookingRice = "cookingRice";
    public final String busket = "bambooBasket";
    public final String crop_rice = "cropRice";
    public final String log_sakura = "logCherry";
    public final String crop_straw = "cropStraw";
    public final String natto = "natto";
    public final String zunda = "zunda";
    public final String soy_beans = "soybeans";
    public final String red_beans = "redbeans";
    public final String mochi = "mochi";
    public final String cooled_mochi = "cookedMochi";
    public final String flour = "foodFlour";
    public final String dough = "foodDough";
    public final String tofu_kinu = "tofuKinu";
    public final String men = "foodNoodle";
    public final String seaweed = "foodSeaweed";
    public final String tomato = "foodTomato";

    /**
     * 鉱石辞書
     */
    public void oreDicRegist() {
        OreDictionary.registerOre("logWood", getIS(SAKURA_LOG));
        OreDictionary.registerOre("plankWood", getIS(SAKURA_PLANKS));
        OreDictionary.registerOre(bamboo, getIS(BAMBOO));
        OreDictionary.registerOre(tudura, getIS(TUDURA));
        OreDictionary.registerOre(crop_straw, getIS(STRAW));
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
        addRecipe(getIS(TATAMI), " S ", "STS", " S ", 'S', crop_straw, 'T', tudura);
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
        addRecipe(getIS(BAMBOO_PANE, 8, 0), "XXX", "XXX", 'X', bamboo);
        addRecipe(getIS(BAMBOO_PANE, 8, 1), "XYX", "XYX", 'X', bamboo, 'Y', getIS(BAMBOO_PANE, 8, 0));
        addRecipe(getIS(BAMBOO_PANE, 8, 2), "XYX", "XYX", 'X', bamboo, 'Y', getIS(BAMBOO_PANE, 8, 1));
        addRecipe(getIS(BAMBOO_PANE, 8, 3), "XYX", "XXX", 'X', plankWood, 'Y', tudura);
        // おんせん
        addRecipe(getIS(SPRING_BLOCK), "X#X", "XYX", "XZX", 'X', Blocks.COBBLESTONE, '#', TUDURA, 'Y', Items.WATER_BUCKET, 'Z', Items.LAVA_BUCKET);
        // 囲炉裏
        addRecipe(getIS(CAMPFIRE), " # ", "XYX", "ZZZ", 'X', Blocks.IRON_BARS, '#', TUDURA, 'Y', Items.FLINT_AND_STEEL, 'Z', getIS(Items.COAL, 1, 1));
        // 行灯
        addRecipe(getIS(ANDON, 1, 0), "###", "#Y#", "#X#", '#', Items.STICK, 'X', Blocks.TORCH, 'Y', TUDURA);
        // 布団
        addRecipe(getIS(HUTON, 1, 0), " # ", "XXX", "XXX", '#', TUDURA, 'X', Blocks.WOOL);

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

    public void addCookingRecipe() {
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 1), Items.BEEF, crop_rice);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 2), Items.PORKCHOP, crop_rice);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 3), Blocks.BROWN_MUSHROOM, crop_rice);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 2, 4), Items.PORKCHOP, BAMBOO);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 2, 5), Items.BEEF, BAMBOO);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 6), BAMBOOSHOOT, crop_rice);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 7), Items.EGG, crop_rice);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 8), Items.EGG, Items.CHICKEN, crop_rice);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 9), new ItemStack(Items.FISH, 1, 0), crop_rice);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 2, 10), Items.CHICKEN, BAMBOO);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 11), crop_rice, seaweed);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 12), crop_rice, seaweed, new ItemStack(Items.FISH, 1, 1));
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 13), crop_rice, seaweed, new ItemStack(Items.FISH, 1, 0), Items.EGG);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 14), crop_rice, seaweed, Blocks.BROWN_MUSHROOM);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 15), crop_rice, seaweed, BAMBOOSHOOT);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 16), crop_rice, seaweed, seaweed);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 17), new ItemStack(BAMBOO_FOOD, 1, 22), BAMBOO, Items.SUGAR, Items.SUGAR, soy_beans);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 18), new ItemStack(BAMBOO_FOOD, 1, 22), BAMBOO, Items.SUGAR, red_beans);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 19), new ItemStack(BAMBOO_FOOD, 1, 22), BAMBOO, Items.SUGAR, Items.SUGAR);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 20), new ItemStack(BAMBOO_FOOD, 1, 22), BAMBOO, Items.SUGAR, new ItemStack(SAKURA_LEAVE, 1, WILD), new ItemStack(Blocks.TALLGRASS, 1, WILD));
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 21), new ItemStack(BAMBOO_FOOD, 1, 22), BAMBOO, Items.SUGAR, zunda, zunda);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 24), new ItemStack(BAMBOO_FOOD, 1, 22), Items.SUGAR, Items.SUGAR, soy_beans);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 25), new ItemStack(BAMBOO_FOOD, 1, 22), Items.SUGAR, red_beans);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 26), new ItemStack(BAMBOO_FOOD, 1, 22), Items.SUGAR, zunda, zunda);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 27), crop_straw, soy_beans);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 28), crop_straw, soy_beans, crop_rice);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 29), crop_straw, soy_beans, crop_rice, Items.EGG);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 30), new ItemStack(BAMBOO_FOOD, 1, 22), new ItemStack(SAKURA_LEAVE, 1, WILD));
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 31), Items.BEEF, crop_rice, Items.EGG);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 32), Items.PORKCHOP, crop_rice, Items.EGG);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 33), crop_rice, red_beans);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 34), crop_rice, red_beans, seaweed);
        CookingManager.addShapelessRecipe(new ItemStack(Items.PUMPKIN_PIE, 2), dough, Items.SUGAR, Blocks.PUMPKIN);
        CookingManager.addShapelessRecipe(new ItemStack(Items.CAKE, 2, 0), dough, Items.SUGAR, Items.MILK_BUCKET);
        CookingManager.addShapelessRecipe(new ItemStack(Items.COOKIE, 4), dough, Items.SUGAR, new ItemStack(Items.DYE, 1, 3));
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 35), soy_beans, Items.WATER_BUCKET);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 36), tofu_kinu, flour);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 38), men, soy_beans, Items.WATER_BUCKET, Items.FISH);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 39), men, soy_beans, Items.WATER_BUCKET, Items.FISH, Items.EGG);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 40), men, soy_beans, Items.WATER_BUCKET, Items.FISH, Items.EGG, seaweed, Items.PORKCHOP);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 41), dough, tomato, Items.MILK_BUCKET, Items.PORKCHOP);
        CookingManager.addShapelessRecipe(new ItemStack(BAMBOO_FOOD, 1, 42), new ItemStack(Items.FISH, 1, 1), crop_rice);
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
