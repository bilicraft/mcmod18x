package ruby.bamboo.block.decoration;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import ruby.bamboo.core.Constants;
import ruby.bamboo.model.IRetexture;

//メモ
//registerBlock(53, "oak_stairs", (new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK))).setUnlocalizedName("stairsWood"));

public class DecorationStairs extends BlockStairs implements IRetexture {

    private String name;

    protected DecorationStairs(IBlockState modelState) {
        super(modelState);
        this.setHardness(0.5F);
        this.setResistance(300F);
    }

    public DecorationStairs setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public ImmutableMap<String, String> getTextureMap(int meta) {
        String path = Constants.getBlockTexPath() + this.getName();
        return ImmutableMap.of("bottom", path, "top", path, "side", path);
    }
}
