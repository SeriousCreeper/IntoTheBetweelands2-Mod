package com.seriouscreeper.bladditions.common.dimensions;

import java.util.List;
import java.util.Random;

import com.gildedgames.aether.api.registrar.BlocksAether;
import com.gildedgames.aether.common.blocks.natural.BlockHolystone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.NoiseGeneratorPerlin;

public class ChunkGeneratorSkyIslands implements IChunkGenerator {
    private final World world;
    private final Random rand;
    private final long seed;

    private final NoiseGeneratorPerlin shapeNoise;     // main shape
    private final NoiseGeneratorPerlin thicknessNoise; // thickness variation
    private final NoiseGeneratorPerlin warpNoiseX;     // domain warp
    private final NoiseGeneratorPerlin warpNoiseZ;


    // --- TUNABLES ---
    private final int seaLevel = 0;            // not used, but Chunk expects something
    private final int baseY = 120;             // average island height
    private final int heightJitter = 70;       // +/- height variation
    private final int islandRadiusMin = 8;    // in blocks
    private final int islandRadiusMax = 32;

    // Distance between island "cells" (bigger = further apart)
    private final int cellSize = 160;          // try 128-256 for “far apart”
    private final int islandChancePercent = 35;// chance an island exists per cell (lower = fewer islands)

    // Make sure spawn has something to stand on:
    private final boolean forceSpawnIsland = false;
    private final int spawnIslandRadius = 26;
    private final int spawnIslandY = 128;

    public ChunkGeneratorSkyIslands(World world, long seed) {
        this.world = world;
        this.seed = seed;
        this.rand = new Random(seed);

        this.shapeNoise     = new NoiseGeneratorPerlin(new Random(seed ^ 0xA2B3C4D5L), 2);
        this.thicknessNoise = new NoiseGeneratorPerlin(new Random(seed ^ 0x55AA55AAL), 2);
        this.warpNoiseX     = new NoiseGeneratorPerlin(new Random(seed ^ 0x11112222L), 1);
        this.warpNoiseZ     = new NoiseGeneratorPerlin(new Random(seed ^ 0x33334444L), 1);
    }

    private double fbm(NoiseGeneratorPerlin n, double x, double z, int octaves, double lacunarity, double gain) {
        double amp = 1.0;
        double freq = 1.0;
        double sum = 0.0;
        double norm = 0.0;

        for (int i = 0; i < octaves; i++) {
            sum += n.getValue(x * freq, z * freq) * amp;
            norm += amp;
            amp *= gain;
            freq *= lacunarity;
        }
        return sum / norm; // roughly [-1..1]
    }

    private double ridgedFbm(NoiseGeneratorPerlin n, double x, double z, int octaves, double lacunarity, double gain) {
        double amp = 1.0;
        double freq = 1.0;
        double sum = 0.0;
        double norm = 0.0;

        for (int i = 0; i < octaves; i++) {
            double v = n.getValue(x * freq, z * freq); // [-1..1]
            v = 1.0 - Math.abs(v);                    // ridges: [0..1], higher = sharper features
            v *= v;                                   // sharpen
            sum += v * amp;
            norm += amp;
            amp *= gain;
            freq *= lacunarity;
        }
        return sum / norm; // [0..1]
    }


    @Override
    public Chunk generateChunk(int chunkX, int chunkZ) {
        ChunkPrimer primer = new ChunkPrimer();

        generateIslandsForChunk(chunkX, chunkZ, primer);

        Chunk chunk = new Chunk(world, primer, chunkX, chunkZ);

        // IMPORTANT: set biome bytes for this chunk from the biome provider
        Biome[] biomes = world.getBiomeProvider().getBiomesForGeneration(
                null, chunkX * 16, chunkZ * 16, 16, 16
        );

        byte[] biomeArray = chunk.getBiomeArray();
        for (int i = 0; i < biomeArray.length; i++) {
            biomeArray[i] = (byte) Biome.getIdForBiome(biomes[i]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    private void generateIslandsForChunk(int chunkX, int chunkZ, ChunkPrimer primer) {
        // chunk origin in block coords
        int chunkBlockX = chunkX << 4;
        int chunkBlockZ = chunkZ << 4;

        // We want to check nearby cells that might overlap this chunk
        int minCellX = floorDiv(chunkBlockX - (cellSize), cellSize);
        int maxCellX = floorDiv(chunkBlockX + 16 + (cellSize), cellSize);
        int minCellZ = floorDiv(chunkBlockZ - (cellSize), cellSize);
        int maxCellZ = floorDiv(chunkBlockZ + 16 + (cellSize), cellSize);

        for (int cellX = minCellX; cellX <= maxCellX; cellX++) {
            for (int cellZ = minCellZ; cellZ <= maxCellZ; cellZ++) {

                // Force a guaranteed island near spawn (0,0) if desired
                if (forceSpawnIsland && cellX == 0 && cellZ == 0) {
                    placeIsland(primer, chunkBlockX, chunkBlockZ,
                            0, spawnIslandY, 0, spawnIslandRadius, seedForCell(cellX, cellZ));
                    continue;
                }

                Random cellRand = new Random(seedForCell(cellX, cellZ));

                if (cellRand.nextInt(100) >= islandChancePercent) {
                    continue; // no island in this cell
                }

                // Jitter the island center inside the cell so it isn't perfectly grid-aligned
                int centerX = cellX * cellSize + cellRand.nextInt(cellSize);
                int centerZ = cellZ * cellSize + cellRand.nextInt(cellSize);

                int radius = islandRadiusMin + cellRand.nextInt(islandRadiusMax - islandRadiusMin + 1);

                int y = baseY + cellRand.nextInt(heightJitter * 2 + 1) - heightJitter;

                // Early out: if island is too far to overlap this chunk, skip
                if (!circleOverlapsChunk(centerX, centerZ, radius + 8, chunkBlockX, chunkBlockZ)) {
                    continue;
                }

                placeIsland(primer, chunkBlockX, chunkBlockZ, centerX, y, centerZ, radius, seedForCell(cellX, cellZ));
            }
        }
    }

    private void placeIsland(ChunkPrimer primer,
                             int chunkBlockX, int chunkBlockZ,
                             int centerX, int centerY, int centerZ,
                             int radius, long islandSeed) {

        Random r = new Random(islandSeed ^ 0x1234ABCD);

        // Island thickness and shaping
        int maxThickness = MathHelper.clamp(radius / 2 + r.nextInt(6), 6, 18);

        // Deterministic per-island random (don’t recreate this in the inner loops)
        Random islandRand = new Random(islandSeed ^ 0xCAFEBABEL);
        double stretch = 0.75 + islandRand.nextDouble() * 0.95; // 0.75..1.70
        double rot = islandRand.nextDouble() * Math.PI * 2.0;
        double cosR = Math.cos(rot);
        double sinR = Math.sin(rot);

        // First pass: compute top/bottom for each column in this chunk
        boolean[] hasIsland = new boolean[16 * 16];
        int[] rawTop = new int[16 * 16];
        int[] rawBottom = new int[16 * 16];

        for (int localX = 0; localX < 16; localX++) {
            int x = chunkBlockX + localX;

            for (int localZ = 0; localZ < 16; localZ++) {
                int z = chunkBlockZ + localZ;
                int idx = localX + localZ * 16;

                double dx = x - centerX;
                double dz = z - centerZ;

                // Domain warp the position a bit so outlines are more organic
                double outlineWarpFreq = 0.03;
                double outlineWarpAmp = 8.0;

                double wox = fbm(warpNoiseX, x * outlineWarpFreq, z * outlineWarpFreq, 2, 2.0, 0.5) * outlineWarpAmp;
                double woz = fbm(warpNoiseZ, x * outlineWarpFreq, z * outlineWarpFreq, 2, 2.0, 0.5) * outlineWarpAmp;

                double px = dx + wox;
                double pz = dz + woz;

                // Rotate
                double rx = cosR * px + sinR * pz;
                double rz = -sinR * px + cosR * pz;

                // Stretch one axis (ellipse)
                rx *= stretch;

                // Angle around the island center
                double ang = Math.atan2(rz, rx);

                // Radius multiplier based on angle (stable per-island)
                double a = Math.cos(ang);
                double b = Math.sin(ang);

                double angleNoise = fbm(
                        shapeNoise,
                        (centerX * 0.01) + a * 4.0,
                        (centerZ * 0.01) + b * 4.0,
                        3, 2.0, 0.5
                );

                double radiusMult = 1.0 + angleNoise * 0.40;
                radiusMult = MathHelper.clamp(radiusMult, 0.60, 1.45);

                double effRadius = radius * radiusMult;

                double dist = Math.sqrt(rx * rx + rz * rz);
                if (dist > effRadius) continue;

                double t = dist / effRadius; // 0..1

                // Domain warp (breaks up wavy bands)
                double warpFreq = 0.015;
                double warpAmp = 20.0;

                double wx = fbm(warpNoiseX, x * warpFreq, z * warpFreq, 2, 2.0, 0.5) * warpAmp;
                double wz = fbm(warpNoiseZ, x * warpFreq, z * warpFreq, 2, 2.0, 0.5) * warpAmp;

                double sx = x + wx;
                double sz = z + wz;

                // Noise for variation
                double shapeFreq = 0.02;
                double ridged = ridgedFbm(shapeNoise, sx * shapeFreq, sz * shapeFreq, 4, 2.0, 0.55); // [0..1]

                // IMPORTANT: make surface smoother by using LOWER frequency & LOWER amplitude
                // Also reduce octaves; your previous smooth used (1, 2.0, 0.8) which can still be “chattery” with warping.
                double surface = fbm(shapeNoise, sx * 0.0045, sz * 0.0045, 2, 2.0, 0.5); // [-1..1], gentle

                // Dome still defines silhouette; smoothing comes from blur later
                double dome = (1.0 - t);
                dome = dome * dome;

                // Top height: keep large-scale shape, reduce per-block noise
                int top = centerY
                        + (int) Math.round(dome * 8.0)
                        + (int) Math.round((ridged - 0.5) * 4.0)   // reduced from 6.0
                        + (int) Math.round(surface * 2.0);         // reduced from *3.0

                // Thickness: keep chunky islands
                double thickBase = 10.0 + dome * 22.0;
                double thickVarNoise = ridgedFbm(thicknessNoise, sx * 0.018, sz * 0.018, 3, 2.0, 0.6);
                double thickVar = (thickVarNoise - 0.5) * 10.0;

                int thickness = (int) Math.round(thickBase + thickVar);
                thickness = MathHelper.clamp(thickness, 8, 44);

                int bottom = top - thickness;

                // Underside shaping stays “interesting”
                bottom = MathHelper.clamp(bottom, 1, 254);
                top = MathHelper.clamp(top, 1, 254);

                double under = ridgedFbm(shapeNoise, sx * 0.03, sz * 0.03, 3, 2.1, 0.55);
                int underCut = (int) Math.round((0.6 - under) * 10.0);
                bottom = MathHelper.clamp(bottom + underCut, 1, 254);

                hasIsland[idx] = true;
                rawTop[idx] = top;
                rawBottom[idx] = bottom;
            }
        }

        // Second pass: blur ONLY the surface top (3×3) for smoother terrain
        int[] smoothTop = new int[16 * 16];

        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                int idx = x + z * 16;
                if (!hasIsland[idx]) continue;

                int sum = 0;
                int count = 0;

                for (int oz = -1; oz <= 1; oz++) {
                    for (int ox = -1; ox <= 1; ox++) {
                        int nx = x + ox;
                        int nz = z + oz;
                        if (nx < 0 || nx >= 16 || nz < 0 || nz >= 16) continue;

                        int nidx = nx + nz * 16;
                        if (!hasIsland[nidx]) continue;

                        sum += rawTop[nidx];
                        count++;
                    }
                }

                smoothTop[idx] = (count > 0) ? (sum / count) : rawTop[idx];
            }
        }

        // Third pass: place blocks using smoothed surface
        for (int localX = 0; localX < 16; localX++) {
            int x = chunkBlockX + localX;

            for (int localZ = 0; localZ < 16; localZ++) {
                int z = chunkBlockZ + localZ;

                int idx = localX + localZ * 16;
                if (!hasIsland[idx]) continue;

                int top = MathHelper.clamp(smoothTop[idx], 1, 254);
                int bottom = MathHelper.clamp(rawBottom[idx], 1, 254);

                // Optional safety: don’t allow bottom to exceed top
                if (bottom > top) continue;

                Biome biome = world.getBiome(new BlockPos(x, 0, z));

                IBlockState blockTop = biome.topBlock;
                IBlockState blockFiller = biome.fillerBlock;

                if (blockTop == null) {
                    blockTop = BlocksAether.aether_grass.getStateFromMeta(4);
                }
                if (blockFiller == null) {
                    blockFiller = BlocksAether.aether_dirt.getDefaultState();
                }

                for (int y = bottom; y <= top; y++) {
                    int depthFromTop = top - y;
                    IBlockState state;

                    if (depthFromTop == 0) {
                        state = blockTop;
                    } else if (depthFromTop <= 3) {
                        state = blockFiller;
                    } else {
                        // Use the per-island RNG, not a global rand
                        if (r.nextInt(5) == 1) {
                            state = BlocksAether.holystone.getStateFromMeta(1);
                        } else {
                            state = BlocksAether.holystone.getDefaultState();
                        }
                    }

                    primer.setBlockState(localX, y, localZ, state);
                }
            }
        }
    }


    private boolean circleOverlapsChunk(int cx, int cz, int r, int chunkBlockX, int chunkBlockZ) {
        // chunk AABB in world coords: [chunkBlockX..chunkBlockX+15], [chunkBlockZ..chunkBlockZ+15]
        int minX = chunkBlockX;
        int maxX = chunkBlockX + 15;
        int minZ = chunkBlockZ;
        int maxZ = chunkBlockZ + 15;

        // clamp circle center to AABB
        int clampedX = MathHelper.clamp(cx, minX, maxX);
        int clampedZ = MathHelper.clamp(cz, minZ, maxZ);

        int dx = cx - clampedX;
        int dz = cz - clampedZ;

        return (dx * dx + dz * dz) <= (r * r);
    }

    private long seedForCell(int cellX, int cellZ) {
        // Deterministic per-cell seed
        long h = seed;
        h ^= (long) cellX * 341873128712L;
        h ^= (long) cellZ * 132897987541L;
        return h;
    }

    private int floorDiv(int a, int b) {
        int r = a / b;
        // Java / is trunc toward 0; correct for negatives
        if ((a ^ b) < 0 && (r * b != a)) r--;
        return r;
    }

    private BlockPos findTopSolid(int x, int z) {
        for (int y = 250; y >= 20; y--) {
            BlockPos pos = new BlockPos(x, y, z);
            IBlockState state = world.getBlockState(pos);

            if (state.getBlock() != Blocks.AIR) {
                BlockPos above = pos.up();
                if (world.isAirBlock(above)) {
                    return pos;
                }
            }
        }
        return null;
    }

    @Override
    public void populate(int chunkX, int chunkZ) {
        long chunkSeed = ((long) chunkX * 341873128712L) ^ ((long) chunkZ * 132897987541L) ^ seed;
        Random random = new Random(chunkSeed);

        int baseX = chunkX << 4;
        int baseZ = chunkZ << 4;

        for (int i = 0; i < 48; i++) {
            int x = baseX + random.nextInt(16);
            int z = baseZ + random.nextInt(16);

            BlockPos top = findTopSolid(x, z);
            if (top == null) continue;

            BlockPos place = top.up();
            if (!world.isAirBlock(place)) continue;

            if (random.nextInt(2) == 0) {
                IBlockState grass = BlocksAether.tall_aether_grass.getStateFromMeta(random.nextInt(3));
                if (grass.getBlock().canPlaceBlockAt(world, place)) {
                    world.setBlockState(place, grass, 2);
                    continue;
                }
            }

            if (random.nextInt(10) == 0) {
                IBlockState twig = BlocksAether.skyroot_twigs.getStateFromMeta(random.nextInt(3));
                if (twig.getBlock().canPlaceBlockAt(world, place)) {
                    world.setBlockState(place, twig, 2);
                    continue;
                }
                continue;
            }

            if (random.nextInt(20) == 0) {
                placeRockPile(top, random);
            }
        }
    }

    private void placeRockPile(BlockPos surface, Random rand) {
        // tiny pile around surface.up()
        BlockPos base = surface.up();
        int rocks = 2 + rand.nextInt(5);

        for (int i = 0; i < rocks; i++) {
            int dx = rand.nextInt(3) - 1;
            int dz = rand.nextInt(3) - 1;
            BlockPos p = base.add(dx, 0, dz);

            if (world.isAirBlock(p) && !world.isAirBlock(p.down())) {
                // Use your holystone variants
                IBlockState s = (rand.nextInt(5) == 0)
                        ? BlocksAether.holystone.getStateFromMeta(BlockHolystone.MOSSY_HOLYSTONE.getMeta())
                        : BlocksAether.holystone.getDefaultState();
                world.setBlockState(p, s, 2);
            }
        }
    }



    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(
            net.minecraft.entity.EnumCreatureType creatureType, net.minecraft.util.math.BlockPos pos) {
        return world.getBiome(pos).getSpawnableList(creatureType);
    }

    @Override
    public BlockPos getNearestStructurePos(
            World worldIn, String structureName, net.minecraft.util.math.BlockPos position, boolean findUnexplored) {
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {
    }

    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
        return false;
    }
}
