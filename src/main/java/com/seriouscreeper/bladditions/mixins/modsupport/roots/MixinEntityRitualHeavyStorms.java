package com.seriouscreeper.bladditions.mixins.modsupport.roots;

import epicsquid.roots.entity.EntityLifetimeBase;
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
public class MixinEntityRitualHeavyStorms extends EntityLifetimeBase {
    @Shadow private RitualHeavyStorms ritual;

    public MixinEntityRitualHeavyStorms(World worldIn) {
        super(worldIn);
    }


    @Inject(method = "onUpdate", at = @At("RETURN"), cancellable = true)
    public void onUpdate(CallbackInfo ci) {
        float alpha = (float)Math.min(40, RitualRegistry.ritual_heavy_storms.getDuration() + 20 - (Integer)this.getDataManager().get(lifetime)) / 40.0F;

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
            List<EntityLivingBase> entities = world
                    .getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX - ritual.radius_x, posY - ritual.radius_y, posZ - ritual.radius_z, posX + ritual.radius_x, posY + ritual.radius_y, posZ + ritual.radius_z));
            for (EntityLivingBase e : entities) {
                if (e.isBurning()) {
                    e.extinguish();
                    if (world.isRemote) {
                        for (float i = 0; i < 24; i++) {
                            ParticleUtil.spawnParticleGlow(world, (float) e.posX + 0.5f * (rand.nextFloat() - 0.5f), (float) e.posY + e.height / 2.5f + (rand.nextFloat() - 0.5f), (float) e.posZ + 0.5f * (rand.nextFloat() - 0.5f), 0.0625f * (rand.nextFloat() - 0.5f), 0.009375f * (rand.nextFloat()), 0.0625f * (rand.nextFloat() - 0.5f), 50, 50, 255, 0.25f * alpha, 2.0f + 4.0f * rand.nextFloat(), 80);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbtTagCompound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbtTagCompound) {

    }
}
