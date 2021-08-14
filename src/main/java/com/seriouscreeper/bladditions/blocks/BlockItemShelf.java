package com.seriouscreeper.bladditions.blocks;

import com.seriouscreeper.bladditions.BLAdditions;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockItemShelf extends thebetweenlands.common.block.container.BlockItemShelf {
    public BlockItemShelf() {
        super();
        this.setTranslationKey(BLAdditions.MODID + ".item_shelf");
        setRegistryName("item_shelf");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
