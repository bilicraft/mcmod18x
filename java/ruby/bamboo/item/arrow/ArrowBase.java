package ruby.bamboo.item.arrow;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.entity.arrow.BaseArrow;
import ruby.bamboo.util.ItemStackHelper;

public abstract class ArrowBase extends Item implements IBambooArrow {
    public abstract Class<? extends BaseArrow> getArrowClass();

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        Class<? extends BaseArrow> arrow = getArrowClass();
        if (arrow == null) {
            return itemStackIn;
        }
        float power = 0.5F;
        try {
            BaseArrow entityArrow;
            try {
                entityArrow = arrow.getConstructor(World.class, EntityLivingBase.class, float.class, ItemStack.class).newInstance(worldIn, playerIn, power, itemStackIn);
            } catch (NoSuchMethodException e) {
                entityArrow = arrow.getConstructor(World.class, EntityLivingBase.class, float.class).newInstance(worldIn, playerIn, power);
            }
            if (!worldIn.isRemote) {
                worldIn.spawnEntityInWorld(entityArrow);
            }
            if (!isNoResources(playerIn)) {
                ItemStackHelper.decrStackSize(playerIn.inventory, itemStackIn, 1);
            } else {
                entityArrow.canBePickedUp = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemStackIn;
    }
}
