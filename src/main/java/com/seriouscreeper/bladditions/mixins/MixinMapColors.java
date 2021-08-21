package com.seriouscreeper.bladditions.mixins;

import net.minecraft.block.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MapColor.class)
public class MixinMapColors implements MapColorAccessor {
    @Shadow
    public static final MapColor WATER = MapColorAccessor.createMapColor(12, 2836269);
}
