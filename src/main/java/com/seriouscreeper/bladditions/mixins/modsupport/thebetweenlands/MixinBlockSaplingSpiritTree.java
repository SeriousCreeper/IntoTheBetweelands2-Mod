package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.spongepowered.asm.mixin.Mixin;
import thebetweenlands.common.block.plant.BlockSaplingBetweenlands;
import thebetweenlands.common.block.plant.BlockSaplingSpiritTree;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.world.gen.biome.decorator.SurfaceType;

@Mixin(value = BlockSaplingSpiritTree.class)
public class MixinBlockSaplingSpiritTree extends BlockSaplingBetweenlands {
    public MixinBlockSaplingSpiritTree(WorldGenerator gen) {
        super(gen);
    }

    @Override
    protected boolean canSustainBush(IBlockState state) {
        if(BlockMatcher.forBlock(BlockRegistry.LOG_SPIRIT_TREE).test(state)) {
            return true;
        }

        return SurfaceType.PLANT_DECORATION_SOIL.matches(state) || super.canSustainBush(state);
    }
}
