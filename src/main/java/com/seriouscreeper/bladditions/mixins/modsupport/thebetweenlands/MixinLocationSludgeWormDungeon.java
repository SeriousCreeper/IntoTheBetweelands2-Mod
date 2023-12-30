package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.api.storage.IWorldStorage;
import thebetweenlands.api.storage.LocalRegion;
import thebetweenlands.api.storage.StorageID;
import thebetweenlands.common.world.WorldProviderBetweenlands;
import thebetweenlands.common.world.biome.spawning.BoxMobSpawner;
import thebetweenlands.common.world.storage.location.LocationGuarded;
import thebetweenlands.common.world.storage.location.LocationSludgeWormDungeon;

import javax.annotation.Nullable;

@Mixin(value = LocationSludgeWormDungeon.class)
public class MixinLocationSludgeWormDungeon extends LocationGuarded {
    @Final
    @Shadow
    protected static DataParameter<Float> GROUND_FOG_STRENGTH;

    @Shadow
    private boolean defeated = false;

    @Final
    @Shadow
    private BoxMobSpawner mazeMobSpawner;

    @Final
    @Shadow
    private BoxMobSpawner walkwaysMobSpawner;


    public MixinLocationSludgeWormDungeon(IWorldStorage worldStorage, StorageID id, @Nullable LocalRegion region) {
        super(worldStorage, id, region);
    }

    /**
     * @author SC
     * @reason allow spawns in other dimensions
     */
    @Overwrite
    public void update() {
        super.update();
        World world = this.getWorldStorage().getWorld();
        if (!world.isRemote) {
            float fogStrength = (Float)this.dataManager.get(GROUND_FOG_STRENGTH);
            if (this.defeated && fogStrength > 0.0F) {
                this.dataManager.set(GROUND_FOG_STRENGTH, Math.max(0.0F, fogStrength - 0.01F));
                this.setDirty(true);
            } else if (!this.defeated && fogStrength < 1.0F) {
                this.dataManager.set(GROUND_FOG_STRENGTH, Math.min(1.0F, fogStrength + 0.01F));
                this.setDirty(true);
            }
        }

        if (!this.defeated && world instanceof WorldServer && world.getGameRules().getBoolean("doMobSpawning") && world.getTotalWorldTime() % 4L == 0L) {
            boolean spawnHostiles = true;
            boolean spawnAnimals = true;

            if(world.provider instanceof WorldProviderBetweenlands) {
                spawnHostiles = ((WorldProviderBetweenlands)world.provider).getCanSpawnHostiles();
                spawnAnimals = ((WorldProviderBetweenlands)world.provider).getCanSpawnAnimals();
            }

            BlockPos pos = this.getStructurePos();
            this.mazeMobSpawner.clearAreas();
            this.mazeMobSpawner.addArea(new AxisAlignedBB((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 29), (double)(pos.getY() - 40 - 3), (double)(pos.getZ() + 29)));
            this.mazeMobSpawner.populate((WorldServer)world, spawnHostiles, spawnAnimals);
            this.walkwaysMobSpawner.clearAreas();
            this.walkwaysMobSpawner.addArea(new AxisAlignedBB((double)(pos.getX() - 3), (double)(pos.getY() - 43), (double)(pos.getZ() - 3), (double)pos.getX(), (double)(pos.getY() - 24), (double)(pos.getZ() + 29)));
            this.walkwaysMobSpawner.addArea(new AxisAlignedBB((double)pos.getX(), (double)(pos.getY() - 43), (double)(pos.getZ() - 3), (double)(pos.getX() + 29), (double)(pos.getY() - 24), (double)pos.getZ()));
            this.walkwaysMobSpawner.populate((WorldServer)world, spawnHostiles, spawnAnimals);
        }
    }

    @Shadow
    public BlockPos getStructurePos() {
        return null;
    }
}
