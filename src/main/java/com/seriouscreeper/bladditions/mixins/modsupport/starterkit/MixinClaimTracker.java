package com.seriouscreeper.bladditions.mixins.modsupport.starterkit;

import com.valkyrieofnight.sk.m_kit.claimtracker.ClaimTracker;
import com.valkyrieofnight.sk.m_kit.json.JsonKit;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClaimTracker.class, remap = false)
public class MixinClaimTracker {
    @Inject(method = "giveKit", at = @At("RETURN"))
    private static void injectGiveKit(EntityPlayer pl, JsonKit kit, CallbackInfo ci) {
        pl.getHeldItemMainhand().shrink(1);
        pl.closeScreen();
    }
}
