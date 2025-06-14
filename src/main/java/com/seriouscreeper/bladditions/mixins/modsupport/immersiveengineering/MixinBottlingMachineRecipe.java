package com.seriouscreeper.bladditions.mixins.modsupport.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.BottlingMachineRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BottlingMachineRecipe.class, remap = false)
public class MixinBottlingMachineRecipe {
    @Shadow
    @Final
    public IngredientStack input;

    @Inject(method = "<init>", remap = false, at = @At("RETURN"))
    private void injectConstructor(ItemStack output, Object input, FluidStack fluidInput, CallbackInfo ci) {
        this.input.setUseNBT(true);
    }
}
