package com.seriouscreeper.bladditions.mixins.modsupport.grue;

import com.shinoow.grue.common.handlers.GrueEventHandler;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.tiffit.sanity.SanityCapability;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GrueEventHandler.class, remap = false)
public class MixinGrueEventHandler {
    @Inject(method = "spawnGrue", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;isSpectator()Z"), cancellable = true)
    private void injectIsWhitelisted(LivingEvent.LivingUpdateEvent event, CallbackInfo ci) {
        SanityCapability cap = event.getEntityLiving().getCapability(SanityCapability.INSTANCE, (EnumFacing)null);

        if(cap == null || cap.getSanityExact() > -10) {
            ci.cancel();
        }
    }
}
