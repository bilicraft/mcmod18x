package ruby.bamboo.enchant;

public class Power extends EnchantBase {

    public Power(int id, String name) {
        super(id, name);
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public int getEnchantListMaxLevel() {
        return 1;
    }

}
