package com.seriouscreeper.bladditions.items;

import epicsquid.mysticallib.item.ItemFoodBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import thebetweenlands.api.item.IFoodSicknessItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PatchedItemCookedPereskia extends ItemFoodBase implements IFoodSicknessItem {

    public PatchedItemCookedPereskia(@Nonnull String name, int amount, float saturation, boolean isWolfFood) {
        super(name, amount, saturation, isWolfFood);
    }

    @Override
    public boolean canGetSickOf(@Nullable EntityPlayer player, ItemStack stack) {
        return true;
    }
}
