package ruby.bamboo.enchant;

public class ElementDefense extends EnchantBase {

    // 状態異常防御！毒、火、闇、空腹、吐き気、ウィザー
    final String[] type = new String[] { "Poison", "Flame", "Dark", "Haggar", "Nausea", "Wither" };

    public ElementDefense(int id, String name) {
        super(id, name);
    }

    @Override
    public String[] getSubTypes() {
        return type;
    }

    @Override
    public EnchantType[] getEnchantType() {
        return new EnchantType[] { EnchantType.TOOL, EnchantType.WEAPON };
    }

}
