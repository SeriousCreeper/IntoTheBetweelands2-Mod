package com.seriouscreeper.bladditions.mixins.modsupport.arcanearchives;

import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = RadiantTankTileEntity.class, remap = false)
public class MixinRadiantTankTileEntity {
    @Shadow
    @Final
    private RadiantTankTileEntity.VoidingFluidTank inventory;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getCapacity(int capacity) {
        return 10000 + (capacity * 2000);
    }

    @ModifyConstant(
            method = "<init>",
            constant = @Constant(intValue = 16000)
    )
    private static int changeBaseCapacity(int original) {
        return 10000;
    }

    @ModifyArg(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/aranaira/arcanearchives/tileentities/RadiantTankTileEntity$VoidingFluidTank;<init>(Lcom/aranaira/arcanearchives/tileentities/RadiantTankTileEntity;I)V"
            ),
            index = 1
    )
    private int changeTankCapacity(int original) {
        return 10000;
    }
}
