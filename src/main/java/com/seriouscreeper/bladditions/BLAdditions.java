package com.seriouscreeper.bladditions;

import com.seriouscreeper.bladditions.commands.BLAdditionsCommands;
import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import com.seriouscreeper.bladditions.events.BLAdditionsEventHandler;
import com.seriouscreeper.bladditions.proxy.CommonProxy;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = BLAdditions.MODID, name = BLAdditions.NAME, version = BLAdditions.VERSION, dependencies = "after:arcaneworld;after:botania;after:arcanearchives;after:sanity;after:toughasnails;after:teastory;required-after:grue;required-after:antiqueatlas;required-after:gamestages;required-after:growthcraft;required-after:mysticalmechanics;required-after:embers;required-after:fairylights;required-after:pyrotech;required-after:pizzacraft;required-after:crafttweaker;required-after:deliverymerchants;required-after:thebetweenlands;required-after:roots;required-after:thaumcraft;required-after:thaumicperiphery")
public class BLAdditions
{
    public static final String MODID = "bladditions";
    public static final String NAME = "BL Additions";
    public static final String VERSION = "1.5.8";

    public static final ResourceLocation ANCIENT_ARMOR_CHEST = registerLootTable("loot/ancient_armor_chest");



    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.Instance
    public static BLAdditions instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);

        MinecraftForge.EVENT_BUS.register(new BLAdditionsEventHandler());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new BLAdditionsCommands());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);

        ConfigBLAdditions.parseFluxItems();
    }


    private static ResourceLocation registerLootTable(String id) {
        return LootTableList.register(new ResourceLocation(BLAdditions.MODID, id));
    }
}
