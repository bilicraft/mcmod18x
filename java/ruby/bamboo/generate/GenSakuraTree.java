package ruby.bamboo.generate;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import ruby.bamboo.block.SakuraLeave;
import ruby.bamboo.block.SakuraLeave.EnumLeave;
import ruby.bamboo.block.SakuraLog;
import ruby.bamboo.block.SakuraSapling;
import ruby.bamboo.core.DataManager;

public class GenSakuraTree extends WorldGenAbstractTree {
    private static final IBlockState field_181653_a = DataManager.getState(SakuraLog.class);
    private static final IBlockState field_181654_b = DataManager.getState(SakuraLeave.class).withProperty(SakuraLeave.VARIANT, EnumLeave.WHITE).withProperty(SakuraLeave.CHECK_DECAY, Boolean.valueOf(false));

    private final int minTreeHeight;
    private final boolean vinesGrow;
    private final IBlockState metaWood;
    private final IBlockState metaLeaves;

    public GenSakuraTree(boolean blockNotify) {
        this(blockNotify, 4, field_181653_a, field_181654_b, false);
    }

    public GenSakuraTree(boolean blockNotify, int minHeight, IBlockState metaWood, IBlockState metaLeaves, boolean isVineGrow) {
        super(blockNotify);
        this.minTreeHeight = minHeight;
        this.metaWood = metaWood;
        this.metaLeaves = metaLeaves;
        this.vinesGrow = isVineGrow;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int i = rand.nextInt(3) + this.minTreeHeight;
        boolean flag = true;

        if (position.getY() >= 1 && position.getY() + i + 1 <= 256) {
            byte b0;
            int l;

            for (int j = position.getY(); j <= position.getY() + 1 + i; ++j) {
                b0 = 1;

                if (j == position.getY()) {
                    b0 = 0;
                }

                if (j >= position.getY() + 1 + i - 2) {
                    b0 = 2;
                }

                for (int k = position.getX() - b0; k <= position.getX() + b0 && flag; ++k) {
                    for (l = position.getZ() - b0; l <= position.getZ() + b0 && flag; ++l) {
                        if (j >= 0 && j < 256) {
                            if (!this.isReplaceable(worldIn, new BlockPos(k, j, l))) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag) {
                return false;
            } else {
                BlockPos down = position.down();
                Block block1 = worldIn.getBlockState(down).getBlock();
                boolean isSoil = block1.canSustainPlant(worldIn, down, net.minecraft.util.EnumFacing.UP, DataManager.getBlock(SakuraSapling.class));

                if (isSoil && position.getY() < 256 - i - 1) {
                    block1.onPlantGrow(worldIn, down, position);
                    b0 = 3;
                    byte b1 = 0;
                    int i1;
                    int j1;
                    int k1;
                    int l1;
                    BlockPos blockpos1;

                    for (l = position.getY() - b0 + i; l <= position.getY() + i; ++l) {
                        i1 = l - (position.getY() + i);
                        j1 = b1 + 1 - i1 / 2;

                        for (k1 = position.getX() - j1; k1 <= position.getX() + j1; ++k1) {
                            l1 = k1 - position.getX();

                            for (int i2 = position.getZ() - j1; i2 <= position.getZ() + j1; ++i2) {
                                int j2 = i2 - position.getZ();

                                if (Math.abs(l1) != j1 || Math.abs(j2) != j1 || rand.nextInt(2) != 0 && i1 != 0) {
                                    blockpos1 = new BlockPos(k1, l, i2);
                                    Block block = worldIn.getBlockState(blockpos1).getBlock();

                                    if (block.isAir(worldIn, blockpos1) || block.isLeaves(worldIn, blockpos1) || block.getMaterial() == Material.vine) {
                                        this.setBlockAndNotifyAdequately(worldIn, blockpos1, this.metaLeaves);
                                    }
                                }
                            }
                        }
                    }

                    for (l = 0; l < i; ++l) {
                        BlockPos upN = position.up(l);
                        Block block2 = worldIn.getBlockState(upN).getBlock();

                        if (block2.isAir(worldIn, upN) || block2.isLeaves(worldIn, upN) || block2.getMaterial() == Material.vine) {
                            this.setBlockAndNotifyAdequately(worldIn, position.up(l), this.metaWood);

                        }
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

}
