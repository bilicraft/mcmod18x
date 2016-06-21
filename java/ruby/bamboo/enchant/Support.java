package ruby.bamboo.enchant;

public class Support extends EnchantBase {

    //移動(ksk)、松明(暗いところは残さない)、eat(食事が面倒？)、ヘルス(あ！プレイヤーちゃんが危ない！ポーション！)、サーチ(この辺りには何かが埋まっている気がする…)
    //コレクト(アイテム収集範囲の拡張)
    final String[] type = new String[] { "Move", "Touch", "Eat", "Health", "Search", "Collect" };

    public Support(int id, String name) {
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

    @Override
    public int getRarity() {
        return 20;
    }
}
