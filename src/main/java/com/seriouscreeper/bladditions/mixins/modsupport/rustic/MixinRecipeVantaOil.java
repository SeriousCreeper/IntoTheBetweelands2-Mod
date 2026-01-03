package com.seriouscreeper.bladditions.mixins.modsupport.rustic;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import rustic.common.crafting.RecipeVantaOil;

@Mixin(value = RecipeVantaOil.class)
public class MixinRecipeVantaOil {
    @Overwrite
    public boolean matches(InventoryCrafting inv, World worldIn) {
        return false;
    }

    @Overwrite
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return ItemStack.EMPTY;
    }

    @Overwrite
    public boolean isDynamic() {
        return false;
    }
}
