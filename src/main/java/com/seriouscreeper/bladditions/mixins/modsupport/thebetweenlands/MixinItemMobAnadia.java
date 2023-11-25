package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.tiffit.sanity.Sanity;
import net.tiffit.sanity.SanityCapability;
import net.tiffit.sanity.SanityModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thebetweenlands.common.item.misc.ItemMobAnadia;

import java.util.Iterator;
import java.util.List;

@Mixin(value = ItemMobAnadia.class, remap = false)
public class MixinItemMobAnadia {
    @Inject(method = "onCapturedByPlayer", at = @At("HEAD"))
    private void injectOnCapturedByPlayer(EntityPlayer player, EnumHand hand, ItemStack stack, EntityLivingBase entity, CallbackInfo ci) {
        if (!player.world.isRemote) {
            SanityCapability cap = (SanityCapability)player.getCapability(SanityCapability.INSTANCE, (EnumFacing)null);
            List<SanityModifier> mods = Sanity.getModifierValues("misc");
            Iterator var3 = mods.iterator();

            while(var3.hasNext()) {
                SanityModifier mod = (SanityModifier)var3.next();
                if (mod.value.equals("fish")) {
                    cap.increaseSanity(mod.amount);
                }
            }
        }
    }
}
