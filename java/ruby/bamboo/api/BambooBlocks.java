package ruby.bamboo.api;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public enum BambooBlocks {
    BAMBOO("bamboo"),
    BAMBOOSHOOT("bambooshoot"),
    BROAD_LEAVE("broad_leave"),
    JPCHEST("jpchest"),
    KITUNEBI("kitunebi"),
    RICEPLANT("riceplant"),
    SAKURA_LEAVE("sakura_leave"),
    SAKURA_LOG("sakura_log"),
    SAKURA_PLANKS("sakura_planks"),
    SAKURA_SAPLING("sakura_sapling"),
    TATAMI("tatami"),

    // デコレーション系
    KAWARA("kawara"),
    KAWARA_DOUBLE_SLAB("kawara_double_slab"),
    KAWARA_SLAB("kawara_slab"),
    KAWARA_STAIRS("kawara_stairs"),
    PLASTER("plaster"),
    PLASTER_DOUBLE_SLAB("plaster_double_slab"),
    PLASTER_SLAB("plaster_slab"),
    PLASTER_STAIRS("plaster_stairs"),
    NAMAKO("namako"),
    NAMAKO_DOUBLE_SLAB("namako_double_slab"),
    NAMAKO_SLAB("namako_slab"),
    NAMAKO_STAIRS("namako_stairs"),
    WARA("wara"),
    WARA_DOUBLE_SLAB("wara_double_slab"),
    WARA_SLAB("wara_slab"),
    WARA_STAIRS("wara_stairs"),
    KAYA("kaya"),
    KAYA_DOUBLE_SLAB("kaya_double_slab"),
    KAYA_SLAB("kaya_slab"),
    KAYA_STAIRS("kaya_stairs"),
    CBIRCH("cbirch"),
    CBIRCH_DOUBLE_SLAB("cbirch_double_slab"),
    CBIRCH_SLAB("cbirch_slab"),
    CBIRCH_STAIRS("cbirch_stairs"),
    COAK("coak"),
    COAK_DOUBLE_SLAB("coak_double_slab"),
    COAK_SLAB("coak_slab"),
    COAK_STAIRS("coak_stairs"),
    CPINE("cpine"),
    CPINE_DOUBLE_SLAB("cpine_double_slab"),
    CPINE_SLAB("cpine_slab"),
    CPINE_STAIRS("cpine_stairs");

    private String name;

    BambooBlocks(String name) {
        this.name = name;
    }

    public Block getBlock() {
        return Block.getBlockFromName(this.toString());
    }

    public IBlockState getState() {
        return getBlock().getDefaultState();
    }

    @Override
    public String toString() {
        return "BambooMod:" + name;
    }

    public static Block getBlock(BambooBlocks block) {
        return block.getBlock();
    }

    public static IBlockState getState(BambooBlocks block) {
        return block.getState();
    }
}
