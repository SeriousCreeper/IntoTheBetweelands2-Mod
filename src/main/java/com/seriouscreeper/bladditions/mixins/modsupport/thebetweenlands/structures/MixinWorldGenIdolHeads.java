package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands.structures;

import com.seriouscreeper.bladditions.proxy.CommonProxy;
import hunternif.mc.atlas.api.AtlasAPI;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thebetweenlands.common.world.gen.feature.structure.WorldGenIdolHeads;

import java.util.Random;

@Mixin(value = WorldGenIdolHeads.class)
public class MixinWorldGenIdolHeads {
    @Inject(method = "generate", at = @At("RETURN"))
    private void injectMapMarker(World worldIn, Random rand, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if(!worldIn.isRemote && cir.getReturnValue()) {
            AtlasAPI.getMarkerAPI().putGlobalMarker(worldIn, false, CommonProxy.MARKER_IDOL_HEAD.toString(), "Idol Head", pos.getX(), pos.getZ());
        }
    }
}
