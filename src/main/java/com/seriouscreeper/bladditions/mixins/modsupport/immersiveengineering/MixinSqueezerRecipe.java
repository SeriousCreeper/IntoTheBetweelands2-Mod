package com.seriouscreeper.bladditions.mixins.modsupport.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.SqueezerRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SqueezerRecipe.class, remap = false)
public class MixinSqueezerRecipe {
    @Shadow @Final public IngredientStack input;

    @Inject(method = "<init>", remap = false, at = @At("RETURN"))
    private void injectConstructor(FluidStack fluidOutput, ItemStack itemOutput, Object input, int energy, CallbackInfo ci) {
        this.input.setUseNBT(true);
    }
}
