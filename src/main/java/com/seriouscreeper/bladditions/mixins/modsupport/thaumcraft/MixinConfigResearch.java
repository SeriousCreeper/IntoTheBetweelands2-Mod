package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import thaumcraft.common.config.ConfigResearch;

@Mixin(value = ConfigResearch.class, remap = false)
public class MixinConfigResearch {
    @ModifyConstant(method = "checkPeriodicStuff(Lnet/minecraft/entity/player/EntityPlayer;)V", constant = @Constant(doubleValue = 0.4))
    private static double injected(double value) {
        return 0.6;
    }
}
