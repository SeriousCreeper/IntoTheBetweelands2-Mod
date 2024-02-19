package com.seriouscreeper.bladditions.mixins.modsupport.arcanearchives;

import com.aranaira.arcanearchives.util.ItemUtils;
import com.seriouscreeper.bladditions.util.BlockedItemsHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemUtils.class, remap = false)
public class MixinItemUtilsAA {
    @Inject(method = "areStacksEqualIgnoreSize", at = @At("HEAD"), cancellable = true)
    private static void checkBlocked(ItemStack stackA, ItemStack stackB, CallbackInfoReturnable<Boolean> cir) {
        if(BlockedItemsHelper.isBlocked(stackA) || BlockedItemsHelper.isBlocked(stackB)) {
            cir.setReturnValue(false);
        }
    }
}
