package com.seriouscreeper.bladditions.mixins.modsupport.roots;

import epicsquid.mysticallib.block.BlockCropBase;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.block.farming.BlockGenericDugSoil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
}
