package com.seriouscreeper.bladditions.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thebetweenlands.common.world.gen.feature.structure.WorldGenWightFortress;

import java.util.Random;

public class WorldGenWightFortressCustom extends WorldGenWightFortress {
    @Override
    public boolean generateStructure(World world, Random rand, BlockPos pos) {
        return super.generateStructure(world, rand, pos);
    }
}
