package ruby.bamboo.enchant;

public class Ability extends EnchantBase {

    // ソニック(敵性mobへの攻撃力分の範囲ダメージ),ヒール(自身を含めた周囲への回復、ペットも含む？),タイム(対象の時間を止める、強力すぎる？),デス(対象のHPが一定割合以下の場合、確率で即死させる)
    private String[] type = new String[] { "Sonic", "Heal", "Time", "Death" };

    public Ability(int id, String name) {
        super(id, name);
    }

    @Override
    public int getMaxLevel() {
        return 1500;
    }

    @Override
    public int getRarity() {
        return 100;
    }

    @Override
    public String[] getSubTypes() {
        return type;
    }

}
