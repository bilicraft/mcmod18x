package ruby.bamboo.block;

import javax.annotation.Nullable;

import net.minecraft.block.BlockBed;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ruby.bamboo.core.init.BambooData.BambooBlock;
import ruby.bamboo.core.init.BambooData.BambooBlock.StateIgnore;
import ruby.bamboo.core.init.EnumCreateTab;
import ruby.bamboo.entity.Chair;
import ruby.bamboo.item.itemblock.ItemHuton;

@BambooBlock(itemBlock = ItemHuton.class, createiveTabs = EnumCreateTab.TAB_BAMBOO)
public class Huton extends BlockBed {

    private final static AxisAlignedBB HUTON_AABB = new AxisAlignedBB(0, 0, 0, 1, 0.25, 1);
    private static final int timeacc = 1200;

    public Huton() {
        super();
        this.setHardness(0.5F);
        this.setResistance(300F);
    }

    @Override
    public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, Entity player) {
        return true;
    }

    @StateIgnore
    public IProperty[] getIgnoreState() {
        return new IProperty[] { OCCUPIED };
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return HUTON_AABB;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }

        if (state.getValue(PART) != BlockBed.EnumPartType.FOOT) {
            pos = pos.offset((EnumFacing) state.getValue(FACING).getOpposite());
            state = worldIn.getBlockState(pos);

            if (state.getBlock() != this) {
                return true;
            }
        }

        EntityPlayer.SleepResult result = playerIn.trySleep(pos);
        if (EntityPlayer.SleepResult.NOT_POSSIBLE_NOW == result) {
            Chair chair = new Chair(worldIn);
            chair.setPosition(pos.getX() + 0.5, pos.getY() - 1.3, pos.getZ() + 0.5);
            chair.setListner((world, entity) -> {
                if (world.getWorldInfo().getWorldTime() % 100 == 0) {
                    world.getWorldInfo().setWorldTime(world.getWorldInfo().getWorldTime() + timeacc + 1);
                    if (world.getWorldInfo().isRaining()) {
                        if (world.getWorldInfo().getRainTime() > timeacc) {
                            world.getWorldInfo().setRainTime(world.getWorldInfo().getRainTime() - timeacc);
                        }
                    }
                    if (world.getWorldInfo().isThundering()) {
                        if (world.getWorldInfo().getThunderTime() > timeacc) {
                            world.getWorldInfo().setThunderTime(world.getWorldInfo().getThunderTime() - timeacc);
                        }
                    }
                    int hour = (int) (((world.getWorldInfo().getWorldTime() + 6000) % 24000) / 1000);
                    int minute = (int) (((world.getWorldInfo().getWorldTime() + 6000) % 600) / 10);
                    Entity player = entity.getPassengers().isEmpty() ? null : entity.getPassengers().get(0);
                    if (player != null && player instanceof EntityPlayer) {
                        ((EntityPlayer) player).addChatMessage(new TextComponentString(hour + ":" + minute + (world.getWorldInfo().isRaining() ? " RainTime at" + world.getWorldInfo().getRainTime() : "") + (world.getWorldInfo()
                                .isThundering() ? " ThunderTime at" + world.getWorldInfo().getThunderTime() : "")));
                    }
                }
            });
            worldIn.spawnEntityInWorld(chair);
            playerIn.startRiding(chair);
            return true;
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
    }

    @Override
    public MapColor getMapColor(IBlockState state) {
        return MapColor.AIR;
    }
}
