package com.seriouscreeper.bladditions.mixins.modsupport.growthcraft;

import com.seriouscreeper.bladditions.items.PatchedItemHoneyJar;
import growthcraft.bees.common.Init;
import growthcraft.bees.common.items.*;
import growthcraft.bees.shared.init.GrowthcraftBeesItems;
import growthcraft.cellar.shared.item.ItemBoozeBottle;
import growthcraft.core.shared.definition.ItemDefinition;
import growthcraft.core.shared.definition.ItemTypeDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = Init.class, remap = false)
public class MixinInitBees {
    /**
     * @author SC
     */
    @Overwrite
    public static void preInitItems() {
        GrowthcraftBeesItems.honeyCombEmpty = new ItemDefinition(new ItemHoneyCombEmpty("honey_comb_empty"));
        GrowthcraftBeesItems.honeyCombFilled = new ItemDefinition(new ItemHoneyCombFilled("honey_comb_filled"));
        GrowthcraftBeesItems.honeyJar = new ItemDefinition(new PatchedItemHoneyJar("honey_jar"));
        GrowthcraftBeesItems.bee = new ItemDefinition(new ItemBee("bee"));
        GrowthcraftBeesItems.beesWax = new ItemDefinition(new ItemBeesWax("bees_wax"));
        GrowthcraftBeesItems.honeyMeadBottle = new ItemTypeDefinition(new ItemBoozeBottle());
    }
}
