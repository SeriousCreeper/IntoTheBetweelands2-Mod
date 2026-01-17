package com.seriouscreeper.bladditions.common.dimensions;

import com.gildedgames.aether.api.registrar.BiomesAether;
import net.minecraft.world.DimensionType;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.biome.BiomeProviderSingle;
import thebetweenlands.common.world.WorldProviderBetweenlands;

public class WorldProviderSkyIslands extends WorldProviderBetweenlands {
    @Override
    public void init() {
        this.biomeProvider = new BiomeProviderSingle(BiomesAether.HIGHLANDS);
        this.hasSkyLight = true;
    }

    @Override
    public DimensionType getDimensionType() {
        return ModDimensions.SKY_ISLAND_DIM_TYPE;
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new ChunkGeneratorSkyIslands(world, world.getSeed());
    }

    @Override
    public boolean canRespawnHere() {
        return false; // typically false for custom dims
    }

    @Override
    public boolean isSurfaceWorld() {
        return false; // keeps it “nether-like” for some behaviors; tweak if desired
    }
}