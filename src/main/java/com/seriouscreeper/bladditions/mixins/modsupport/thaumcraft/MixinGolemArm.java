package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import com.joshiegemfinder.betweenlandsredstone.ModBlocks;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.api.golems.parts.GolemArm;
import thaumcraft.api.items.ItemsTC;
import thebetweenlands.common.item.misc.ItemMisc;
import thebetweenlands.common.registries.ItemRegistry;

@Mixin(value = GolemArm.class, remap = false)
public class MixinGolemArm {
    @Inject(method = "register", at = @At("HEAD"))
    private static void injectRegister(GolemArm thing, CallbackInfo ci) {
        switch(thing.key) {
            case "CLAWS":
            thing.components = new Object[]{new ItemStack(ItemsTC.modules, 1, 1), new ItemStack(ItemRegistry.SYRMORITE_SHEARS, 2), "base"};
            break;

            case "BREAKERS":
            thing.components = new Object[]{ItemMisc.EnumItemMisc.VALONITE_SHARD.create(2), "base", new ItemStack(ModBlocks.SCABYST_PISTON, 2)};
            break;

            case "DARTS":
            thing.components = new Object[]{new ItemStack(ItemsTC.modules, 1, 1), new ItemStack(ModBlocks.SCABYST_DISPENSER, 2), new ItemStack(ItemRegistry.ANGLER_TOOTH_ARROW, 32), "mech"};
            break;
        }
    }
}
