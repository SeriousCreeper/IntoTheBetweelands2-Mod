package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import com.seriouscreeper.bladditions.proxy.CommonProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import thebetweenlands.common.entity.mobs.EntityClimberBase;
import thebetweenlands.common.entity.mobs.EntitySwarm;

@Mixin(value = EntitySwarm.class)
public class MixinEntitySwarm extends EntityClimberBase {

    public MixinEntitySwarm(World world) {
        super(world);
    }


    @Redirect(method = "onUpdate", at = @At(value = "INVOKE", target = "Lthebetweenlands/common/entity/mobs/EntitySwarm;canEntityBeSeen(Lnet/minecraft/entity/Entity;)Z"))
    private boolean checkForPacifist(EntitySwarm instance, Entity entity) {
        if(CommonProxy.IsPacifist((EntityPlayerMP) entity)) {
            return false;
        }

        return instance.canEntityBeSeen(entity);
    }
}
