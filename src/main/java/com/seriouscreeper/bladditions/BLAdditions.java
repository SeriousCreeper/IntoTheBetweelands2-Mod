package com.seriouscreeper.bladditions;

import com.seriouscreeper.bladditions.commands.BLAdditionsCommands;
import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import com.seriouscreeper.bladditions.events.BLAdditionsEventHandler;
import com.seriouscreeper.bladditions.proxy.CommonProxy;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import thebetweenlands.common.block.farming.BlockGenericDugSoil;
import thebetweenlands.common.entity.projectiles.EntityAngryPebble;
import thebetweenlands.common.entity.projectiles.EntityBLArrow;
import thebetweenlands.common.item.misc.ItemOctineIngot;
import thebetweenlands.common.item.tools.bow.EnumArrowType;
import thebetweenlands.common.registries.ItemRegistry;
import thebetweenlands.common.registries.SoundRegistry;
import thebetweenlands.common.tile.TileEntityDugSoil;

@Mod(modid = BLAdditions.MODID, name = BLAdditions.NAME, version = BLAdditions.VERSION, dependencies = "after:arcanearchives;after:sanity;required-after:immersiveengineering;after:toughasnails;after:teastory;required-after:grue;required-after:antiqueatlas;required-after:gamestages;required-after:growthcraft;required-after:mysticalmechanics;required-after:embers;required-after:fairylights;required-after:pyrotech;required-after:pizzacraft;required-after:crafttweaker;required-after:deliverymerchants;required-after:thebetweenlands;required-after:roots;required-after:thaumcraft;required-after:thaumicperiphery")
public class BLAdditions
{
    public static final String MODID = "bladditions";
    public static final String NAME = "BL Additions";
    public static final String VERSION = "1.5.3";

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
