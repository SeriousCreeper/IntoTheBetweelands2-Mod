package com.seriouscreeper.bladditions.mixins.modsupport.immersiveengineering;

import blusunrize.immersiveengineering.common.items.ItemIEBase;
import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ItemIEBase.class, remap = false)
public class MixinItemIEBase extends Item {
    @Override
    public int getItemStackLimit(ItemStack stack) {
        if(stack.isEmpty()) {
            return this.maxStackSize;
        }

        if (stack.getMetadata() == 5 || stack.getMetadata() == 6 || stack.getMetadata() == 8) {
            return ConfigBLAdditions.configGeneral.StackSizeIngots;
        }

        return this.maxStackSize;
    }
}
