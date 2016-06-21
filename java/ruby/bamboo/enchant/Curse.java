package ruby.bamboo.enchant;

public class Curse extends EnchantBase {

    public Curse(int id, String name) {
        super(id, name);
    }

    @Override
    public boolean isLevelupEnchant() {
        return false;
    }

}
