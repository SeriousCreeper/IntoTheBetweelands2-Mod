package com.seriouscreeper.bladditions.mixins.modsupport.roots;

import epicsquid.roots.entity.EntityLifetimeBase;
import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualHeavyStorms;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.ritual.RitualHeavyStorms;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thebetweenlands.api.environment.IEnvironmentEvent;
import thebetweenlands.common.world.storage.BetweenlandsWorldStorage;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Mixin(value = EntityRitualHeavyStorms.class, remap = false)
public class MixinEntityRitualHeavyStorms extends EntityRitualBase {
    @Shadow private RitualHeavyStorms ritual;

    public MixinEntityRitualHeavyStorms(World worldIn) {
        super(worldIn);
    }


    @Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true)
    public void onUpdate(CallbackInfo ci) {
        super.onUpdate();
        float alpha = (float)Math.min(40, RitualRegistry.ritual_heavy_storms.getDuration() + 20 - (Integer)this.getDataManager().get(lifetime)) / 40.0F;
        float i;
        if (this.world.isRemote && (Integer)this.getDataManager().get(lifetime) > 0) {
            ParticleUtil.spawnParticleStar(this.world, (float)this.posX, (float)this.posY, (float)this.posZ, 0.0F, 0.0F, 0.0F, 50.0F, 50.0F, 255.0F, 0.5F * alpha, 20.0F, 40);
            if (this.rand.nextInt(5) == 0) {
                ParticleUtil.spawnParticleSpark(this.world, (float)this.posX, (float)this.posY, (float)this.posZ, 0.125F * (this.rand.nextFloat() - 0.5F), 0.0625F * this.rand.nextFloat(), 0.125F * (this.rand.nextFloat() - 0.5F), 50.0F, 50.0F, 255.0F, 1.0F * alpha, 1.0F + this.rand.nextFloat(), 160);
            }

            float vx;
            float vz;
            for(i = 0.0F; i < 360.0F; i += this.rand.nextFloat() * 90.0F) {
                vx = (float)this.posX + 2.5F * (float)Math.sin(Math.toRadians((double)i));
                vz = (float)this.posY;
                i = (float)this.posZ + 2.5F * (float)Math.cos(Math.toRadians((double)i));
                ParticleUtil.spawnParticleSmoke(this.world, vx, vz, i, 0.0F, 0.0F, 0.0F, 70.0F, 70.0F, 70.0F, 0.25F * alpha, 14.0F, 80, false);
            }

            for(i = 0.0F; i < 360.0F; i += this.rand.nextFloat() * 90.0F) {
                vx = (float)this.posX + 3.75F * (float)Math.sin(Math.toRadians((double)i));
                vz = (float)this.posY;
                i = (float)this.posZ + 3.75F * (float)Math.cos(Math.toRadians((double)i));
                ParticleUtil.spawnParticleSmoke(this.world, vx, vz, i, 0.0F, 0.0F, 0.0F, 70.0F, 70.0F, 70.0F, 0.125F * alpha, 7.0F, 80, false);
            }

            for(i = 0.0F; i < 360.0F; i += this.rand.nextFloat() * 90.0F) {
                vx = 0.25F * (float)Math.sin(Math.toRadians((double)i));
                vz = 0.25F * (float)Math.cos(Math.toRadians((double)i));
                i = (float)this.posX + 3.75F * (float)Math.sin(Math.toRadians((double)i));
                float ty = (float)this.posY;
                float tz = (float)this.posZ + 3.75F * (float)Math.cos(Math.toRadians((double)i));
                ParticleUtil.spawnParticleSmoke(this.world, i, ty, tz, vx, 0.0F, vz, 70.0F, 70.0F, 70.0F, 0.125F * alpha, 7.0F, 80, false);
            }
        }

        if (this.ticksExisted % 20 == 0) {
            if (!world.isRemote) {
                BetweenlandsWorldStorage storage = BetweenlandsWorldStorage.forWorld(world);

                if (storage != null) {
                    List<IEnvironmentEvent> activeEvents = storage.getEnvironmentEventRegistry().getActiveEvents();

                    if(!activeEvents.contains(storage.getEnvironmentEventRegistry().heavyRain)) {
                        storage.getEnvironmentEventRegistry().heavyRain.setActive(true);
                    }
                }
            }
        }

        ci.cancel();
    }
}
