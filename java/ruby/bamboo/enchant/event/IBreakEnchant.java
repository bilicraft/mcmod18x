package ruby.bamboo.enchant.event;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public interface IBreakEnchant {
    public void onBreakBlock(ItemStack stack, World world, Block block, BlockPos pos, EntityLivingBase player, RayTraceResult rtr);
}
