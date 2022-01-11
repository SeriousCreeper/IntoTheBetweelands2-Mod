package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import com.seriouscreeper.bladditions.proxy.CommonProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.tile.TileEntityAbstractBLFurnace;
import thebetweenlands.common.tile.TileEntityBasicInventory;

import java.util.Map;

@Mixin(value = TileEntityAbstractBLFurnace.class, remap = false)
public class MixinBLFurnace extends TileEntityBasicInventory {
    public MixinBLFurnace(int invSize, String name) {
        super(invSize, name);
    }

    /**
     * @author SC
     */
    @Overwrite
    private void smeltItem(TileEntityAbstractBLFurnace.FurnaceData data) {
        ItemStack inputStack = this.getStackInSlot(data.getInputSlot());

        if (this.canSmelt(data) && !isFluxableItem(inputStack)) {
            ItemStack smeltingResult = FurnaceRecipes.instance().getSmeltingResult(inputStack);
            ItemStack outputStack = this.getStackInSlot(data.getOutputSlot());
            if (outputStack.isEmpty()) {
                this.setInventorySlotContents(data.getOutputSlot(), smeltingResult.copy());
            } else if (outputStack.getItem() == smeltingResult.getItem()) {
                outputStack.grow(smeltingResult.getCount());
            }

            inputStack.shrink(1);
            if (inputStack.getCount() <= 0) {
                this.setInventorySlotContents(data.getInputSlot(), ItemStack.EMPTY);
            }
        } else if(isFluxableItem(inputStack)) { // TODO: probably have to use a method to compare items and meta instead? in case it also considers stack size
            ItemStack fluxStack = this.getStackInSlot(data.getFluxSlot());

            if (fluxStack.isEmpty()) {
                return;
            }

            ItemStack smeltingResult = getFluxableItem(inputStack);
            ItemStack outputStack = this.getStackInSlot(data.getOutputSlot());

            if (outputStack.isEmpty()) {
                this.setInventorySlotContents(data.getOutputSlot(), smeltingResult.copy());
            } else if (outputStack.getItem() == smeltingResult.getItem()) {
                outputStack.grow(smeltingResult.getCount());
            }

            fluxStack.shrink(1);
            inputStack.shrink(1);

            if (inputStack.getCount() <= 0) {
                this.setInventorySlotContents(data.getInputSlot(), ItemStack.EMPTY);
            }
        }
    }

    /**
     * @author SC
     */
    @Overwrite
    private boolean canSmelt(TileEntityAbstractBLFurnace.FurnaceData data) {
        ItemStack inputStack = this.getStackInSlot(data.getInputSlot());
        if (inputStack.isEmpty()) {
            return false;
        } else {
            if(isFluxableItem(inputStack)) {
                ItemStack fluxStack = this.getStackInSlot(data.getFluxSlot());

                if (fluxStack.isEmpty()) {
                    return false;
                }

                ItemStack smeltingResult = getFluxableItem(inputStack);

                ItemStack outputStack = this.getStackInSlot(data.getOutputSlot());
                if (outputStack.isEmpty()) {
                    return true;
                } else if (!outputStack.isItemEqual(smeltingResult)) {
                    return false;
                } else {
                    int result = outputStack.getCount() + smeltingResult.getCount();
                    return result <= this.getInventoryStackLimit() && result <= outputStack.getMaxStackSize();
                }
            }

            ItemStack smeltingResult = FurnaceRecipes.instance().getSmeltingResult(inputStack);
            if (smeltingResult.isEmpty()) {
                return false;
            } else {
                ItemStack outputStack = this.getStackInSlot(data.getOutputSlot());
                if (outputStack.isEmpty()) {
                    return true;
                } else if (!outputStack.isItemEqual(smeltingResult)) {
                    return false;
                } else {
                    int result = outputStack.getCount() + smeltingResult.getCount();
                    return result <= this.getInventoryStackLimit() && result <= outputStack.getMaxStackSize();
                }
            }
        }
    }


    private ItemStack getFluxableItem(ItemStack input) {
        if(input == ItemStack.EMPTY)
            return ItemStack.EMPTY;

        for(Map.Entry<ItemStack, ItemStack> entry : CommonProxy.FLUXABLE_ITEMS.entrySet()) {
            if(entry.getKey().getItem() == input.getItem() && entry.getKey().getItemDamage() == input.getItemDamage()) {
                return entry.getValue();
            }
        }

        return ItemStack.EMPTY;
    }


    private boolean isFluxableItem(ItemStack input) {
        if(input == ItemStack.EMPTY)
            return false;

        for(ItemStack stack : CommonProxy.FLUXABLE_ITEMS.keySet()) {
            if(stack.getItem() == input.getItem() && stack.getItemDamage() == input.getItemDamage()) {
                return true;
            }
        }

        return false;
    }
}
