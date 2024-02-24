package com.seriouscreeper.bladditions.mixins.modsupport.toolbelt;

import gigaherz.toolbelt.Config;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Config.class, remap = false)
public class MixinToolbeltConfig {
    @Final
    @Shadow
    private static java.util.Set<net.minecraft.item.ItemStack> blackList;

    @Inject(method = "isItemStackAllowed", at = @At("HEAD"), cancellable = true)
    private static void injectIsItemStackAllowed(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if(blackList.stream().anyMatch((s) -> s.getItem() == stack.getItem())) {
            cir.setReturnValue(false);
        }
    }
}
