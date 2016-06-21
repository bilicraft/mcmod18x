package ruby.bamboo.enchant;

public class SilkTouch extends EnchantBase {

    public SilkTouch(int id, String name) {
        super(id, name);
    }

    @Override
    public int getRarity() {
        return 10;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}
