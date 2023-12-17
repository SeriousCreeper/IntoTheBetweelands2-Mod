package com.seriouscreeper.bladditions.mixins.modsupport.growthcraft;

import growthcraft.hops.common.handler.HarvestDropsEventHandler;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(value = HarvestDropsEventHandler.class, remap = false)
public class MixinHarvestDropsEventHandler {
    /**
     * @author SC
     * @reason disable seed drops
     */
    @Overwrite
    private void doAdditionalDrop(BlockEvent.HarvestDropsEvent event) {
    }
}
