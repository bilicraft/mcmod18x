package ruby.bamboo.block.decoration;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import ruby.bamboo.core.Constants;
import ruby.bamboo.model.IRetexture;

public class DecorationBlock extends Block implements IRetexture {

    private String name;

    public DecorationBlock(Material materialIn) {
        super(materialIn);
        this.setHardness(0.5F);
        this.setResistance(300F);
    }

    public DecorationBlock setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public ImmutableMap<String, String> getTextureMap(int meta) {
        String path=Constants.getBlockTexPath()+this.getName();
        return ImmutableMap.of("all",path);
    }

}
