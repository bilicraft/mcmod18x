package ruby.bamboo.block;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruby.bamboo.api.BambooBlocks;
import ruby.bamboo.block.SakuraLeave.EnumLeave;
import ruby.bamboo.core.init.BambooData.BambooBlock;
import ruby.bamboo.core.init.BambooData.BambooBlock.StateIgnore;
import ruby.bamboo.core.init.EnumCreateTab;
import ruby.bamboo.generate.GenSakuraBigTree;
import ruby.bamboo.generate.GenSakuraTree;

@BambooBlock(name = "sakura_sapling", createiveTabs = EnumCreateTab.TAB_BAMBOO)
public class SakuraSapling extends BlockSapling implements IGrowable {

    public SakuraSapling() {
        super();
        this.setDefaultState(this.blockState.getBaseState().withProperty(STAGE, Integer.valueOf(0)));
        float f = 0.4F;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            super.updateTick(worldIn, pos, state, rand);

            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0) {
                this.grow(worldIn, rand, pos, state);
            }
        }
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        if (state.getValue(STAGE).intValue() == 0) {
            worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
        } else {
            this.generateTree(worldIn, pos, state, rand);
        }
    }

    @Override
    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos)) {
            return;
        }
        WorldGenAbstractTree object = rand.nextInt(10) == 0 ? new GenSakuraBigTree(true) : new GenSakuraTree(true);
        generate(worldIn, pos, state, rand, object);
    }

    private void generate(World worldIn, BlockPos pos, IBlockState state, Random rand, WorldGenAbstractTree tree) {
        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos))
            return;
        int i = 0;
        int j = 0;
        boolean flag = false;
        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
        if (!((WorldGenerator) tree).generate(worldIn, rand, pos.add(i, 0, j))) {
            worldIn.setBlockState(pos, state, 4);
        }
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return worldIn.rand.nextFloat() < 0.45D;
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { STAGE, TYPE });
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(STAGE, Integer.valueOf((meta)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(STAGE).intValue();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote && heldItem != null && heldItem.getItem() == Items.DYE) {
            Random rand = worldIn.rand;
            if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos)) {
                return false;
            }
            EnumLeave leave = EnumLeave.getLeaveFromDye(heldItem.getItemDamage());
            IBlockState leaveState = BambooBlocks.SAKURA_LEAVE.getDefaultState();
            PropertyEnum<EnumLeave> prop = SakuraLeave.VARIANT;

            if (Arrays.binarySearch(EnumLeave.BROAD_LEAVES, leave) > 0) {
                leaveState = BambooBlocks.BROAD_LEAVE.getDefaultState();
                prop = BroadLeave.VARIANT;
            }

            WorldGenAbstractTree object = rand.nextInt(10) == 0 ? new GenSakuraBigTree(true, leaveState.withProperty(prop, leave)) : new GenSakuraTree(true, leaveState.withProperty(prop, leave));
            generate(worldIn, pos, state, rand, object);
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(itemIn));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }

    @Override
    public boolean isTypeAt(World worldIn, BlockPos pos, BlockPlanks.EnumType type) {
        return false;
    }

    @StateIgnore
    public IProperty[] getIgnoreState() {
        return new IProperty[] { STAGE, TYPE };
    }
}
