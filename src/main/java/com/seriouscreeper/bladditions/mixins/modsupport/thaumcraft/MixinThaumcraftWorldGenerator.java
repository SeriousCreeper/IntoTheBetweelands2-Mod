package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thaumcraft.common.config.ModConfig;
import thaumcraft.common.entities.monster.cult.EntityCultistPortalLesser;
import thaumcraft.common.world.ThaumcraftWorldGenerator;
import thaumcraft.common.world.biomes.BiomeHandler;
import thaumcraft.common.world.objects.WorldGenMound;

import java.util.Random;

@Mixin(value = ThaumcraftWorldGenerator.class, remap = false)
public class MixinThaumcraftWorldGenerator {
    /**
     * @author SC
     */
    @Overwrite
    public static boolean generateGreatwood(World world, Random random, int chunkX, int chunkZ) {
        return false;
    }

    /**
     * @author SC
     */
    @Overwrite
    public static boolean generateSilverwood(World world, Random random, int chunkX, int chunkZ) {
        return false;
    }


    /**
     * @author
     * @reason
     */
    @Overwrite
    private void generateSurface(World world, Random random, int chunkX, int chunkZ, boolean newGen) {
        if (world.provider.getDimension() == 20 && newGen) {
            int randPosX = chunkX * 16 + 8 + MathHelper.getInt(random, -4, 4);
            int randPosZ = chunkZ * 16 + 8 + MathHelper.getInt(random, -4, 4);
            BlockPos p = world.getPrecipitationHeight(new BlockPos(randPosX, 0, randPosZ)).down(9);
            if (p.getY() < world.getActualHeight()) {
                if (random.nextInt(ConfigBLAdditions.configGeneral.CrimsonCultSpawnRate) == 0) {
                    BlockPos p2 = p.up(8);
                    IBlockState bs = world.getBlockState(p2);
                    if (bs.getMaterial() == Material.GROUND || bs.getMaterial() == Material.ROCK || bs.getMaterial() == Material.SAND || bs.getMaterial() == Material.SNOW) {
                        EntityCultistPortalLesser eg = new EntityCultistPortalLesser(world);
                        eg.setPosition((double)p2.getX() + 0.5, (double)(p2.getY() + 1), (double)p2.getZ() + 0.5);
                        eg.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(eg)), (IEntityLivingData)null);
                        world.spawnEntity(eg);
                    }
                }
            }
        }
    }
}
