package com.seriouscreeper.bladditions.mixins.modsupport;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import thebetweenlands.common.item.tools.ItemBLBucket;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = ItemBLBucket.class)
public class MixinBLFluidRegistry {
}
