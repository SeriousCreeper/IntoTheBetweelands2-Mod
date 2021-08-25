package com.seriouscreeper.bladditions.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import thebetweenlands.common.entity.projectiles.EntityAngryPebble;

public class EntityAngrierPebble extends EntityAngryPebble {
    public EntityAngrierPebble(World world) {
        super(world);
    }

    public EntityAngrierPebble(World world, EntityLivingBase entity) {
        super(world, entity);
    }

    @Override
    protected void explode() {
        boolean blockDamage = this.world.getGameRules().getBoolean("mobGriefing");
        this.world.createExplosion(this, this.posX, this.posY, this.posZ, 9F, blockDamage);
    }
}
