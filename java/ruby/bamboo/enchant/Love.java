package ruby.bamboo.enchant;

public class Love extends EnchantBase {

    public Love(int id, String name) {
        super(id, name);
    }

    @Override
    public EnchantType[] getEnchantType() {
        return new EnchantType[] { EnchantType.TOOL, EnchantType.WEAPON };
    }

    @Override
    public int getRarity() {
        return 10;
    }

}
