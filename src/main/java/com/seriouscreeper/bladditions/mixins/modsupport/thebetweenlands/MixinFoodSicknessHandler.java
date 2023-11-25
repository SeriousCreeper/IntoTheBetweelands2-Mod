package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.tiffit.sanity.Sanity;
import net.tiffit.sanity.SanityCapability;
import net.tiffit.sanity.SanityModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thebetweenlands.common.handler.FoodSicknessHandler;

import java.util.List;

@Mixin(value = FoodSicknessHandler.class)
public class MixinFoodSicknessHandler {
    @Inject(method = "onUseItemTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/FoodStats;addStats(IF)V"))
    private static void injectOnUseItemTick(LivingEntityUseItemEvent.Tick event, CallbackInfo ci) {
        SanityCapability cap = event.getEntityLiving().getCapability(SanityCapability.INSTANCE, (EnumFacing)null);

        if(cap == null) {
            return;
        }

        List<SanityModifier> mods = Sanity.getModifierValues("misc");

        for (SanityModifier mod : mods) {
            if (mod.value.equals("food_sickness")) {
                cap.increaseSanity(mod.amount);
                return;
            }
        }
    }
}
