package com.seriouscreeper.bladditions.mixins.modsupport.embers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import teamroots.embers.api.tile.IMechanicallyPowered;
import teamroots.embers.tileentity.TileEntityEmberBore;

@Mixin(value = TileEntityEmberBore.class)
public class MixinTileEntityEmberBore implements IMechanicallyPowered {
    /**
     * @author SC
     * @reason limit speed when powere by mechanical power
     */
    @Override
    public double getMechanicalSpeed(double power) {
        if (power <= 25.0) return 0.0;
        if (power >= 100.0) return 0.3;

        // normalized log curve from 25..100 mapped to 0..0.3
        double t = Math.log10(power / 25.0) / Math.log10(4.0); // 0 at 25, 1 at 100
        return 0.3 * t;
    }

    @Shadow
    @Override
    public double getNominalSpeed() {
        return 0;
    }
}
