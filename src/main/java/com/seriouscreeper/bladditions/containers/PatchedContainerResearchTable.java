package com.seriouscreeper.bladditions.containers;

import com.seriouscreeper.bladditions.tiles.PatchedTileResearchTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.items.IScribeTools;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;
import thaumcraft.common.container.slot.SlotLimitedByClass;
import thaumcraft.common.container.slot.SlotLimitedByItemstack;
import thaumcraft.common.lib.utils.InventoryUtils;
import thebetweenlands.common.registries.ItemRegistry;

import java.util.HashMap;
import java.util.Iterator;

public class PatchedContainerResearchTable extends Container {
    public PatchedTileResearchTable tileEntity;
    String[] aspects;
    EntityPlayer player;
    static HashMap<Integer, Long> antiSpam = new HashMap();

    public PatchedContainerResearchTable(InventoryPlayer iinventory, PatchedTileResearchTable iinventory1) {
        this.player = iinventory.player;
        this.tileEntity = iinventory1;
        this.aspects = (String[]) Aspect.aspects.keySet().toArray(new String[0]);
        this.addSlotToContainer(new SlotLimitedByClass(IScribeTools.class, iinventory1, 0, 16, 15));
        this.addSlotToContainer(new SlotLimitedByItemstack(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 32), iinventory1, 1, 224, 16));
        this.bindPlayerInventory(iinventory);
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
        int i;
        int j;
        for(i = 0; i < 3; ++i) {
            for(j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 77 + j * 18, 190 + i * 18));
            }
        }

        for(i = 0; i < 3; ++i) {
            for(j = 0; j < 3; ++j) {
                this.addSlotToContainer(new Slot(inventoryPlayer, i + j * 3, 20 + i * 18, 190 + j * 18));
            }
        }

    }

    public boolean enchantItem(EntityPlayer playerIn, int button) {
        if (button == 1) {
            if (this.tileEntity.data.lastDraw != null) {
                this.tileEntity.data.savedCards.add(this.tileEntity.data.lastDraw.card.getSeed());
            }

            Iterator var13 = this.tileEntity.data.cardChoices.iterator();

            while(var13.hasNext()) {
                ResearchTableData.CardChoice cc = (ResearchTableData.CardChoice)var13.next();
                if (cc.selected) {
                    this.tileEntity.data.lastDraw = cc;
                    break;
                }
            }

            this.tileEntity.data.cardChoices.clear();
            this.tileEntity.syncTile(false);
            return true;
        } else {
            if (button == 4 || button == 5 || button == 6) {
                long tn = System.currentTimeMillis();
                long to = 0L;
                if (antiSpam.containsKey(playerIn.getEntityId())) {
                    to = (Long)antiSpam.get(playerIn.getEntityId());
                }

                if (tn - to < 333L) {
                    return false;
                }

                antiSpam.put(playerIn.getEntityId(), tn);

                try {
                    TheorycraftCard card = ((ResearchTableData.CardChoice)this.tileEntity.data.cardChoices.get(button - 4)).card;
                    if (card.getRequiredItems() != null) {
                        ItemStack[] var8 = card.getRequiredItems();
                        int var9 = var8.length;

                        for(int var10 = 0; var10 < var9; ++var10) {
                            ItemStack stack = var8[var10];
                            if (stack != null && !stack.isEmpty() && !InventoryUtils.isPlayerCarryingAmount(this.player, stack, true)) {
                                return false;
                            }
                        }

                        if (card.getRequiredItemsConsumed() != null && card.getRequiredItemsConsumed().length == card.getRequiredItems().length) {
                            for(int a = 0; a < card.getRequiredItems().length; ++a) {
                                if (card.getRequiredItemsConsumed()[a] && card.getRequiredItems()[a] != null && !card.getRequiredItems()[a].isEmpty()) {
                                    InventoryUtils.consumePlayerItem(this.player, card.getRequiredItems()[a], true, true);
                                }
                            }
                        }
                    }

                    if (card.activate(playerIn, this.tileEntity.data)) {
                        this.tileEntity.consumeInkFromTable();
                        ((ResearchTableData.CardChoice)this.tileEntity.data.cardChoices.get(button - 4)).selected = true;
                        this.tileEntity.data.addInspiration(-card.getInspirationCost());
                        this.tileEntity.syncTile(false);
                        return true;
                    }
                } catch (Exception var12) {
                    var12.printStackTrace();
                }
            }

            if (button == 7 && this.tileEntity.data.isComplete()) {
                this.tileEntity.finishTheory(playerIn);
                this.tileEntity.syncTile(false);
                return true;
            } else if (button == 9 && !this.tileEntity.data.isComplete()) {
                this.tileEntity.data = null;
                this.tileEntity.syncTile(false);
                return true;
            } else if (button != 2 && button != 3) {
                return false;
            } else {
                if (this.tileEntity.data != null && !this.tileEntity.data.isComplete() && this.tileEntity.consumepaperFromTable()) {
                    this.tileEntity.data.drawCards(button, playerIn);
                    this.tileEntity.syncTile(false);
                }

                return true;
            }
        }
    }

    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slot) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slotObject = (Slot)this.inventorySlots.get(slot);
        if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();
            if (slot < 2) {
                if (!this.tileEntity.isItemValidForSlot(slot, stackInSlot) || !this.mergeItemStack(stackInSlot, 2, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.tileEntity.isItemValidForSlot(slot, stackInSlot) || !this.mergeItemStack(stackInSlot, 0, 2, false)) {
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

    public boolean canInteractWith(EntityPlayer player) {
        return this.tileEntity.isUsableByPlayer(player);
    }
}
