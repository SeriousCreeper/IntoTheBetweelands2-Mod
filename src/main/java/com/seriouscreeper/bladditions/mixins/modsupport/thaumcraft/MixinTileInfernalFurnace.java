package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import com.seriouscreeper.bladditions.util.CustomInfernalFurnaceRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import thaumcraft.common.tiles.devices.TileInfernalFurnace;

@Mixin(value = TileInfernalFurnace.class)
public class MixinTileInfernalFurnace {
    @Redirect(
            method = "canSmelt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/crafting/FurnaceRecipes;getSmeltingResult(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"
            )
    )
    private ItemStack redirectGetSmeltingResult(FurnaceRecipes instance, ItemStack stack) {
        ItemStack returnStack = CustomInfernalFurnaceRecipes.instance().getSmeltingResult(stack);

        if(!returnStack.isEmpty()) {
            System.out.println(returnStack.getItem().getRegistryName());
        }

        return CustomInfernalFurnaceRecipes.instance().getSmeltingResult(stack);
    }

    @Redirect(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/crafting/FurnaceRecipes;getSmeltingResult(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"
            )
    )
    private ItemStack redirectGetSmeltingResultUpdate(FurnaceRecipes instance, ItemStack stack) {
        ItemStack returnStack = CustomInfernalFurnaceRecipes.instance().getSmeltingResult(stack);

        if(!returnStack.isEmpty()) {
            System.out.println(returnStack.getItem().getRegistryName());
        }

        return CustomInfernalFurnaceRecipes.instance().getSmeltingResult(stack);
    }
}
