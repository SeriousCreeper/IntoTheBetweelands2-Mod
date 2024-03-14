package com.seriouscreeper.bladditions.mixins.modsupport.arcaneworld;

import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import party.lemons.arcaneworld.config.ArcaneWorldConfig;
import party.lemons.arcaneworld.gen.dungeon.dimension.DungeonDimension;

@Mixin(value = DungeonDimension.class, remap = false)
public class MixinDungeonDimension {
    /**
     * @author
     * @reason
     */
    @Overwrite
    private static void cancelIfInDim(World world, Event event) {
        if (isInDim(world)) {
            if(event instanceof BlockEvent.PlaceEvent) {
                BlockEvent.PlaceEvent tempEvent = (BlockEvent.PlaceEvent)event;
                if(tempEvent.getPlayer() != null && tempEvent.getPlayer().isCreative()) {
                    return;
                }
            } else if(event instanceof BlockEvent.BreakEvent) {
                BlockEvent.BreakEvent tempEvent = (BlockEvent.BreakEvent)event;
                if(tempEvent.getPlayer() != null && tempEvent.getPlayer().isCreative()) {
                    return;
                }
            } else if(event instanceof PlayerEvent) {
                PlayerEvent tempEvent = (PlayerEvent)event;
                if(tempEvent.getEntityPlayer() != null && tempEvent.getEntityPlayer().isCreative()) {
                    return;
                }
            } else if(event instanceof PlayerInteractEvent) {
                PlayerInteractEvent tempEvent = (PlayerInteractEvent)event;
                if(tempEvent.getEntityPlayer() != null && tempEvent.getEntityPlayer().isCreative()) {
                    return;
                }
            }

            event.setCanceled(true);
        }
    }


    @Shadow
    private static boolean isInDim(World world) {
        return world.provider.getDimension() == ArcaneWorldConfig.DUNGEONS.DIM_ID;
    }
}
