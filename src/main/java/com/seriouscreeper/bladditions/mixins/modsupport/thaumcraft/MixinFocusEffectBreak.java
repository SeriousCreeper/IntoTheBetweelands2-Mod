package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.Trajectory;
import thaumcraft.common.items.casters.foci.FocusEffectBreak;
import thebetweenlands.common.world.storage.location.LocationStorage;

import javax.annotation.Nullable;

@Mixin(value = FocusEffectBreak.class, remap = false)
public class MixinFocusEffectBreak extends FocusEffect {
    @Inject(method = "execute", at = @At("HEAD"), cancellable = true)
    private void injectExecute(RayTraceResult target, Trajectory trajectory, float finalPower, int num, CallbackInfoReturnable<Boolean> cir) {
        if(LocationStorage.isLocationGuarded(this.getPackage().world, null, target.getBlockPos())) {
            cir.setReturnValue(true);
        }
    }

    @Shadow
    @Override
    public boolean execute(RayTraceResult rayTraceResult, @Nullable Trajectory trajectory, float v, int i) {
        return false;
    }

    @Shadow
    @Override
    public void renderParticleFX(World world, double v, double v1, double v2, double v3, double v4, double v5) {

    }

    @Shadow
    @Override
    public int getComplexity() {
        return 0;
    }

    @Shadow
    @Override
    public Aspect getAspect() {
        return null;
    }

    @Shadow
    @Override
    public String getKey() {
        return null;
    }

    @Shadow
    @Override
    public String getResearch() {
        return null;
    }
}
