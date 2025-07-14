package com.seriouscreeper.bladditions.mixins.modsupport.growthcraft;

import growthcraft.grapes.common.Init;
import growthcraft.grapes.shared.config.GrowthcraftGrapesConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Init.class, remap = false)
public class MixinInitGrapes {
    @Inject(method = "preInitFluids", at = @At("HEAD"))
    private static void bladditions$preInitFluids(CallbackInfo ci) {
        GrowthcraftGrapesConfig.grapeWinePurpleColor = 0xE2E9A5;
        GrowthcraftGrapesConfig.ambrosiaPurpleColor = 0xD0FFA7;
        GrowthcraftGrapesConfig.portWinePurpleColor = 0xDBFCAF;
    }
}
