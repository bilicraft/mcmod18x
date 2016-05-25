package ruby.bamboo.entity.arrow;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.core.DataManager;
import ruby.bamboo.item.arrow.LightArrow;

public class EntityLightArrow extends BaseArrow {

    public EntityLightArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityLightArrow(World worldIn, EntityLivingBase shooter, EntityLivingBase p_i1755_3_, float p_i1755_4_, float p_i1755_5_) {
        super(worldIn, shooter, p_i1755_3_, p_i1755_4_, p_i1755_5_);
    }

    public EntityLightArrow(World worldIn, EntityLivingBase shooter, float velocity) {
        super(worldIn, shooter, velocity);
    }

    public EntityLightArrow(World worldIn) {
        super(worldIn);
    }

    @Override
    public void motionUpdate(float xyzVariation, float yDecrease) {
        this.motionX *= (double) xyzVariation;
        this.motionY *= (double) xyzVariation;
        this.motionZ *= (double) xyzVariation;
        this.motionY -= (double) yDecrease * 0.5;
    }

    @Override
    public ItemStack getItemArrow() {
        return DataManager.getItemStack(LightArrow.class, 1, 0);
    }

}
