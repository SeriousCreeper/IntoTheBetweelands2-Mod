package com.seriouscreeper.bladditions.mixins.modsupport.growthcraft;

import growthcraft.milk.common.item.ItemWaxedCheeseSlice;
import org.spongepowered.asm.mixin.Mixin;
import thebetweenlands.api.item.IFoodSicknessItem;

@Mixin(value = ItemWaxedCheeseSlice.class, remap = false)
public class MixinItemWaxedCheeseSlice implements IFoodSicknessItem {
}
