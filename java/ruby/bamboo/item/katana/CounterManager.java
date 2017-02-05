package ruby.bamboo.item.katana;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ruby.bamboo.api.BambooItems;

public class CounterManager {

    @SubscribeEvent
    public void counter(LivingAttackEvent e) {
        if (e.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e.getEntity();
            if (player.getCooldownTracker().getCooldown(BambooItems.COMMON_KATANA, 0F) > 0.9F) {
                e.getEntityLiving();
                Entity attacker = e.getSource().getSourceOfDamage();
                if (attacker != null) {
                    if (e.getSource().damageType.equals("arrow")) {
                        e.setCanceled(true);
                        if (attacker instanceof EntityArrow) {
                            ((EntityArrow) attacker).shootingEntity = player;
                        }
                        attacker.motionX = attacker.motionX * 10;
                        attacker.motionZ = attacker.motionZ * 10;
                        attacker.motionY = attacker.motionY * 10;
                    } else if (e.getSource().damageType.equals("player") || e.getSource().damageType.equals("mob")) {
                        e.setCanceled(true);
                        attacker.hurtResistantTime = 0;
                        if (attacker.attackEntityFrom(DamageSource.magic, e.getAmount()) && attacker instanceof EntityLivingBase) {
                            //((EntityLivingBase)source.getSourceOfDamage()).knockBack(this, 0.5F, this.posX - source.getSourceOfDamage().posX, this.posZ - source.getSourceOfDamage().posZ);
                            ((EntityLivingBase) attacker).knockBack(player, 0.5F, player.posX - attacker.posX, player.posZ - attacker.posZ);
                            attacker.hurtResistantTime = 0;
                        }
                    }
                }
            }
        }
    }
}
