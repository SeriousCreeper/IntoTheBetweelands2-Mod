package com.seriouscreeper.bladditions.mixins.modsupport.darknesslib;

import com.shinoow.darknesslib.api.DarknessLibAPI;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = DarknessLibAPI.class, remap = false)
public class MixinDarknessLibAPI {
    /**
     * @author SC
     * @reason Fix lighting issues while sitting down
     */
    @Overwrite
    public int getLight(Entity entityIn, boolean strict) {
        BlockPos blockpos = new BlockPos(entityIn.posX, entityIn.getEntityBoundingBox().minY, entityIn.posZ);

        if (strict && !entityIn.world.isBlockFullCube(blockpos) && !entityIn.world.getBlockState(blockpos).getBlock().isReplaceable(entityIn.world, blockpos)) {
            blockpos = blockpos.up();
        } else if(entityIn.isRiding()) {
            blockpos = blockpos.up();
        }

        return entityIn.world.getLightFromNeighbors(blockpos);
    }
}
