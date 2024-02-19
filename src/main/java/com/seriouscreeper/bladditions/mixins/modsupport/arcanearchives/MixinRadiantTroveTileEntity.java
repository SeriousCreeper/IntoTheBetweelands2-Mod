package com.seriouscreeper.bladditions.mixins.modsupport.arcanearchives;

import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import com.seriouscreeper.bladditions.util.BlockedItemsHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RadiantTroveTileEntity.class, remap = false)
public class MixinRadiantTroveTileEntity {
    @Inject(method = "onRightClickTrove", at = @At("HEAD"), cancellable = true)
    private void onBlocked(EntityPlayer player, CallbackInfo ci) {
        ItemStack mainhand = player.getHeldItemMainhand();

        if (!mainhand.isEmpty() && BlockedItemsHelper.isBlocked(mainhand)) {
            ci.cancel();
        }
    }
}
