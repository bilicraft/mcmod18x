package ruby.bamboo.entity.arrow;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.api.BambooItems;

public class EntityLightArrow extends BaseArrow {

    public EntityLightArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityLightArrow(World worldIn, EntityLivingBase shooter, float velocity) {
        super(worldIn, shooter, velocity);
    }

    public EntityLightArrow(World worldIn) {
        super(worldIn);
    }

    @Override
    public void motionUpdate(float xyzVariation, float yDecrease) {
        this.motionX *= xyzVariation;
        this.motionY *= xyzVariation;
        this.motionZ *= xyzVariation;
        this.motionY -= yDecrease * 0.5;
    }

    @Override
    public ItemStack getItemArrow() {
        return new ItemStack(BambooItems.LIGHT_ARROW, 1, 0);
    }

}
