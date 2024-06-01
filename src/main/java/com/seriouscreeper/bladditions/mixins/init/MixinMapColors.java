package com.seriouscreeper.bladditions.mixins.init;

import net.minecraft.block.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = MapColor.class)
public class MixinMapColors implements MapColorAccessor {
    @Shadow
    public static final MapColor WATER = MapColorAccessor.createMapColor(12, 2836269);
}
