package com.seriouscreeper.bladditions.mixins.modsupport.pyrotech;

import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ItemMaterial.class, remap = false)
public class MixinItemMaterial extends Item {
    @Override
    public int getItemStackLimit(ItemStack stack) {
        if(stack.isEmpty()) {
            return this.maxStackSize;
        }

        if (stack.getMetadata() == 5 || stack.getMetadata() == 16) {
            return ConfigBLAdditions.configGeneral.StackSizeIngots;
        }

        return this.maxStackSize;
    }
}
