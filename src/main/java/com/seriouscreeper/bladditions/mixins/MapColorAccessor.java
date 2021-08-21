package com.seriouscreeper.bladditions.mixins;

import net.minecraft.block.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MapColor.class)
public interface MapColorAccessor {
    @Invoker
    static MapColor createMapColor(int index, int color) {
        throw new UnsupportedOperationException();
    }
}
