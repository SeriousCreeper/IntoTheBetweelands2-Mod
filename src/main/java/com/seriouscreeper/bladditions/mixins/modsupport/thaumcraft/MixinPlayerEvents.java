package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.items.curios.ItemThaumonomicon;
import thaumcraft.common.lib.events.PlayerEvents;
import thaumcraft.common.lib.potions.PotionUnnaturalHunger;

import java.util.Arrays;

@Mixin(value = PlayerEvents.class, remap = false)
public class MixinPlayerEvents {
    /**
     * @author SC
     */
    @SubscribeEvent
    @Overwrite
    public static void pickupItem(EntityItemPickupEvent event) {
        if (event.getEntityPlayer() != null && !event.getEntityPlayer().world.isRemote && event.getItem() != null && event.getItem().getItem() != null) {
            IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(event.getEntityPlayer());

            if (event.getItem().getItem().getItem() instanceof ItemThaumonomicon && !knowledge.isResearchKnown("!gotthaumonomicon")) {
                knowledge.addResearch("!gotthaumonomicon");
                knowledge.sync((EntityPlayerMP)event.getEntityPlayer());
            }
        }
    }


    /**
     * @author SC
     * @reason More curative items
     */
    @Overwrite
    @SubscribeEvent
    public static void finishedUsingItem(LivingEntityUseItemEvent.Finish event) {
        if (!event.getEntity().world.isRemote && event.getEntityLiving().isPotionActive(PotionUnnaturalHunger.instance)) {
            boolean validFood = Arrays.asList(ConfigBLAdditions.configThaumcraft.unnaturalHungerCures).contains(event.getItem().getItem().getRegistryName().toString());

            if (!validFood) {
                if (event.getItem().getItem() instanceof ItemFood) {
                    event.getEntityLiving().sendMessage(new TextComponentString("§4§o" + I18n.translateToLocal("warp.text.hunger.1")));
                }
            } else {
                PotionEffect pe = event.getEntityLiving().getActivePotionEffect(PotionUnnaturalHunger.instance);
                int amp = pe.getAmplifier() - 1;
                int duration = pe.getDuration() - 600;
                event.getEntityLiving().removePotionEffect(PotionUnnaturalHunger.instance);
                if (duration > 0 && amp >= 0) {
                    pe = new PotionEffect(PotionUnnaturalHunger.instance, duration, amp, true, false);
                    pe.getCurativeItems().clear();
                    pe.addCurativeItem(new ItemStack(Items.ROTTEN_FLESH));
                    event.getEntityLiving().addPotionEffect(pe);
                }

                event.getEntityLiving().sendMessage(new TextComponentString("§2§o" + I18n.translateToLocal("warp.text.hunger.2")));
            }
        }

    }
}
