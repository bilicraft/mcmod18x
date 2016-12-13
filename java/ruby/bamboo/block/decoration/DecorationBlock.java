package ruby.bamboo.block.decoration;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class DecorationBlock extends Block {

    public DecorationBlock(Material materialIn, MapColor mapColor) {
        super(materialIn,mapColor);
        this.setHardness(0.5F);
        this.setResistance(300F);
    }

}
