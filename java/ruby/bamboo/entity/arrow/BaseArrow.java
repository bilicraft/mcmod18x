package ruby.bamboo.entity.arrow;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BaseArrow extends EntityArrow implements IProjectile {
    int xTile = -1;
    int yTile = -1;
    int zTile = -1;
    Block inTile;
    int inData;
    boolean inGround;
    int ticksInGround;
    int ticksInAir;
    double damage = 2.0D;
    int knockbackStrength;

    public BaseArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public BaseArrow(World worldIn, EntityLivingBase shooter, EntityLivingBase p_i1755_3_, float p_i1755_4_, float p_i1755_5_) {
        super(worldIn, shooter, p_i1755_3_, p_i1755_4_, p_i1755_5_);
    }

    public BaseArrow(World worldIn, EntityLivingBase shooter, float velocity) {
        super(worldIn, shooter, velocity);
    }

    public BaseArrow(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z
     * direction.
     */
    @Override
    public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
        super.setThrowableHeading(x, y, z, velocity, inaccuracy);
        this.ticksInGround = 0;
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void setVelocity(double x, double y, double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(x * x + z * z);
            this.prevRotationYaw = this.rotationYaw = (float) (MathHelper.atan2(x, z) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (MathHelper.atan2(y, (double) f) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    /**
     * DamageSource.causeArrowDamageではEntityArrow継承必須なのだが、元のままでは拡張しようがない…
     */
    @Override
    public void onUpdate() {
        this.onEntityUpdate();
        // 更新前処理
        this.preUpdate();

        // 矢更新処理
        this.onArrowUpdate();

        // 更新後処理
        this.postUpdate();
    }

    private void onArrowUpdate() {
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f) * 180.0D / Math.PI);
        }

        BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
        IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if (block.getMaterial() != Material.air) {
            block.setBlockBoundsBasedOnState(this.worldObj, blockpos);
            AxisAlignedBB axisalignedbb = block.getCollisionBoundingBox(this.worldObj, blockpos, iblockstate);

            if (axisalignedbb != null && axisalignedbb.isVecInside(new Vec3(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }

        if (this.arrowShake > 0) {
            --this.arrowShake;
        }

        if (this.inGround) {
            // 接地処理拡張用
            this.onGround(blockpos, block, iblockstate);

        } else {
            ++this.ticksInAir;
            Vec3 vec31 = new Vec3(this.posX, this.posY, this.posZ);
            Vec3 vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec31, vec3, false, true, false);
            vec31 = new Vec3(this.posX, this.posY, this.posZ);
            vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (movingobjectposition != null) {
                vec3 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }

            Entity entity = null;
            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;

            for (int i = 0; i < list.size(); ++i) {
                Entity entity1 = (Entity) list.get(i);

                if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5)) {
                    float f1 = 0.3F;
                    AxisAlignedBB axisalignedbb1 = entity1.getEntityBoundingBox().expand((double) f1, (double) f1, (double) f1);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);

                    if (movingobjectposition1 != null) {
                        double d1 = vec31.squareDistanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }

            if (movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;

                if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer)) {
                    movingobjectposition = null;
                }
            }

            if (movingobjectposition != null) {
                if (movingobjectposition.entityHit != null) {
                    float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    int l = MathHelper.ceiling_double_int((double) f2 * this.damage);

                    if (this.getIsCritical()) {
                        l += this.rand.nextInt(l / 2 + 2);
                    }

                    DamageSource damagesource;

                    if (this.shootingEntity == null) {
                        // ダメージソース拡張
                        damagesource = getDamageSource(this, this);
                    } else {
                        // ダメージソース拡張
                        damagesource = getDamageSource(this, this.shootingEntity);
                    }

                    if (this.isBurning() && !(movingobjectposition.entityHit instanceof EntityEnderman)) {
                        movingobjectposition.entityHit.setFire(5);
                    }

                    if (movingobjectposition.entityHit.attackEntityFrom(damagesource, (float) l)) {
                        // 命中時処理拡張
                        if (this.onEntityHit(movingobjectposition.entityHit)) {
                            if (movingobjectposition.entityHit instanceof EntityLivingBase) {
                                EntityLivingBase entitylivingbase = (EntityLivingBase) movingobjectposition.entityHit;

                                if (!this.worldObj.isRemote) {
                                    entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                                }

                                if (this.knockbackStrength > 0) {
                                    float f7 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

                                    if (f7 > 0.0F) {
                                        movingobjectposition.entityHit.addVelocity(this.motionX * (double) this.knockbackStrength * 0.6000000238418579D / (double) f7, 0.1D, this.motionZ * (double) this.knockbackStrength * 0.6000000238418579D / (double) f7);
                                    }
                                }

                                if (this.shootingEntity instanceof EntityLivingBase) {
                                    EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
                                    EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) this.shootingEntity, entitylivingbase);
                                }

                                if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity && movingobjectposition.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
                                    ((EntityPlayerMP) this.shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
                                }
                            }
                            this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                        }

                        // 命中後処理拡張
                        if (this.onEntityHited(movingobjectposition.entityHit)) {
                            if (!(movingobjectposition.entityHit instanceof EntityEnderman)) {
                                this.setDead();
                            }
                        }

                    } else {

                        if (this.onInvalidEntityHit()) {
                            this.motionX *= -0.10000000149011612D;
                            this.motionY *= -0.10000000149011612D;
                            this.motionZ *= -0.10000000149011612D;
                            this.rotationYaw += 180.0F;
                            this.prevRotationYaw += 180.0F;
                            this.ticksInAir = 0;
                        }
                    }
                } else {
                    if (this.onGroundHit()) {
                        BlockPos blockpos1 = movingobjectposition.getBlockPos();
                        this.xTile = blockpos1.getX();
                        this.yTile = blockpos1.getY();
                        this.zTile = blockpos1.getZ();
                        IBlockState iblockstate1 = this.worldObj.getBlockState(blockpos1);
                        this.inTile = iblockstate1.getBlock();
                        this.inData = this.inTile.getMetaFromState(iblockstate1);
                        this.motionX = (double) ((float) (movingobjectposition.hitVec.xCoord - this.posX));
                        this.motionY = (double) ((float) (movingobjectposition.hitVec.yCoord - this.posY));
                        this.motionZ = (double) ((float) (movingobjectposition.hitVec.zCoord - this.posZ));
                        float f5 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                        this.posX -= this.motionX / (double) f5 * 0.05000000074505806D;
                        this.posY -= this.motionY / (double) f5 * 0.05000000074505806D;
                        this.posZ -= this.motionZ / (double) f5 * 0.05000000074505806D;
                        this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                        this.inGround = true;
                        this.arrowShake = 7;
                        this.setIsCritical(false);

                        if (this.inTile.getMaterial() != Material.air) {
                            this.inTile.onEntityCollidedWithBlock(this.worldObj, blockpos1, iblockstate1, this);
                        }
                    }
                }
            }

            if (this.getIsCritical()) {
                for (int k = 0; k < 4; ++k) {
                    this.worldObj.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * (double) k / 4.0D, this.posY + this.motionY * (double) k / 4.0D, this.posZ + this.motionZ * (double) k / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ, new int[0]);
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            float f3 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

            for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f3) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
                ;
            }

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
            float f4 = 0.99F;
            float f6 = 0.05F;

            if (this.isInWater()) {
                for (int i1 = 0; i1 < 4; ++i1) {
                    float f8 = 0.25F;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double) f8, this.posY - this.motionY * (double) f8, this.posZ - this.motionZ * (double) f8, this.motionX, this.motionY, this.motionZ, new int[0]);
                }

                f4 = 0.6F;
            }

            if (this.isWet()) {
                this.extinguish();
            }

            // 速度更新拡張
            this.motionUpdate(f4, f6);

            this.setPosition(this.posX, this.posY, this.posZ);
            this.doBlockCollisions();
        }
    }

    /**
     * Entityにhitしたが、無敵時間などで攻撃が有効とならなかったパターン
     *
     * @return flase 命中処理のキャンセル
     */
    private boolean onInvalidEntityHit() {
        return true;
    }

    /**
     * 地面命中時
     *
     * @return flase 命中処理のキャンセル
     */
    public boolean onGroundHit() {
        return true;
    }

    /**
     * Entityへの命中時
     *
     * @return flase 命中処理のキャンセル
     */
    public boolean onEntityHit(Entity entityHit) {
        return true;
    }

    /**
     * Entityへの命中後処理
     *
     * @return flase setDeadのキャンセル
     */
    public boolean onEntityHited(Entity entityHit) {
        return true;
    }

    public DamageSource getDamageSource(EntityArrow arrow, Entity entity) {
        return DamageSource.causeArrowDamage(arrow, entity);
    }

    public void onGround(BlockPos pos, Block block, IBlockState iblockstate) {
        int j = block.getMetaFromState(iblockstate);

        if (block == this.inTile && j == this.inData) {
            ++this.ticksInGround;

            if (this.ticksInGround >= 1200) {
                this.setDead();
            }
        } else {
            this.inGround = false;
            this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
            this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
            this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
            this.ticksInGround = 0;
            this.ticksInAir = 0;
        }
    }

    public void motionUpdate(float xyzVariation, float yDecrease) {
        this.motionX *= (double) xyzVariation;
        this.motionY *= (double) xyzVariation;
        this.motionZ *= (double) xyzVariation;
        this.motionY -= (double) yDecrease;
    }

    public void preUpdate() {

    }

    public void postUpdate() {

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        tagCompound.setShort("xTile", (short) this.xTile);
        tagCompound.setShort("yTile", (short) this.yTile);
        tagCompound.setShort("zTile", (short) this.zTile);
        tagCompound.setShort("life", (short) this.ticksInGround);
        ResourceLocation resourcelocation = (ResourceLocation) Block.blockRegistry.getNameForObject(this.inTile);
        tagCompound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
        tagCompound.setByte("inData", (byte) this.inData);
        tagCompound.setByte("shake", (byte) this.arrowShake);
        tagCompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        tagCompound.setByte("pickup", (byte) this.canBePickedUp);
        tagCompound.setDouble("damage", this.damage);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        this.xTile = tagCompund.getShort("xTile");
        this.yTile = tagCompund.getShort("yTile");
        this.zTile = tagCompund.getShort("zTile");
        this.ticksInGround = tagCompund.getShort("life");

        if (tagCompund.hasKey("inTile", 8)) {
            this.inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
        } else {
            this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 255);
        }

        this.inData = tagCompund.getByte("inData") & 255;
        this.arrowShake = tagCompund.getByte("shake") & 255;
        this.inGround = tagCompund.getByte("inGround") == 1;

        if (tagCompund.hasKey("damage", 99)) {
            this.damage = tagCompund.getDouble("damage");
        }

        if (tagCompund.hasKey("pickup", 99)) {
            this.canBePickedUp = tagCompund.getByte("pickup");
        } else if (tagCompund.hasKey("player", 99)) {
            this.canBePickedUp = tagCompund.getBoolean("player") ? 1 : 0;
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0) {
            boolean flag = this.canBePickedUp == 1 || this.canBePickedUp == 2 && entityIn.capabilities.isCreativeMode;

            if (this.canBePickedUp == 1 && !entityIn.inventory.addItemStackToInventory(this.getItemArrow())) {
                flag = false;
            }

            if (flag) {
                this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityIn.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }

    public abstract ItemStack getItemArrow();

    // 畑を荒らす
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    /**
     * 初期値2.0
     */
    @Override
    public void setDamage(double damageIn) {
        this.damage = damageIn;
    }

    @Override
    public double getDamage() {
        return this.damage;
    }

    @Override
    public void setKnockbackStrength(int knockbackStrengthIn) {
        this.knockbackStrength = knockbackStrengthIn;
    }

    @Override
    public boolean canAttackWithItem() {
        return false;
    }

    @Override
    public float getEyeHeight() {
        return 0.0F;
    }

    @Override
    public void setIsCritical(boolean critical) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);

        if (critical) {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 | 1)));
        } else {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 & -2)));
        }
    }

    @Override
    public boolean getIsCritical() {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);
        return (b0 & 1) != 0;
    }

}
