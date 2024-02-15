package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thebetweenlands.common.item.misc.ItemBoneWayfinder;

@Mixin(value = ItemBoneWayfinder.class, remap = false)
public class MixinItemBoneWayfinder {
    @Inject(method = "onUsingTick", at = @At("HEAD"), cancellable = true)
    private void injectOnUsingTick(ItemStack stack, EntityLivingBase entity, int count, CallbackInfo ci) {
        if (!entity.world.isRemote && entity.world.provider.getDimension() != 20) {
            if (count < 80) {
                if (entity instanceof EntityPlayerMP) {
                    ((EntityPlayerMP)entity).sendStatusMessage(new TextComponentTranslation("chat.bone_wayfinder.wrong_dimension", new Object[0]), true);
                }

                entity.stopActiveHand();
                entity.world.playSound((EntityPlayer)null, entity.posX, entity.posY, entity.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                ci.cancel();
            }
        }
    }
}
