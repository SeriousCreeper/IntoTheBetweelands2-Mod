package com.seriouscreeper.bladditions.mixins.modsupport.growthcraft;

import growthcraft.milk.common.item.ItemAgedCheeseSlice;
import org.spongepowered.asm.mixin.Mixin;
import thebetweenlands.api.item.IFoodSicknessItem;

@Mixin(value = ItemAgedCheeseSlice.class, remap = false)
public class MixinItemAgedCheeseSlice implements IFoodSicknessItem {
}
