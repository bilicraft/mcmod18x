package ruby.bamboo.enchant;

public class Hate extends EnchantBase {

    final String[] type = new String[] { "Zombie", "Insect" };

    public Hate(int id, String name) {
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
