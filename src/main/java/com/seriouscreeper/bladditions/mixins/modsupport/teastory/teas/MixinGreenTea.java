package com.seriouscreeper.bladditions.mixins.modsupport.teastory.teas;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import roito.teastory.api.drink.DailyDrink;
import roito.teastory.config.ConfigMain;
import roito.teastory.helper.DrinkingHelper;
import roito.teastory.item.ItemRegister;
import roito.teastory.item.drink.GreenTea;

@Mixin(value = GreenTea.class, remap = false)
public class MixinGreenTea {
    /**
     * @author SC
     * @reason
     */
    @Overwrite
    public static void addPotion(int tier, World world, EntityPlayer entityplayer) {
        if (!world.isRemote) {
            DailyDrink dailyDrink = DrinkingHelper.getLevelAndTimeImprovement(world, entityplayer);
            tier = 0;
            tier += dailyDrink.getLevel();
            int time = ConfigMain.drink.greenTeaDrink_Time;
            time = (int)((float)time * (1.0F + dailyDrink.getTime()));
            int addedTimePercent = (int)(dailyDrink.getTime() * 100.0F);
            entityplayer.sendMessage(new TextComponentTranslation("teastory.message.daily_drinking.normal", new Object[]{dailyDrink.getInstantDay(), addedTimePercent + "%", dailyDrink.getLevel()}));
            if (ConfigMain.general.useTeaResidueAsBoneMeal) {
                ItemHandlerHelper.giveItemToPlayer(entityplayer, new ItemStack(ItemRegister.tea_residue, 1, 1));
            }

            String[] var6 = ConfigMain.drink.greenTeaDrink_Effect;
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                String potion = var6[var8];
                if (Potion.getPotionFromResourceLocation(potion) != null) {
                    entityplayer.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation(potion), time / (tier + 1), tier));
                }
            }
        }
    }

    @Inject(method = "onFoodEaten", at = @At("HEAD"), cancellable = true)
    private void injectOnFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer, CallbackInfo ci) {
        addPotion(0, world, entityplayer);
        ci.cancel();
    }
}
