package ruby.bamboo.dimension;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.gen.ChunkProviderFlat;
import ruby.bamboo.core.Config;

public class BambooWorldProvider extends WorldProviderSurface {
    public BambooWorldProvider() {
    }

    @Override
    public DimensionType getDimensionType() {
        return DimensionType.getById(Config.DIMID.get());
    }

    @Override
    public boolean isSurfaceWorld() {
        return true;
    }

    @Override
    public BlockPos getSpawnCoordinate() {
        return new BlockPos(0, 10, 0);
    }

    @Override
    public int getAverageGroundLevel() {
        return 4;
    }

    @Override
    public void createBiomeProvider() {
        this.biomeProvider = WorldType.FLAT.getBiomeProvider(worldObj);
    }

    @Override
    public String getSaveFolder() {
        return "bamboo_dim";
    }

    @Override
    public BlockPos getRandomizedSpawnPoint() {
        return getSpawnPoint();
    }

    @Override
    public BlockPos getSpawnPoint() {
        return BlockPos.ORIGIN.up(5);
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new NonEntity(this.worldObj, this.worldObj.getSeed(), false, "3;minecraft:bedrock,2*minecraft:dirt,minecraft:grass;");
    }

    public class NonEntity extends ChunkProviderFlat {

        public NonEntity(World worldIn, long seed, boolean generateStructures, String flatGeneratorSettings) {
            super(worldIn, seed, generateStructures, flatGeneratorSettings);
        }

        @Override
        public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
            return ImmutableList.of();
        }
    }


    @Override
    public double getVoidFogYFactor()
    {
        return 1;
    }
}