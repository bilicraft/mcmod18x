package ruby.bamboo.entity.arrow;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import ruby.bamboo.core.DataManager;
import ruby.bamboo.item.arrow.ExplodeArrow;

public class EntityExplodeArrow extends BaseArrow {

    public EntityExplodeArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityExplodeArrow(World worldIn, EntityLivingBase shooter, float velocity) {
        super(worldIn, shooter, velocity);
    }

    public EntityExplodeArrow(World worldIn) {
        super(worldIn);
    }

    @Override
    public void onEntityHited(Entity entityHit, float power) {
        if (entityHit instanceof EntityLivingBase) {
            TimerBomb bomb = new TimerBomb(worldObj);
            if (!worldObj.isRemote) {
                bomb.setParentEntity(entityHit);

                bomb.setTimer((int) (200 * power));
                worldObj.spawnEntityInWorld(bomb);
            }
        }

    }

    @Override
    public int getArrowDamage(Entity entity) {
        return 1;
    }

    @Override
    public void spawnCritParticle() {
        for (int k = 0; k < 4; ++k) {
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX + this.motionX * k / 4.0D, this.posY + this.motionY * k / 4.0D, this.posZ + this.motionZ * k / 4.0D, 0, -0.025, 0, new int[0]);
        }
    }

    @Override
    public ItemStack getItemArrow() {
        return DataManager.getItemStack(ExplodeArrow.class, 1, 0);
    }

    @Override
    protected ItemStack getArrowStack() {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

}
