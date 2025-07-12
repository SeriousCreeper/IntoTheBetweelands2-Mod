package com.seriouscreeper.bladditions.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.Fluid;

public class FluidInsertCombo {
    public ItemStack inputItem;
    public Fluid fluid;
    public int amount;
    public ItemStack outputItem;
    public SoundEvent sound;

    public FluidInsertCombo(ItemStack in, Fluid f, int amt, ItemStack out, SoundEvent sfx) {
        inputItem = in;
        fluid = f;
        amount = amt;
        outputItem = out;
        sound = sfx;
    }
}