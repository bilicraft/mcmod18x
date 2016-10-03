package ruby.bamboo.enchant;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import ruby.bamboo.enchant.event.IBreakEnchant;
import ruby.bamboo.enchant.event.IInteractionForEntity;

public class Shears extends EnchantBase implements IInteractionForEntity, IBreakEnchant {

    public Shears(int id, String name) {
        super(id, name);
    }

    @Override
    public void onBreakBlock(ItemStack stack, World world, Block block, BlockPos pos, EntityLivingBase player, RayTraceResult rtr) {
        System.out.println("call Shears");
    }

    @Override
    public void onInteract(ItemStack stack, EntityLivingBase target, EntityLivingBase user) {

    }

}
