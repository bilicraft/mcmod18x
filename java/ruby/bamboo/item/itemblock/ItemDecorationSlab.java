package ruby.bamboo.item.itemblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruby.bamboo.block.decoration.DecorationDoubleSlab;
import ruby.bamboo.block.decoration.DecorationSlab;

public class ItemDecorationSlab extends ItemBlock {
    private final BlockSlab singleSlab;
    private final BlockSlab doubleSlab;

    public ItemDecorationSlab(Block block, BlockSlab singleSlab, BlockSlab doubleSlab) {
        super(block);
        this.singleSlab = singleSlab;
        this.doubleSlab = doubleSlab;
        this.setMaxDamage(0);
    }

    public ItemDecorationSlab(Block block, DecorationSlab singleSlab, DecorationDoubleSlab doubleSlab) {
        this(block, (BlockSlab) singleSlab, (BlockSlab) doubleSlab);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
//        if (stack.stackSize == 0) {
//            return false;
//        } else if (!playerIn.canPlayerEdit(pos.offset(facing), facing, stack)) {
//            return false;
//        } else {
//            IBlockState iblockstate = worldIn.getBlockState(pos);
//
//            if (iblockstate.getBlock() == this.singleSlab) {
//                BlockSlab.EnumBlockHalf enumblockhalf = iblockstate.getValue(BlockSlab.HALF);
//
//                if ((facing == EnumFacing.UP && enumblockhalf == BlockSlab.EnumBlockHalf.BOTTOM || facing == EnumFacing.DOWN && enumblockhalf == BlockSlab.EnumBlockHalf.TOP)) {
//                    IBlockState iblockstate1 = this.doubleSlab.getDefaultState();
//
//                    if (worldIn.checkNoEntityCollision(this.doubleSlab.getCollisionBoundingBox(worldIn, pos, iblockstate1)) && worldIn.setBlockState(pos, iblockstate1, 3)) {
//                        worldIn.playSoundEffect(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, this.doubleSlab.stepSound
//                                .getPlaceSound(), (this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F, this.doubleSlab.stepSound.getFrequency() * 0.8F);
//                        --stack.stackSize;
//                    }
//
//                    return true;
//                }
//            }
//
//            return this.tryPlace(stack, worldIn, pos.offset(facing)) ? true : super.onItemUse(stack, playerIn, worldIn, pos, facing, hitX, hitY, hitZ);
//        }
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (!block.isReplaceable(worldIn, pos))
        {
            pos = pos.offset(facing);
        }

        if (stack.stackSize != 0 && playerIn.canPlayerEdit(pos, facing, stack) && worldIn.canBlockBePlaced(this.block, pos, false, facing, (Entity)null, stack))
        {
            int i = this.getMetadata(stack.getMetadata());
            IBlockState iblockstate1 = this.block.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, i, playerIn);

            if (placeBlockAt(stack, playerIn, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1))
            {
                SoundType soundtype = this.block.getSoundType();
                worldIn.playSound(playerIn, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                --stack.stackSize;
            }

            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
        BlockPos blockpos1 = pos;
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (iblockstate.getBlock() == this.singleSlab) {
            boolean flag = iblockstate.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP;

            if ((side == EnumFacing.UP && !flag || side == EnumFacing.DOWN && flag)) {
                return true;
            }
        }

        pos = pos.offset(side);
        IBlockState iblockstate1 = worldIn.getBlockState(pos);
        return iblockstate1.getBlock() == this.singleSlab ? true : super.canPlaceBlockOnSide(worldIn, blockpos1, side, player, stack);
    }

//    private boolean tryPlace(ItemStack stack, World worldIn, BlockPos pos) {
//        IBlockState iblockstate = worldIn.getBlockState(pos);
//
//        if (iblockstate.getBlock() == this.singleSlab) {
//            IBlockState iblockstate1 = this.doubleSlab.getDefaultState();
//
//            if (worldIn.checkNoEntityCollision(this.doubleSlab.getCollisionBoundingBox(worldIn, pos, iblockstate1)) && worldIn.setBlockState(pos, iblockstate1, 3)) {
//                worldIn.playSoundEffect(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, this.doubleSlab.stepSound
//                        .getPlaceSound(), (this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F, this.doubleSlab.stepSound.getFrequency() * 0.8F);
//                --stack.stackSize;
//            }
//
//            return true;
//
//        }
//
//        return false;
//    }

    public Block getSingleSlab() {
        return singleSlab;
    }
}
