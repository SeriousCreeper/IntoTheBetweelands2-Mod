package com.seriouscreeper.bladditions.mixins.modsupport.sanity;

import net.tiffit.sanity.consequences.IConsequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = IConsequence.class, remap = false)
public interface MixinIConsequences {
    /**
     * @author SC
     */
    @Overwrite
    default boolean worksInDimension(int dimension) {
        return dimension == 20;
    }
}
