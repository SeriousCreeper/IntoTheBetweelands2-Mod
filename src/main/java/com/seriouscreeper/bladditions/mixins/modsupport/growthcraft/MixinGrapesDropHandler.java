package com.seriouscreeper.bladditions.mixins.modsupport.growthcraft;

import growthcraft.grapes.common.handler.HarvestDropsEventHandler;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = HarvestDropsEventHandler.class, remap = false)
public class MixinGrapesDropHandler {
    /**
     * @author
     * @reason
     */
    @Overwrite
    private void doAdditionalDrop(BlockEvent.HarvestDropsEvent event) {
    }
}
