package ruby.bamboo.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class Chair extends Entity {

    private IChairUpdate listener = null;

    public Chair(World worldIn) {
        super(worldIn);
    }

    public void setListner(IChairUpdate listener) {
        this.listener = listener;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!worldObj.isRemote) {
            if (getPassengers().isEmpty()) {
                this.setDead();
            }
        }
        if (listener != null) {
            listener.apply(getEntityWorld(), this);
        }
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }

    public interface IChairUpdate {
        public void apply(World worldIn, Entity entity);
    }
}
