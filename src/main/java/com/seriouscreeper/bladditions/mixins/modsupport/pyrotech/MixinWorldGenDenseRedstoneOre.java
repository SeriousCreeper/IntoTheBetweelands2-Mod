package com.seriouscreeper.bladditions.mixins.modsupport.pyrotech;

import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.worldgen.ModuleWorldGenConfig;
import com.codetaylor.mc.pyrotech.modules.worldgen.world.WorldGenDenseRedstoneOre;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.registries.BlockRegistry;

import java.util.Random;

@Mixin(value = WorldGenDenseRedstoneOre.class, remap = false)
public class MixinWorldGenDenseRedstoneOre {
    /**
     * @author SC
     */
    @Overwrite
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int blockXPos = chunkX << 4;
        int blockZPos = chunkZ << 4;
        if (!((double)random.nextFloat() > ModuleWorldGenConfig.DENSE_REDSTONE_ORE.CHANCE_TO_SPAWN)) {
            int toPlaceLargeCount = 1 + random.nextInt(3);
            int[] toPlaceLarge = new int[]{toPlaceLargeCount};
            int[] toPlaceSmall = new int[]{3 + random.nextInt(3)};
            int[] toPlaceRocks = new int[]{6 + random.nextInt(3)};
            double redstoneOreChance = ModuleWorldGenConfig.DENSE_REDSTONE_ORE.CHANCE_TO_SPAWN_REDSTONE_ORE;
            int[] posX = new int[]{blockXPos + random.nextInt(16) + 8};
            int min = Math.min(255, Math.max(0, ModuleWorldGenConfig.DENSE_REDSTONE_ORE.VERTICAL_BOUNDS[0]));
            int max = ModuleWorldGenConfig.DENSE_REDSTONE_ORE.VERTICAL_BOUNDS[1];
            int range = Math.max(1, max - min + 1);
            int[] posY = new int[]{Math.min(255, Math.max(0, min + random.nextInt(range)))};
            int[] posZ = new int[]{blockZPos + random.nextInt(16) + 8};
            BlockHelper.forBlocksInCube(world, new BlockPos(posX[0], posY[0], posZ[0]), 4, 4, 4, (w, p, bs) -> {
                BlockPos posDown = p.down();
                if (w.isAirBlock(p) && this.canSpawnOnTopOf(w, posDown, w.getBlockState(posDown))) {
                    world.setBlockState(p, ModuleCore.Blocks.ORE_DENSE_REDSTONE_LARGE.getDefaultState(), 18);
                    int var10002 = toPlaceLarge[0]--;
                    if (world.getBlockState(posDown).getBlock() == BlockRegistry.PITSTONE && (double)random.nextFloat() < redstoneOreChance) {
                        world.setBlockState(posDown, net.minecraft.init.Blocks.REDSTONE_ORE.getDefaultState(), 18);
                    }

                    if (toPlaceLarge[0] == 0) {
                        posX[0] = p.getX();
                        posY[0] = p.getY();
                        posZ[0] = p.getZ();
                        return false;
                    }
                }

                return true;
            });
            if (toPlaceLarge[0] != toPlaceLargeCount) {
                BlockHelper.forBlocksInCubeShuffled(world, new BlockPos(posX[0], posY[0], posZ[0]), 4, 4, 4, (w, p, bs) -> {
                    BlockPos posDown = p.down();
                    if (w.isAirBlock(p) && this.canSpawnOnTopOf(w, posDown, w.getBlockState(posDown))) {
                        world.setBlockState(p, ModuleCore.Blocks.ORE_DENSE_REDSTONE_SMALL.getDefaultState(), 18);
                        int var10002 = toPlaceSmall[0]--;
                        if (world.getBlockState(posDown).getBlock() == BlockRegistry.PITSTONE && (double)random.nextFloat() < redstoneOreChance) {
                            world.setBlockState(posDown, net.minecraft.init.Blocks.REDSTONE_ORE.getDefaultState(), 18);
                        }
                    }

                    return toPlaceSmall[0] > 0;
                });
                BlockHelper.forBlocksInCubeShuffled(world, new BlockPos(posX[0], posY[0], posZ[0]), 4, 4, 4, (w, p, bs) -> {
                    BlockPos posDown = p.down();
                    if (w.isAirBlock(p) && this.canSpawnOnTopOf(w, posDown, w.getBlockState(posDown))) {
                        world.setBlockState(p, ModuleCore.Blocks.ORE_DENSE_REDSTONE_ROCKS.getDefaultState(), 18);
                        int var10002 = toPlaceRocks[0]--;
                        if (world.getBlockState(posDown).getBlock() == BlockRegistry.PITSTONE && (double)random.nextFloat() < redstoneOreChance) {
                            world.setBlockState(posDown, net.minecraft.init.Blocks.REDSTONE_ORE.getDefaultState(), 18);
                        }
                    }

                    return toPlaceRocks[0] > 0;
                });
            }
        }
    }

    @Shadow
    private boolean canSpawnOnTopOf(World world, BlockPos pos, IBlockState blockState) {
        if (!blockState.isSideSolid(world, pos, EnumFacing.UP)) {
            return false;
        } else {
            Material material = blockState.getMaterial();
            return material == Material.ROCK;
        }
    }
}
