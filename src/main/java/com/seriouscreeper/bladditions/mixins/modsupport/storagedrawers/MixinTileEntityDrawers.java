package com.seriouscreeper.bladditions.mixins.modsupport.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.seriouscreeper.bladditions.util.BlockedItemsHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = TileEntityDrawers.class, remap = false)
public class MixinTileEntityDrawers {
    @Inject(method = "putItemsIntoSlot", at = @At("HEAD"), cancellable = true)
    private void injectPutItemsIntoSlot(int slot, ItemStack stack, int count, CallbackInfoReturnable<Integer> cir) {
        if(BlockedItemsHelper.isBlocked(stack)) {
            cir.setReturnValue(0);
        }
    }

    @Inject(method = "interactPutItemsIntoSlot", at = @At("HEAD"), cancellable = true)
    private void injectInteractPutItemsIntoSlot(int slot, EntityPlayer player, CallbackInfoReturnable<Integer> cir) {
        ItemStack playerStack = player.inventory.getCurrentItem();

        if(BlockedItemsHelper.isBlocked(playerStack)) {
            cir.setReturnValue(0);
        }
    }
}
