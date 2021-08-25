package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thebetweenlands.common.item.equipment.ItemRingOfDispersion;

@Mixin(value = ItemRingOfDispersion.class, remap = false)
public class MixinItemRingOfDisperion {
    @Inject(method = "canPhase", at = @At("HEAD"), cancellable = true)
    public void canPhase(EntityPlayer player, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if(player.world.provider.getDimension() != 20)
            cir.setReturnValue(false);
    }
}
