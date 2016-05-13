package ruby.bamboo.item.arrow;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IBambooArrow {
    public void execute(World world, ItemStack bow, ItemStack arrow, float power);
    public default void onHitEntity(World world,Entity hitEntity,Entity shooter,ItemStack arrow){}

    public ModelResourceLocation getBowModel(int useRemaining);
}
