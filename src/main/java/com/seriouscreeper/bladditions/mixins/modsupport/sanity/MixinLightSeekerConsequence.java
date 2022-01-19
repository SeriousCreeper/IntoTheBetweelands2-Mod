package com.seriouscreeper.bladditions.mixins.modsupport.sanity;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.tiffit.sanity.SanityCapability;
import net.tiffit.sanity.consequences.LightSeekerConsequence;
import net.tiffit.sanity.entity.LightSeekerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = LightSeekerConsequence.class, remap = false)
public class MixinLightSeekerConsequence {
    /**
     * @author SC
     */
    @Overwrite
    public void run(EntityPlayerMP player, SanityCapability.SanityLevel level) {
        BlockPos pos = this.findLight(player);
        if (pos != null) {
            LightSeekerEntity ent = new LightSeekerEntity(player.world);
            ent.setTarget(pos);
            BlockPos spawn = pos.add(this.getOffset());
            ent.setPosition(spawn.getX() + .5, spawn.getY() + .5, spawn.getZ() + .5);
            player.world.spawnEntity(ent);
        }
    }


    @Shadow
    private BlockPos findLight(EntityPlayerMP p) { return null; }

    @Shadow
    private BlockPos getOffset() { return null; }
}
