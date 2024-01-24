package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.common.entities.monster.cult.EntityCultistPortalLesser;
import thebetweenlands.api.capability.IPuppetCapability;
import thebetweenlands.api.capability.IPuppeteerCapability;
import thebetweenlands.api.capability.ProtectionShield;
import thebetweenlands.common.item.equipment.ItemRingOfRecruitment;
import thebetweenlands.common.registries.CapabilityRegistry;

import java.util.Arrays;

@Mixin(value = thebetweenlands.common.handler.PuppetHandler.class, remap = false)
public class PuppetHandler {
    @Inject(method = "onPlayerUpdate", at = @At("HEAD"), cancellable = true)
    private static void onPlayerUpdate(TickEvent.PlayerTickEvent event, CallbackInfo ci) {
        if (event.phase == TickEvent.Phase.END) {
            IPuppeteerCapability cap = (IPuppeteerCapability) event.player.getCapability(CapabilityRegistry.CAPABILITY_PUPPETEER, (EnumFacing) null);
            if (cap != null) {
                ProtectionShield shield = cap.getShield();
                if (shield != null) {
                    if (!event.player.world.isRemote && !ItemRingOfRecruitment.isRingActive(event.player, (IPuppetCapability) null) && shield.hasShield()) {
                        for (int i = 0; i < 20; ++i) {
                            shield.setActive(i, false);
                        }
                    }

                    cap.updateShield();
                }

                Entity activatingEntity = cap.getActivatingEntity();

                if(activatingEntity != null) {
                    if(ConfigBLAdditions.configGeneral.DebugRecruitmentNames) {
                        System.out.println(activatingEntity.getClass().getName());
                    }

                    if( Arrays.asList(ConfigBLAdditions.configGeneral.RecruitmentBlacklist).contains(activatingEntity.getClass().getName())) {
                        ci.cancel();
                    }
                }
            }
        }
    }
}
