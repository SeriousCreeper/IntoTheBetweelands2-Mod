package com.seriouscreeper.bladditions.mixins.modsupport.roots;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.item.ILivingRepair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = ILivingRepair.class, remap = false)
public interface MixinILivingRepair {
    /**
     * @author SC
     */
    @Overwrite
    default void update(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected, int bound) {
        if (!worldIn.isRemote && entityIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entityIn;
            if (player.isHandActive() && isSelected) {
                return;
            }

            if (stack.getItemDamage() > 0 && Util.rand.nextInt(Math.max(1, bound * 5)) == 0) {
                stack.setItemDamage(stack.getItemDamage() - 1);
            }
        }
    }
}
