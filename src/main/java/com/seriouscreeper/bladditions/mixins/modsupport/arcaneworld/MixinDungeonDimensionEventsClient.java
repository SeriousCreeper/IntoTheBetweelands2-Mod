package com.seriouscreeper.bladditions.mixins.modsupport.arcaneworld;

import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import party.lemons.arcaneworld.gen.dungeon.dimension.DungeonDimension;

@Mixin(value = DungeonDimension.DungeonDimensionEventsClient.class, remap = false)
public class MixinDungeonDimensionEventsClient {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public static void onDrawBlockHighlight(DrawBlockHighlightEvent event) {
    }
}
