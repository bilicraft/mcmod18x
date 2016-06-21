package ruby.bamboo.enchant;

public class Food extends EnchantBase {

    public Food(int id, String name) {
        super(id, name);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

}
