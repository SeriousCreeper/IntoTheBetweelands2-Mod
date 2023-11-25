package com.seriouscreeper.bladditions.mixins.modsupport.simpledifficulty;

import com.charles445.simpledifficulty.api.temperature.ITemperatureModifier;
import com.charles445.simpledifficulty.api.temperature.TemperatureRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = TemperatureRegistry.class, remap = false)
public class MixinTemperatureRegistry {
    /**
     * @author SC
     * @reason
     */
    @Overwrite
    public static void registerModifier(ITemperatureModifier modifier) {
    }
}
