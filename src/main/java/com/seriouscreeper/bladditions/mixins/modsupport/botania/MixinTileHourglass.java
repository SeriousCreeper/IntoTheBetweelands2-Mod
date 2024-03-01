package com.seriouscreeper.bladditions.mixins.modsupport.botania;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.registries.BlockRegistry;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.tile.TileHourglass;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

@Mixin(value = TileHourglass.class, remap = false)
public class MixinTileHourglass extends TileSimpleInventory {
    @Shadow private int time = 0;
    @Shadow public float timeFraction = 0.0F;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static int getStackItemTime(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        } else if (stack.getItem() == Item.getItemFromBlock(BlockRegistry.FILTERED_SILT)) {
            return 20;
        } else if (stack.getItem() == Item.getItemFromBlock(BlockRegistry.SILT)) {
            return 100;
        } else if (stack.getItem() == Item.getItemFromBlock(BlockRegistry.MUD)) {
            return 200;
        } else if (stack.getItem() == Item.getItemFromBlock(BlockRegistry.SLUDGY_DIRT)) {
            return 1200;
        } else {
            return stack.getItem() == ModItems.manaResource ? 1 : 0;
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    @SideOnly(Side.CLIENT)
    public int getColor() {
        ItemStack stack = this.itemHandler.getStackInSlot(0);
        if (stack.isEmpty()) {
            return 0;
        } else if (stack.getItem() == Item.getItemFromBlock(BlockRegistry.SILT)) {
            return 4009770;
        } else if (stack.getItem() == Item.getItemFromBlock(BlockRegistry.FILTERED_SILT)) {
            return 5785404;
        } else if (stack.getItem() == Item.getItemFromBlock(BlockRegistry.MUD)) {
            return 3088662;
        } else if (stack.getItem() == Item.getItemFromBlock(BlockRegistry.SLUDGY_DIRT)) {
            return 7101515;
        } else {
            return stack.getItem() == ModItems.manaResource ? 240639 : 0;
        }
    }

    @Shadow
    public int getSizeInventory() {
        return 0;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    protected TileSimpleInventory.SimpleItemStackHandler createItemHandler() {
        return new TileSimpleInventory.SimpleItemStackHandler(this, true) {
            @Nonnull
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                return (stack.isEmpty() || stack.getItem() != Item.getItemFromBlock(BlockRegistry.SILT) && stack.getItem() != Item.getItemFromBlock(BlockRegistry.FILTERED_SILT) && stack.getItem() != Item.getItemFromBlock(BlockRegistry.MUD)&& stack.getItem() != Item.getItemFromBlock(BlockRegistry.SLUDGY_DIRT)) && (stack.getItem() != ModItems.manaResource || stack.getItemDamage() != 23) ? stack : super.insertItem(slot, stack, simulate);
            }

            public void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                if (!world.isRemote) {
                    time = 0;
                    timeFraction = 0.0F;
                    VanillaPacketDispatcher.dispatchTEToNearbyPlayers(MixinTileHourglass.this);
                }
            }
        };
    }
}
