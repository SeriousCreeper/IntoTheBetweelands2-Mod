package com.seriouscreeper.bladditions.mixins.modsupport.botania;

import net.minecraft.block.BlockMushroom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import thebetweenlands.api.block.IFarmablePlant;
import vazkii.botania.common.block.decor.BlockModMushroom;

import java.util.Random;

@Mixin(value = BlockModMushroom.class, remap = false)
public class MixinBlockModMushroom extends BlockMushroom implements IFarmablePlant {
    @Override
    public boolean isFarmable(World world, BlockPos blockPos, IBlockState iBlockState) {
        return true;
    }

    @Override
    public boolean canSpreadTo(World world, BlockPos blockPos, IBlockState iBlockState, BlockPos blockPos1, Random random) {
        return world.isAirBlock(blockPos1) && this.canPlaceBlockAt(world, blockPos1);
    }

    @Override
    public int getCompostCost(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        return 4;
    }

    @Override
    public void decayPlant(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        world.setBlockToAir(blockPos);
    }

    @Override
    public void spreadTo(World world, BlockPos blockPos, IBlockState iBlockState, BlockPos blockPos1, Random random) {
        world.setBlockState(blockPos1, iBlockState);
    }
}
