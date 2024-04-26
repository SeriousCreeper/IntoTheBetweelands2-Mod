package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.common.tiles.TileThaumcraft;
import thaumcraft.common.tiles.devices.TileLampGrowth;

@Mixin(value = TileLampGrowth.class, remap = false)
public class MixinTileLampGrowth extends TileThaumcraft {
    @Inject(method = "isPlant", at = @At("HEAD"), cancellable = true)
    private void injectIsPlant(BlockPos bp, CallbackInfoReturnable<Boolean> cir) {
        IBlockState b = this.world.getBlockState(bp);

        if(b.getBlock() == ModBlocks.cloud_berry || b.getBlock() == ModBlocks.infernal_bulb || b.getBlock() == ModBlocks.stalicripe || b.getBlock() == ModBlocks.dewgonia) {
            cir.setReturnValue(false);
        }
    }
}
