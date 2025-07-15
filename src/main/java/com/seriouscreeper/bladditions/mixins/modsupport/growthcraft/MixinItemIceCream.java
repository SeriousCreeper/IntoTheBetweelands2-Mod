package com.seriouscreeper.bladditions.mixins.modsupport.growthcraft;

import growthcraft.milk.common.item.ItemIceCream;
import org.spongepowered.asm.mixin.Mixin;
import thebetweenlands.api.item.IFoodSicknessItem;

@Mixin(value = ItemIceCream.class, remap = false)
public class MixinItemIceCream implements IFoodSicknessItem {
}
