package com.seriouscreeper.bladditions.mixins.modsupport.growthcraft;

import growthcraft.milk.common.item.ItemCheeseBowl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thebetweenlands.common.item.misc.ItemMisc;

@Mixin(value = ItemCheeseBowl.class, remap = false)
public class MixinItemCheeseBowl {
    @Inject(
            method = "onItemUseFinish",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/InventoryPlayer;addItemStackToInventory(Lnet/minecraft/item/ItemStack;)Z",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true)
    private void injectCustomReturnItem(ItemStack stack, World world, EntityLivingBase entity, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack customItem = ItemMisc.EnumItemMisc.WEEDWOOD_BOWL.create(1);
        cir.setReturnValue(customItem);
    }
}
