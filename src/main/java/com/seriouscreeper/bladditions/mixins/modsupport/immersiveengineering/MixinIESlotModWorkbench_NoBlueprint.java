package com.seriouscreeper.bladditions.mixins.modsupport.immersiveengineering;

import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.items.ItemEngineersBlueprint;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;

@Mixin(value = IESlot.ModWorkbench.class)
public class MixinIESlotModWorkbench_NoBlueprint {

    @Inject(method = "isItemValid", at = @At("HEAD"), cancellable = true)
    private void ie_noBlueprints(@Nonnull ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!stack.isEmpty() && stack.getItem() instanceof ItemEngineersBlueprint) {
            cir.setReturnValue(false);
        }
    }
}