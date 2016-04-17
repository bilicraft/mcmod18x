package ruby.bamboo.block.decoration;

import java.util.Random;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruby.bamboo.core.Constants;
import ruby.bamboo.model.IRetexture;

public class DecorationDoubleSlab extends DecorationSlab implements IRetexture{

    private final String singleName;
    private String name;

    public DecorationDoubleSlab(Material materialIn, String singleName) {
        super(materialIn);
        this.singleName = singleName;
    }

    @Override
    public boolean isDouble() {
        // なぜこんな拡張性の低い設計なのか不思議。
        return true;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Block.getBlockFromName(singleName));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos) {
        return Item.getItemFromBlock(Block.getBlockFromName(singleName));
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getStateFromMeta(meta);
    }
    public DecorationDoubleSlab setName(String name) {
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
