package com.seriouscreeper.bladditions.mixins.modsupport.botania;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import thebetweenlands.common.registries.ItemRegistry;
import vazkii.botania.client.integration.jei.crafting.CompositeLensRecipeWrapper;

import javax.annotation.Nonnull;
import java.util.List;

@Mixin(value = CompositeLensRecipeWrapper.class, remap = false)
public class MixinCompositeLensRecipeWrapper {
    @Final
    @Shadow
    private static List<ItemStack> lenses;

    /**
     * @author SC
     * @reason
     */
    @Overwrite
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, ImmutableList.of(lenses, ImmutableList.of(new ItemStack(ItemRegistry.SAP_SPIT), new ItemStack(ItemRegistry.SLUDGE_BALL)), lenses));
    }
}
