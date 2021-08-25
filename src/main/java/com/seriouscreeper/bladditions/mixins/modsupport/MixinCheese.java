package com.seriouscreeper.bladditions.mixins.modsupport;

import growthcraft.milk.common.tileentity.struct.Cheese;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = Cheese.class)
public class MixinCheese {
    @Shadow
    private int ageMax = 24000;
}
