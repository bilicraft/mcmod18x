package ruby.bamboo.enchant;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import ruby.bamboo.enchant.event.IBreakEnchant;
import ruby.bamboo.util.BlockDestroyHelper;

public class Extender extends EnchantBase implements IBreakEnchant {

    public Extender(int id, String name) {
        super(id, name);
    }

    @Override
    public void onBreakBlock(ItemStack stack, World world, Block block, BlockPos pos, EntityLivingBase player, MovingObjectPosition mop) {
        if (!world.isRemote) {
            EnumFacing facing = mop.sideHit;
            int size = 1;
            int length = (int) Math.floor(IBambooEnchantable.getEnchLevel(stack, this, SUB_WILD) / 150);
            List<BlockPos> list;
            EnumFacing ratated;
            switch (facing) {
                case NORTH:
                case SOUTH:
                case EAST:
                case WEST:
                    ratated = facing.rotateAround(Axis.Y);
                    list = Lists.newArrayList(BlockPos.getAllInBox(pos.offset(ratated, size).up(size), pos.offset(facing.getOpposite(), length).offset(ratated.getOpposite(), size).down(size)));
                    break;
                case DOWN:
                case UP:
                    ratated = facing.rotateAround(Axis.X);
                    list = Lists.newArrayList(BlockPos.getAllInBox(pos.offset(ratated, size).east(size), pos.offset(facing.getOpposite(), length).offset(ratated.getOpposite(), size).west(size)));
                    break;

                default:
                    list = Lists.newArrayList();
                    break;
            }
            list = list.stream().filter(target -> !world.isAirBlock(target)).sorted(new Comparator<BlockPos>() {
                @Override
                public int compare(BlockPos o1, BlockPos o2) {
                    return (int) (o1.distanceSq(pos) - o2.distanceSq(pos));
                }
            }).collect(Collectors.toList());
            ((IBambooEnchantable) stack.getItem()).addExp(stack, list.size(), 1);
            BlockDestroyHelper.addQueue(world, list);
        }
    }

    @Override
    public int getRarity() {
        return 15;
    }

}
