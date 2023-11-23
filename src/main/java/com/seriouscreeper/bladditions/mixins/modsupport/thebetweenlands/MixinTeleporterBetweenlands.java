package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thebetweenlands.common.world.teleporter.TeleporterBetweenlands;

@Mixin(value = TeleporterBetweenlands.class)
public class MixinTeleporterBetweenlands {
    @Mutable
    @Final
    @Shadow private boolean makePortal = false;


    @Inject(method = "makePortal", at = @At("HEAD"), cancellable = true)
    private void injectMakePortal(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "placeInPortal", at = @At("HEAD"))
    private void injectPlaceInPortal(Entity entity, float rotationYaw, CallbackInfo ci) {
        this.makePortal = false;
    }
}
