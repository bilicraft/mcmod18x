package ruby.bamboo.entity.arrow;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ruby.bamboo.core.DataManager;
import ruby.bamboo.item.arrow.BambooArrow;

public class EntityBambooArrow extends BaseArrow {
    private int count = 0;
    private float power = 1;
    private boolean isDie;
    private int maxAge = 600;

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
    public void onGround(BlockPos pos, Block block, IBlockState iblockstate) {
        super.onGround(pos, block, iblockstate);
        if (this.ticksInGround > maxAge) {
            this.setDead();
        }
    }

    // クリエイティブ連射負荷対策
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
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
        return DataManager.getItemStack(BambooArrow.class, 1, 0);
    }

    @Override
    protected ItemStack getArrowStack() {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

}