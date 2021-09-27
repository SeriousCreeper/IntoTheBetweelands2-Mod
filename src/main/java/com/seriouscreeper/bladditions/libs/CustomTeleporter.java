package com.seriouscreeper.bladditions.libs;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldServer;

public class CustomTeleporter extends Teleporter {
    private final WorldServer world;

    public CustomTeleporter(WorldServer worldIn)
    {
        super(worldIn);

        this.world = worldIn;
    }

    @Override
    public boolean makePortal(Entity entityIn)
    {
        return true;
    }

    @Override
    public boolean placeInExistingPortal(Entity entityIn, float rotationYaw)
    {
        return true;
    }

    @Override
    public void removeStalePortalLocations(long worldTime)
    {
        // NO-OP
    }

    @Override
    public void placeInPortal(Entity entityIn, float rotationYaw)
    {
        BlockPos spawnCoord = this.world.provider.getSpawnCoordinate();

        // For End type dimensions, generate the platform and place the entity there,
        // UNLESS the world has a different spawn point set.
        if ((this.world.provider.getDimensionType().getId() == 1 || this.world.provider instanceof WorldProviderEnd)
                && this.world.getSpawnPoint().equals(spawnCoord))
        {
            IBlockState obsidian = Blocks.OBSIDIAN.getDefaultState();
            IBlockState air = Blocks.AIR.getDefaultState();
            int spawnX = spawnCoord.getX();
            int spawnY = spawnCoord.getY();
            int spawnZ = spawnCoord.getZ();

            for (int zOff = -2; zOff <= 2; ++zOff)
            {
                for (int xOff = -2; xOff <= 2; ++xOff)
                {
                    for (int yOff = -1; yOff < 3; yOff++)
                    {
                        this.world.setBlockState(new BlockPos(spawnX + xOff, spawnY + yOff, spawnZ + zOff), yOff < 0 ? obsidian : air);
                    }
                }
            }

            entityIn.setLocationAndAngles((double)spawnX, (double)spawnY, (double)spawnZ, entityIn.rotationYaw, 0.0F);
            entityIn.motionX = 0.0D;
            entityIn.motionY = 0.0D;
            entityIn.motionZ = 0.0D;
        }
    }
}
