package com.seriouscreeper.bladditions.mixins.modsupport.bloodmagic;

import WayofTime.bloodmagic.tile.TileInversionPillar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = TileInversionPillar.class, remap = false)
public class MixinTileInversionPillar {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onUpdate() {
    }
}
