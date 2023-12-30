package com.seriouscreeper.bladditions.mixins.modsupport.eyesinthedarkness;

import gigaherz.eyes.entity.EntityEyes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.api.entity.IEntityBL;
import thebetweenlands.api.environment.IEnvironmentEvent;
import thebetweenlands.common.world.storage.BetweenlandsWorldStorage;

import java.util.List;

@Mixin(value = EntityEyes.class, remap = false)
public class MixinEntityEyes extends EntityMob implements IEntityBL {
    public MixinEntityEyes(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean getCanSpawnHere() {
        boolean defaultCondition = this.world.getDifficulty() != EnumDifficulty.PEACEFUL && this.isValidLightLevel() && super.getCanSpawnHere();

        BetweenlandsWorldStorage storage = BetweenlandsWorldStorage.forWorld(world);

        if (storage != null) {
            List<IEnvironmentEvent> activeEvents = storage.getEnvironmentEventRegistry().getActiveEvents();

            if(activeEvents.contains(storage.getEnvironmentEventRegistry().bloodSky)) {
                return defaultCondition;
            }
        }

        return false;
    }
}
