package com.seriouscreeper.bladditions.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import thebetweenlands.common.block.BasicBlock;
import thebetweenlands.common.block.container.BlockBarrel;
import thebetweenlands.common.config.BetweenlandsConfig;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.tile.TileEntityBarrel;

import java.util.Random;

public class PatchedBlockBarrel extends BlockBarrel {
    public PatchedBlockBarrel(IBlockState material, boolean isHeatResistant) {
        super(material, isHeatResistant);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        System.out.println("TICKING");

        for(int y = 1; y < 16; y++) {
            if(worldIn.getBlockState(pos.up(y)).getBlock() == BlockRegistry.CAVE_MOSS) {
                if (worldIn.provider.getDimension() == BetweenlandsConfig.WORLD_AND_DIMENSION.dimensionId && worldIn.getTileEntity(pos) instanceof TileEntityBarrel) {
                    TileEntityBarrel tile = (TileEntityBarrel)worldIn.getTileEntity(pos);
                    tile.fill(new FluidStack(FluidRegistry.WATER, 10), true);
                }

                break;
            }
        }

        super.updateTick(worldIn, pos, state, rand);
    }
}
