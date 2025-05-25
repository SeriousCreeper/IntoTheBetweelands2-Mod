package com.seriouscreeper.bladditions.init;

import com.seriouscreeper.bladditions.blocks.BlockItemShelf;
import com.seriouscreeper.bladditions.items.ItemAngrierPebble;
import com.seriouscreeper.bladditions.items.ItemCorruptedBoneWayfinder;
import com.seriouscreeper.bladditions.items.ItemImpetusBoneWayfinder;
import com.seriouscreeper.bladditions.items.ItemWaterBowl;
import com.seriouscreeper.bladditions.proxy.CommonProxy;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.common.TheBetweenlands;

public class ModItems {
    @GameRegistry.ObjectHolder("bladditions:angrier_pebble")
    public static ItemAngrierPebble angrierPebble;
    @GameRegistry.ObjectHolder("bladditions:corrupted_bone_wayfinder")
    public static ItemCorruptedBoneWayfinder corrupted_bone_wayfinder;

    @GameRegistry.ObjectHolder("bladditions:impetus_bone_wayfinder")
    public static ItemImpetusBoneWayfinder impetus_bone_wayfinder;


    @SideOnly(Side.CLIENT)
    public static void initModels() {
        angrierPebble.initModel();
        corrupted_bone_wayfinder.initModel();
        impetus_bone_wayfinder.initModel();
        //TheBetweenlands.proxy.registerDefaultItemRenderer(CommonProxy.DENTROTHYST_VIAL);
        //TheBetweenlands.proxy.registerDefaultItemRenderer(CommonProxy.DENTROTHYST_FLUID_VIAL);
    }
}
