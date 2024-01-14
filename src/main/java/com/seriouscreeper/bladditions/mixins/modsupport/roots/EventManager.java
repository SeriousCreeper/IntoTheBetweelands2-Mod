package com.seriouscreeper.bladditions.mixins.modsupport.roots;

import epicsquid.roots.init.ModPotions;
import epicsquid.roots.util.SlaveUtil;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = epicsquid.roots.EventManager.class, remap = false)
public class EventManager {
    /**
     * @author SC
     * @reason
     */
    @Overwrite
    @SubscribeEvent
    public static void onEntityTarget(LivingSetAttackTargetEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity.getActivePotionEffect(ModPotions.geas) != null) {
            if (SlaveUtil.isSlave(entity)) {
                return;
            }

            if (entity instanceof EntityLiving && !SlaveUtil.isSlave(entity)) {
                EntityLiving living = (EntityLiving)entity;
                if (living.getAttackTarget() != null) {
                    living.setAttackTarget(null);
                }
            }
        }
    }
}
