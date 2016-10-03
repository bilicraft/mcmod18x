package ruby.bamboo.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruby.bamboo.entity.arrow.TimerBomb;

public class EntityRuneEffect extends Entity {

    private static final DataParameter<Integer> PARENT = EntityDataManager.<Integer> createKey(TimerBomb.class, DataSerializers.VARINT);

    private float ringSize;
    public float roll;
    private float rollSpeed;
    private int colorCode = 0xFF00FF;

    public EntityRuneEffect(World par1World) {
        super(par1World);
        ringSize = 1;
        roll = 0;
        rollSpeed = 1;
        this.setSize(1F, 1F);
    }

    // 移動するときの違和感が拭えない…
    @Override
    public void onUpdate() {
        //super.onUpdate();

        Entity parent = getParentEntity();

        setRingsize(1);

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        //System.out.println(parent);
        if (parent != null && !parent.isDead) {
            //            this.posX = parent.posX;
            //            this.posY = parent.posY;
            //            this.posZ = parent.posZ;
            if (!worldObj.isRemote) {
                this.setPosition(parent.posX, parent.posY, parent.posZ);
            } else {
                this.moveEntity(parent.motionX, parent.motionY, parent.motionZ);
            }

        } else {
            if (!worldObj.isRemote) {
                this.setDead();
            }
        }
        if (worldObj.isRemote) {
            this.roll = roll < 360 ? roll + rollSpeed : 0;
        }
    }

    @Override
    public AxisAlignedBB getEntityBoundingBox() {
        return super.getEntityBoundingBox();
    }

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_) {
        // コリジョン処理は不要
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    public void setRingColor(int color) {
        colorCode = color;
    }

    public int getRingColor() {
        return colorCode;
    }

    public void setParentEntity(Entity entity) {
        this.dataManager.set(PARENT, entity.getEntityId());
        this.posX = entity.posX;
        this.posY = entity.posY;
        this.posZ = entity.posZ;
    }

    public Entity getParentEntity() {
        return worldObj.getEntityByID(this.dataManager.get(PARENT));
    }

    public void setRollSpeed(float speed) {
        rollSpeed = speed;
    }

    public float getRollSpeed() {
        return rollSpeed;
    }

    public void setRingsize(float i) {
        ringSize = i;
        setSize(i, 1);
    }

    public float getRingsize() {
        return ringSize;
    }

    public EntityRuneEffect copy() {
        EntityRuneEffect copy = new EntityRuneEffect(worldObj);
        copy.setRingsize(this.ringSize);
        copy.setRollSpeed(this.rollSpeed);
        copy.setParentEntity(this.getParentEntity());
        copy.setRingColor(this.colorCode);
        return copy;
    }

    @Override
    public void moveEntity(double par1, double par3, double par5) {
        super.moveEntity(par1, par3, par5);
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(PARENT, 0);
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompund) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {

    }
}
