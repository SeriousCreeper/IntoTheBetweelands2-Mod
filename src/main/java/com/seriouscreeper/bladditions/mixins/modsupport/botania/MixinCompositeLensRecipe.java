package com.seriouscreeper.bladditions.mixins.modsupport.botania;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.registries.ItemRegistry;
import vazkii.botania.api.mana.ICompositableLens;
import vazkii.botania.common.crafting.recipe.CompositeLensRecipe;

import javax.annotation.Nonnull;

@Mixin(value = CompositeLensRecipe.class, remap = false)
public class MixinCompositeLensRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    /**
     * @author SC
     * @reason
     */
    @Overwrite
    public boolean matches(@Nonnull InventoryCrafting var1, @Nonnull World var2) {
        boolean foundLens = false;
        boolean foundSecondLens = false;
        boolean foundSlimeball = false;

        for(int i = 0; i < var1.getSizeInventory(); ++i) {
            ItemStack stack = var1.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ICompositableLens && !foundSecondLens) {
                    if (foundLens) {
                        foundSecondLens = true;
                    } else {
                        foundLens = true;
                    }
                } else {
                    if (stack.getItem() != ItemRegistry.SLUDGE_BALL && stack.getItem() != ItemRegistry.SAP_SPIT) {
                        return false;
                    }

                    foundSlimeball = true;
                }
            }
        }

        return foundSecondLens && foundSlimeball;
    }

    @Shadow
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        return null;
    }

    @Shadow
    public boolean canFit(int i, int i1) {
        return false;
    }

    @Shadow
    public ItemStack getRecipeOutput() {
        return null;
    }
}
