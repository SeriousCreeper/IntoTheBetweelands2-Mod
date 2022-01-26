package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thebetweenlands.common.block.terrain.BlockSwampWater;

import java.util.Random;

@Mixin(value = BlockSwampWater.class, remap = false)
public class MixinBlockSwampWater extends BlockFluidClassic {
    public MixinBlockSwampWater(Fluid fluid, Material material, MapColor mapColor) {
        super(fluid, material, mapColor);
    }

    @Inject(method = "updateTick", at = @At(value = "HEAD"), cancellable = true)
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand, CallbackInfo ci) {
        int quantaRemaining = this.quantaPerBlock - (Integer)state.getValue(LEVEL);
        int expQuanta;
        if (quantaRemaining < this.quantaPerBlock && !world.isAirBlock(pos.down())) {
            expQuanta = 0;
            if (this.isSourceBlock(world, pos.east())) {
                ++expQuanta;
            }

            if (this.isSourceBlock(world, pos.north())) {
                ++expQuanta;
            }

            if (this.isSourceBlock(world, pos.south())) {
                ++expQuanta;
            }

            if (this.isSourceBlock(world, pos.west())) {
                ++expQuanta;
            }

            if (expQuanta >= 2) {
                ci.cancel();
            }
        }
    }
}
