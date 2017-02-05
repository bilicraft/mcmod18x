package ruby.bamboo.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import ruby.bamboo.core.init.BambooData.BambooItem;
import ruby.bamboo.core.init.EnumCreateTab;

@BambooItem(createiveTabs = EnumCreateTab.TAB_BAMBOO)
public class CommonKatana extends KatanaBase {

    @Override
    public float getDamageVsEntity(ItemStack stack, Entity entity) {
        float dmg = 4 ;

        if (entity == null) {
            return dmg;
        }

        float dmgRate = 1;

        // 敵の種類による攻撃力変動補正
        if (entity instanceof EntityZombie || entity instanceof EntityCreeper || entity instanceof EntitySpider) {
            dmgRate = 2F;
        } else if (entity instanceof EntityAnimal) {
            dmgRate = 3F;
        } else if (entity instanceof EntitySkeleton) {
            dmgRate = 1.5F;
        } else if (entity instanceof EntitySlime) {
            dmgRate = 0;
        } else if (entity instanceof EntityBlaze) {
            dmgRate = 0.7F;
        } else if (entity instanceof EntityGolem) {
            dmgRate = 0.1F;
        }

        // 小数点切り上げ
        dmg = dmg * dmgRate + 1;
        return dmg;
    }

}
