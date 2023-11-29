package com.seriouscreeper.bladditions.mixins.modsupport.embers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import teamroots.embers.recipe.AnvilRepairRecipe;
import teamroots.embers.recipe.DawnstoneAnvilRecipe;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = AnvilRepairRecipe.class, remap = false)
public class MixinAnvilRepairRecipe {
    @Inject(method = "getWrappers", at = @At("HEAD"), cancellable = true)
    private void injectGetWrapper(CallbackInfoReturnable<List<DawnstoneAnvilRecipe>> cir) {
        cir.setReturnValue(new ArrayList<>());
    }
}
