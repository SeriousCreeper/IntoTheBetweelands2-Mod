package com.seriouscreeper.bladditions.mixins.modsupport.roots;

import epicsquid.roots.integration.jei.shears.RunicShearsEntityCategory;
import epicsquid.roots.integration.jei.shears.RunicShearsEntityWrapper;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(value = RunicShearsEntityCategory.class, remap = false)
public class MixinRunicShearsEntityCategory {
    /**
     * @author SC
     * @reason
     */
    @Overwrite
    public void setRecipe(IRecipeLayout recipeLayout, RunicShearsEntityWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup group = recipeLayout.getItemStacks();
        group.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            tooltip.add("");
            tooltip.add(I18n.format("jei.roots.runic_shears.cooldown", new Object[]{ recipeWrapper.getCooldown(), recipeWrapper.getCooldown() / 4 }));
        });
        group.init(0, true, 104, 32);
        group.set(0, (List)ingredients.getOutputs(VanillaTypes.ITEM).get(0));
    }
}
