package ruby.bamboo.dimension;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import ruby.bamboo.core.Config;

public class BambooDimension {
    public BambooDimension() {
        create();
    }

    public void create() {
        DimensionType.register("BambooMod", "", Config.DIMID.get(), BambooWorldProvider.class, false);
        DimensionManager.registerDimension(Config.DIMID.get(), DimensionType.getById(Config.DIMID.get()));
    }

}
