package com.seriouscreeper.bladditions.mixins.modsupport.botania;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.registries.BlockRegistry;
import vazkii.botania.client.integration.jei.orechid.OrechidRecipeWrapper;

@Mixin(value = OrechidRecipeWrapper.class, remap = false)
public class MixinOrechidRecipeWrapper {
    /**
     * @author
     * @reason
     */
    @Overwrite
    protected ItemStack getInputStack() {
        return new ItemStack(BlockRegistry.BETWEENSTONE, 64);
    }
}
