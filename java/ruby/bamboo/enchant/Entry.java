package ruby.bamboo.enchant;

import com.google.common.base.Strings;

import net.minecraft.item.ItemStack;

public class Entry {
    private final short enchantId;
    private final int amount;
    final int curseLv;
    private String preFix;
    private int clolr = 0xFFFFFF;

    public Entry(int enchantId, int amount) {
        this(enchantId, amount, 0);
    }

    public Entry(int enchantId, int amount, int curseLv) {
        this.enchantId = (short) enchantId;
        this.amount = amount;
        this.curseLv = curseLv;
    }

    public short getEnchantId() {
        return enchantId;
    }

    public int getMainId() {
        return EnchantBase.getMainID(enchantId);
    }

    public int getSubType() {
        return EnchantBase.getSubID(enchantId);
    }

    public int getAmount() {
        return amount;
    }

    public int getCurseLv() {
        return curseLv;
    }

    public EnchantBase getBase() {
        return SpecialEnchant.getEnchantmentById(enchantId);
    }

    public String getName(ItemStack stack) {
        return getBase().getName(stack, getSubType());
    }

    public void setPrefix(String str) {
        this.preFix = str;
    }

    public String getPrefix() {
        return Strings.nullToEmpty(preFix);
    }

    public int getClolr() {
        return clolr;
    }

    public void setColor(int clolr) {
        this.clolr = clolr;
    }
}