package com.seriouscreeper.bladditions.mixins.modsupport.embers;

import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import teamroots.embers.tileentity.TileEntitySeedNew;

import java.util.Random;

@Mixin(value = TileEntitySeedNew.class, remap = false)
public class MixinTileEntitySeedNew {
    @Shadow protected Random random = new Random();

    @Inject(method = "inject", at = @At("HEAD"), cancellable = true)
    public void inject(TileEntity injector, double ember, CallbackInfo ci) {
        if(random.nextInt(ConfigBLAdditions.configGeneral.EmberSeedChance / 100) != 0) {
            ci.cancel();
        }
    }
}
