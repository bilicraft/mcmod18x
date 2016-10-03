package ruby.bamboo.enchant;

import static ruby.bamboo.enchant.EnchantConstants.*;

import java.util.Arrays;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ruby.bamboo.enchant.event.IAttackEnchant;
import ruby.bamboo.item.arrow.AntiArrow.AntiType;

public class ElementAttack extends EnchantBase implements IAttackEnchant {

    // 炎(もえる・燃えない敵にはきかない)、地獄(吸収・アンデッド耐性)、毒(どく・蜘蛛は耐性)、聖属性(めつぶし・アンデッド以外には効果低下)
    final String[] element = new String[] { "Fire", "Hell", "Poison", "Holy" };

    public ElementAttack(int id, String name) {
        super(id, name);
    }

    @Override
    public void onEntityAttack(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        float dmgSum = 0;
        for (NBTTagCompound nbt : getSubTypesToNBT(stack)) {
            int lvl = nbt.getShort(ENCHANT_LV);
            float baseDmg = lvl / 50F;
            switch (element[getSubID(nbt.getShort(ENCHANT_ID)) % element.length]) {
                case "Fire":
                    if (!target.isImmuneToFire()) {
                        dmgSum += baseDmg / 6;
                        target.setFire((int) Math.ceil(baseDmg));
                    }
                    break;
                case "Hell":
                    if (!Arrays.stream(AntiType.UNDEAD.getEntity()).anyMatch(e -> e.isInstance(target))) {
                        dmgSum += baseDmg / 3;
                        attacker.heal(dmgSum / 2);
                    }
                    break;
                case "Poison":
                    if (!(target instanceof EntitySpider)) {
                        dmgSum += baseDmg / 5;
                    }
                    break;
                case "Holy":
                    dmgSum += baseDmg / 10;
                    if (Arrays.stream(AntiType.UNDEAD.getEntity()).anyMatch(e -> e.isInstance(target))) {
                        dmgSum += baseDmg / 2;
                    }
                    break;
            }
        }
        target.setHealth(target.getHealth() - dmgSum);
    }

    @Override
    public String[] getSubTypes() {
        return element;
    }

    @Override
    public EnchantType[] getEnchantType() {
        return new EnchantType[] { EnchantType.TOOL, EnchantType.WEAPON };
    }

}
