package com.seriouscreeper.bladditions.mixins.modsupport.arcanearchives;

import com.aranaira.arcanearchives.inventory.handlers.ExtendedItemStackHandler;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nonnull;

@Mixin(value = ExtendedItemStackHandler.class, remap = false)
public class MixinExtendedItemStackHandler {
    /**
     * @author SC
     * @reason fix stack limit
     */
    @Overwrite
    public int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return stack.getMaxStackSize();
    }
}
