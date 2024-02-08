package com.seriouscreeper.bladditions.mixins.modsupport.botania;

import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import vazkii.botania.common.entity.EntityThornChakram;

@Mixin(value = EntityThornChakram.class, remap = false)
public class MixinEntityThornChakram {
    @ModifyArg(method = "onImpact", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z"), index = 1)
    private float modifyOnImpact(float amount) {
        return ConfigBLAdditions.configBotania.DamageChakram;
    }
}
