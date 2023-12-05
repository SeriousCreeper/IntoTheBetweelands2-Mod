package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thebetweenlands.common.registries.RecipeRegistry;

@Mixin(value = RecipeRegistry.class, remap = false)
public class MixinRecipeRegistry {
    @Inject(method = "registerDruidAltarRecipes", at = @At("HEAD"), cancellable = true)
    private static void inject(CallbackInfo ci) {
        ci.cancel();
    }
}
