package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.api.golems.parts.GolemAddon;
import thebetweenlands.common.item.misc.ItemMisc;
import thebetweenlands.common.registries.BlockRegistry;

@Mixin(value = GolemAddon.class, remap = false)
public class MixinGolemAddon {
    @Inject(method = "register", at = @At("HEAD"))
    private static void injectRegister(GolemAddon thing, CallbackInfo ci) {
        if (thing.key.equals("HAULER")) {
            thing.components = new Object[]{ItemMisc.EnumItemMisc.LURKER_SKIN.create(1), new ItemStack(BlockRegistry.WEEDWOOD_CHEST)};
        }
    }
}
