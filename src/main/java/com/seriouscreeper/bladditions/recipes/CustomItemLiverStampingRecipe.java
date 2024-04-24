package com.seriouscreeper.bladditions.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import soot.Registry;
import soot.item.ItemSulfurClump;
import teamroots.embers.recipe.ItemStampingRecipe;
import teamroots.embers.register.ItemRegister;
import thebetweenlands.common.item.misc.ItemMisc;

public class CustomItemLiverStampingRecipe extends ItemStampingRecipe {
    public CustomItemLiverStampingRecipe() {
        super(Ingredient.fromItem(Registry.SULFUR_CLUMP), (FluidStack)null, Ingredient.fromItem(ItemRegister.STAMP_FLAT), ItemMisc.EnumItemMisc.SULFUR.create(1));
    }

    public ItemStack getResult(TileEntity tile, ItemStack item, FluidStack fluid, ItemStack stamp) {
        int amount = 1;
        if (item.getItem() instanceof ItemSulfurClump) {
            amount = ((ItemSulfurClump)item.getItem()).getSize(item);
        }

        return ItemMisc.EnumItemMisc.SULFUR.create(amount);
    }
}
