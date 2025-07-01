package com.seriouscreeper.bladditions.mixins.modsupport.aether;

import com.gildedgames.aether.common.init.CurrencyAetherInit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = CurrencyAetherInit.class, remap = false)
public class MixinCurrencyAetherInit {
    /**
     * @author SC
     * @reason disable currency
     */
    @Overwrite
    public static void onServerAboutToStart() {
    }
}
