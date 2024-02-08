package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import thebetweenlands.common.entity.mobs.EntityClimberBase;
import thebetweenlands.common.entity.mobs.EntitySwarm;

@Mixin(value = EntitySwarm.class)
public class MixinEntitySwarm extends EntityClimberBase {

    public MixinEntitySwarm(World world) {
        super(world);
    }


    @Override
    public void heal(float healAmount) {
    }
}
