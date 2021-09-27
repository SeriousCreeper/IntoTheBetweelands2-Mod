package com.seriouscreeper.bladditions.libs;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import java.util.List;

public class BLAdditionsUtils {
    public static ItemStack getStackFromOredict(String oredict) {
        List<ItemStack> items = OreDictionary.getOres(oredict, false);
        String[] oreNames = OreDictionary.getOreNames();

        if(!items.isEmpty()) {
            ItemStack stack = items.get(0);
            stack = stack.copy();

            if(!stack.isEmpty() && stack.getMetadata() == OreDictionary.WILDCARD_VALUE) {
                stack.setItemDamage(0);
            }

            return stack;
        } else return ItemStack.EMPTY;
    }
}
