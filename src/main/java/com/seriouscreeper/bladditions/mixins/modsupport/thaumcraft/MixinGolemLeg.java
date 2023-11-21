package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.golems.parts.GolemLeg;
import thaumcraft.api.items.ItemsTC;
import thebetweenlands.common.item.misc.ItemMisc;
import thebetweenlands.common.registries.ItemRegistry;

@Mixin(value = GolemLeg.class, remap = false)
public class MixinGolemLeg {
    @Inject(method = "register", at = @At("HEAD"))
    private static void injectRegister(GolemLeg thing, CallbackInfo ci) {
        switch(thing.key) {
            case "ROLLER":
                thing.components = new Object[]{ItemMisc.EnumItemMisc.WEEDWOOD_BOWL.create(2), ItemMisc.EnumItemMisc.LURKER_SKIN.create(1), "mech"};
                break;

            case "CLIMBER":
                thing.components = new Object[]{new ItemStack(ItemRegistry.SILT_CRAB_CLAW, 4), "base", "mech", "mech"};
                break;

            case "FLYER":
                thing.components = new Object[]{new ItemStack(BlocksTC.levitator), new ItemStack(ItemsTC.plate, 4), new ItemStack(ItemRegistry.SAP_SPIT), "mech"};
                break;
        }
    }
}
