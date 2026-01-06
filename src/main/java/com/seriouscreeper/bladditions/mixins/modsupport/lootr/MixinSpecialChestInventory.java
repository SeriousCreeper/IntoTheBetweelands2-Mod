package com.seriouscreeper.bladditions.mixins.modsupport.lootr;

import noobanidus.mods.lootr.data.SpecialChestInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = SpecialChestInventory.class, remap = false)
public class MixinSpecialChestInventory {
    @ModifyConstant(
            method = "<init>(Lnoobanidus/mods/lootr/data/ChestData;Lnet/minecraft/nbt/NBTTagCompound;Ljava/lang/String;Lnet/minecraft/util/math/BlockPos;)V",
            constant = @Constant(intValue = 27)
    )
    private int bladditions$initSize(int original) {
        return 18;
    }

    @ModifyConstant(
            method = "getSizeInventory()I",
            constant = @Constant(intValue = 27)
    )
    private int bladditions$getSizeInventory(int original) {
        return 18;
    }
}
