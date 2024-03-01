package com.seriouscreeper.bladditions.mixins.modsupport.botania;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thebetweenlands.common.registries.FluidRegistry;
import vazkii.botania.common.block.BlockAltar;

@Mixin(value = BlockAltar.class, remap = false)
public class MixinBlockAltar {
    @Inject(method = "isValidWaterContainer", at = @At("HEAD"), cancellable = true)
    private void injectIsValidWaterContainer(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!stack.isEmpty() && stack.getCount() == 1) {
            if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, (EnumFacing)null)) {
                IFluidHandler handler = (IFluidHandler)stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, (EnumFacing)null);
                FluidStack simulate = handler.drain(new FluidStack(FluidRegistry.SWAMP_WATER, 1000), false);
                if (simulate != null && simulate.getFluid() == FluidRegistry.SWAMP_WATER && simulate.amount == 1000) {
                    cir.setReturnValue(true);
                }
            }
        }
    }

    @ModifyArg(remap = true, method = "onBlockActivated", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;setHeldItem(Lnet/minecraft/util/EnumHand;Lnet/minecraft/item/ItemStack;)V"), index = 1)
    private ItemStack modifySetHeldItem(ItemStack stack) {
        return this.drain(FluidRegistry.SWAMP_WATER, stack);
    }

    @Shadow
    private ItemStack drain(Fluid fluid, ItemStack stack) {
        return null;
    }

}
