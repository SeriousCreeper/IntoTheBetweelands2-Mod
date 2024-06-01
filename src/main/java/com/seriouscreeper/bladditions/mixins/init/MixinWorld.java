package com.seriouscreeper.bladditions.mixins.init;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(value = World.class)
public abstract class MixinWorld {
    @Shadow @Final public Random rand;

    @Shadow public abstract void spawnParticle(int particleID, boolean ignoreRange, double xCood, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters);

    @Inject(method = "spawnParticle(Lnet/minecraft/util/EnumParticleTypes;ZDDDDDD[I)V", at = @At("HEAD"), cancellable = true)
    private void spawnParticles(EnumParticleTypes particleType, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int[] parameters, CallbackInfo ci) {
        if(particleType == EnumParticleTypes.REDSTONE && xSpeed == 0) {
            float f = rand.nextFloat() - 0.5F;
            float f1 = 0.15F + 0.02F * f;
            float f2 = 0.67F + 0.145F * f;
            float f3 = 0.83F + 0.18F * f;

            this.spawnParticle(particleType.getParticleID(), particleType.getShouldIgnoreRange(), xCoord, yCoord, zCoord, f1, f2, f3, parameters);

            ci.cancel();
        }
    }
}
