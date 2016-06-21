package ruby.bamboo.enchant;

public class Fortune extends EnchantBase {

    public Fortune(int id, String name) {
        super(id, name);
    }

    @Override
    public int getRarity() {
        return 10;
    }
}
