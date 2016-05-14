package ruby.bamboo.entity.arrow;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import ruby.bamboo.core.DataManager;
import ruby.bamboo.item.arrow.TouchArrow;

public class EntityTouchArrow extends BaseArrow {

    public EntityTouchArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityTouchArrow(World worldIn, EntityLivingBase shooter, EntityLivingBase p_i1755_3_, float p_i1755_4_, float p_i1755_5_) {
        super(worldIn, shooter, p_i1755_3_, p_i1755_4_, p_i1755_5_);
    }

    public EntityTouchArrow(World worldIn, EntityLivingBase shooter, float velocity) {
        super(worldIn, shooter, velocity);
    }

    public EntityTouchArrow(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean onGroundHit(BlockPos blockpos, EnumFacing sideHit) {
        BlockPos targetPos = blockpos.offset(sideHit);
        IBlockState state = worldObj.getBlockState(targetPos);

        if (state.getBlock() == Blocks.air) {
            IBlockState torch = Blocks.torch.getDefaultState();

            if (Blocks.torch.canPlaceBlockAt(worldObj, targetPos)) {
                torch = Blocks.torch.onBlockPlaced(worldObj, targetPos, sideHit, 0.5f, 0.5f, 0.5f, 0, null);
                worldObj.setBlockState(targetPos, torch, 3);
                setDead();
            }
        }
        return true;
    }

    @Override
    public void spawnCritParticle() {
        for (int k = 0; k < 4; ++k) {
            this.worldObj.spawnParticle(EnumParticleTypes.FLAME, this.posX + this.motionX * (double) k / 4.0D, this.posY + this.motionY * (double) k / 4.0D, this.posZ + this.motionZ * (double) k / 4.0D, 0, -0.025, 0, new int[0]);
        }
    }

    @Override
    public boolean getIsCritical() {
        return true;
    }

    @Override
    public ItemStack getItemArrow() {
        return DataManager.getItemStack(TouchArrow.class, 1, 0);
    }

}
