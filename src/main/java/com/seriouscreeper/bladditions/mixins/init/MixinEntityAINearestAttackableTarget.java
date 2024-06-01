package com.seriouscreeper.bladditions.mixins.init;

import com.seriouscreeper.bladditions.proxy.CommonProxy;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityAINearestAttackableTarget.class)
public class MixinEntityAINearestAttackableTarget<T extends EntityLivingBase> extends EntityAITarget {
    @Shadow
    protected T targetEntity;

    public MixinEntityAINearestAttackableTarget(EntityCreature creature, boolean checkSight) {
        super(creature, checkSight);
    }

    @Inject(method = "startExecuting", at = @At("HEAD"), cancellable = true)
    private void avoidPacifist(CallbackInfo ci) {
        if(CommonProxy.IsPacifistMob(this.taskOwner))
        {
            if(this.targetEntity instanceof EntityPlayer && !this.targetEntity.world.isRemote) {
                EntityPlayerMP player = (EntityPlayerMP) this.targetEntity;

                if(!CommonProxy.IsPacifist(player)) {
                    return;
                }

                ci.cancel();
            }
        }
    }

    @Shadow
    public boolean shouldExecute() {
        return false;
    }
}
