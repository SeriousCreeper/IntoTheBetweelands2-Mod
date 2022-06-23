package com.seriouscreeper.bladditions.items;

import growthcraft.bees.shared.Reference;
import growthcraft.core.shared.item.GrowthcraftItemFoodBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thebetweenlands.common.registries.BlockRegistry;

public class PatchedItemHoneyJar extends GrowthcraftItemFoodBase {
    public PatchedItemHoneyJar(String unlocalizedName) {
        super(6, false);
        this.setContainerItem(new ItemStack(BlockRegistry.MUD_FLOWER_POT).getItem());
        this.setMaxStackSize(1);
        this.setRegistryName(Reference.MODID, unlocalizedName);
        this.setTranslationKey(unlocalizedName);
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        if (stack.getCount() != 0)
            player.inventory.addItemStackToInventory(getContainerItem(stack));
    }
}
