package ruby.bamboo.block;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruby.bamboo.core.init.BambooData.BambooBlock;
import ruby.bamboo.core.init.BambooData.BambooBlock.StateIgnore;
import ruby.bamboo.core.init.EnumCreateTab;

@BambooBlock(createiveTabs = EnumCreateTab.TAB_BAMBOO)
public class IndLight extends BlockDirectional {

    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool SECOND_LINK = PropertyBool.create("secondlink");

    public static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0, 0.9, 0, 1, 1, 1);
    public static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0, 0, 0, 1, 0.1, 1);
    public static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.9, 0, 0, 1, 1, 1);
    public static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0, 0, 0, 1, 1, 0.1);
    public static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0, 0, 0.9, 1, 1, 1);
    public static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0, 0, 0, 0.1, 1, 1);

    public IndLight() {
        super(Material.GROUND);
        this.setDefaultState(this.blockState.getBaseState().withProperty(SECOND_LINK, false));
        this.setHardness(0.3F);
        this.setResistance(300F);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getStateFromMeta(meta).withProperty(FACING, facing.getOpposite()).withProperty(SECOND_LINK, placer.isSneaking());
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
        switch (enumfacing) {
            case DOWN:
                return DOWN_AABB;
            case EAST:
                return EAST_AABB;
            case NORTH:
                return NORTH_AABB;
            case SOUTH:
                return SOUTH_AABB;
            case UP:
                return UP_AABB;
            case WEST:
                return WEST_AABB;
            default:
                return super.getBoundingBox(state, source, pos);
        }
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 15;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {

        switch (state.getValue(FACING)) {
            case UP:
                return state.withProperty(NORTH, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.NORTH))))
                        .withProperty(SOUTH, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.SOUTH))))
                        .withProperty(EAST, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.EAST))))
                        .withProperty(WEST, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.WEST))));
            case DOWN:
                return state.withProperty(NORTH, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.SOUTH))))
                        .withProperty(SOUTH, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.NORTH))))
                        .withProperty(EAST, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.EAST))))
                        .withProperty(WEST, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.WEST))));
            case NORTH:
                return state.withProperty(NORTH, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.UP))))
                        .withProperty(SOUTH, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.DOWN))))
                        .withProperty(EAST, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.WEST))))
                        .withProperty(WEST, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.EAST))));
            case SOUTH:
                return state.withProperty(NORTH, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.UP))))
                        .withProperty(SOUTH, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.DOWN))))
                        .withProperty(EAST, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.EAST))))
                        .withProperty(WEST, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.WEST))));

            case EAST:
                return state.withProperty(NORTH, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.UP))))
                        .withProperty(SOUTH, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.DOWN))))
                        .withProperty(EAST, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.NORTH))))
                        .withProperty(WEST, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.SOUTH))));
            case WEST:
                return state.withProperty(NORTH, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.UP))))
                        .withProperty(SOUTH, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.DOWN))))
                        .withProperty(EAST, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.SOUTH))))
                        .withProperty(WEST, canChain(state, worldIn.getBlockState(pos.offset(EnumFacing.NORTH))));
            default:
                break;

        }
        return super.getActualState(state, worldIn, pos);
    }

    private boolean canChain(IBlockState state, IBlockState targetState) {
        return targetState.getBlock() == this && state.getValue(SECOND_LINK) == targetState.getValue(SECOND_LINK) && targetState.getValue(FACING) == state.getValue(FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7)).withProperty(SECOND_LINK, (meta & 8) != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex() + (state.getValue(SECOND_LINK) ? 8 : 0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, NORTH, EAST, SOUTH, WEST, SECOND_LINK);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @StateIgnore
    public IProperty[] getIgnoreState() {
        return new IProperty[] { SECOND_LINK };
    }
}
