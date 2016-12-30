package ruby.bamboo.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ruby.bamboo.api.BambooBlocks;
import ruby.bamboo.core.init.BambooData.BambooBlock;
import ruby.bamboo.core.init.EnumCreateTab;
import ruby.bamboo.tileentity.TileSpringWater;

@BambooBlock(name = "spring_block", createiveTabs = EnumCreateTab.TAB_BAMBOO)
public class SpringBlock extends Block {

    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public SpringBlock(Material materialIn) {
        super(materialIn);
        this.setDefaultState(this.getDefaultState().withProperty(ACTIVE, false));
        this.setHardness(1);
        this.setResistance(300F);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        BlockPos upperPos = pos.up();
        IBlockState upperState = worldIn.getBlockState(upperPos);

        if (upperState.getBlock() == BambooBlocks.SPRING_WATER) {
            //水位の増加
            if (SpringWater.levelUp(worldIn, upperPos, upperState)) {
                worldIn.scheduleUpdate(upperPos, upperState.getBlock(), upperState.getBlock().tickRate(worldIn));
            } else {
                // 満水の時はスケジューリングを行わない
                return;
            }
        } else if (upperState.getBlock() == Blocks.AIR) {
            //水セット
            worldIn.setBlockState(upperPos, BambooBlocks.SPRING_WATER.getDefaultState());
        } else {
            //停止
            worldIn.setBlockState(pos, state.withProperty(ACTIVE, false));
        }
        if (state.getValue(ACTIVE)) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
    }

    @Override
    public int tickRate(World worldIn) {
        return 20;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        if (state.getValue(ACTIVE)) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        boolean activeFlg = !state.getValue(ACTIVE);

        worldIn.setBlockState(pos, state.withProperty(ACTIVE, activeFlg));

        if (activeFlg) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        } else {
            IBlockState upperState = worldIn.getBlockState(pos.up());
            if (upperState.getBlock() == BambooBlocks.SPRING_WATER) {
                ((TileSpringWater) worldIn.getTileEntity(pos.up())).setDead();
            }
        }
        return true;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ACTIVE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ACTIVE, meta > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ACTIVE) ? 1 : 0;
    }

}
