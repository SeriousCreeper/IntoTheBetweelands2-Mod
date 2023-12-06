package com.seriouscreeper.bladditions.mixins.modsupport.fishundeadrising;

import com.Fishmod.mod_LavaCow.client.Modconfig;
import com.Fishmod.mod_LavaCow.init.FishItems;
import com.Fishmod.mod_LavaCow.util.ModEventHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Arrays;

@Mixin(value = ModEventHandler.class, remap = false)
public class MixinModEventHandler {
    /**
     * @author SC
     * @reason turn blacklist into whitelist
     */
    @Overwrite
    @SubscribeEvent
    public void onEntityDrop(LivingDropsEvent event) {
        if (event.getEntityLiving() instanceof EntityWolf && event.getEntityLiving().getRNG().nextInt(5) == 1) {
            event.getEntityLiving().dropItem(FishItems.SHARPTOOTH, 1);
        }

        if (event.getEntityLiving() instanceof EntityLiving && (event.getEntityLiving().width > 1.0F || event.getEntityLiving().height > 1.0F) && event.getEntityLiving().getRNG().nextFloat() < 0.01F * (float) Modconfig.General_Intestine) {
            EntityEntry ee = EntityRegistry.getEntry(event.getEntityLiving().getClass());
            if (ee != null && Arrays.asList(Modconfig.Intestine_banlist).contains(ee.getRegistryName().toString())) {
                event.getEntityLiving().dropItem(FishItems.INTESTINE, 1);
            }
        }
    }
}
