package ruby.bamboo.block;

import java.util.Random;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruby.bamboo.core.init.BambooData.BambooBlock;
import ruby.bamboo.core.init.EnumMaterial;
import ruby.bamboo.fluid.HotSpring;
import ruby.bamboo.paticle.PaticleFactory;
import ruby.bamboo.tileentity.TileSpringWater;

@BambooBlock(name = "spring_water", material = EnumMaterial.WATER)
public class SpringWater extends BlockFluidFinite implements ITileEntityProvider {

    private static final byte MAX_LV = 8;

    public SpringWater(Material material) {
        super(FluidRegistry.getFluid(HotSpring.FLUID_NAME), material);
        setTickRate(30);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileSpringWater();
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        TileSpringWater myTile = ((TileSpringWater) worldIn.getTileEntity(pos));
        if (myTile.getParent() != null) {
            return;
        }

        for (EnumFacing dir : EnumFacing.VALUES) {
            BlockPos offPos = pos.offset(dir);
            IBlockState offState = worldIn.getBlockState(offPos);
            if (offState != null && offState.getBlock() == this) {
                BlockPos parent = ((TileSpringWater) worldIn.getTileEntity(offPos)).getParent();
                if (parent != null) {
                    myTile.setParent(parent);
                    return;
                }
            }
        }
        ((TileSpringWater) worldIn.getTileEntity(pos)).setParent(pos);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        TileSpringWater water = (TileSpringWater) world.getTileEntity(pos);

        if (water == null) {
            return;
        }
        if (water.getParent() == null || water.isDead()) {
            levelDown(world, pos, state);
            return;
        }

        if (!water.isParent()) {
            if (!water.isParentAlive()) {
                water.setDead();
            } else {
                // 高速化のため自身と比べて周囲で最も水位が低い部分の加算
                int level = MAX_LV;
                BlockPos updatePos = null;

                for (EnumFacing dir : EnumFacing.HORIZONTALS) {
                    BlockPos off = pos.offset(dir);
                    IBlockState offState = world.getBlockState(off);
                    if (offState.getBlock() == this) {
                        int offLv = offState.getValue(LEVEL);
                        if (offLv < level) {
                            updatePos = off;
                            level = offLv;
                        }
                    }
                }
                if (updatePos != null) {
                    levelUp(world, updatePos);
                }

            }
        }

        if (world.getBlockState(pos.down()).getBlock() != this) {
            super.updateTick(world, pos, state, rand);
        } else {
            levelUp(world, pos.down());
        }

    }

    public static boolean levelUp(World world, BlockPos pos) {
        return levelUp(world, pos, world.getBlockState(pos));
    }

    public static boolean levelUp(World world, BlockPos pos, IBlockState state) {
        int level = state.getValue(LEVEL);
        if (level < MAX_LV) {
            world.setBlockState(pos, state.withProperty(LEVEL, ++level));
            world.scheduleUpdate(pos, state.getBlock(), state.getBlock().tickRate(world));
            return true;
        }
        return false;
    }

    public static void levelDown(World world, BlockPos pos, IBlockState state) {
        int level = state.getValue(LEVEL);
        if (level < 1) {
            world.setBlockToAir(pos);
        } else {
            world.setBlockState(pos, state.withProperty(LEVEL, --level));
            world.scheduleUpdate(pos, state.getBlock(), state.getBlock().tickRate(world));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (rand.nextInt(10) != 0) {
            return;
        }
        if (world.isAirBlock(pos.up())) {
            double d0 = (double) pos.getX() + rand.nextFloat();
            double d1 = (double) pos.getY() + 1.1D;
            double d2 = (double) pos.getZ() + rand.nextFloat();
            PaticleFactory.createColoerSomoke(world, d0, d1, d2, 0xEEEEEE);
        }
    }
}
