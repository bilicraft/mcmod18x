package ruby.bamboo.enchant;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import ruby.bamboo.enchant.event.IAttackEnchant;

public class ElementAttack extends EnchantBase implements IAttackEnchant {

    // 炎(もえる・燃えない敵にはきかない)、地獄(吸収・アンデッド耐性)、毒(どく・蜘蛛は耐性)、聖属性(めつぶし・アンデッド以外には効果低下)
    final String[] element = new String[] { "Fire", "Hell", "Poison", "Holy" };

    public ElementAttack(int id, String name) {
        super(id, name);
    }

    @Override
    public void onEntityAttack(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {

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
