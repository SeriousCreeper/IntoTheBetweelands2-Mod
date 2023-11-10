package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.common.items.curios.ItemThaumonomicon;
import thaumcraft.common.lib.events.PlayerEvents;

@Mixin(value = PlayerEvents.class, remap = false)
public class MixinPlayerEvents {
    /**
     * @author SC
     */
    @SubscribeEvent
    @Overwrite
    public static void pickupItem(EntityItemPickupEvent event) {
        if (event.getEntityPlayer() != null && !event.getEntityPlayer().world.isRemote && event.getItem() != null && event.getItem().getItem() != null) {
            IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(event.getEntityPlayer());

            if (event.getItem().getItem().getItem() instanceof ItemThaumonomicon && !knowledge.isResearchKnown("!gotthaumonomicon")) {
                knowledge.addResearch("!gotthaumonomicon");
                knowledge.sync((EntityPlayerMP)event.getEntityPlayer());
            }
        }
    }
}
