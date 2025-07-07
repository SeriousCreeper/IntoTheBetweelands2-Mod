package com.seriouscreeper.bladditions.mixins.modsupport.asetherworks;

import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import v0id.aw.common.item.AWResource;

@Mixin(value = AWResource.class, remap = false)
public class MixinAWResource extends Item {
    @Override
    public int getItemStackLimit(ItemStack stack) {
        if(stack.isEmpty()) {
            return this.maxStackSize;
        }

        if (stack.getMetadata() == 4) {
            return ConfigBLAdditions.configGeneral.StackSizeIngots;
        }

        return this.maxStackSize;
    }
}
