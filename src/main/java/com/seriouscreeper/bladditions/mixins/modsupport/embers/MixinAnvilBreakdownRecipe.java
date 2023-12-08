package com.seriouscreeper.bladditions.mixins.modsupport.embers;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.recipe.AnvilBreakdownRecipe;
import teamroots.embers.recipe.RecipeRegistry;
import teamroots.embers.util.Misc;

@Mixin(value = AnvilBreakdownRecipe.class, remap = false)
public class MixinAnvilBreakdownRecipe {
    /**
     * @author SC
     */
    @Overwrite
    public static boolean canBreakdown(ItemStack stack) {
        return false;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean matches(ItemStack input1, ItemStack input2) {
        return false;
    }
}
