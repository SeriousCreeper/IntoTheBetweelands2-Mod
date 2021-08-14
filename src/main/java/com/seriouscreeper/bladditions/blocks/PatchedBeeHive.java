package com.seriouscreeper.bladditions.blocks;

import com.seriouscreeper.bladditions.proxy.CommonProxy;
import growthcraft.bees.common.block.BlockBeeHive;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class PatchedBeeHive extends BlockBeeHive {
    public PatchedBeeHive(String unlocalizedName) {
        super(unlocalizedName);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), CommonProxy.BEE_SWARM, SoundCategory.BLOCKS, 1,1);

        System.out.println("BEE HIVE NOISES");

        super.updateTick(worldIn, pos, state, rand);
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        super.randomTick(worldIn, pos, state, random);
    }
}
