package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

import java.util.ArrayList;

@Mixin(value = ThaumcraftCraftingManager.class, remap = false)
public class MixinThaumcraftCraftingManager {
    @Inject(method = "getObjectTags(Lnet/minecraft/item/ItemStack;Ljava/util/ArrayList;)Lthaumcraft/api/aspects/AspectList;", at = @At("HEAD"), cancellable = true)
    private static void getObjectTags(ItemStack itemstack, ArrayList<String> history, CallbackInfoReturnable<AspectList> cir) {
        if( itemstack.getItem() == RegistrarBloodMagicItems.SENTIENT_ARMOUR_HELMET ||
            itemstack.getItem() == RegistrarBloodMagicItems.SENTIENT_ARMOUR_CHEST ||
            itemstack.getItem() == RegistrarBloodMagicItems.SENTIENT_ARMOUR_LEGGINGS ||
            itemstack.getItem() == RegistrarBloodMagicItems.SENTIENT_ARMOUR_BOOTS
        ) {
            cir.setReturnValue(new AspectList());
        }
    }
}
