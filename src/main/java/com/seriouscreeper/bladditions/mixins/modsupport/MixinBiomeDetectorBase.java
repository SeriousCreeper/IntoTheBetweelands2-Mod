package com.seriouscreeper.bladditions.mixins.modsupport;

import hunternif.mc.atlas.core.BiomeDetectorBase;
import hunternif.mc.atlas.ext.ExtTileIdMap;
import hunternif.mc.atlas.util.ByteUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.registries.BlockRegistry;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Mixin(value = BiomeDetectorBase.class, remap = false)
public class MixinBiomeDetectorBase {
    @Shadow
    private boolean doScanPonds = true;
    @Shadow
    private static final int waterPoolBiomeID = Biome.getIdForBiome(Biomes.RIVER);
    @Shadow
    private static final int priorityWaterPool = 9, prioritylavaPool = 6;
    @Shadow int priorityForBiome(Biome biome) { return 0; }

    /**
     * @author SC
     */
    @Overwrite
    public int getBiomeID(Chunk chunk) {
        int biomeCount = Biome.REGISTRY.getKeys().size();

        int[] chunkBiomes = ByteUtil.unsignedByteToIntArray(chunk.getBiomeArray());
        Map<Integer, Integer> biomeOccurrences = new HashMap<>(biomeCount);

        // The following important pseudo-biomes don't have IDs:
        int lavaOccurrences = 0;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int biomeID = chunkBiomes[x << 4 | z];
                if (doScanPonds) {
                    int y = chunk.getHeightValue(x, z);

                    if (y > 0) {
                        Block topBlock = chunk.getBlockState(x, y, z).getBlock();
                        // For some reason lava doesn't count in height value
                        // TODO: check if 1.8 fixes this!
                        // Check if there's surface of water at (x, z), but not swamp
                        if (topBlock == BlockRegistry.SWAMP_WATER) {
                            int occurrence = biomeOccurrences.getOrDefault(waterPoolBiomeID, 0) + priorityWaterPool;
                            biomeOccurrences.put(waterPoolBiomeID, occurrence);
                        }
                    }
                }

                if (biomeID >= 0 && Biome.getBiomeForId(biomeID) != null) {
                    int occurrence = biomeOccurrences.getOrDefault(biomeID, 0) + priorityForBiome(Biome.getBiomeForId(biomeID));
                    biomeOccurrences.put(biomeID, occurrence);
                }
            }
        }

        Map.Entry<Integer, Integer> meanBiome = Collections.max(biomeOccurrences.entrySet(), Comparator.comparingInt(Map.Entry::getValue));
        int meanBiomeId = meanBiome.getKey();

        return meanBiomeId;
    }
}
