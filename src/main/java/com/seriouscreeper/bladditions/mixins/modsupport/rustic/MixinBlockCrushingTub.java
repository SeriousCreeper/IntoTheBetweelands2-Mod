package com.seriouscreeper.bladditions.mixins.modsupport.rustic;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rustic.common.blocks.BlockBase;
import rustic.common.blocks.BlockCrushingTub;

@Mixin(value = BlockCrushingTub.class, remap = false)
public class MixinBlockCrushingTub extends BlockBase {
    public MixinBlockCrushingTub(Material mat, String name) {
        super(mat, name);
    }

    @Inject(method = "onFallenUpon", at =@At("HEAD"), cancellable = true)
    public void onFallenUponMixin(World worldIn, BlockPos pos, Entity entityIn, float fallDistance, CallbackInfo ci) {
        super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        ci.cancel();
    }
}
