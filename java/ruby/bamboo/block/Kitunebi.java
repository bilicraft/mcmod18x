package ruby.bamboo.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruby.bamboo.api.Constants;
import ruby.bamboo.core.init.BambooData.BambooBlock;
import ruby.bamboo.core.init.EnumCreateTab;

@BambooBlock(createiveTabs = EnumCreateTab.TAB_BAMBOO)

public class Kitunebi extends Block {

    public static final AxisAlignedBB INVISIBLE_BLOCK_AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

    private boolean isVisible = false;

    // 必要になったら
    //public static final PropertyInteger META = PropertyInteger.create(Constants.META, 0, 7);
    public static final PropertyBool VISIBLE = PropertyBool.create(Constants.FLG);

    public Kitunebi() {
        super(MaterialNone.instance);
        //this.setDefaultState(this.blockState.getBaseState().withProperty(META, 0).withProperty(VISIBLE, false));
        this.setDefaultState(this.blockState.getBaseState().withProperty(VISIBLE, false));
        setTickRandomly(true);
        setHardness(0.0F);
        setLightLevel(1F);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        //return this.getDefaultState().withProperty(META, meta & 7).withProperty(VISIBLE, (meta & 8) > 0);
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        //int meta= (Integer) state.getValue(META);
        //return meta;
        return 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        //return new BlockState(this, META, VISIBLE);
        return new BlockStateContainer(this, VISIBLE);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (state.getValue(VISIBLE)) {
            return FULL_BLOCK_AABB;
        } else {
            return INVISIBLE_BLOCK_AABB;
        }
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return NULL_AABB;
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
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @SideOnly(Side.CLIENT)
    private void setVisibleFlg(World world, BlockPos pos, IBlockState state, Random rand) {
        if (world.isRemote) {
            EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
            ItemStack[] handItems = { player.getHeldItemMainhand(), player.getHeldItemOffhand() };
            isVisible = false;

            for (ItemStack is : handItems) {
                if (is != null) {
                    Item item = is.getItem();

                    if (item instanceof ItemBlock) {
                        if (Block.getBlockFromItem(item) == this) {
                            isVisible = true;
                            break;
                        }
                    }
                }
            }

            if (isVisible != state.getValue(VISIBLE)) {
                world.setBlockState(pos, state.withProperty(VISIBLE, isVisible), 3);
            }
        }
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return super.canRenderInLayer(state, layer);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        this.setVisibleFlg(worldIn, pos, stateIn, rand);
    }

    //    @Override
    //    public String getTexName(IBlockState state, EnumFacing side) {
    //        return "bamboomod:blocks/kitunebi";
    //    }

}
