package ruby.bamboo.item.arrow;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.item.BambooBow;

public interface IBambooArrow {
    public void execute(World world, ItemStack bow, ItemStack arrow, float power, int chargeFrame, EntityPlayer player);

    public default void onHitEntity(World world, Entity hitEntity, Entity shooter, ItemStack arrow) {
    }

    public default boolean isNoResources(EntityPlayer player) {
        return player.capabilities.isCreativeMode;
    }

    public default ModelResourceLocation getBowModel(int chargeFrame) {
        String jsonName = BambooBow.ICON_PULL_NAMES[chargeFrame >= 40 ? 3 : chargeFrame > 25 ? 2 : 1];
        return new ModelResourceLocation(jsonName, "inventory");
    }
}
