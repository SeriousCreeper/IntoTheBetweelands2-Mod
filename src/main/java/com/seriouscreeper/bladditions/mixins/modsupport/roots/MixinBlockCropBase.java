package com.seriouscreeper.bladditions.mixins.modsupport.roots;

import epicsquid.mysticallib.block.BlockCropBase;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.block.farming.BlockGenericDugSoil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

@Mixin(value = BlockCropBase.class, remap = false)
public class MixinBlockCropBase extends BlockCrops {
    /**
     * @author SC
     */
    @Nonnull
    @Overwrite
    public EnumPlantType getPlantType(@Nullable IBlockAccess world, @Nullable BlockPos pos) {
        return EnumPlantType.Crop;
    }


    public boolean isDecayed(IBlockAccess world, BlockPos pos) {
        IBlockState blockState = world.getBlockState(pos.down());

        if (blockState.getBlock() instanceof BlockGenericDugSoil) {
            return (Boolean)blockState.getValue(BlockGenericDugSoil.DECAYED);
        }

        return false;
    }


    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return !this.isMaxAge(state) && !isDecayed(worldIn, pos);
    }


    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        if (worldIn.isAreaLoaded(pos, 1)) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                float f = getGrowthChance(this, worldIn, pos);
                if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int)(25.0F / f) + 1) == 0)) {
                    worldIn.setBlockState(pos, this.withAge(i + 1), 2);
                    ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
                }
            }
        }
    }
}
