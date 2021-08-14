package com.seriouscreeper.bladditions.containers.slot;

import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import thebetweenlands.common.registries.ItemRegistry;

public class SlotElixir extends Slot {
    int limit = 64;

    public SlotElixir(IInventory par2IInventory, int par3, int par4, int par5) {
        super(par2IInventory, par3, par4, par5);
    }

    public SlotElixir(int limit, IInventory par2IInventory, int par3, int par4, int par5) {
        super(par2IInventory, par3, par4, par5);
        this.limit = limit;
    }

    public boolean isItemValid(ItemStack stack) {
        return stack != null && !stack.isEmpty() && stack.getItem() == ItemRegistry.ELIXIR;
    }

    public static boolean isValidPotion(ItemStack stack) {
        if (stack.getItem() == ItemRegistry.ELIXIR) {
            try {
                PotionType potion = PotionUtils.getPotionFromItem(stack);
                return potion != null && potion != PotionTypes.WATER && potion != PotionTypes.AWKWARD && potion != PotionTypes.EMPTY && potion != PotionTypes.MUNDANE && potion != PotionTypes.THICK;
            } catch (Exception var2) {
            }
        }

        return false;
    }

    public int getSlotStackLimit() {
        return this.limit;
    }
}
