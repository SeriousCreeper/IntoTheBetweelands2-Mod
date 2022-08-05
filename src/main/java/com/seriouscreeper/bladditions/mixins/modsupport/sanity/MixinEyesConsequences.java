package com.seriouscreeper.bladditions.mixins.modsupport.sanity;

import gigaherz.eyes.entity.EntityEyes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.tiffit.sanity.SanityCapability;
import net.tiffit.sanity.consequences.EyesConsequence;
import net.tiffit.sanity.proxy.CommonProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = EyesConsequence.class, remap = false)
public class MixinEyesConsequences {
    /**
     * @author SC
     */
    @Overwrite
    public void run(EntityPlayerMP player, SanityCapability.SanityLevel level) {
        BlockPos pos = this.findBestSpot(player);
        if (pos != null) {
            SanityCapability cap = player.getCapability(SanityCapability.INSTANCE, null);

            if(cap != null && cap.getSanityExact() < 0) {
                float chance = cap.getSanityExact() / -200F;

                if(player.rand.nextFloat() < chance) {
                    EntityEyes eyes = new EntityEyes(player.world);
                    eyes.setPosition(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5);
                    player.world.spawnEntity(eyes);
                    return;
                }
            }

            player.getServerWorld().spawnParticle(player, CommonProxy.EYES_PARTICLE, true, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 1, 0.5D, 0.5D, 0.5D, 0.0D, new int[0]);
        }
    }

    @Shadow
    private BlockPos findBestSpot(EntityPlayerMP p) {
        return null;
    }
}
