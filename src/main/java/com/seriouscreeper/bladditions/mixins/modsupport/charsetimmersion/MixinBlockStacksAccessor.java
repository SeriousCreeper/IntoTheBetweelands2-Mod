package com.seriouscreeper.bladditions.mixins.modsupport.charsetimmersion;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import pl.asie.charset.lib.utils.UnlistedPropertyGeneric;
import pl.asie.charset.module.immersion.stacks.BlockStacks;
import pl.asie.charset.module.immersion.stacks.TileEntityStacks;

@Mixin(BlockStacks.class)
public interface BlockStacksAccessor {

    @Accessor("PROPERTY_TILE")
    static UnlistedPropertyGeneric<TileEntityStacks> getPropertyTile() {
        throw new AssertionError();
    }
}
