package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import com.seriouscreeper.bladditions.entities.GreeblingMerchantEntity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thebetweenlands.common.entity.mobs.EntityRockSnot;

@Mixin(value = EntityRockSnot.class, remap = false)
public class MixinEntityRockSnot {
    @Inject(method = "canTarget(Lnet/minecraft/entity/EntityLivingBase;)Z", at = @At("HEAD"), cancellable = true)
    private void inject(EntityLivingBase entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof GreeblingMerchantEntity) {
            cir.setReturnValue(false);
        }
    }


    @Inject(method = "canAttackClass(Ljava/lang/Class;)Z", at = @At("HEAD"), cancellable = true)
    private void injectCanAttack(Class entity, CallbackInfoReturnable<Boolean> cir) {
        if(entity == GreeblingMerchantEntity.class) {
            cir.setReturnValue(false);
        }
    }
}
