package ruby.bamboo.crafting;

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
import ruby.bamboo.block.Bamboo;
import ruby.bamboo.block.JPChest;
import ruby.bamboo.block.Kitunebi;
import ruby.bamboo.block.SakuraLog;
import ruby.bamboo.block.SakuraPlank;
import ruby.bamboo.block.Tatami;
import ruby.bamboo.block.decoration.EnumDecoration;
import ruby.bamboo.core.DataManager;
import ruby.bamboo.entity.EnumSlideDoor;
import ruby.bamboo.item.BambooBow;
import ruby.bamboo.item.FoldingFan;
import ruby.bamboo.item.ItemSlideDoor;
import ruby.bamboo.item.RiceSeed;
import ruby.bamboo.item.Sack;
import ruby.bamboo.item.Straw;
import ruby.bamboo.item.Tudura;
import ruby.bamboo.item.arrow.AntiArrow;
import ruby.bamboo.item.arrow.AntiArrow.AntiType;
import ruby.bamboo.item.arrow.BambooArrow;
import ruby.bamboo.item.arrow.ExplodeArrow;
import ruby.bamboo.item.arrow.LightArrow;
import ruby.bamboo.item.arrow.TorchArrow;

public class BambooRecipes {
    private int WILD = Short.MAX_VALUE;
    private String bamboo = "bamboo";
    private String tudura = "tudura";
    private String straw = "straw";

    /**
     * 鉱石辞書
     */
    public void oreDicRegist() {
        OreDictionary.registerOre("logWood", getIS(SakuraLog.class));
        OreDictionary.registerOre("plankWood", getIS(SakuraPlank.class));
        OreDictionary.registerOre(bamboo, getIS(Bamboo.class));
        OreDictionary.registerOre(tudura, getIS(Tudura.class));
        OreDictionary.registerOre(straw, getIS(Straw.class));
    }

    /**
     * クラフトテーブル
     */
    public void craftingTableRecipes() {
        // サクラ原木→木材
        addShapelessRecipe(getIS(SakuraPlank.class, 4, 0), getIS(SakuraLog.class, 1, WILD));
        // 袋開放
        addShapelessRecipe(getIS(Sack.class), getIS(Sack.class, 1, WILD));
        // つづら
        addRecipe(getIS(Tudura.class), " B ", "B B", " B ", 'B', bamboo);
        // 扇子
        addRecipe(getIS(FoldingFan.class), "PPB", "PPB", "BBB", 'P', Items.PAPER, 'B', bamboo);
        // 袋
        addRecipe(getIS(Sack.class), "SSS", "WTW", "WWW", 'S', Items.STRING, 'T', tudura, 'W', getIS(Blocks.WOOL, 1, WILD));
        // たんす
        addCircleRecipe(getIS(JPChest.class), tudura, "logWood");
        // たたみ
        addRecipe(getIS(Tatami.class), " S ", "STS", " S ", 'S', straw, 'T', tudura);
        // 引き戸類
        addRecipe(getIS(ItemSlideDoor.class, 2, EnumSlideDoor.HUSUMA.getId()), "XYX", "X#X", "XYX", 'X', Items.STICK, 'Y', Items.PAPER, '#', tudura);
        addRecipe(getIS(ItemSlideDoor.class, 2, EnumSlideDoor.SHOZI.getId()), "XYX", "Y#Y", "XYX", '#', tudura, 'X', Items.STICK, 'Y', Items.PAPER);
        addRecipe(getIS(ItemSlideDoor.class, 2, EnumSlideDoor.GLASS.getId()), "XYX", "X#X", "XYX", '#', tudura, 'X', Blocks.IRON_BARS, 'Y', Blocks.GLASS_PANE);
        addRecipe(getIS(ItemSlideDoor.class, 2, EnumSlideDoor.GGLASS.getId()), "XYX", "X#X", "XYX", '#', tudura, 'X', Items.STICK, 'Y', Blocks.GLASS_PANE);
        addRecipe(getIS(ItemSlideDoor.class, 2, EnumSlideDoor.YUKI.getId()), "XYX", "X#X", "XZX", '#', tudura, 'X', Items.STICK, 'Y', Items.PAPER, 'Z', Blocks.GLASS_PANE);
        addRecipe(getIS(ItemSlideDoor.class, 2, EnumSlideDoor.AMADO.getId()), "XYX", "X#X", "XYX", '#', tudura, 'X', Items.STICK, 'Y', "plankWood");
        // きつねび
        addRecipe(getIS(Kitunebi.class, 6, 0), "XXX", "Y#Y", "XXX", 'X', "gemLapis", 'Y', tudura, '#', Blocks.LIT_PUMPKIN);
        addRecipe(getIS(Kitunebi.class, 6, 0), "XXX", "Y#Y", "XXX", 'X', Items.ENDER_PEARL, 'Y', tudura, '#', Blocks.LIT_PUMPKIN);
        addRecipe(getIS(Kitunebi.class, 6, 0), "XYX", "X#X", "XYX", 'X', "gemLapis", 'Y', tudura, '#', Blocks.LIT_PUMPKIN);
        addRecipe(getIS(Kitunebi.class, 6, 0), "XYX", "X#X", "XYX", 'X', Items.ENDER_PEARL, 'Y', tudura, '#', Blocks.LIT_PUMPKIN);

        //******デコレーション
        // 瓦
        addCircleRecipe(getBlockIS(EnumDecoration.KAWARA.getModName(), 8, 0), tudura, Items.BRICK);
        // 漆喰
        addCircleRecipe(getBlockIS(EnumDecoration.PLASTER.getModName(), 8, 0), tudura, Blocks.SAND);
        // なまこ
        addAltCircleRecipe(getBlockIS(EnumDecoration.NAMAKO.getModName(), 8, 0), tudura, getBlockIS(EnumDecoration.PLASTER.getModName()), getBlockIS(EnumDecoration.KAWARA.getModName()));
        // ワラ
        addRecipe(getBlockIS(EnumDecoration.WARA.getModName(), 4, 0), "XXX", "XXX", "XXX", 'X', getIS(Straw.class));
        // かやぶき
        addCircleRecipe(getBlockIS(EnumDecoration.KAYA.getModName(), 8, 0), tudura, "cropWheat");
        // 市松各種
        addCircleRecipe(getBlockIS(EnumDecoration.CBIRCH.getModName(), 8, 0), tudura, new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
        addCircleRecipe(getBlockIS(EnumDecoration.COAK.getModName(), 8, 0), tudura, new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.OAK.getMetadata()));
        addCircleRecipe(getBlockIS(EnumDecoration.CPINE.getModName(), 8, 0), tudura, new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
        // デコレーション用半ブロと階段の登録
        registerDeco();

        //***弓とか槍とか
        addRecipe(getIS(BambooBow.class), " BS", "T S", " BS", 'B', bamboo, 'T', tudura, 'S', Items.STRING);
        addRecipe(getIS(BambooArrow.class, 8, 0), "B", "T", "T", 'B', bamboo, 'T', tudura);
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
        addShapelessRecipe(getIS(TorchArrow.class), getIS(BambooArrow.class), Blocks.TORCH);
        addShapelessRecipe(getIS(LightArrow.class), getIS(BambooArrow.class), Items.FEATHER);
        addShapelessRecipe(getIS(ExplodeArrow.class), getIS(BambooArrow.class), Items.GUNPOWDER);

    }

    private void addAntiArrowsRecipe(AntiType type, int ammo, Object material) {
        addRecipe(getIS(AntiArrow.class, ammo, type.getID()), " B ", " T ", "MTM", 'B', bamboo, 'T', tudura, 'M', material);
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
        GameRegistry.addSmelting(getIS(SakuraLog.class), getIS(Items.COAL, 1, 1), 0.2F);
    }

    /**
     * 燃料
     */
    public void registFuel() {
        IFuelHandler handler = new IFuelHandler() {
            private Item sakuraLog = Item.getItemFromBlock(DataManager.getBlock(SakuraLog.class));

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
        MinecraftForge.addGrassSeed(getIS(RiceSeed.class), 10);
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
        if (objArray[0] instanceof Class) {
            output = getIS((Class) objArray[0]);
        } else {
            output = (ItemStack) objArray[0];
        }
        for (int i = 1; i < objArray.length; i++) {
            if (objArray[i] instanceof Class) {
                params[i - 1] = getIS((Class) objArray[i]);
            } else {
                params[i - 1] = objArray[i];
            }
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

    private ItemStack getIS(Class cls) {
        return this.getIS(cls, 0);
    }

    private ItemStack getIS(Class cls, int meta) {
        return this.getIS(cls, 1, meta);
    }

    private ItemStack getIS(Class cls, int amo, int meta) {
        ItemStack is = null;
        if (Block.class.isAssignableFrom(cls)) {
            is = new ItemStack(DataManager.getBlock(cls), amo, meta);
        } else {
            is = new ItemStack(DataManager.getItem(cls), amo, meta);
        }
        if (is.getItem() == null) {
            throw new IllegalArgumentException("Illegal recipe!" + cls.getSimpleName());
        }
        return is;
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
