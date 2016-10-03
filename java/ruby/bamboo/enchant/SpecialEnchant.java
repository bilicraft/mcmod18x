package ruby.bamboo.enchant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import ruby.bamboo.core.init.ClassFinder;
import ruby.bamboo.enchant.event.IAttackEnchant;
import ruby.bamboo.enchant.event.IBreakEnchant;
import ruby.bamboo.enchant.event.ITickableEnchant;

public class SpecialEnchant {
    private static final Map<Integer, EnchantBase> baseMap;
    private static final Multimap<Class<?>, EnchantBase> ifMap = ArrayListMultimap.create();

    static {
        List<EnchantBase> enchantList = new ArrayList<>();
        // 連鎖破壊みたいな
        enchantList.add(new Chain(0, "Chain"));
        // 属性ダメージって面白そう
        enchantList.add(new ElementAttack(1, "ElementsAttack"));
        // 特殊強化、+1とかみたいな
        enchantList.add(new Power(2, "Power"));
        // 状態異常防御
        enchantList.add(new ElementDefense(3, "ElementDefense"));
        // ツール機能拡張、斧しゃべる
        enchantList.add(new ExtraTool(4, "ExTool"));
        // 浮遊,落下ダメージ軽減？
        enchantList.add(new Floating(5, "Floating"));
        // あなたに首ったけ！もうはなれない！
        enchantList.add(new Love(6, "Love"));
        // ハサミ機能、最上位はもう通りがかるだけで羊の毛なんて丸裸！
        enchantList.add(new Shears(7, "Shears"));
        // サポート系
        enchantList.add(new Support(8, "Support"));
        // こいつ…食えるぞ？レベルに応じて強化ポーション的な効果、消滅するため高い効果
        enchantList.add(new Food(9, "Food"));
        // ゾンビ嫌い、虫嫌い
        enchantList.add(new Hate(10, "Hate"));
        // バニラのラップ、レベル計算を違うものにするため
        enchantList.add(new Efficiency(11, "Efficiency"));
        enchantList.add(new Unbreaking(12, "Unbreaking"));
        enchantList.add(new SilkTouch(13, "SilkTouch"));
        enchantList.add(new Fortune(14, "Fortune"));
        // もんすたー風能力
        enchantList.add(new Monster(15, "Monster"));
        // 特殊ランダム発動系
        enchantList.add(new Ability(16, "Ability"));
        // 拡張範囲
        enchantList.add(new Extender(17, "Extender"));
        // 呪い
        enchantList.add(new Curse(255, "Curse"));
        List<Class<?>> list = ImmutableList.of();
        try {
            list = new ClassFinder().search("ruby.bamboo.enchant.event");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        // ベースマップ
        baseMap = enchantList.stream().collect(Collectors.toMap(base -> base.getEffectId(0), base -> base));

        // I/F呼び出し用マップ登録
        for (Class<?> c : list) {
            ifMap.putAll(c, enchantList.parallelStream().filter(c::isInstance).collect(Collectors.toList()));
        }

    }

    public static String getEnchantmentName(ItemStack stack, short enchId) {
        return getEnchantmentById(enchId).getName(stack, EnchantBase.getSubID(enchId));
    }

    public static EnchantBase getEnchantmentById(short enchId) {
        return baseMap.get(EnchantBase.getMainID(enchId));
    }

    public static EnchantBase getEnchantmentByClass(Class<? extends EnchantBase> base) {
        return baseMap.values().parallelStream().filter(encha -> encha.getClass() == base).findAny().get();
    }

    public static ImmutableList<EnchantBase> getEnchantList() {
        return ImmutableList.<EnchantBase> builder().addAll(baseMap.values()).build();
    }

    public static void onBreakBlock(ItemStack stack, World world, Block block, BlockPos pos, EntityLivingBase player, RayTraceResult rtr) {
        getEnchantEnableStream(IBreakEnchant.class, stack).forEach(e -> e.onBreakBlock(stack, world, block, pos, player, rtr));
    }

    public static void onAttackEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        getEnchantEnableStream(IAttackEnchant.class, stack).forEach(e -> e.onEntityAttack(stack, target, attacker));
    }

    public static void onUpdate(ItemStack stack, EntityLivingBase ticker,int itemSlot, boolean isSelected) {
        getEnchantEnableStream(ITickableEnchant.class, stack).forEach(e -> e.onTick(stack, ticker, itemSlot, isSelected));
    }

    private static <T> Stream<T> getEnchantEnableStream(Class<T> cls, ItemStack stack) {
        return (Stream<T>) ifMap.get(cls).parallelStream().filter(base -> IBambooEnchantable.isEnchantEnable(stack, base, EnchantBase.SUB_WILD));
    }

}
