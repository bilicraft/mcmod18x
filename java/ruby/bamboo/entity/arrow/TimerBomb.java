package ruby.bamboo.entity.arrow;

import java.util.List;

import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class TimerBomb extends Entity {

    private boolean fuse;
    private float startHP;
    private float pow;
    private final byte FUSE_TIMER = 40;

    public TimerBomb(World worldIn) {
        super(worldIn);
        this.fuse = true;
        this.pow = 0;
    }

    @Override
    public void onUpdate() {
        Entity parent = getParentEntity();
        short timer = this.getTimer();
        if (firstUpdate) {
            if (!worldObj.isRemote) {
                if (parent != null && parent instanceof EntityLivingBase) {
                    this.startHP = ((EntityLivingBase) parent).getHealth();
                } else {
                    setDead();
                }
            }
        }

        if (fuse) {
            if (parent == null || parent.isDead) {
                pow = startHP;
                this.worldObj.playSoundAtEntity(this, "game.tnt.primed", 1.0F, 1.0F);
                fuse = false;
            }
            if (parent != null && timer < 0) {
                pow = startHP - ((EntityLivingBase) parent).getHealth();
                this.worldObj.playSoundAtEntity(this, "game.tnt.primed", 1.0F, 1.0F);
                fuse = false;
            }
        } else {
            if (timer < -FUSE_TIMER) {
                this.explode(pow);
                this.setDead();
            }
        }

        if (!worldObj.isRemote) {
            if (parent == null) {
                if (fuse) {
                    this.setDead();
                } else {
                    if (timer > 0) {
                        this.setTimer(0);
                    }
                }
            } else {
                this.setPosition(parent.posX, parent.posY, parent.posZ);
            }

        } else {
            float eyeHeight = 0;
            if (parent != null) {
                eyeHeight = parent.getEyeHeight();
            }
            this.worldObj.spawnParticle(timer > 60 ? EnumParticleTypes.SMOKE_NORMAL : EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY + eyeHeight, this.posZ, (rand.nextFloat() - 0.5F) * 0.1F, 0, (rand.nextFloat() - 0.5F) * 0.1F, new int[0]);
        }

        this.setTimer(timer - 1);
        this.firstUpdate = false;
    }

    public void setParentEntity(Entity entity) {
        this.dataWatcher.updateObject(16, entity.getEntityId());
        this.posX = entity.posX;
        this.posY = entity.posY;
        this.posZ = entity.posZ;
    }

    private void explode(float pow) {
        List<Entity> list = worldObj.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(3, 3, 3), entity -> {
            return (entity instanceof EntityLivingBase || entity instanceof TimerBomb);
        });
        for (Entity e : list) {
            if (e instanceof TimerBomb) {
                if (e != this) {
                    e.setDead();
                }
            } else {
                if (!e.isImmuneToExplosions()) {
                    Vec3 vec3 = this.getPositionVector();
                    double distance = e.getDistance(this.posX, this.posY, this.posZ) / (double) 6;

                    double offsetX = e.posX - this.posX;
                    double offsetY = e.posY + (double) e.getEyeHeight() - this.posY;
                    double offsetZ = e.posZ - this.posZ;
                    double offsetSqrt = (double) MathHelper.sqrt_double(offsetX * offsetX + offsetY * offsetY + offsetZ * offsetZ);

                    if (offsetSqrt != 0.0D) {
                        offsetX = offsetX / offsetSqrt;
                        offsetY = offsetY / offsetSqrt;
                        offsetZ = offsetZ / offsetSqrt;
                        double blockDen = (double) this.worldObj.getBlockDensity(vec3, e.getEntityBoundingBox());
                        double d10 = (1.0D - distance) * blockDen;
                        e.attackEntityFrom(DamageSource.generic, (float) ((int) ((d10 * d10 + d10) / 2.0D * 8.0D * (double) pow + 1.0D)));
                        double d11 = EnchantmentProtection.func_92092_a(e, d10);
                        e.motionX += offsetX * d11;
                        e.motionY += offsetY * d11;
                        e.motionZ += offsetZ * d11;
                    }
                }

            }
        }
        this.worldObj.playSoundAtEntity(this, "random.explode", 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
        for (int i = 0; i < 4; i++) {
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + (rand.nextInt(6) - 3), this.posY + rand.nextInt(2) - 1, this.posZ + rand.nextInt(6) - 3, 0, 0, 0, new int[0]);
        }
        this.setDead();
    }

    public Entity getParentEntity() {
        return worldObj.getEntityByID(this.dataWatcher.getWatchableObjectInt(16));
    }

    public void setTimer(int time) {
        this.dataWatcher.updateObject(17, (short) time);
    }

    public short getTimer() {
        return this.dataWatcher.getWatchableObjectShort(17);
    }

    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(16, 0);
        this.dataWatcher.addObject(17, (short) 0);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompund) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {

    }

}
