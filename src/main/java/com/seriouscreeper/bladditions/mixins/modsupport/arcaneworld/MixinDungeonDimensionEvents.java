package com.seriouscreeper.bladditions.mixins.modsupport.arcaneworld;

import com.daeruin.basketcase.items.ItemBlockBasket;
import com.seriouscreeper.bladditions.proxy.CommonProxy;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import party.lemons.arcaneworld.gen.dungeon.dimension.DungeonDimension;

@Mixin(value = DungeonDimension.DungeonDimensionEvents.class, remap = false)
public class MixinDungeonDimensionEvents {
    /**
     * @author
     * @reason
     */
    @Overwrite
    @SubscribeEvent
    public static void onUseItem(PlayerInteractEvent.RightClickItem event) {
        if (CommonProxy.IsInDungeonWorld(event.getWorld())) {
            ItemStack stack = event.getItemStack();
            if (stack.getItem() instanceof ItemBlock && !(stack.getItem() instanceof ItemBlockBasket)) {
                event.setCanceled(true);
            }
        }
    }
}
