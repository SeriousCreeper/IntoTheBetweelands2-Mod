package com.seriouscreeper.bladditions.events;

import com.daeruin.basketcase.items.ItemBlockBasket;
import com.seriouscreeper.bladditions.proxy.CommonProxy;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thebetweenlands.common.entity.mobs.EntitySludge;

@Mod.EventBusSubscriber
public class DungeonEventHandler {
    private static void cancelIfInDim(World world, Event event) {
        if (CommonProxy.IsInDungeonWorld(world)) {
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

    private static void denyIfInDim(World world, Event event) {
        if (CommonProxy.IsInDungeonWorld(world)) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public void onSpawn(LivingSpawnEvent.CheckSpawn event) {
        if (CommonProxy.IsInDungeonWorld(event.getWorld()) && event.getResult() != Event.Result.DENY && !event.isSpawner()) {
            denyIfInDim(event.getWorld(), event);
        }
    }

    @SubscribeEvent
    public static void onPlaceBlock(BlockEvent.PlaceEvent event) {
        cancelIfInDim(event.getWorld(), event);
    }

    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent event) {
        cancelIfInDim(event.getWorld(), event);
    }

    @SubscribeEvent
    public static void onExplode(ExplosionEvent.Detonate event) {
        if (CommonProxy.IsInDungeonWorld(event.getWorld())) {
            event.getAffectedBlocks().clear();
        }
    }

    @SubscribeEvent
    public static void onMobGriefing(EntityMobGriefingEvent event) {
        if (event.getEntity() != null) {
            denyIfInDim(event.getEntity().world, event);
        }
    }

    @SubscribeEvent
    public static void onPlayerBreakSpeed(PlayerEvent.BreakSpeed event) {
        cancelIfInDim(event.getEntityPlayer().world, event);
    }

    @SubscribeEvent
    public static void onUseItem(PlayerInteractEvent.RightClickItem event) {
        if (CommonProxy.IsInDungeonWorld(event.getWorld())) {
            ItemStack stack = event.getItemStack();
            if (stack.getItem() instanceof ItemBlock && !(stack.getItem() instanceof ItemBlockBasket)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        cancelIfInDim(event.getWorld(), event);
    }
}
