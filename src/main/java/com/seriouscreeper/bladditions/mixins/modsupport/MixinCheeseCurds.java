package com.seriouscreeper.bladditions.mixins.modsupport;

import growthcraft.milk.common.tileentity.struct.CheeseCurd;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CheeseCurd.class, remap = false)
public class MixinCheeseCurds {
    @Shadow private int ageMax = 24000;

    @Inject(method = "update()V", at = @At(value = "HEAD"))
    public void update(CallbackInfo ci) {
        this.ageMax = 24000;
    }
}
