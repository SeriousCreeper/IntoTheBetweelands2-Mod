package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.tile.TileEntityMothHouse;

@Mixin(value = TileEntityMothHouse.class, remap = false)
public class MixinTileEntityMothHouse {
    @Shadow private final float productionEfficiency = 0.0F;
    @Shadow private int productionTime = 0;


    @Shadow
    public int getSilkProductionStage() {
        return 0;
    }


    /**
     * @author SC
     */
    @Overwrite
    protected void resetProductionTime() {
        float efficiencyMultiplier = 1.0F / (0.1F + this.productionEfficiency * 0.9F);
        this.productionTime = (int)(ConfigBLAdditions.configGeneral.MothHouseProductionSpeed * Math.pow(3.0D, (double)this.getSilkProductionStage()) * (double)efficiencyMultiplier);
    }
}
