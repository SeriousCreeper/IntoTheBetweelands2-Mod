package com.seriouscreeper.bladditions.mixins.modsupport.arcanearchives;

import com.aranaira.arcanearchives.inventory.ContainerRadiantChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ContainerRadiantChest.class)
public class MixinContainerRadiantChest {
    @Redirect(method = "mergeItemStack", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/inventory/Slot;getItemStackLimit(Lnet/minecraft/item/ItemStack;)I"))
    private int redirectGetItemStackLimit(Slot slot, ItemStack stack) {
        return stack.getMaxStackSize();
    }
}
