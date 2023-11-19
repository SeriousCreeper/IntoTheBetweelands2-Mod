package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands.structures;

import com.seriouscreeper.bladditions.proxy.CommonProxy;
import hunternif.mc.atlas.api.AtlasAPI;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(value = thebetweenlands.common.world.gen.feature.structure.WorldGenWightFortress.class)
public class WorldGenWightFortress {
    @Inject(method = "generate", at = @At("RETURN"))
    private void injectMapMarker(World worldIn, Random rand, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if(!worldIn.isRemote && cir.getReturnValue()) {
            AtlasAPI.getMarkerAPI().putGlobalMarker(worldIn, false, CommonProxy.MARKER_WIGHT_FORTRESS.toString(), "Wight Fortress", pos.getX(), pos.getZ());
        }
    }
}
