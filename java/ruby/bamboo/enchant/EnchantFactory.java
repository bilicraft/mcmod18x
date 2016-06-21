package ruby.bamboo.enchant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.ImmutableList;

import net.minecraft.item.ItemStack;

public class EnchantFactory {

    private static final boolean debug = true;

    // リスト生成
    public static List<Entry> createViewList(ItemStack stack) {

        if (!(stack.getItem() instanceof IBambooEnchantable)) {
            return ImmutableList.of();
        }
        IBambooEnchantable item = (IBambooEnchantable) stack.getItem();
        int num = (int) (2 + (Math.ceil(IBambooEnchantable.getEnchLevel(stack, SpecialEnchant.getEnchantmentByClass(Power.class), 0) * 0.02) * 3));

        // debug
        if (debug)
            return EnchantFactory.getDebugList();

        Long baseSeed = IBambooEnchantable.getRandSeed(stack);
        Random rand = new Random();

        int levelSeed = IBambooEnchantable.getLV(stack) * 20;
        return ImmutableList.<Entry> builder().addAll(IntStream.rangeClosed(levelSeed, levelSeed + num).mapToObj(e -> {
            rand.setSeed(baseSeed + (e << 32L));
            return EnchantFactory.getRandomEnchant(rand);
        }).collect(Collectors.toList())).build();
    }

    //ベースとサブタイプ
    private final static Entry getRandomEnchant(Random rand) {
        final int mod;
        String prefix = "";
        int color = 0xFFFFFF;
        // 1個目、乱数の偏りが酷いので捨てる
        rand.nextDouble();
        if (rand.nextFloat() < 0.2F) {
            if (rand.nextFloat() < 0.2F) {
                if (rand.nextFloat() < 0.2F) {
                    mod = 14;
                    color = 0x6C00C6;
                } else {
                    mod = 10;
                    color = 0xFF7400;
                }
                if (rand.nextFloat() < 0.3F) {
                    // 確率マスキング
                    prefix = "§k";
                }
            } else {
                mod = 6;
                color = 0xFFFF00;
            }
        } else {
            mod = rand.nextInt(4);
        }
        EnchantBase base;
        int rarity;
        do {
            base = SpecialEnchant.getEnchantList().get(rand.nextInt(SpecialEnchant.getEnchantList().size()));
            rarity = base.getRarity();
        } while (!base.isLevelupEnchant() || (1 / Math.max(rarity - mod, 1)) < rand.nextFloat());
        Entry entry = new Entry(base.getEffectId(rand.nextInt(base.getSubTypeCount() + 1)), IntStream.rangeClosed(1, 1 + mod).map(e -> rand.nextInt(25) + 1 + mod).sum(), rand.nextInt(18) + (int) (mod * 0.5) + 3);
        entry.setPrefix(prefix);
        entry.setColor(color);
        return entry;
    }

    public static void setEnchant(ItemStack stack, int num) {
        List<Entry> list = debug ? EnchantFactory.getDebugList() : createViewList(stack);
        if (list.size() <= num) {
            return;
        }
        if (stack == null || !(stack.getItem() instanceof IBambooEnchantable)) {
            return;
        }
        IBambooEnchantable item = (IBambooEnchantable) stack.getItem();
        if (!IBambooEnchantable.canLevelUp(stack)) {
            return;
        }
        Entry entry = list.get(num);
        item.addSpecialEnchLevelLevel(stack, entry.getBase(), entry.getSubType(), entry.getAmount());
        if (IBambooEnchantable.getLV(stack) > entry.curseLv) {
            item.addSpecialEnchLevelLevel(stack, SpecialEnchant.getEnchantmentByClass(Curse.class), 0, entry.getAmount());
        }
        item.levelUp(stack);
    }

    private final static List<Entry> getDebugList() {
        List<Entry> list = new ArrayList<>();
        SpecialEnchant.getEnchantList().stream().forEach(base -> IntStream.rangeClosed(0, Math.max(base.getSubTypeCount() - 1, 0)).forEach(e -> list.add(new Entry(base.getEffectId(e), 300, 100))));
        return list;
    }

}
