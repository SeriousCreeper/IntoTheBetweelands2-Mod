package com.seriouscreeper.bladditions.mixins.modsupport.botania;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.registries.BlockRegistry;
import vazkii.botania.client.integration.jei.orechid.OrechidIgnemRecipeWrapper;

@Mixin(value = OrechidIgnemRecipeWrapper.class, remap = false)
public class MixinOrechidIgnemRecipeWrapper {
    /**
     * @author
     * @reason
     */
    @Overwrite
    protected ItemStack getInputStack() {
        return new ItemStack(BlockRegistry.PITSTONE, 64);
    }
}
