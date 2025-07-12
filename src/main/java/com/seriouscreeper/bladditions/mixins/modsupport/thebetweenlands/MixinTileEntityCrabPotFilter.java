package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thebetweenlands.common.tile.TileEntityBasicInventory;
import thebetweenlands.common.tile.TileEntityCrabPotFilter;

@Mixin(value = TileEntityCrabPotFilter.class, remap = false)
public class MixinTileEntityCrabPotFilter extends TileEntityBasicInventory {
    @Shadow private boolean active;
    @Shadow protected int maxFilteringTime = 200;
    @Shadow private int itemsToFilterCount = 3;

    public MixinTileEntityCrabPotFilter(int invSize, String name) {
        super(invSize, name);
    }

    @Inject(method = "filterItem", at = @At("HEAD"), cancellable = true)
    public void onFilterItem(int input, int output, CallbackInfo ci) {
        if (this.canFilterSlots(input, output)) {
            ItemStack inputStack = this.getStackInSlot(input);
            ItemStack result = this.getRecipeOutput(inputStack, false, false);
            ItemStack outputStack = this.getStackInSlot(output);

            if (!outputStack.isEmpty() && result.getItem() == outputStack.getItem()) {
                // Use the actual result count instead of 1
                outputStack.grow(result.getCount());
                this.setInventorySlotContents(output, outputStack);
            } else if (outputStack.isEmpty()) {
                this.setInventorySlotContents(output, result.copy());
            }

            this.setSlotProgress(0);
            this.markForUpdate();

            if (this.getBaitProgress() > this.maxFilteringTime * this.itemsToFilterCount) {
                this.setBaitProgress(0);
                this.markForUpdate();
            }

            inputStack.shrink(1);
        }

        ci.cancel();
    }

    /**
     * @author SC
     * @reason check if the output can be increased if recipe output has multiple items
     */
    @Overwrite
    private boolean canFilterSlots(int input, int output) {
        if (this.active && this.hasBait() && !this.getStackInSlot(input).isEmpty() && (this.getStackInSlot(output).isEmpty() || this.getStackInSlot(output).getItem() == this.getRecipeOutput(this.getStackInSlot(input), false, false).getItem())) {
            ItemStack result = this.getRecipeOutput(this.getStackInSlot(input), false, false);
            return !result.isEmpty() && result.getCount() + this.getStackInSlot(output).getCount() <= this.getStackInSlot(output).getMaxStackSize();
        } else {
            return false;
        }
    }

    @Shadow
    public boolean hasBait() {
        return true;
    }

    @Shadow
    public ItemStack getRecipeOutput(ItemStack input, boolean checkAnyIfNoCrabs, boolean checkAny) {
        return ItemStack.EMPTY;
    }

    @Shadow
    private void setSlotProgress(int counter) {
    }

    @Shadow
    public void markForUpdate() {
    }

    @Shadow
    public int getBaitProgress() {
        return 0;
    }

    @Shadow
    public void setBaitProgress(int duration) {
    }
}
