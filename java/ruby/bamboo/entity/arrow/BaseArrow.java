package ruby.bamboo.entity.arrow;

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
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
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
    double baseDamage = 2.0D;
    int knockbackStrength;
    float velocity;
    int maxAge = 1200;

    public BaseArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    //    public BaseArrow(World worldIn, EntityLivingBase shooter, EntityLivingBase p_i1755_3_, float p_i1755_4_, float p_i1755_5_) {
    //        super(worldIn, shooter, p_i1755_3_, p_i1755_4_, p_i1755_5_);
    //    }
    //
    public BaseArrow(World worldIn, EntityLivingBase shooter, float velocity) {
        super(worldIn, shooter);
        this.velocity = velocity;
        setAim(shooter, shooter.rotationPitch, shooter.rotationYaw, 0.0F,velocity, 1.0F);
    }

    public BaseArrow(World worldIn, EntityLivingBase shooter, float velocity, ItemStack is) {
        this(worldIn, shooter, velocity);
    }

    public BaseArrow(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        //        this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
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
            this.prevRotationPitch = this.rotationPitch = (float) (MathHelper.atan2(y, f) * 180.0D / Math.PI);
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
            this.prevRotationPitch = this.rotationPitch = (float) (MathHelper.atan2(this.motionY, f) * 180.0D / Math.PI);
        }

        BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
        IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if (iblockstate.getMaterial() != Material.AIR) {
            AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(this.worldObj, blockpos);

            if (axisalignedbb != Block.NULL_AABB && axisalignedbb.offset(blockpos).isVecInside(new Vec3d(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }

        if (this.arrowShake > 0) {
            --this.arrowShake;
        }

        if (this.inGround) {
            // 接地処理拡張用
            this.onGround(blockpos, block, iblockstate);
            ++this.timeInGround;
        } else {
            ++this.ticksInAir;
            Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            RayTraceResult raytraceresult = this.worldObj.rayTraceBlocks(vec3d1, vec3d, false, true, false);
            vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
            vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (raytraceresult != null) {
                vec3d = new Vec3d(raytraceresult.hitVec.xCoord, raytraceresult.hitVec.yCoord, raytraceresult.hitVec.zCoord);
            }

            Entity entity = this.findEntityOnPath(vec3d1, vec3d);

            if (entity != null) {
                raytraceresult = new RayTraceResult(entity);
            }

            if (raytraceresult != null && raytraceresult.entityHit != null && raytraceresult.entityHit instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) raytraceresult.entityHit;

                if (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer)) {
                    raytraceresult = null;
                }
            }

            if (raytraceresult != null) {
                if (raytraceresult.entityHit != null) {

                    int l = this.getArrowDamage(raytraceresult.entityHit);

                    if (0 < l) {
                        DamageSource damagesource;

                        if (this.shootingEntity == null) {
                            // ダメージソース拡張
                            damagesource = getDamageSource(this, this);
                        } else {
                            // ダメージソース拡張
                            damagesource = getDamageSource(this, this.shootingEntity);
                        }

                        if (this.isBurning() && !(raytraceresult.entityHit instanceof EntityEnderman)) {
                            raytraceresult.entityHit.setFire(5);
                        }

                        if (raytraceresult.entityHit.attackEntityFrom(damagesource, l)) {
                            // 命中時処理拡張
                            if (this.onEntityHit(raytraceresult.entityHit)) {
                                if (raytraceresult.entityHit instanceof EntityLivingBase) {
                                    EntityLivingBase entitylivingbase = (EntityLivingBase) raytraceresult.entityHit;

                                    if (!this.worldObj.isRemote) {
                                        entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                                    }

                                    if (this.knockbackStrength > 0) {
                                        float f7 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

                                        if (f7 > 0.0F) {
                                            raytraceresult.entityHit.addVelocity(this.motionX * this.knockbackStrength * 0.6000000238418579D / f7, 0.1D, this.motionZ * this.knockbackStrength * 0.6000000238418579D / f7);
                                        }
                                    }

                                    if (this.shootingEntity instanceof EntityLivingBase) {
                                        EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
                                        EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) this.shootingEntity, entitylivingbase);
                                    }

                                    if (this.shootingEntity != null && raytraceresult.entityHit != this.shootingEntity && raytraceresult.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
                                        ((EntityPlayerMP) this.shootingEntity).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
                                    }
                                }
                                this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                            }

                            // ダメージ後処理拡張
                            if (this.onEntityDamaged(raytraceresult.entityHit)) {
                                if (!(raytraceresult.entityHit instanceof EntityEnderman)) {
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
                    }
                    // 命中後処理
                    this.onEntityHited(raytraceresult.entityHit, velocity);
                } else {
                    BlockPos blockpos1 = raytraceresult.getBlockPos();
                    if (this.onGroundHit(blockpos1, raytraceresult.sideHit)) {
                        this.xTile = blockpos1.getX();
                        this.yTile = blockpos1.getY();
                        this.zTile = blockpos1.getZ();
                        IBlockState iblockstate1 = this.worldObj.getBlockState(blockpos1);
                        this.inTile = iblockstate1.getBlock();
                        this.inData = this.inTile.getMetaFromState(iblockstate1);
                        this.motionX = ((float) (raytraceresult.hitVec.xCoord - this.posX));
                        this.motionY = ((float) (raytraceresult.hitVec.yCoord - this.posY));
                        this.motionZ = ((float) (raytraceresult.hitVec.zCoord - this.posZ));
                        float f5 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                        this.posX -= this.motionX / f5 * 0.05000000074505806D;
                        this.posY -= this.motionY / f5 * 0.05000000074505806D;
                        this.posZ -= this.motionZ / f5 * 0.05000000074505806D;
                        this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                        this.inGround = true;
                        this.arrowShake = 7;
                        this.setIsCritical(false);

                        if (iblockstate.getMaterial() != Material.AIR) {
                            this.inTile.onEntityCollidedWithBlock(this.worldObj, blockpos1, iblockstate1, this);
                        }
                    }
                }
            }

            this.spawnCritParticle();

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            float f3 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

            for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, f3) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
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
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * f8, this.posY - this.motionY * f8, this.posZ - this.motionZ * f8, this.motionX, this.motionY, this.motionZ, new int[0]);
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

    public void onEntityHited(Entity entityHit, float power) {

    }

    /**
     * ダメージ計算
     */
    public int getArrowDamage(Entity entity) {
        float motionDamage = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        int damage = MathHelper.ceiling_double_int(motionDamage * this.baseDamage);

        if (this.getIsCritical()) {
            damage += this.rand.nextInt(damage / 2 + 2);
        }
        return damage;
    }

    /**
     * クリティカル時のパーティクル
     */
    public void spawnCritParticle() {
        if (this.getIsCritical()) {
            for (int k = 0; k < 4; ++k) {
                this.worldObj
                        .spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * k / 4.0D, this.posY + this.motionY * k / 4.0D, this.posZ + this.motionZ * k / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ, new int[0]);
            }
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
     * @param sideHit
     * @param blockpos1
     *
     * @return flase 命中処理のキャンセル
     */
    public boolean onGroundHit(BlockPos blockpos, EnumFacing sideHit) {
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
    public boolean onEntityDamaged(Entity entityHit) {
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
            this.motionX *= this.rand.nextFloat() * 0.2F;
            this.motionY *= this.rand.nextFloat() * 0.2F;
            this.motionZ *= this.rand.nextFloat() * 0.2F;
            this.ticksInGround = 0;
            this.ticksInAir = 0;
        }

        if (this.ticksInGround > maxAge) {
            this.setDead();
        }
    }

    public void motionUpdate(float xyzVariation, float yDecrease) {
        this.motionX *= xyzVariation;
        this.motionY *= xyzVariation;
        this.motionZ *= xyzVariation;
        this.motionY -= yDecrease;
    }

    public void preUpdate() {

    }

    public void postUpdate() {

    }

    @Override
    public final void writeEntityToNBT(NBTTagCompound tagCompound) {
        tagCompound.setShort("xTile", (short) this.xTile);
        tagCompound.setShort("yTile", (short) this.yTile);
        tagCompound.setShort("zTile", (short) this.zTile);
        tagCompound.setShort("life", (short) this.ticksInGround);
        ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(this.inTile);
        tagCompound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
        tagCompound.setByte("inData", (byte) this.inData);
        tagCompound.setByte("shake", (byte) this.arrowShake);
        tagCompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        tagCompound.setByte("pickup", (byte) this.pickupStatus.ordinal());
        tagCompound.setDouble("damage", this.baseDamage);
        tagCompound.setFloat("power", velocity);
        // 追加情報
        NBTTagCompound nbt = writeCustomNBT();
        if (nbt != null) {
            tagCompound.setTag("custom", nbt);
        }
    }

    public NBTTagCompound writeCustomNBT() {
        return null;
    }

    @Override
    public final void readEntityFromNBT(NBTTagCompound tagCompound) {
        this.xTile = tagCompound.getShort("xTile");
        this.yTile = tagCompound.getShort("yTile");
        this.zTile = tagCompound.getShort("zTile");
        this.ticksInGround = tagCompound.getShort("life");

        if (tagCompound.hasKey("inTile", 8)) {
            this.inTile = Block.getBlockFromName(tagCompound.getString("inTile"));
        } else {
            this.inTile = Block.getBlockById(tagCompound.getByte("inTile") & 255);
        }

        this.inData = tagCompound.getByte("inData") & 255;
        this.arrowShake = tagCompound.getByte("shake") & 255;
        this.inGround = tagCompound.getByte("inGround") == 1;

        if (tagCompound.hasKey("damage", 99)) {
            this.baseDamage = tagCompound.getDouble("damage");
        }

        if (tagCompound.hasKey("pickup", 99)) {
            this.pickupStatus = EntityArrow.PickupStatus.getByOrdinal(tagCompound.getByte("pickup"));
        } else if (tagCompound.hasKey("player", 99)) {
            this.pickupStatus = tagCompound.getBoolean("player") ? EntityArrow.PickupStatus.ALLOWED : EntityArrow.PickupStatus.DISALLOWED;
        }
        if (tagCompound.hasKey("power", 99)) {
            this.velocity = tagCompound.getFloat("power");
        }

        if (tagCompound.hasKey("custom")) {
            readCustomNBT((NBTTagCompound) tagCompound.getTag("custom"));
        }
    }

    public void readCustomNBT(NBTTagCompound nbt) {

    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0) {
            boolean flag = this.pickupStatus == EntityArrow.PickupStatus.ALLOWED || this.pickupStatus == EntityArrow.PickupStatus.CREATIVE_ONLY && entityIn.capabilities.isCreativeMode;

            if (this.pickupStatus == EntityArrow.PickupStatus.ALLOWED && !entityIn.inventory.addItemStackToInventory(this.getArrowStack())) {
                flag = false;
            }

            if (flag) {
                this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
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
        this.baseDamage = damageIn;
    }

    @Override
    public double getDamage() {
        return this.baseDamage;
    }

    @Override
    public void setKnockbackStrength(int knockbackStrengthIn) {
        this.knockbackStrength = knockbackStrengthIn;
    }

    @Override
    public boolean canBeAttackedWithItem() {
        return false;
    }

    @Override
    public float getEyeHeight() {
        return 0.0F;
    }

    public void setPickedUpStatus(EntityArrow.PickupStatus stat) {
        this.pickupStatus = stat;
    }

    public void setNoPick() {
        this.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
    }
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    protected ItemStack getArrowStack() {
        return getItemArrow();
    }

    //    @Override
    //    public void setIsCritical(boolean critical) {
    //        byte b0 = this.dataWatcher.getWatchableObjectByte(16);
    //
    //        if (critical) {
    //            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 | 1)));
    //        } else {
    //            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 & -2)));
    //        }
    //    }
    //
    //    @Override
    //    public boolean getIsCritical() {
    //        byte b0 = this.dataWatcher.getWatchableObjectByte(16);
    //        return (b0 & 1) != 0;
    //    }

}
