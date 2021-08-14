package com.seriouscreeper.bladditions.containers;

import com.seriouscreeper.bladditions.containers.slot.SlotElixir;
import com.seriouscreeper.bladditions.tiles.PatchedTilePotionSprayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class PatchedContainerPotionSprayer extends Container {
    private PatchedTilePotionSprayer sprayer;
    private int lastBreakTime;

    public PatchedContainerPotionSprayer(InventoryPlayer par1InventoryPlayer, PatchedTilePotionSprayer tilePotionSprayer) {
        this.sprayer = tilePotionSprayer;
        this.addSlotToContainer(new SlotElixir(tilePotionSprayer, 0, 56, 64));

        int i;
        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 16 + j * 18, 151 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, i, 16 + i * 18, 209));
        }

    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
        return this.sprayer.isUsableByPlayer(par1EntityPlayer);
    }

    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slot) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slotObject = (Slot)this.inventorySlots.get(slot);
        if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();
            if (slot == 0) {
                if (!this.sprayer.isItemValidForSlot(slot, stackInSlot) || !this.mergeItemStack(stackInSlot, 1, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.sprayer.isItemValidForSlot(slot, stackInSlot) || !this.mergeItemStack(stackInSlot, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.getCount() == 0) {
                slotObject.putStack(ItemStack.EMPTY);
            } else {
                slotObject.onSlotChanged();
            }
        }

        return stack;
    }
}
