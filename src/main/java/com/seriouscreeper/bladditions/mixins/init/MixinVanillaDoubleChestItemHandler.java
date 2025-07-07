package com.seriouscreeper.bladditions.mixins.init;

import coolsquid.misctweaks.config.ConfigManager;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.VanillaDoubleChestItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mixin(value = VanillaDoubleChestItemHandler.class, remap = false)
public class MixinVanillaDoubleChestItemHandler {

    @Shadow
    @Nullable
    public TileEntityChest getChest(boolean accessingUpper)
    {
        return null;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getSlots()
    {
        return ConfigManager.chestSize * 2;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    @Nonnull
    public ItemStack getStackInSlot(int slot)
    {
        boolean accessingUpperChest = slot < ConfigManager.chestSize;
        int targetSlot = accessingUpperChest ? slot : slot - ConfigManager.chestSize;
        TileEntityChest chest = getChest(accessingUpperChest);
        return chest != null ? chest.getStackInSlot(targetSlot) : ItemStack.EMPTY;
    }


    /**
     * @author
     * @reason
     */
    @Overwrite
    public void setStackInSlot(int slot, @Nonnull ItemStack stack)
    {
        boolean accessingUpperChest = slot < ConfigManager.chestSize;
        int targetSlot = accessingUpperChest ? slot : slot - ConfigManager.chestSize;
        TileEntityChest chest = getChest(accessingUpperChest);
        if (chest != null)
        {
            IItemHandler singleHandler = chest.getSingleChestHandler();
            if (singleHandler instanceof IItemHandlerModifiable)
            {
                ((IItemHandlerModifiable) singleHandler).setStackInSlot(targetSlot, stack);
            }
        }

        chest = getChest(!accessingUpperChest);
        if (chest != null)
            chest.markDirty();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        boolean accessingUpperChest = slot < ConfigManager.chestSize;
        int targetSlot = accessingUpperChest ? slot : slot - ConfigManager.chestSize;
        TileEntityChest chest = getChest(accessingUpperChest);
        if (chest == null)
            return stack;

        int starting = stack.getCount();
        ItemStack ret = chest.getSingleChestHandler().insertItem(targetSlot, stack, simulate);
        if (ret.getCount() != starting && !simulate)
        {
            chest = getChest(!accessingUpperChest);
            if (chest != null)
                chest.markDirty();
        }

        return ret;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        boolean accessingUpperChest = slot < ConfigManager.chestSize;
        int targetSlot = accessingUpperChest ? slot : slot - ConfigManager.chestSize;
        TileEntityChest chest = getChest(accessingUpperChest);
        if (chest == null)
            return ItemStack.EMPTY;

        ItemStack ret = chest.getSingleChestHandler().extractItem(targetSlot, amount, simulate);
        if (!ret.isEmpty() && !simulate)
        {
            chest = getChest(!accessingUpperChest);
            if (chest != null)
                chest.markDirty();
        }

        return ret;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getSlotLimit(int slot)
    {
        boolean accessingUpperChest = slot < ConfigManager.chestSize;
        return getChest(accessingUpperChest).getInventoryStackLimit();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
    {
        boolean accessingUpperChest = slot < ConfigManager.chestSize;
        int targetSlot = accessingUpperChest ? slot : slot - ConfigManager.chestSize;
        TileEntityChest chest = getChest(accessingUpperChest);
        if (chest != null)
        {
            return chest.getSingleChestHandler().isItemValid(targetSlot, stack);
        }
        return true;
    }
}
