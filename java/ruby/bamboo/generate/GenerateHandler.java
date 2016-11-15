package ruby.bamboo.generate;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;
import ruby.bamboo.api.BambooBlocks;
import ruby.bamboo.block.SakuraSapling;

public class GenerateHandler implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        //        WorldChunkManager worldchunkmanager = world.getWorldChunkManager();
        //        if (worldchunkmanager != null) {
        //            BiomeGenBase biome = worldchunkmanager.getBiomeGenerator(new BlockPos(chunkX * 16, 64, chunkZ * 16));
        Biome biome = world.getBiomeForCoordsBody(new BlockPos(chunkX * 16, 64, chunkZ * 16));
        if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.MOUNTAIN) || BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.FOREST)) {
            if (!BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.SNOWY)) {
                switch (random.nextInt(4)) {
                    case 1:
                        this.generateBambooshoot(random, world, chunkX, chunkZ, 60);
                        break;
                    case 2:
                        this.generateSakura(random, world, chunkX, chunkZ, 90);
                        break;
                }
            }
        } else if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.BEACH) || BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.WATER) || BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.SWAMP)) {
            switch (random.nextInt(4)) {
                case 0:
                    // 海藻
            }
        }
        //        }
    }

    private void generateBambooshoot(Random random, World world, int chunkX, int chunkZ, int maxHeight) {
        for (int l = random.nextInt(10); 0 < l; l--) {
            int x = chunkX * 16 + random.nextInt(16);
            int y = random.nextInt(maxHeight) + 60;
            int z = chunkZ * 16 + random.nextInt(16);
            int posX, posY, posZ;
            for (int var6 = 0; var6 < 20; ++var6) {
                posX = x + random.nextInt(8) - random.nextInt(8);
                posY = y + random.nextInt(4) - random.nextInt(4);
                posZ = z + random.nextInt(8) - random.nextInt(8);
                BlockPos pos = new BlockPos(posX, posY, posZ);
                IBlockState block = world.getBlockState(pos.down());
                if (world.isAirBlock(pos)) {
                    if (block.getBlock() == Blocks.GRASS || block.getBlock() == Blocks.DIRT) {
                        world.setBlockState(pos, BambooBlocks.BAMBOOSHOOT.getDefaultState());
                    }
                }
            }
        }
    }

    private void generateSakura(Random random, World world, int chunkX, int chunkZ, int maxHeight) {
        for (int l = random.nextInt(5); 0 < l; l--) {
            BlockPos orign = new BlockPos(chunkX * 16 + random.nextInt(16), random.nextInt(maxHeight) + 60, chunkZ * 16 + random.nextInt(16));
            for (int var6 = 0; var6 < 10; ++var6) {
                BlockPos pos = orign.add(8 - random.nextInt(16), 4 - random.nextInt(8), 8 - random.nextInt(16));
                IBlockState block = world.getBlockState(pos.down());
                if (world.isAirBlock(pos)) {
                    if (block.getBlock() == Blocks.GRASS || block.getBlock() == Blocks.DIRT) {
                        ((SakuraSapling) BambooBlocks.SAKURA_SAPLING).generateTree(world, pos, BambooBlocks.SAKURA_SAPLING.getDefaultState(), random);
                        return;
                    }
                }
            }
        }
    }
}
