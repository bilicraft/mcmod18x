package ruby.bamboo.entity.arrow;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import ruby.bamboo.core.DataManager;
import ruby.bamboo.item.arrow.AntiArrow;
import ruby.bamboo.item.arrow.AntiArrow.AntiType;

public class EntityAntiArrow extends BaseArrow {

    public EntityAntiArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityAntiArrow(World worldIn, EntityLivingBase shooter, EntityLivingBase p_i1755_3_, float p_i1755_4_, float p_i1755_5_) {
        super(worldIn, shooter, p_i1755_3_, p_i1755_4_, p_i1755_5_);
    }

    public EntityAntiArrow(World worldIn, EntityLivingBase shooter, float velocity) {
        super(worldIn, shooter, velocity);
    }

    public EntityAntiArrow(World worldIn) {
        super(worldIn);
    }

    public EntityAntiArrow(World worldIn, EntityLivingBase shooter, float velocity, ItemStack is) {
        super(worldIn, shooter, velocity, is);
        this.setArrowType(AntiType.getType(is.getItemDamage()));
    }

    public void setArrowType(AntiType type) {
        dataWatcher.updateObject(17, type.getID());
    }

    public AntiType getArrowType() {
        return AntiType.getType(dataWatcher.getWatchableObjectByte(17));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(17, Byte.valueOf((byte) 0));
    }

    @Override
    public int getArrowDamage(Entity entity) {
        int damage = super.getArrowDamage(entity);
        for (Class<? extends Entity> clazz : getArrowType().getEntity()) {
            if (clazz.isInstance(entity)) {
                damage *= 1 + (1.5 / (float) getArrowType().getEntity().length);
                break;
            }
        }
        return damage;
    }

    @Override
    public DamageSource getDamageSource(EntityArrow arrow, Entity entity) {
        if (getArrowType() != AntiType.ENDERMAN) {
            return DamageSource.causeArrowDamage(arrow, entity);
        } else {
            // ワープ対策、クライアント側のみに0ダメージが発生して一瞬消えるのは本体のバグ？
            // クライアントのみなので、サーバーから同期されてPosに変化は無いが気持ち悪い、本体バグ挙動なため解決策なし
            return new EntityDamageSource("antiEnder", entity);
        }
    }

    @Override
    public boolean onEntityDamaged(Entity entityHit) {
        if (getArrowType() != AntiType.ENDERMAN) {
            return true;
        } else {
            this.setDead();
            return false;
        }
    }

    @Override
    public void motionUpdate(float xyzVariation, float yDecrease) {
        if (getArrowType() == AntiType.WATER) {
            // 水中用減速の無効化
            xyzVariation = 0.99F;
        }
        this.motionX *= (double) xyzVariation;
        this.motionY *= (double) xyzVariation;
        this.motionZ *= (double) xyzVariation;
        this.motionY -= (double) yDecrease * 0.5;
    }

    @Override
    public void spawnCritParticle() {
        for (int k = 0; k < 4; ++k) {
            this.worldObj.spawnParticle(EnumParticleTypes.CRIT_MAGIC, this.posX + this.motionX * (double) k / 4.0D, this.posY + this.motionY * (double) k / 4.0D, this.posZ + this.motionZ * (double) k / 4.0D, 0, -0.025, 0, new int[0]);
        }
    }

    @Override
    public ItemStack getItemArrow() {
        return DataManager.getItemStack(AntiArrow.class, 1, getArrowType().getID());
    }

    @Override
    public NBTTagCompound writeCustomNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("id", getArrowType().getID());
        return nbt;

    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("id")) {
            setArrowType(AntiType.getType(nbt.getByte("id")));
        }
    }

}
