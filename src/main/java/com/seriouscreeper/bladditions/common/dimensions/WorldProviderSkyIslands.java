package com.seriouscreeper.bladditions.common.dimensions;

import com.gildedgames.aether.api.registrar.BiomesAether;
import com.gildedgames.aether.common.world.biomes.BiomeAetherBase;
import com.gildedgames.aether.common.world.biomes.forgotten_highlands.BiomeForgottenHighlands;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.init.Biomes;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.api.misc.Fog;
import thebetweenlands.client.handler.FogHandler;
import thebetweenlands.client.render.sky.BLSkyRenderer;
import thebetweenlands.common.world.WorldProviderBetweenlands;
import thebetweenlands.common.world.event.EventRift;
import thebetweenlands.common.world.storage.BetweenlandsWorldStorage;

public class WorldProviderSkyIslands extends WorldProvider {
    private final float[] sunriseSunsetColors = new float[4];

    @Override
    protected void init() {
        // One biome everywhere keeps it simple. Swap to a real BiomeProvider if you want variety.
        this.biomeProvider = new BiomeProviderSingle(BiomesAether.HIGHLANDS);
        this.hasSkyLight = true;
    }

    @SideOnly(Side.CLIENT)
    public IRenderHandler getSkyRenderer() {
        return WorldProviderBetweenlands.getBLSkyRenderer();
    }

    @SideOnly(Side.CLIENT)
    public boolean isSkyColored() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {
        return new Vec3d((double)0.1F, (double)0.8F, (double)0.55F);
    }

    @SideOnly(Side.CLIENT)
    public float getSunBrightness(float partialTicks) {
        EventRift rift = BetweenlandsWorldStorage.forWorld(this.world).getEnvironmentEventRegistry().rift;
        return rift.getVisibility(partialTicks) * this.getOverworldSunBrightness(partialTicks) * 0.6F + rift.getVisibility(partialTicks) * 0.2F;
    }

    public float getSunBrightnessFactor(float partialTicks) {
        BetweenlandsWorldStorage storage = BetweenlandsWorldStorage.forWorldNullable(this.world);
        if (storage != null) {
            EventRift rift = storage.getEnvironmentEventRegistry().rift;
            return rift.getVisibility(partialTicks) * this.getOverworldSunBrightnessFactor(partialTicks);
        } else {
            return 0.2F;
        }
    }

    protected float getOverworldSunBrightnessFactor(float partialTicks) {
        float f = this.getOverworldCelestialAngle(partialTicks);
        float f1 = 1.0F - (MathHelper.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.5F);
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        f1 = 1.0F - f1;
        return f1;
    }

    @SideOnly(Side.CLIENT)
    protected float getOverworldSunBrightness(float partialTicks) {
        float f = this.getOverworldCelestialAngle(partialTicks);
        float f1 = 1.0F - (MathHelper.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.2F);
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        f1 = 1.0F - f1;
        return f1 * 0.8F + 0.2F;
    }

    public float getOverworldCelestialAngle(float partialTicks) {
        int i = (int)(this.world.getWorldTime() % 24000L);
        float f = ((float)i + partialTicks) / 24000.0F - 0.25F;
        if (f < 0.0F) {
            ++f;
        }

        if (f > 1.0F) {
            --f;
        }

        float f1 = 1.0F - (float)((Math.cos((double)f * Math.PI) + (double)1.0F) / (double)2.0F);
        f += (f1 - f) / 3.0F;
        return f;
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

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        // optional: keep a normal day/night cycle
        return super.calculateCelestialAngle(worldTime, partialTicks);
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float celestialAngle, float partialTickTime) {
        Fog fog = FogHandler.getFogState().getFog(partialTickTime);
        return new Vec3d((double)fog.getRed(), (double)fog.getGreen(), (double)fog.getBlue());
    }

    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int x, int z) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public float getCloudHeight() {
        return 200.0F;
    }
}