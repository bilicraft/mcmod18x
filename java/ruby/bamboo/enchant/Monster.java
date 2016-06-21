package ruby.bamboo.enchant;

import ruby.bamboo.enchant.event.IPlayerDamagedEnchant;

public class Monster extends EnchantBase implements IPlayerDamagedEnchant {
    // えんだー(飛び道具のテレポ回避)、クリーパー(爆発ダメージの軽減)、ブレイズ(炎軽減、もう無効でもいいかも、スライム(全体的な割合ダメカ),ゾンビ(プレイヤー経験値獲得量の上昇、なぜかって？スポブロさ)
    private final static String[] mobType = new String[] { "Ender", "Creeper", "Blaze", "Slime", "Zombie" };

    public Monster(int id, String name) {
        super(id, name);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getRarity() {
        return 40;
    }

    @Override
    public String[] getSubTypes() {
        return mobType;
    }

}
