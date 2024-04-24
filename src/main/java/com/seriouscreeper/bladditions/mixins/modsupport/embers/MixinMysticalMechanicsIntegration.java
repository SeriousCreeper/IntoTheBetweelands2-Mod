package com.seriouscreeper.bladditions.mixins.modsupport.embers;

import com.seriouscreeper.bladditions.tiles.PatchedTileEntitySteamEngine;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import teamroots.embers.compat.MysticalMechanicsIntegration;
import teamroots.embers.config.ConfigMaterial;
import teamroots.embers.register.BlockRegister;
import teamroots.embers.register.ItemRegister;
import teamroots.embers.register.TileEntityRegister;
import teamroots.embers.tileentity.TileEntityMechActuator;
import teamroots.embers.tileentity.TileEntityMechActuatorSingle;

@Mixin(value = MysticalMechanicsIntegration.class, remap = false)
public class MixinMysticalMechanicsIntegration {
    @Final
    @Shadow public static Item GEAR_DAWNSTONE;
    @Final
    @Shadow public static Block STEAM_ENGINE;
    @Final
    @Shadow public static Block MECH_ACTUATOR;
    @Final
    @Shadow public static Block MECH_ACTUATOR_SINGLE;

    /**
     * @author SC
     */
    @Overwrite
    public static void registerAll() {
        if (ConfigMaterial.DAWNSTONE.isNotOff()) {
            ItemRegister.INSTANCE.add(GEAR_DAWNSTONE);
        }

        BlockRegister.INSTANCE.add(STEAM_ENGINE);
        BlockRegister.INSTANCE.add(MECH_ACTUATOR);
        BlockRegister.INSTANCE.add(MECH_ACTUATOR_SINGLE);
        TileEntityRegister.register(PatchedTileEntitySteamEngine.class, "tile_entity_steam_engine");
        TileEntityRegister.register(TileEntityMechActuator.class, "tile_entity_mech_actuator");
        TileEntityRegister.register(TileEntityMechActuatorSingle.class, "tile_entity_mech_actuator_single");
    }
}
