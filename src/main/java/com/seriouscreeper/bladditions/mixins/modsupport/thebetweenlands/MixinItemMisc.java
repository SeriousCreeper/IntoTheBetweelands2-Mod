package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import thebetweenlands.common.item.misc.ItemMisc;

@Mixin(value = ItemMisc.class, remap = false)
public class MixinItemMisc extends Item {
    @Override
    public int getItemStackLimit(ItemStack stack) {
        if (!stack.isEmpty() && stack.getMetadata() == 11) {
            return 16;
        }

        return this.maxStackSize;
    }
}
