package com.seriouscreeper.bladditions.mixins.modsupport.arcaneworld;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import party.lemons.arcaneworld.gen.dungeon.dimension.DungeonDimensionProvider;

@Mixin(value = DungeonDimensionProvider.class, remap = false)
public class MixinDungeonDimensionProvider extends WorldProvider {
    @Shadow
    public DimensionType getDimensionType() {
        return null;
    }


    /**
     * @author
     * @reason
     */
    @Overwrite
    protected void generateLightBrightnessTable() {
        super.generateLightBrightnessTable();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void getLightmapColors(float partialTicks, float sunBrightness, float skyLight, float blockLight, float[] colors) {
        super.getLightmapColors(partialTicks, sunBrightness, skyLight, blockLight, colors);
    }
}
