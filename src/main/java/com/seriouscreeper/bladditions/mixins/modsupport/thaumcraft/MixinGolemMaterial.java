package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.api.golems.parts.GolemMaterial;
import thebetweenlands.common.registries.BlockRegistry;

@Mixin(value = GolemMaterial.class, remap = false)
public class MixinGolemMaterial {
    @Inject(method = "register", at = @At("HEAD"))
    private static void injectRegister(GolemMaterial thing, CallbackInfo ci) {
        if (thing.key.equals("CLAY")) {
            thing.componentBase = new ItemStack(BlockRegistry.MUD_BRICKS, 1, 0);
        }
    }
}
