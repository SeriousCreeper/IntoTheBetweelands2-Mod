package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import epicsquid.roots.advancements.Advancements;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thebetweenlands.common.entity.EntityAnimalBL;
import thebetweenlands.common.entity.EntityTameableBL;
import thebetweenlands.common.entity.mobs.EntityChiromawTame;

import javax.annotation.Nullable;

@Mixin(value = EntityChiromawTame.class)
public class MixinEntityChiromawTame extends EntityTameableBL {
    public MixinEntityChiromawTame(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "shouldAttackEntity", at = @At("HEAD"), cancellable = true)
    private void injectShouldAttackEntity(EntityLivingBase entityTarget, EntityLivingBase entityTarget2, CallbackInfoReturnable<Boolean> cir) {
        if(entityTarget instanceof EntityAnimalBL) {
            ResourceLocation id = Advancements.PACIFIST_ID;
            NetHandlerPlayClient conn = Minecraft.getMinecraft().getConnection();

            if (conn != null) {
                ClientAdvancementManager manager = conn.getAdvancementManager();
                Advancement adv = manager.getAdvancementList().getAdvancement(id);

                if (adv != null) {
                    if (!((EntityPlayerMP) getOwner()).getAdvancements().getProgress(adv).isDone()) {
                        cir.setReturnValue(false);
                    }
                }
            }
        }
    }

    @Shadow
    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable entityAgeable) {
        return null;
    }
}
