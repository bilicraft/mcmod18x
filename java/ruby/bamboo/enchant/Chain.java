package ruby.bamboo.enchant;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import ruby.bamboo.enchant.event.IBreakEnchant;
import ruby.bamboo.util.BlockDestroyHelper;

public class Chain extends EnchantBase implements IBreakEnchant {

    public Chain(int id, String name) {
        super(id, name);
    }

    @Override
    public void onBreakBlock(ItemStack stack, World world, Block block, BlockPos pos, EntityLivingBase player, RayTraceResult rtr) {
        if (!world.isRemote) {
            if (block instanceof BlockOre) {
                int limit = (int) Math.ceil(IBambooEnchantable.getEnchLevel(stack, this, SUB_WILD) / 15) + 3;
                Set<BlockPos> set = getBreakSet(world, pos, block, limit);
                IBambooEnchantable item = (IBambooEnchantable) stack.getItem();
                int exp = (int) (item.getBlockBreakExp(stack, world, block, pos) * set.size());
                item.addExp(stack, exp, 0.5F);
                BlockDestroyHelper.addQueue(player.getEntityWorld(), set);
            }
        }

    }

    /**
     * 連結一致ブロック探索
     *
     * @param world 対象ワールド
     * @param firstPos 開始pos
     * @param targetBlock 対象block
     * @param limit 上限値
     * @return 破壊BlockPosいりset
     */
    private Set<BlockPos> getBreakSet(World world, BlockPos firstPos, Block targetBlock, int limit) {
        List<BlockPos> nextTargets = Lists.newArrayList(firstPos);
        Set<BlockPos> fouds = new LinkedHashSet<>();
        do {
            nextTargets = nextTargets.stream()
                    .flatMap(target -> Arrays.stream(EnumFacing.VALUES).map(target::offset))
                    .filter(fixedPos -> world.getBlockState(fixedPos).getBlock().equals(targetBlock))
                    .filter(fixedPos -> fouds.size() <= limit && fouds.add(fixedPos))
                    .collect(Collectors.toList());
        } while (fouds.size() <= limit && !nextTargets.isEmpty());
        return fouds;
    }

    @Override
    public int getRarity() {
        return 15;
    }

}
