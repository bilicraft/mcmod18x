package ruby.bamboo.texture;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public interface IMultiTextuer {

    public String getTexName(IBlockState state, EnumFacing side);
}
