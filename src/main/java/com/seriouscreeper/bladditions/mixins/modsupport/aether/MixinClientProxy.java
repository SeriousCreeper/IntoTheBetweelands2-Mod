package com.seriouscreeper.bladditions.mixins.modsupport.aether;

import com.gildedgames.aether.client.ClientProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientProxy.class, remap = false)
public class MixinClientProxy {
    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lcom/gildedgames/aether/client/events/listeners/gui/GuiOverlayListener;init()V"), cancellable = true)
    private void bladditions$init(CallbackInfo ci) {
        ci.cancel();
    }
}
