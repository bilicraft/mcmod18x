package ruby.bamboo.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ruby.bamboo.api.Constants;
import ruby.bamboo.core.init.BambooData.BambooBlock;
import ruby.bamboo.core.init.BambooData.BambooBlock.StateIgnore;
import ruby.bamboo.core.init.EnumCreateTab;
import ruby.bamboo.gui.GuiHandler;
import ruby.bamboo.tileentity.TileMillStone;

@BambooBlock(createiveTabs = EnumCreateTab.TAB_BAMBOO)
public class MillStone extends BlockContainer {

    public static PropertyInteger META = PropertyInteger.create(Constants.META, 0, 15);

    public MillStone() {
        super(Material.ROCK);
        this.setHardness(1);
        this.setResistance(300F);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {

        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile != null) {
            GuiHandler.openGui(worldIn, playerIn, GuiHandler.GUI_MILLSTONE, pos);
        }
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(META, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(META);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, META);
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

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileMillStone();
    }

    @StateIgnore
    public IProperty[] getIgnoreState() {
        return new IProperty[] { META };
    }

    @Override
    public MapColor getMapColor(IBlockState state) {
        return MapColor.STONE;
    }
}
