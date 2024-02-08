package com.seriouscreeper.bladditions.mixins.modsupport.botania;

import com.seriouscreeper.bladditions.proxy.CommonProxy;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.item.lens.LensPiston;

@Mixin(value = LensPiston.class, remap = false)
public class MixinLensPiston {
    @Inject(method = "collideBurst", at = @At("HEAD"), cancellable = true)
    private void injectColliderBurst(IManaBurst burst, EntityThrowable entity, RayTraceResult rtr, boolean isManaBlock, boolean dead, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        BlockPos collidePos = rtr.getBlockPos();
        World world = entity.world;

        if (!world.isRemote) {
            if(CommonProxy.IsWithinLocation(world, collidePos)) {
                cir.setReturnValue(false);
            }
        }
    }
}
