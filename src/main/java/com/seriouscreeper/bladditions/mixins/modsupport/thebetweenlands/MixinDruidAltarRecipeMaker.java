package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import mezz.jei.api.recipe.IRecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Inject;
import thebetweenlands.api.recipes.IDruidAltarRecipe;
import thebetweenlands.common.recipe.misc.DruidAltarRecipe;
import thebetweenlands.compat.jei.recipes.druid_altar.DruidAltarReactivationRecipeJEI;
import thebetweenlands.compat.jei.recipes.druid_altar.DruidAltarRecipeJEI;
import thebetweenlands.compat.jei.recipes.druid_altar.DruidAltarRecipeMaker;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(value = DruidAltarRecipeMaker.class)
public class MixinDruidAltarRecipeMaker {
    /**
     * @author SC
     * @reason Hide spawner reactivation recipe
     */
    @Overwrite
    @Nonnull
    public static List<IRecipeWrapper> getRecipes() {
        ArrayList<IRecipeWrapper> recipes = new ArrayList();
        Iterator var1 = DruidAltarRecipe.getRecipes().iterator();

        while(var1.hasNext()) {
            IDruidAltarRecipe recipe = (IDruidAltarRecipe)var1.next();
            if (recipe instanceof DruidAltarRecipe) {
                recipes.add(new DruidAltarRecipeJEI((DruidAltarRecipe)recipe));
            }
        }

        return recipes;
    }
}
