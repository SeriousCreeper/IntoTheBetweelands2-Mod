package com.seriouscreeper.bladditions.mixins.modsupport.botania;

import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import thebetweenlands.common.registries.BlockRegistry;
import vazkii.botania.api.subtile.SubTileGenerating;

@Mixin(value = SubTileGenerating.class)
public class MixinSubTileGenerating {
    @ModifyArg(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;)Z"), index = 1)
    private IBlockState modifyDeadBush(IBlockState state) {
        return BlockRegistry.DEAD_WEEDWOOD_BUSH.getDefaultState();
    }
}
