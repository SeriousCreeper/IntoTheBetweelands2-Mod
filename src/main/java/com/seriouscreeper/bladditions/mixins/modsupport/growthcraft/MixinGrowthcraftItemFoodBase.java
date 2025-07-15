package com.seriouscreeper.bladditions.mixins.modsupport.growthcraft;

import growthcraft.core.shared.item.GrowthcraftItemFoodBase;
import org.spongepowered.asm.mixin.Mixin;
import thebetweenlands.api.item.IFoodSicknessItem;

@Mixin(value = GrowthcraftItemFoodBase.class, remap = false)
public class MixinGrowthcraftItemFoodBase implements IFoodSicknessItem {
}
