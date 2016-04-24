package ruby.bamboo.block.decoration;

import net.minecraft.block.material.Material;
import ruby.bamboo.core.Constants;
import ruby.bamboo.core.init.EnumCreateTab;

public enum EnumDecoration {
    KAWARA("kawara"),
    PLASTER("plaster"),
    NAMAKO("namako"),
    WARA("wara"),
    KAYA("kaya"),
    CBIRCH("cbirch"),
    COAK("coak"),
    CPINE("cpine"),;

    public static final String SLAB = "_slab";
    public static final String DOUBLE_SLAB = "_double_slab";
    public static final String STAIRS = "_stairs";

    private static final byte NORMAL = 1;
    private static final byte HALF = 2;
    private static final byte STAIR = 4;

    private String name;
    private Material material;
    private EnumCreateTab tab;

    private byte typeFlg;

    EnumDecoration(String name) {
        this(name, Material.ground, EnumCreateTab.TAB_BAMBOO, NORMAL, HALF, STAIR);
    }

    EnumDecoration(String name, Material material, EnumCreateTab tab, byte... typeFlg) {
        this.name = name;
        this.material = material;
        this.tab = tab;
        byte flg = 0;
        for (byte b : typeFlg) {
            flg += b;
        }
        this.typeFlg = flg;
    }

    public String getName() {
        return this.name;
    }

    public String getModName() {
        return Constants.MODID + Constants.DMAIN_SEPARATE + this.name;
    }

    public Material getMaterial() {
        return this.material;
    }

    public EnumCreateTab getCreateTab() {
        return this.tab;
    }

    public boolean isNormal() {
        return (typeFlg & NORMAL) != 0;
    }

    public boolean isHalf() {
        return (typeFlg & HALF) != 0;
    }

    public boolean isStair() {
        return (typeFlg & STAIR) != 0;
    }

}
