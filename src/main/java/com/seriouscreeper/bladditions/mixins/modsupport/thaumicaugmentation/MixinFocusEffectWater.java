package com.seriouscreeper.bladditions.mixins.modsupport.thaumicaugmentation;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import thebetweenlands.common.registries.FluidRegistry;
import thecodex6824.thaumicaugmentation.common.item.foci.FocusEffectWater;

@Mixin(value = FocusEffectWater.class, remap = false)
public class MixinFocusEffectWater {
    @Redirect(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fluids/capability/IFluidHandler;fill(Lnet/minecraftforge/fluids/FluidStack;Z)I"))
    private int redirectFill(IFluidHandler fluidHandler, FluidStack resource, boolean doFill) {
        return fluidHandler.fill(new FluidStack(FluidRegistry.SWAMP_WATER, resource.amount), doFill);
    }
}
