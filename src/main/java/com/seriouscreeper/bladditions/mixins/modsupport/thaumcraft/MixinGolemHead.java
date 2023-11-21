package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.api.golems.parts.GolemHead;
import thaumcraft.api.items.ItemsTC;
import thebetweenlands.common.registries.BlockRegistry;

@Mixin(value = GolemHead.class, remap = false)
public class MixinGolemHead {
    @Inject(method = "register", at = @At("HEAD"))
    private static void injectRegister(GolemHead thing, CallbackInfo ci) {
        if (thing.key.equals("SMART_ARMORED")) {
            thing.components = new Object[]{new ItemStack(ItemsTC.mind, 1, 1), new ItemStack(ItemsTC.plate), "base", new ItemStack(BlockRegistry.DENTROTHYST, 1, 1)};
        }
    }
}
