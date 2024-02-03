package com.seriouscreeper.bladditions.mixins.modsupport.embers;

import baubles.api.BaubleType;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import teamroots.embers.item.bauble.BaublesUtil;
import teamroots.embers.item.bauble.ItemNonbelieverAmulet;

@Mixin(value = ItemNonbelieverAmulet.class, remap = false)
public class MixinItemNonbelieverAmulet {
    /**
     * @author
     * @reason
     */
    @Overwrite
    @SubscribeEvent
    public void onDamage(LivingDamageEvent event) {
        EntityLivingBase killer = event.getEntityLiving();
        DamageSource source = event.getSource();
        if (source.isMagicDamage()) {
            if (killer.hasCapability(BaublesCapabilities.CAPABILITY_BAUBLES, (EnumFacing)null)) {
                NonNullList<ItemStack> stacks = BaublesUtil.getBaubles((IBaublesItemHandler)killer.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, (EnumFacing)null), BaubleType.AMULET);
                if (((ItemStack)stacks.get(0)).getItem()instanceof ItemNonbelieverAmulet && event.getAmount() > 0.5F) {
                    event.setAmount(Math.max(event.getAmount() * 0.7F, 0.5F));
                }
            }
        }
    }
}
