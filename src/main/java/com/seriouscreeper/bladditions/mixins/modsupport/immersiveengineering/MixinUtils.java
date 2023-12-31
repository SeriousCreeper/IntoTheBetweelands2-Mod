package com.seriouscreeper.bladditions.mixins.modsupport.immersiveengineering;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import teamroots.embers.RegistryManager;

@Mixin(value = Utils.class, remap = false)
public class MixinUtils {
    @Inject(method = "isBlockAt", at = @At("HEAD"), cancellable = true)
    private static void injectIsBlockAt(World world, BlockPos pos, Block b, int meta, CallbackInfoReturnable<Boolean> cir) {
    }
}
