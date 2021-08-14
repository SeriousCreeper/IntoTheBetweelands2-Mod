package com.seriouscreeper.bladditions.mixins.modsupport;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.item.herblore.ItemDentrothystFluidVial;
import thebetweenlands.common.registries.FluidRegistry;

@Mixin(value = ItemDentrothystFluidVial.class, remap = false)
@SuppressWarnings("unused")
public abstract class MixinItemDentrothystFluidVial {
    @Overwrite
    public boolean canFillWith(ItemStack stack, FluidStack fluid) {
        return fluid != null;
    }
}
