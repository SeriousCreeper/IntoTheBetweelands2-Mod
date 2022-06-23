package com.seriouscreeper.bladditions.recipes;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.Collectors;
import mezz.jei.api.recipe.IFocus;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import soot.Registry;
import soot.util.IngredientAny;
import soot.util.MiscUtil;
import teamroots.embers.recipe.IFocusRecipe;
import teamroots.embers.recipe.IWrappableRecipe;
import teamroots.embers.recipe.ItemStampingRecipe;
import thebetweenlands.common.registries.ItemRegistry;

public class CustomItemRenameStampingRecipe extends ItemStampingRecipe implements IFocusRecipe, IWrappableRecipe {
    public CustomItemRenameStampingRecipe() {
        super(new IngredientAny(), (FluidStack)null, Ingredient.fromItem(ItemRegistry.BL_NAME_TAG), ItemStack.EMPTY);
    }

    private static ItemStack apply(ItemStack stack) {
        ItemStack rstack = stack.copy();
        rstack.setStackDisplayName("Renamed Item");
        return rstack;
    }

    public List<ItemStack> getOutputs() {
        return (List)this.getInputs().stream().map(CustomItemRenameStampingRecipe::apply).collect(Collectors.toList());
    }

    public ItemStack getResult(TileEntity tile, ItemStack item, FluidStack fluid, ItemStack stamp) {
        ItemStack output = item.copy();
        output.setCount(1);
        List<String> lore = MiscUtil.getLore(stamp.getTagCompound());
        if (stamp.hasDisplayName()) {
            output.setStackDisplayName(stamp.getDisplayName());
        } else {
            output.clearCustomName();
        }

        MiscUtil.setLore(lore, output.getTagCompound(), false);
        return output;
    }

    public List<ItemStack> getOutputs(IFocus<ItemStack> focus, int slot) {
        return slot == 2 ? Lists.newArrayList(new ItemStack[]{apply((ItemStack)focus.getValue())}) : Lists.newArrayList();
    }

    public List<ItemStack> getInputs(IFocus<ItemStack> focus, int slot) {
        if (slot == 0) {
            return Lists.newArrayList(new ItemStack[]{(ItemStack)focus.getValue()});
        } else {
            return (List)(slot == 1 ? this.getStamps() : Lists.newArrayList());
        }
    }

    public List<IWrappableRecipe> getWrappers() {
        return Lists.newArrayList();
    }
}
