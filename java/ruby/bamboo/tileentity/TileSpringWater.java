package ruby.bamboo.tileentity;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileSpringWater extends TileEntity {

    private BlockPos parentPos = null;
    private boolean isDead = false;

    public void setParent(BlockPos parent) {
        parentPos = parent;
    }

    @Nullable
    public BlockPos getParent() {
        //循環防止
        return parentPos != null ? parentPos : null;
    }

    public boolean isParentAlive() {
        return parentPos != null && this.getWorld().getBlockState(parentPos).getBlock() == this.getWorld().getBlockState(this.getPos()).getBlock();
    }

    public boolean isParent() {
        return parentPos != null && parentPos.equals(this.getPos());
    }

    public void setDead() {
        isDead = true;
    }

    public boolean isDead() {
        if (isParent()) {
            return isDead;
        } else {
            TileEntity tile = this.getWorld().getTileEntity(parentPos);
            return tile != null ? tile instanceof TileSpringWater ? ((TileSpringWater) tile).isDead() : true : true;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("parent")) {
            NBTTagCompound parent = compound.getCompoundTag("parent");
            parentPos = new BlockPos(parent.getInteger("x"), parent.getInteger("y"), parent.getInteger("z"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (isParentAlive()) {
            NBTTagCompound parent = new NBTTagCompound();
            parent.setInteger("x", this.parentPos.getX());
            parent.setInteger("y", this.parentPos.getY());
            parent.setInteger("z", this.parentPos.getZ());
            compound.setTag("parent", parent);
        }
        return compound;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

}
