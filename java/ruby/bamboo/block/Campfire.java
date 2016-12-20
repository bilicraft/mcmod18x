package ruby.bamboo.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruby.bamboo.api.Constants;
import ruby.bamboo.core.init.BambooData.BambooBlock;
import ruby.bamboo.core.init.BambooData.BambooBlock.StateIgnore;
import ruby.bamboo.core.init.EnumCreateTab;
import ruby.bamboo.gui.GuiHandler;
import ruby.bamboo.tileentity.TileCampfire;

@BambooBlock(name = "campfire", createiveTabs = EnumCreateTab.TAB_BAMBOO)
public class Campfire extends BlockContainer {
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create(Constants.META, EnumFacing.class);

    public Campfire() {
        super(Material.GROUND);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setHardness(1);
        this.setResistance(300F);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing enumfacing = EnumFacing.getHorizontal(MathHelper.floor_double((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();
        worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 3);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {

        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile != null) {
            GuiHandler.openGui(worldIn, playerIn, GuiHandler.GUI_CAMPFIRE, pos);
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        float var7 = pos.getX() + 0.5F;
        float var8 = pos.getY() + 0.2F;
        float var9 = pos.getZ() + 0.5F;
        float var10 = rand.nextFloat() * 0.4F - 0.2F;
        float var11 = rand.nextFloat() * 0.4F - 0.2F;
        //        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var7 + var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
        if (rand.nextFloat() < 0.1F) {
            world.spawnParticle(EnumParticleTypes.FLAME, var7 + var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileCampfire) {
            for (ItemStack stack : ((TileCampfire) tile).getInventorySlots()) {
                if (stack != null) {
                    spawnAsEntity(worldIn, pos, stack);
                }
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileCampfire();
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @StateIgnore
    public IProperty[] getIgnoreState() {
        return new IProperty[] { FACING };
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.IGNORE;
    }

    @Override
    public MapColor getMapColor(IBlockState state) {
        return MapColor.AIR;
    }

}
