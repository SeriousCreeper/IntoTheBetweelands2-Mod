package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thaumcraft.common.world.ThaumcraftWorldGenerator;

import java.util.Random;

@Mixin(value = ThaumcraftWorldGenerator.class, remap = false)
public class MixinThaumcraftWorldGenerator {
    /**
     * @author SC
     */
    @Overwrite
    public static boolean generateGreatwood(World world, Random random, int chunkX, int chunkZ) {
        return false;
    }

    /**
     * @author SC
     */
    @Overwrite
    public static boolean generateSilverwood(World world, Random random, int chunkX, int chunkZ) {
        return false;
    }
}
