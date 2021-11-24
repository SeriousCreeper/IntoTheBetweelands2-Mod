package com.seriouscreeper.bladditions.mixins.modsupport.embers;

import com.seriouscreeper.bladditions.tiles.PatchedTileEntitySteamEngine;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;
import teamroots.embers.block.BlockMechActuator;
import teamroots.embers.block.BlockMechActuatorSingle;
import teamroots.embers.block.BlockSteamEngine;
import teamroots.embers.compat.MysticalMechanicsIntegration;
import teamroots.embers.item.ItemBase;
import teamroots.embers.tileentity.TileEntityMechActuator;
import teamroots.embers.tileentity.TileEntityMechActuatorSingle;
import teamroots.embers.tileentity.TileEntitySteamEngine;

@Mixin(value = MysticalMechanicsIntegration.class, remap = false)
public class MixinMysticalMechanicsIntegration {
    @Shadow public static Item gear_dawnstone;
    @Shadow public static Block steam_engine;
    @Shadow public static Block mech_actuator;
    @Shadow public static Block mech_actuator_single;

    /**
     * @author SC
     */
    @Overwrite
    public static void registerAll()
    {
        RegistryManager.blocks.add(steam_engine = (new BlockSteamEngine(Material.ROCK,"steam_engine",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
        RegistryManager.blocks.add(mech_actuator = (new BlockMechActuator(Material.ROCK,"mech_actuator",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
        RegistryManager.blocks.add(mech_actuator_single = (new BlockMechActuatorSingle(Material.ROCK,"mech_actuator_single",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));

        RegistryManager.items.add(gear_dawnstone = new ItemBase("gear_dawnstone",true));

        System.out.println("register patched");

        GameRegistry.registerTileEntity(PatchedTileEntitySteamEngine.class, Embers.MODID+":tile_entity_steam_engine");
        GameRegistry.registerTileEntity(TileEntityMechActuator.class, Embers.MODID+":tile_entity_mech_actuator");
        GameRegistry.registerTileEntity(TileEntityMechActuatorSingle.class, Embers.MODID+":tile_entity_mech_actuator_single");
    }
}
