package ruby.bamboo.entity.arrow;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.api.BambooItems;

public class EntityBambooArrow extends BaseArrow {
    private int count = 0;
    private float power = 1;
    private boolean isDie;

    public EntityBambooArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityBambooArrow(World worldIn, EntityLivingBase shooter, float velocity) {
        super(worldIn, shooter, velocity);
        count = 0;
        power = velocity;
        isDie = false;
    }

    public EntityBambooArrow(World worldIn) {
        super(worldIn);
    }

    /**
     * 連射量
     */
    public EntityBambooArrow setBarrage(int attackCount) {
        this.count = attackCount;
        return this;
    }

    @Override
    public void postUpdate() {
        if (0 < --count) {
            this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 1.2F) + power * 0.5F);

            if (!worldObj.isRemote) {
                EntityBambooArrow eba = new EntityBambooArrow(worldObj, (EntityLivingBase) shootingEntity, power);

                eba.setIsCritical(power >= 1);
                eba.setDamage(getDamage() * 0.7);
                eba.setBarrage(0);
                eba.setFire(isBurning() ? 100 : 0);
                eba.pickupStatus = this.pickupStatus;
                eba.setMaxAge(this.maxAge);
                worldObj.spawnEntityInWorld(eba);
            }

        } else {
            if (isDie) {
                setDead();
            }
        }
    }

    @Override
    public boolean onEntityHit(Entity entityHit) {
        entityHit.hurtResistantTime = 0;
        return true;
    }

    @Override
    public void setDead() {
        if (count < 1) {
            super.setDead();
        } else {
            isDie = true;
        }
    }

    @Override
    public ItemStack getItemArrow() {
        return new ItemStack(BambooItems.BAMBOO_ARROW, 1, 0);
    }

    @Override
    protected ItemStack getArrowStack() {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

}