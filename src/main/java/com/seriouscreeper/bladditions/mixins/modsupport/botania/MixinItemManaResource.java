package com.seriouscreeper.bladditions.mixins.modsupport.botania;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thecodex6824.thaumicaugmentation.common.world.WorldProviderEmptiness;
import vazkii.botania.common.item.material.ItemManaResource;

@Mixin(value = ItemManaResource.class, remap = false)
public class MixinItemManaResource extends Item {
    /**
     * @author
     * @reason
     */
    @Overwrite
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        boolean correctStack = !stack.isEmpty() && stack.getItem() == Items.GLASS_BOTTLE;
        int dimensionID = event.getWorld().provider.getDimension();
        boolean ender = event.getWorld().provider instanceof WorldProviderEmptiness || (dimensionID >= 21 && dimensionID <= 30);
        if (correctStack && ender) {
            if (event.getWorld().isRemote) {
                event.getEntityPlayer().swingArm(event.getHand());
            } else {
                ItemStack stack1 = new ItemStack(this, 1, 15);
                ItemHandlerHelper.giveItemToPlayer(event.getEntityPlayer(), stack1);
                stack.shrink(1);
                event.getWorld().playSound((EntityPlayer)null, event.getPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.5F, 1.0F);
            }

            event.setCanceled(true);
            event.setCancellationResult(EnumActionResult.SUCCESS);
        }
    }
}
