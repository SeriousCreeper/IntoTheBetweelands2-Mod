package com.seriouscreeper.bladditions.mixins.modsupport.roots;

import epicsquid.roots.config.MossConfig;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(value = MossConfig.class, remap = false)
public class MixinMossConfig {
    @Shadow private static Map<ItemStack, ItemStack> mossyCobblestones;

    @Inject(method = "getMossyCobblestones", at = @At("HEAD"))
    private static void injectMossy(CallbackInfoReturnable<Map<ItemStack, ItemStack>> cir) {
        mossyCobblestones = null;
    }
}
