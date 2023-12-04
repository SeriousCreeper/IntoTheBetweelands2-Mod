package com.seriouscreeper.bladditions.mixins.modsupport.teastory.teas;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import roito.teastory.item.drink.BlackTea;

@Mixin(value = BlackTea.class, remap = false)
public class MixinBlackTea {
    @Shadow
    public static void addPotion(int tier, World world, EntityPlayer entityplayer) {
    }

    @Inject(method = "onFoodEaten", at = @At("HEAD"), cancellable = true)
    private void injectOnFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer, CallbackInfo ci) {
        addPotion(0, world, entityplayer);
        ci.cancel();
    }
}
