package com.seriouscreeper.bladditions.mixins.modsupport.embers;

import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.tileentity.TileEntityPumpBottom;
import teamroots.embers.tileentity.TileEntityPumpTop;
import teamroots.embers.util.FluidUtil;

@Mixin(value = TileEntityPumpBottom.class, remap = false)
public class MixinTileEntityPumpBottom extends TileEntity {
    @Shadow public IEmberCapability capability;

    /**
     * @author SC
     * @reason fix pump requiring embers
     */
    @Overwrite
    public boolean attemptPump(BlockPos pos) {
        IBlockState state = this.world.getBlockState(pos);
        if ((state.getBlock() instanceof IFluidBlock && ((IFluidBlock)state.getBlock()).canDrain(this.world, pos) || state.getBlock() instanceof BlockStaticLiquid)) {
            FluidStack stack = FluidUtil.getFluid(this.world, pos, state);
            if (stack != null) {
                TileEntityPumpTop t = (TileEntityPumpTop)this.world.getTileEntity(this.getPos().up());
                int filled = t.getTank().fill(stack, false);
                if (filled == stack.amount) {
                    if (!this.world.isRemote) {
                        t.getTank().fill(stack, true);
                    }

                    t.markDirty();
                    this.world.setBlockToAir(pos);
                    EnumFacing[] var6 = EnumFacing.HORIZONTALS;
                    int var7 = var6.length;

                    for(int var8 = 0; var8 < var7; ++var8) {
                        EnumFacing facing = var6[var8];
                        this.updateWater(pos.offset(facing));
                    }

                    return false;
                }
            }
        }

        return true;
    }

    @Shadow
    private void updateWater(BlockPos pos) {
    }
}
