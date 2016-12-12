package ruby.bamboo.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruby.bamboo.api.BambooBlocks;
import ruby.bamboo.api.Constants;
import ruby.bamboo.core.init.BambooData.BambooBlock;
import ruby.bamboo.core.init.BambooData.BambooBlock.StateIgnore;
import ruby.bamboo.core.init.EnumCreateTab;
import ruby.bamboo.entity.SakuraPetal;
import ruby.bamboo.entity.SakuraPetal.ICustomPetal;
import ruby.bamboo.item.itemblock.ItemSakuraLeave;

@BambooBlock(name = "sakura_leave", itemBlock = ItemSakuraLeave.class, createiveTabs = EnumCreateTab.TAB_BAMBOO)
public class SakuraLeave extends BlockLeaves implements ILeave, ICustomPetal, IBlockColorWrapper {

    public final static PropertyEnum VARIANT = PropertyEnum.create("variant", EnumLeave.class, new Predicate() {
        public boolean apply(EnumLeave type) {
            ;
            return type.getMetadata() < 4;
        }

        @Override
        public boolean apply(Object p_apply_1_) {
            return this.apply((EnumLeave) p_apply_1_);
        }
    });;

    public SakuraLeave() {
        super();
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumLeave.WHITE).withProperty(CHECK_DECAY, Boolean.valueOf(true)).withProperty(DECAYABLE, Boolean.valueOf(true)));
        this.leavesFancy = true;
        this.setLightLevel(0.75F);
        this.setHardness(0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        byte b0 = 0;
        int i = b0 | ((EnumLeave) state.getValue(VARIANT)).getMetadata();

        if (!state.getValue(DECAYABLE).booleanValue()) {
            i |= 4;
        }

        if (state.getValue(CHECK_DECAY).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, EnumLeave.getLeave(meta & 3)).withProperty(DECAYABLE, (meta & 4) == 0).withProperty(CHECK_DECAY, (meta & 8) > 0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { VARIANT, DECAYABLE, CHECK_DECAY });
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        for (EnumLeave leave : EnumLeave.SAKURA_LEAVES) {
            list.add(new ItemStack(itemIn, 1, leave.getMetadata()));
        }
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        IBlockState state = world.getBlockState(pos);
        return new ArrayList(Arrays.asList(new ItemStack(this, 1, ((EnumLeave) state.getValue(VARIANT)).getMetadata())));
    }

    @Override
    public EnumType getWoodType(int meta) {
        return null;
    }

    @Override
    public String getLeaveName(int metadata) {
        return EnumLeave.getLeave(metadata).getName();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(BambooBlocks.SAKURA_SAPLING);
    }

    @Override
    protected ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, ((EnumLeave) state.getValue(VARIANT)).getMetadata());
    }

    @StateIgnore
    public IProperty[] getIgnoreState() {
        return new IProperty[] { DECAYABLE, CHECK_DECAY };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (placer != null) {
            worldIn.setBlockState(pos, state.withProperty(DECAYABLE, false), 4);
        }
    }

    public enum EnumLeave implements IStringSerializable {
        WHITE(0, 0xFFFFFF, 1, 15),
        PURPLE(1, 0xFFC5FC, 1, 5),
        MAGENTA(2, 0xF09090, 1, 13),
        PINK(3, 0xFFC5CC, 1, 9),
        GREEN(4, 0x3F9E55, 1, 2),
        RED(5, 0xc80010, 2, 1),
        YELLOW(6, 0xf5e600, 3, 11),
        ORANGE(7, 0xFFC600, 3, 14),;

        EnumLeave(int meta, int color, int petal, int dyeCode) {
            this.meta = (byte) meta;
            this.color = color;
            this.petal = (byte) petal;
            this.dyeCode = dyeCode;
        }

        public static final EnumLeave[] SAKURA_LEAVES = { WHITE, PURPLE, MAGENTA, PINK };
        public static final EnumLeave[] BROAD_LEAVES = { GREEN, RED, YELLOW, ORANGE };
        private byte meta;
        private int color;
        private byte petal;
        private int dyeCode;

        public static EnumLeave getLeave(int meta) {
            return meta < SAKURA_LEAVES.length ? SAKURA_LEAVES[meta] : BROAD_LEAVES[meta % 4];
        }

        public static EnumLeave getLeaveFromDye(int dye) {
            EnumLeave leave = EnumLeave.WHITE;
            for (EnumLeave l : EnumLeave.values()) {
                if (l.dyeCode == dye) {
                    leave = l;
                    break;
                }
            }
            return leave;
        }

        public byte getMetadata() {
            return this.meta;
        }

        public int getColor() {
            return this.color;
        }

        public MapColor getMapColor() {
            switch (this) {
                case GREEN:
                    return MapColor.GREEN;
                case MAGENTA:
                    return MapColor.MAGENTA;
                case ORANGE:
                    return MapColor.ADOBE;
                case PINK:
                    return MapColor.PINK;
                case PURPLE:
                    return MapColor.PURPLE;
                case RED:
                    return MapColor.RED;
                case WHITE:
                    return MapColor.QUARTZ;
                case YELLOW:
                    return MapColor.YELLOW;
                default:
                    return MapColor.PINK;
            }
        }

        public byte getPetal() {
            return this.petal;
        }

        @Override
        public String getName() {
            return this.name().toLowerCase();
        }

    }

    @Override
    public IBlockState getLeaveStateFromMeta(int meta) {
        return this.getStateFromMeta(meta);
    }

    @Override
    public int getLeaveRenderColor(IBlockState state) {
        return ((EnumLeave) state.getValue(VARIANT)).getColor();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (rand.nextInt(100) != 0) {
            return;
        }
        if (world.isAirBlock(pos.down())) {
            SakuraPetal petal = new SakuraPetal(world);
            petal.setPosition(pos.getX() + rand.nextFloat(), pos.getY(), pos.getZ() + rand.nextFloat());
            petal.setCustomPetal(state);
            petal.setColor(this.colorMultiplier(state, world, pos, 0));
            world.spawnEntityInWorld(petal);
        }
    }

    @Override
    public int getTexNum(IBlockState state) {
        return ((EnumLeave) state.getValue(VARIANT)).getPetal();
    }

    @Override
    public String getTexPath(IBlockState state) {
        return Constants.RESOURCED_DOMAIN + "textures/entitys/petal.png";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
        return ((EnumLeave) state.getValue(VARIANT)).getColor();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return createStackedBlock(state);
    }

    @Override
    public MapColor getMapColor(IBlockState state) {
        return ((EnumLeave) state.getValue(VARIANT)).getMapColor();
    }

}
