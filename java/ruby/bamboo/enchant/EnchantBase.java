package ruby.bamboo.enchant;

import static ruby.bamboo.enchant.EnchantConstants.*;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class EnchantBase{
    public static final byte SUB_WILD = (byte) 0xFF;

    enum EnchantType {
        TOOL,
        WEAPON
    }

    private final short mainId;
    public final String name;

    public EnchantBase(int id, String name) {
        this.mainId = (short) id;
        this.name = name;
    }

    public String getName(ItemStack is, int subType) {
        String[] subTypeStr = getSubTypes();
        if (subTypeStr != null && 0 < subTypeStr.length) {
            return name + "_" + subTypeStr[subType < subTypeStr.length ? subType : subType % subTypeStr.length];
        }
        return name;
    }

    public int getMaxLevel() {
        return 1500;
    }

    public int getEnchantListMaxLevel() {
        return getMaxLevel();
    }

    public int getRarity() {
        return 1;
    }

    public final int getEffectId(int subTypeId) {
        return (mainId & 0xFF) + ((subTypeId & 0xFF) << 8);
    }

    public static final int getMainID(int joinedId) {
        return joinedId & 0xFF;
    }

    public static final int getSubID(int joinedId) {
        return (joinedId >> 8) & 0xFF;
    }

    // stackに存在するthisのサブタイプ一覧
    public List<NBTTagCompound> getSubTypesToNBT(ItemStack stack) {
        return IBambooEnchantable.getSpenchList(stack).stream().filter(e -> isEqual(this, e.getShort(ENCHANT_ID), SUB_WILD)).collect(Collectors.toList());
    }

    public String[] getSubTypes() {
        return null;
    }

    public int getSubTypeCount() {
        return getSubTypes() != null ? getSubTypes().length : 0;
    }

    public EnchantType[] getEnchantType() {
        return new EnchantType[] { EnchantType.TOOL };
    }

    public boolean isLevelupEnchant() {
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    public static boolean isEqual(EnchantBase base, int effectId, int subTypeId) {
        if (subTypeId != SUB_WILD) {
            return base.getEffectId(subTypeId) == effectId;
        } else {
            return base.mainId == getMainID(effectId);
        }
    }

}
