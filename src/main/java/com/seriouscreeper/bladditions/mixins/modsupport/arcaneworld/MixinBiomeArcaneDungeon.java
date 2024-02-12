package com.seriouscreeper.bladditions.mixins.modsupport.arcaneworld;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import party.lemons.arcaneworld.gen.BiomeArcaneDungeon;

@Mixin(value = BiomeArcaneDungeon.class)
public class MixinBiomeArcaneDungeon {
    /**
     * @author
     * @reason
     */
    @SideOnly(Side.CLIENT)
    @Overwrite
    public int getGrassColorAtPos(BlockPos pos) {
        return 249646;
    }

    /**
     * @author
     * @reason
     */
    @SideOnly(Side.CLIENT)
    @Overwrite
    public int getFoliageColorAtPos(BlockPos pos) {
        return 249646;
    }
}
