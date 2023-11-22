package com.seriouscreeper.bladditions.mixins.modsupport.pyrotech;

import com.codetaylor.mc.athenaeum.util.FacingHelper;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TileBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.tile.TileWitherForge;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileWitherForge.class, remap = false)
public class MixinTileWitherForge extends TileBloomery {
    @Inject(method = "createSlag", at = @At("HEAD"), cancellable = true)
    private void injectCreateSlag(CallbackInfo ci) {
        int count = 1;
        EnumFacing facing = this.getTileFacing(this.world, this.pos, this.world.getBlockState(this.pos));
        BlockPos[] positions = new BlockPos[]{this.pos.offset(facing), this.pos.offset(FacingHelper.rotateFacingCW(facing)), this.pos.offset(FacingHelper.rotateFacingCW(facing, 3))};

        for(int i = 0; i < count; ++i) {
            int index = this.world.rand.nextInt(positions.length);
            this.createSlag(positions[index]);
        }

        ci.cancel();
    }
}
