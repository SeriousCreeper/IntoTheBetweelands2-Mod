package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.FluidRegistry;
import thebetweenlands.common.tile.TileEntityBarrel;

@Mixin(value = TileEntityBarrel.class, remap = false)
public class MixinTileEntityBarrel extends TileEntity implements ITickable {
    @Shadow @Final private FluidTank fluidTank;

    @Override
    public void update() {
        if(world.rand.nextInt(ConfigBLAdditions.configGeneral.BarrelFillWithWaterChance) == 0) {
            if (world.getBlockState(pos.up()).getBlock() == BlockRegistry.CAVE_MOSS) {
                this.fluidTank.fill(new FluidStack(FluidRegistry.SWAMP_WATER, ConfigBLAdditions.configGeneral.BarrelFillWithWaterAmount), true);
            }
        }
    }
}
