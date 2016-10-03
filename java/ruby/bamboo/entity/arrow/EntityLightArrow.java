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
        return DataManager.getItemStack(LightArrow.class, 1, 0);
    }

    @Override
    protected ItemStack getArrowStack() {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

}
