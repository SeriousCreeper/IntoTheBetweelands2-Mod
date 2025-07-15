package com.seriouscreeper.bladditions.mixins.modsupport.growthcraft;

import growthcraft.milk.common.item.ItemYogurt;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thebetweenlands.api.item.IFoodSicknessItem;

@Mixin(value = ItemYogurt.class, remap = false)
public class MixinItemYogurt implements IFoodSicknessItem {
    @Inject(
            method = "onItemUseFinish",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/InventoryPlayer;addItemStackToInventory(Lnet/minecraft/item/ItemStack;)Z",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true)
    private void injectCustomReturnItem(ItemStack stack, World world, EntityLivingBase entity, CallbackInfoReturnable<ItemStack> cir) {
        cir.setReturnValue(ItemStack.EMPTY);
    }
}
