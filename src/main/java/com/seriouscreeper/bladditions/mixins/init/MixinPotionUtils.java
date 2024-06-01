package com.seriouscreeper.bladditions.mixins.init;

import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(value = PotionUtils.class)
public class MixinPotionUtils {
    @Inject(method = "getPotionColorFromEffectList", at = @At("HEAD"), cancellable = true)
    private static void getPotionColorFromEffectList(Collection<PotionEffect> effects, CallbackInfoReturnable<Integer> cir) {
        if (effects.isEmpty()) {
            cir.setReturnValue(1589792);
        }
    }
}
