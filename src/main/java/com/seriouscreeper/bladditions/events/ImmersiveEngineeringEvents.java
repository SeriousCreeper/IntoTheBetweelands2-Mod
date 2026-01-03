package com.seriouscreeper.bladditions.events;

import blusunrize.immersiveengineering.common.blocks.wooden.BlockWoodenDevice0;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class ImmersiveEngineeringEvents {
    @SubscribeEvent
    public void crateHarvest(BlockEvent.HarvestDropsEvent event) {
        IBlockState state = event.getState();
        if (state.getBlock() instanceof BlockWoodenDevice0) {
            List<ItemStack> drops = event.getDrops();
            List<ItemStack> inventory = new ArrayList<>();

            for(ItemStack stack : event.getDrops()) {
                if (stack.getItem() instanceof ItemBlock && ((ItemBlock)stack.getItem()).getBlock() instanceof BlockWoodenDevice0 && stack.hasTagCompound() && stack.getTagCompound().hasKey("inventory", 9)) {
                    NBTTagList invTagList = stack.getTagCompound().getTagList("inventory", 10);
                    inventory.addAll(Utils.readInventory(invTagList, 27));
                    stack.getTagCompound().removeTag("inventory");
                    if (stack.getTagCompound().isEmpty()) {
                        stack.setTagCompound(null);
                    }
                }
            }

            drops.addAll(inventory);
        }
    }
}
