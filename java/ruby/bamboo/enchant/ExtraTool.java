package ruby.bamboo.enchant;

public class ExtraTool extends EnchantBase {
    //    toolClass = "axe";

    //    toolClass = "shovel";

    final String[] toolClass = new String[] { "Axe", "Shovel" };

    public ExtraTool(int id, String name) {
        super(id, name);
    }

    @Override
    public String[] getSubTypes() {
        return toolClass;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

}
