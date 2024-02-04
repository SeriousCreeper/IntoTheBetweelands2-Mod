package com.seriouscreeper.bladditions.mixins;

import epicsquid.mysticallib.item.ItemFoodBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import thebetweenlands.api.item.IFoodSicknessItem;

import javax.annotation.Nullable;

@Mixin(value = ItemFoodBase.class)
public class MixinItemFoodBase implements IFoodSicknessItem {
    @Override
    public boolean canGetSickOf(@Nullable EntityPlayer player, ItemStack stack) {
        if(stack.getItem().getRegistryName().toString().equals("roots:cooked_pereskia")) {
            return true;
        }

        return false;
    }
}
