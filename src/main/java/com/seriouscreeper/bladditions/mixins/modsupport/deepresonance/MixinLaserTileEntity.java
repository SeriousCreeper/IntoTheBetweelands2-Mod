package com.seriouscreeper.bladditions.mixins.modsupport.deepresonance;

import mcjty.deepresonance.blocks.laser.InfusingBonus;
import mcjty.deepresonance.blocks.laser.LaserTileEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(value = LaserTileEntity.class, remap = false)
public class MixinLaserTileEntity {
    @Shadow
    public static Map<String, InfusingBonus> infusingBonusMap;

    @Inject(method = "getInfusingBonus", at = @At("HEAD"), cancellable = true)
    private static void getInfusingBonus(ItemStack item, CallbackInfoReturnable<InfusingBonus> cir) {
        if (!item.isEmpty()) {
            String name = item.getItem().getRegistryName().toString();

            switch(name) {
                case "thebetweenlands:items_misc":
                    if(item.getItemDamage() == 39) {
                        cir.setReturnValue(infusingBonusMap.get(name));
                    }
                    break;

                case "botania:manaresource":
                case "bloodmagic:item_demon_crystal":
                    if(item.getItemDamage() == 4) {
                        cir.setReturnValue(infusingBonusMap.get(name));
                    }
                    break;
            }
        }
    }
}
