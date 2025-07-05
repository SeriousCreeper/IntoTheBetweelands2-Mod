package com.seriouscreeper.bladditions.util;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class BlankTeleporter extends Teleporter {
    private final WorldServer world;
    private final double x, y, z;

    public BlankTeleporter(WorldServer world, double x, double y, double z) {
        super(world);
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void placeInPortal(Entity entity, float rotationYaw) {
        entity.setPosition(x, y, z);
        entity.motionX = 0.0;
        entity.motionY = 0.0;
        entity.motionZ = 0.0;
    }

    @Override
    public boolean placeInExistingPortal(Entity entity, float rotationYaw) {
        return false; // No existing portal
    }

    @Override
    public boolean makePortal(Entity entity) {
        return false; // Don't create a portal
    }

    @Override
    public void removeStalePortalLocations(long worldTime) {
        // No-op
    }
}
