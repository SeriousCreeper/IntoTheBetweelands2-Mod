package com.seriouscreeper.bladditions.mixins.modsupport.reccomplex;

import ivorius.reccomplex.world.gen.feature.structure.context.StructureSpawnContext;
import ivorius.reccomplex.world.gen.feature.structure.generic.transformers.Transformer;
import ivorius.reccomplex.world.gen.feature.structure.generic.transformers.TransformerNatural;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import thebetweenlands.common.registries.BlockRegistry;

@Mixin(value = TransformerNatural.class, remap = false)
public class MixinTransformerNatural {
    /*
    @ModifyVariable(method = "transformBlock(Livorius/reccomplex/world/gen/feature/structure/generic/transformers/TransformerNatural$InstanceData;Livorius/reccomplex/world/gen/feature/structure/generic/transformers/Transformer$Phase;Livorius/reccomplex/world/gen/feature/structure/context/StructureSpawnContext;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;D)V", at = @At("STORE"), ordinal = 2)
    private IBlockState redirectBlockState(IBlockState value) {
        return BlockRegistry.BETWEENSTONE.getDefaultState();
    }
     */

    /**
     * @author SC
     * @reason
     */
    @Overwrite
    public void transformBlock(TransformerNatural.InstanceData instanceData, Transformer.Phase phase, StructureSpawnContext context, BlockPos sourcePos, BlockPos pos, IBlockState sourceState, double density) {
        World world = context.environment.world;
        Biome biome = context.environment.biome;
        IBlockState topBlock = biome.topBlock != null ? biome.topBlock : Blocks.AIR.getDefaultState();
        IBlockState fillerBlock = biome.fillerBlock != null ? biome.fillerBlock : Blocks.AIR.getDefaultState();
        IBlockState mainBlock;

        if(biome.getRegistryName().getNamespace().equals("thebetweenlands")) {
            if(pos.getY() <= 45) {
                mainBlock = BlockRegistry.PITSTONE.getDefaultState();
            } else {
                mainBlock = BlockRegistry.BETWEENSTONE.getDefaultState();
            }
        } else {
            mainBlock = topBlock;
        }

        boolean useStoneBlock = pos.getY() < world.getSeaLevel() - 3;
        IBlockState setBlock = useStoneBlock ? mainBlock : (instanceData.cloud.containsKey(sourcePos.up()) ? fillerBlock : topBlock);
        if (world.provider.getDimension() == -1) {
            setBlock = Blocks.NETHERRACK.getDefaultState();
        } else if (world.provider.getDimension() == 1) {
            setBlock = Blocks.END_STONE.getDefaultState();
        }

        context.setBlock(pos, setBlock, 2);
    }
}
