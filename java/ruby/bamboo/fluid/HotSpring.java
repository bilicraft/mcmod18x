package ruby.bamboo.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class HotSpring extends Fluid {

    public static final String FLUID_NAME="bamboo_hot_spring";
    public HotSpring() {
        super(FLUID_NAME, new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow"));
    }

}
