package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.entity.ritual.EntityRitualSummonCreatures;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.tile.spawner.MobSpawnerLogicBetweenlands;

import java.util.List;

@Mixin(value = MobSpawnerLogicBetweenlands.class, remap = false)
public class MixinBetweenlandsSpawnerLogic {
    @Shadow
    private final int activatingRangeFromPlayer = 16;
    private EntityRitualSummonCreatures tempRitual;


    /**
     * @author SC
     */
    @Overwrite
    public boolean isActivated() {
        if(tempRitual == null || tempRitual.isDead) {
            BlockPos spawnerPos = new BlockPos((double) this.getSpawnerX() + 0.5D, (double) this.getSpawnerY() + 0.5D, (double) this.getSpawnerZ() + 0.5D);
            List<EntityRitualSummonCreatures> ritual = Util.getEntitiesWithinRadius(getSpawnerWorld(), EntityRitualSummonCreatures.class, spawnerPos, 4, 4, 4);

            for (EntityRitualSummonCreatures entityRitualSummonCreatures : ritual) {
                if (!entityRitualSummonCreatures.isDead) {
                    tempRitual = entityRitualSummonCreatures;
                    break;
                }
            }
        }

        if(tempRitual != null && !tempRitual.isDead) {
            return true;
        }

        return this.getSpawnerWorld().getClosestPlayer((double)this.getSpawnerX() + 0.5D, (double)this.getSpawnerY() + 0.5D, (double)this.getSpawnerZ() + 0.5D, (double)this.activatingRangeFromPlayer, false) != null;
    }

    @Shadow
    public World getSpawnerWorld() {
        return null;
    }

    @Shadow
    public int getSpawnerX() { return 0; }

    @Shadow
    public int getSpawnerY() { return 0; }

    @Shadow
    public int getSpawnerZ() { return 0; }
}
