package com.seriouscreeper.bladditions.mixins.modsupport.immersiveengineering;

import blusunrize.immersiveengineering.common.items.ItemIEBase;
import blusunrize.immersiveengineering.common.items.ItemMaterial;
import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ItemMaterial.class, remap = false)
public class MixinItemMaterial extends ItemIEBase {
    public MixinItemMaterial(String name, int stackSize, String... subNames) {
        super(name, stackSize, subNames);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        if(stack.isEmpty()) {
            return this.maxStackSize;
        }

        if (stack.getMetadata() == 19) {
            return ConfigBLAdditions.configGeneral.StackSizeIngots;
        }

        return this.maxStackSize;
    }
}
