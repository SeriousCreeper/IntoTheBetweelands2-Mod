package com.seriouscreeper.bladditions.mixins.modsupport.fishundeadrising;

import com.Fishmod.mod_LavaCow.util.LootTableHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thebetweenlands.common.world.WorldProviderBetweenlands;

@Mixin(value = LootTableHandler.class, remap = false)
public class MixinLootTableHandler {
    @Inject(method = "dropRareLoot(Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;II)V", at = @At("HEAD"), cancellable = true)
    private static void injectDropRareLoot(Entity entityIn, ItemStack itemstackIn, int chance, int looting, CallbackInfo ci) {
        if(!(entityIn.world.provider instanceof WorldProviderBetweenlands)) {
            ci.cancel();
        }
    }

    @Inject(method = "dropRareLoot(Lnet/minecraft/entity/Entity;Lnet/minecraft/item/Item;ILnet/minecraft/enchantment/Enchantment;II)V", at = @At("HEAD"), cancellable = true)
    private static void injectDropRareLoot(Entity entityIn, Item itemIn, int chance, Enchantment enchantmentIn, int enchantlevel, int looting, CallbackInfo ci) {
        if(!(entityIn.world.provider instanceof WorldProviderBetweenlands)) {
            ci.cancel();
        }
    }
}
