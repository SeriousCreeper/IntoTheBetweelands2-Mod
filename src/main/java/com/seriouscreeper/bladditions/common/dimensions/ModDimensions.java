package com.seriouscreeper.bladditions.common.dimensions;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public final class ModDimensions {
    public static final int SKY_ISLAND_DIM_ID = 12; // change if you want / config it
    public static DimensionType SKY_ISLAND_DIM_TYPE;

    private ModDimensions() {}

    public static void register() {
        SKY_ISLAND_DIM_TYPE = DimensionType.register(
                "sky_islands",          // name
                "_sky_islands",         // suffix
                SKY_ISLAND_DIM_ID,      // id
                WorldProviderSkyIslands.class,
                false                   // keepLoaded
        );

        DimensionManager.registerDimension(SKY_ISLAND_DIM_ID, SKY_ISLAND_DIM_TYPE);
    }
}