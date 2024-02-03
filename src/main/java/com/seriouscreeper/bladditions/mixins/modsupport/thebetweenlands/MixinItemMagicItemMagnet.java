package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thebetweenlands.common.item.misc.ItemMagicItemMagnet;
import vazkii.botania.common.block.subtile.functional.SubTileSolegnolia;

@Mixin(value = ItemMagicItemMagnet.class)
public class MixinItemMagicItemMagnet {
    @Inject(method = "onEquipmentTick", at = @At("HEAD"), cancellable = true)
    private void injectOnEquipmentTick(ItemStack stack, Entity entity, IInventory inventory, CallbackInfo ci) {
        if (SubTileSolegnolia.hasSolegnoliaAround(entity)) {
            ci.cancel();
        }
    }
}
