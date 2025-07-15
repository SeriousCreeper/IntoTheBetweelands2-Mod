package com.seriouscreeper.bladditions.mixins.modsupport.growthcraft;

import growthcraft.milk.common.item.ItemButter;
import org.spongepowered.asm.mixin.Mixin;
import thebetweenlands.api.item.IFoodSicknessItem;

@Mixin(value = ItemButter.class, remap = false)
public class MixinItemButter implements IFoodSicknessItem {
}
