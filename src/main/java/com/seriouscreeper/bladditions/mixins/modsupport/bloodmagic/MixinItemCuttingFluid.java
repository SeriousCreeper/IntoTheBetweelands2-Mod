package com.seriouscreeper.bladditions.mixins.modsupport.bloodmagic;

import WayofTime.bloodmagic.item.alchemy.ItemCuttingFluid;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = ItemCuttingFluid.class, remap = false)
public class MixinItemCuttingFluid {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getMaxUsesForFluid(ItemStack stack) {
        switch (stack.getMetadata()) {
            case 0:
                return 64;
            case 1:
                return 64;
            default:
                return 1;
        }
    }
}
