package com.seriouscreeper.bladditions.init;

import com.seriouscreeper.bladditions.blocks.BlockItemShelf;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {
    @GameRegistry.ObjectHolder("bladditions:item_shelf")
    public static BlockItemShelf blockShelf;

    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
        //blockShelf.initModel();
    }
}
