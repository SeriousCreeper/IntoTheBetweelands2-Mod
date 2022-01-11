package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.item.misc.ItemBarkAmulet;

@Mixin(value = ItemBarkAmulet.class, remap = false)
public class MixinItemBarkAmulet {
    /**
     * @author SC
     */
    @Overwrite
    public void onEquipmentTick(ItemStack stack, Entity entity, IInventory inventory) {
    }
}
