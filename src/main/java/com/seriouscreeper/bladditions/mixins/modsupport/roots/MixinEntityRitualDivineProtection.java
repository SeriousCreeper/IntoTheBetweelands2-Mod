package com.seriouscreeper.bladditions.mixins.modsupport.roots;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualDivineProtection;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.ritual.RitualDivineProtection;
import epicsquid.roots.ritual.RitualHeavyStorms;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thebetweenlands.api.environment.IEnvironmentEvent;
import thebetweenlands.common.world.storage.BetweenlandsWorldStorage;

import java.util.List;

@Mixin(value = EntityRitualDivineProtection.class, remap = false)
public class MixinEntityRitualDivineProtection extends EntityRitualBase {
    @Shadow
    private RitualDivineProtection ritual;

    public MixinEntityRitualDivineProtection(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true)
    public void onUpdate(CallbackInfo ci) {
        super.onUpdate();

        float alpha = (float)Math.min(40, RitualRegistry.ritual_divine_protection.getDuration() + 20 - (Integer)this.getDataManager().get(lifetime)) / 40.0F;
        if (this.world.isRemote && (Integer)this.getDataManager().get(lifetime) > 0) {
            ParticleUtil.spawnParticleStar(this.world, (float)this.posX, (float)this.posY, (float)this.posZ, 0.0F, 0.0F, 0.0F, 255.0F, 255.0F, 75.0F, 0.5F * alpha, 20.0F, 40);
            if (this.rand.nextInt(5) == 0) {
                ParticleUtil.spawnParticleSpark(this.world, (float)this.posX, (float)this.posY, (float)this.posZ, 0.125F * (this.rand.nextFloat() - 0.5F), 0.0625F * this.rand.nextFloat(), 0.125F * (this.rand.nextFloat() - 0.5F), 255.0F, 255.0F, 75.0F, 1.0F * alpha, 1.0F + this.rand.nextFloat(), 160);
            }

            if (this.rand.nextInt(2) == 0) {
                float pitch = this.rand.nextFloat() * 360.0F;
                float yaw = this.rand.nextFloat() * 360.0F;

                for(float i = 0.0F; (double)i < 3.5D; i += 0.2F) {
                    i = (float)this.posX + i * (float)Math.sin(Math.toRadians((double)yaw)) * (float)Math.sin(Math.toRadians((double)pitch));
                    float ty = (float)this.posY + i * (float)Math.cos(Math.toRadians((double)pitch));
                    float tz = (float)this.posZ + i * (float)Math.cos(Math.toRadians((double)yaw)) * (float)Math.sin(Math.toRadians((double)pitch));
                    float coeff = i / 3.5F;
                    ParticleUtil.spawnParticleGlow(this.world, i, ty, tz, 0.0F, 0.0F, 0.0F, 255.0F, 255.0F, 75.0F, 0.5F * alpha * (1.0F - coeff), 18.0F * (1.0F - coeff), 20);
                }
            }
        }

        if (this.ticksExisted % 20 == 0) {
            if (!world.isRemote) {
                BetweenlandsWorldStorage storage = BetweenlandsWorldStorage.forWorld(world);

                if (storage != null) {
                    List<IEnvironmentEvent> activeEvents = storage.getEnvironmentEventRegistry().getActiveEvents();

                    if(!activeEvents.contains(storage.getEnvironmentEventRegistry().rift)) {
                        storage.getEnvironmentEventRegistry().rift.setActive(true);
                    }

                    if(activeEvents.contains(storage.getEnvironmentEventRegistry().heavyRain)) {
                        storage.getEnvironmentEventRegistry().heavyRain.setActive(false);
                    }

                    if(activeEvents.contains(storage.getEnvironmentEventRegistry().thunderstorm)) {
                        storage.getEnvironmentEventRegistry().thunderstorm.setActive(false);
                    }

                    if(activeEvents.contains(storage.getEnvironmentEventRegistry().denseFog)) {
                        storage.getEnvironmentEventRegistry().denseFog.setActive(false);
                    }
                }
            }
        }

        ci.cancel();
    }
}
