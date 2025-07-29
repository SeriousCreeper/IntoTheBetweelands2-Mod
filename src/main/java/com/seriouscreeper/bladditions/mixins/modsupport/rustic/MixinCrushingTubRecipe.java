package com.seriouscreeper.bladditions.mixins.modsupport.rustic;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import rustic.common.crafting.CrushingTubRecipe;

@Mixin(value = CrushingTubRecipe.class, remap = false)
public class MixinCrushingTubRecipe {
    @Shadow protected ItemStack input;

    /**
     * @author SC
     * @reason check NBT
     */
    @Overwrite
    public boolean matches(ItemStack in) {
        return in.getItem() == this.input.getItem() &&
                in.getMetadata() == this.input.getMetadata() &&
                ItemStack.areItemStackTagsEqual(in, this.input);
    }
}
