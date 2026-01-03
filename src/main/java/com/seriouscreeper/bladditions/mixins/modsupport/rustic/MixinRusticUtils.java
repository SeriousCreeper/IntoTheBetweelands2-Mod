package com.seriouscreeper.bladditions.mixins.modsupport.rustic;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import rustic.common.util.RusticUtils;

@Mixin(value = RusticUtils.class)
public class MixinRusticUtils {
    @Overwrite
    public static boolean isVantaOilableWeapon(ItemStack stack) {
        return false;
    }
}
