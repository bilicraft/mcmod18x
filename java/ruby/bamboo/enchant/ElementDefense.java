package ruby.bamboo.enchant;

import static ruby.bamboo.enchant.EnchantConstants.*;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import ruby.bamboo.enchant.event.ITickableEnchant;

public class ElementDefense extends EnchantBase implements ITickableEnchant {

    // 状態異常防御！毒、火、闇、空腹、吐き気、ウィザー
    final String[] type = new String[] { "Poison", "Flame", "Dark", "Haggar", "Nausea", "Wither", "Weak" };

    public ElementDefense(int id, String name) {
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
    public void onTick(ItemStack stack, EntityLivingBase ticker, int slotNum, boolean isSelected) {
        for (NBTTagCompound nbt : getSubTypesToNBT(stack)) {
            int lvl = nbt.getShort(ENCHANT_LV);
            Potion pot = null;
            switch (type[getSubID(nbt.getShort(ENCHANT_ID)) % type.length]) {
                case "Poison":
                    pot = MobEffects.POISON;
                    break;
                case "Flame":
                    ticker.setFire(0);
                    break;
                case "Dark":
                    pot = MobEffects.HUNGER;
                    break;
                case "Haggar":
                    pot = MobEffects.HUNGER;
                    break;
                case "Nausea":
                    pot = MobEffects.NAUSEA;
                    break;
                case "Wither":
                    pot = MobEffects.WITHER;
                    break;
                case "Weak":
                    pot = MobEffects.WEAKNESS;
                    break;

            }
            if (pot != null) {
                if (ticker.isPotionActive(pot)) {
                    ticker.removePotionEffect(pot);
                }
            }
        }

    }

}
