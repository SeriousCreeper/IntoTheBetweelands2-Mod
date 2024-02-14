package com.seriouscreeper.bladditions.mixins.modsupport.mysticallib;

import com.seriouscreeper.bladditions.proxy.CommonProxy;
import epicsquid.mysticallib.util.BreakUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thebetweenlands.common.registries.BlockRegistry;

@Mixin(value = BreakUtil.class, remap = false)
public class MixinBreakUtil {
    @Inject(method = "harvestBlock", at = @At("HEAD"), cancellable = true)
    private static void injectHarvestBlock(World world, BlockPos pos, EntityPlayer player, CallbackInfoReturnable<Boolean> cir) {
        if(CommonProxy.IsWithinLocation(world, pos)) {
            cir.setReturnValue(false);
        }

        if(world.getBlockState(pos).getBlock() == BlockRegistry.BETWEENLANDS_BEDROCK || world.getBlockState(pos).getBlock() == Blocks.BEDROCK) {
            cir.setReturnValue(false);
        }
    }
}
