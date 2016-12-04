package ruby.bamboo.item;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruby.bamboo.core.init.BambooData.BambooItem;
import ruby.bamboo.core.init.EnumCreateTab;
import ruby.bamboo.core.init.EnumCreateTab.ICreativeSoatName;
import ruby.bamboo.texture.IMultiTextuerItem;

@BambooItem(createiveTabs = EnumCreateTab.TAB_BAMBOO)
public class BambooFood extends ItemFood implements IMultiTextuerItem, ICreativeSoatName {

    enum Food {
        RICE(0, 3, 1.5F, 32, "mugimesi"),
        BEEFRICE(1, 10, 5, 32, "gyumesi"),
        BUTAMESI(2, 12, 6, 48, "butamesi"),
        KINOKOMESI(3, 6, 3, 32, "kinokomesi"),
        BUTAKUSI(4, 10, 5, 16, "butakusi"),
        GYUKUSI(5, 8, 4, 8, "gyukusi"),
        TAKEMESI(6, 5, 5, 32, "takemesi"),
        TAMAKAKE(7, 5, 2, 24, "tamakake"),
        OYAKO(8, 12, 6, 64, "oyako"),
        TEKKA(9, 7, 3.5F, 36, "tekka"),
        TORIKUSI(10, 9, 3, 12, "torikusi"),
        UMEONI(11, 4, 3, 18, "umeoni"),
        SAKEONI(12, 7, 5, 24, "sakeoni"),
        TUNAONI(13, 8, 7, 24, "tunaoni"),
        KINOONI(14, 5, 2.6F, 24, "kinooni"),
        TAKEONI(15, 6, 6, 24, "takeoni"),
        WAKAMEONI(16, 6, 5, 24, "wakameoni"),
        DANANKO(17, 7, 4, 16, "dananko"),
        DANKINAKO(18, 7, 4, 16, "dankinako"),
        DANMITARASHI(19, 7, 4, 16, "danmitarashi"),
        DANSANSYOKU(20, 7, 6, 16, "dansansyoku"),
        DANZUNDA(21, 7, 5, 16, "danzunda"),
        MOCHI(22, 6, 3, 72, "mochi"),
        COOKEDMOCHI(23, 6, 5, 36, "cookedmochi"),
        OHAANKO(24, 7, 4, 36, "ohaanko"),
        OHAKINAKO(25, 7, 4, 36, "ohakinako"),
        OHAZUNDA(26, 7, 4, 36, "ohazunda"),
        NATTO(27, 2, 1, 16, "natto"),
        NATTOMESHI(28, 5, 3, 16, "nattomeshi"),
        TAMANATTOMESHI(29, 7, 6, 16, "tamanattomeshi"),
        SAKURAMOCHI(30, 7, 6, 36, "sakuramochi"),
        TAMAGYUMESHI(31, 11, 6, 24, "tamagyumeshi"),
        KATSUDON(32, 11, 6, 24, "katsudon"),
        SEKIHAN(33, 5, 4, 36, "sekihan"),
        ONISEKIHAN(34, 7, 6, 28, "onisekihan"),
        TOFU(35, 1, 0.5F, 10, "tofu"),
        AGEDASHI(36, 3, 2, 16, "agedashi"),
        MEN(37, 1, 0.5F, 32, "men"),
        UDON(38, 10, 3, 64, "udon"),
        SOBA(39, 12, 3, 64, "soba"),
        RAMEN(40, 18, 9, 128, "ramen"),
        PIZZA(41, 10, 5, 64, "pizza"),
        KAISENOYAKO(42, 8, 4, 36, "kaisenoyako");

        Food(int id, int heal, float sMod, int duration, String texName) {
            this.id = id;
            this.heal = heal;
            this.sMod = sMod;
            this.duration = duration;
            this.texPath = "bamboomod:items/" + texName;
        }

        static Map<Integer, Food> idToStateMap = ImmutableMap.copyOf(Arrays.stream(values()).collect(Collectors.toMap(key -> key.id, val -> val)));
        int id;
        int heal;
        float sMod;
        int duration;
        String texPath;

        public static Food getState(int id) {
            return idToStateMap.containsKey(id) ? idToStateMap.get(id) : RICE;
        }

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    public BambooFood() {
        super(0, false);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    public int getMaxItemUseDuration(ItemStack stack) {
        return Food.getState(stack.getItemDamage()).duration;
    }

    public int getHealAmount(ItemStack stack) {
        return Food.getState(stack.getItemDamage()).heal;
    }

    public float getSaturationModifier(ItemStack stack) {
        return Food.getState(stack.getItemDamage()).sMod;
    }

    @Override
    public Map<Integer, String> getTexName() {
        return Arrays.stream(Food.values()).collect(Collectors.toMap(key -> key.id, val -> val.texPath));
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        subItems.addAll(Arrays.stream(Food.values()).map(e -> new ItemStack(itemIn, 1, e.id)).collect(Collectors.toList()));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }

    @Override
    public String getSortName(ItemStack is) {
        return "zzz" + this.getUnlocalizedName(is);
    }
}
