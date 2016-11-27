package ruby.bamboo.block.paticle;

import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.world.World;

public class ColorSmoke extends ParticleSmokeNormal{

    public ColorSmoke(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i46348_8_, double p_i46348_10_, double p_i46348_12_, float p_i46348_14_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i46348_8_, p_i46348_10_, p_i46348_12_, p_i46348_14_);
    }


    public ColorSmoke setColor(int colorCode){
        this.setRBGColorF((colorCode>>16)/255F, ((colorCode>>8)&0xFF)/255F, (colorCode&0xFF)/255F);
        return this;
    }


}
