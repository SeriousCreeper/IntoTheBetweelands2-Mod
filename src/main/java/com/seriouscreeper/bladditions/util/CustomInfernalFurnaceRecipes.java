package com.seriouscreeper.bladditions.util;

import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.google.common.collect.Maps;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import teamroots.embers.register.ItemRegister;

import java.util.Map;
import java.util.Map.Entry;

public class CustomInfernalFurnaceRecipes {
    private static final CustomInfernalFurnaceRecipes INSTANCE = new CustomInfernalFurnaceRecipes();
    private final Map<ItemStack, ItemStack> smeltingList = Maps.newHashMap();
    private final Map<ItemStack, Float> experienceList = Maps.newHashMap();

    public static CustomInfernalFurnaceRecipes instance() {
        return INSTANCE;
    }

    private CustomInfernalFurnaceRecipes() {
        this.addSmeltingRecipe(new ItemStack(ModuleCore.Items.MATERIAL, 1, 4), new ItemStack(ItemRegister.BLEND_CAMINITE, 1, 0), 0.35F);
    }

    public void addSmelting(Item input, ItemStack stack, float experience) {
        this.addSmeltingRecipe(new ItemStack(input, 1, 32767), stack, experience);
    }

    public void addSmeltingRecipe(ItemStack input, ItemStack output, float experience) {
        if (getSmeltingResult(input) != ItemStack.EMPTY) {
            System.out.println("Ignored smelting recipe with conflicting input: " + input + " = " + output);
            return;
        }
        smeltingList.put(input, output);
        experienceList.put(output, experience);
    }

    public ItemStack getSmeltingResult(ItemStack input) {
        for (Entry<ItemStack, ItemStack> entry : smeltingList.entrySet()) {
            if (compareItemStacks(input, entry.getKey())) {
                return entry.getValue();
            }
        }
        return ItemStack.EMPTY;
    }

    public float getSmeltingExperience(ItemStack output) {
        for (Entry<ItemStack, Float> entry : experienceList.entrySet()) {
            if (compareItemStacks(output, entry.getKey())) {
                return entry.getValue();
            }
        }
        return 0.0F;
    }

    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
        return stack2.getItem() == stack1.getItem() &&
                (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }

    public Map<ItemStack, ItemStack> getSmeltingList() {
        return smeltingList;
    }
}
