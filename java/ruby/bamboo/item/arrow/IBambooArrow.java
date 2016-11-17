package ruby.bamboo.item.arrow;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IBambooArrow {
    public void execute(World world, ItemStack bow, ItemStack arrow, float power, int chargeFrame, EntityPlayer player);

    public default void onHitEntity(World world, Entity hitEntity, Entity shooter, ItemStack arrow) {
    }

    public default boolean isNoResources(EntityPlayer player) {
        return player.capabilities.isCreativeMode;
    }

    public default float getBowModel(int chargeFrame) {
        return chargeFrame >= 40 ? 1 : chargeFrame > 25 ? 0.7F : 0;
    }
}
