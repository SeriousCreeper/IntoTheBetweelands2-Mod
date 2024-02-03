package com.seriouscreeper.bladditions.mixins.modsupport.sanity;

import com.seriouscreeper.bladditions.interfaces.ISanityExtraInfo;
import net.tiffit.sanity.SanityCapability;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SanityCapability.class, remap = false)
public class MixinSanityCapability implements ISanityExtraInfo {
    @Unique
    public boolean into_the_betweenlands_mod$canIncrease = true;
    @Unique
    public boolean into_the_betweenlands_mod$canDecrease = true;

    /**
     * @author SC
     * @reason be able to cancel increase or decrease of sanity in certain situations
     */
    @Inject(method = "setSanity", at = @At("HEAD"), cancellable = true)
    public void setSanity(float sanity, CallbackInfo ci) {
        if(sanity > 0 && !into_the_betweenlands_mod$canIncrease) {
            ci.cancel();
        } else if(sanity < 0 && !into_the_betweenlands_mod$canDecrease) {
            ci.cancel();
        }
    }

    @Override
    public boolean getCanIncrease() {
        return true;
    }

    @Override
    public boolean getCanDecrease() {
        return into_the_betweenlands_mod$canDecrease;
    }

    @Override
    public void setCanIncrease(boolean canIncrease) {
        into_the_betweenlands_mod$canIncrease = canIncrease;
    }

    @Override
    public void setCanDecrease(boolean canDecrease) {
        into_the_betweenlands_mod$canDecrease = canDecrease;
    }
}
