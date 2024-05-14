package com.seriouscreeper.bladditions.mixins.modsupport.arcanearchives;

import com.aranaira.arcanearchives.client.gui.GUIRadiantChest;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GUIRadiantChest.class, remap = false)
public class MixinGUIRadiantChest {
    @Shadow private Slot hoveredSlot;

    @Inject(method = "checkHotbarKeys", at = @At("HEAD"), cancellable = true)
    private void injectCheckhotbarKeys(int keyCode, CallbackInfoReturnable<Boolean> cir) {
        if (this.hoveredSlot != null && this.hoveredSlot.getStack().getMaxStackSize() == 1) {
            cir.setReturnValue(false);
        }
    }
}
