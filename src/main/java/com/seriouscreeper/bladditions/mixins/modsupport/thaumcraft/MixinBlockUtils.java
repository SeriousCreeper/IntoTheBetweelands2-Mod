package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import com.seriouscreeper.bladditions.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.common.lib.utils.BlockUtils;

@Mixin(value = BlockUtils.class, remap = false)
public class MixinBlockUtils {
    @Inject(method = "harvestBlock(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/math/BlockPos;ZZIZ)Z", at = @At("HEAD"), cancellable = true)
    private static void injectHarvestBlock(World world, EntityPlayer p, BlockPos pos, boolean alwaysDrop, boolean silkOverride, int fortuneOverride, boolean skipEvent, CallbackInfoReturnable<Boolean> cir) {
        if(CommonProxy.IsWithinLocation(world, pos)) {
            cir.cancel();
        }
    }
}
