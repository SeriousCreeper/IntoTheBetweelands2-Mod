package com.seriouscreeper.bladditions.events;

import com.charles445.simpledifficulty.api.SDPotions;
import com.charles445.simpledifficulty.api.config.QuickConfig;
import growthcraft.cellar.shared.item.ItemBoozeBottle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class DrinkingEventHandler {
    @SubscribeEvent
    public void onLivingEntityUseItemFinish(LivingEntityUseItemEvent.Finish event) {
        if (QuickConfig.isThirstEnabled()) {
            if (event.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                if (player.world.isRemote) {
                    return;
                }

                ItemStack stack = event.getItem();
                if (stack.getItem() instanceof ItemBoozeBottle && Math.random() > 0.5f) {
                    player.addPotionEffect(new PotionEffect(SDPotions.thirsty, 600));
                }
            }
        }
    }
}
