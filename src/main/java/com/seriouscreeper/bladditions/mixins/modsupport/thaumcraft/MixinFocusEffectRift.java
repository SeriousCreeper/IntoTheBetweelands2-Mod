package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.common.items.casters.foci.FocusEffectRift;
import thebetweenlands.common.world.storage.location.LocationStorage;

@Mixin(value = FocusEffectRift.class, remap = false)
public class MixinFocusEffectRift {
    @Inject(method = "createHole", at = @At("HEAD"), cancellable = true)
    private static void createHole(World world, BlockPos pos, EnumFacing facing, byte count, int max, CallbackInfoReturnable<Boolean> cir) {
        if(LocationStorage.isLocationGuarded(world, null, pos)) {
            cir.setReturnValue(false);
        }
    }
}
