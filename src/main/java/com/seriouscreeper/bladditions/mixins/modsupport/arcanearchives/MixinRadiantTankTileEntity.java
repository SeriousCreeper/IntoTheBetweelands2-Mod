package com.seriouscreeper.bladditions.mixins.modsupport.arcanearchives;

import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = RadiantTankTileEntity.class, remap = false)
public class MixinRadiantTankTileEntity {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getCapacity(int capacity) {
        return 10000 + (capacity * 2000);
    }
}
