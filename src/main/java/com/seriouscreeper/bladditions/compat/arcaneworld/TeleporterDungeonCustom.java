package com.seriouscreeper.bladditions.compat.arcaneworld;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import party.lemons.arcaneworld.gen.dungeon.dimension.DungeonSavedData;
import party.lemons.arcaneworld.gen.dungeon.dimension.TeleporterDungeon;
import party.lemons.arcaneworld.gen.dungeon.generation.DungeonGenerator;

public class TeleporterDungeonCustom extends TeleporterDungeon {
    protected String currentDungeonId;
    protected boolean generateNewDungeon = true;

    public TeleporterDungeonCustom(WorldServer worldIn) {
        super(worldIn);
    }
    public TeleporterDungeonCustom(WorldServer worldIn, String dungeonId) {
        super(worldIn);
        this.currentDungeonId = dungeonId;
    }
    public TeleporterDungeonCustom(WorldServer worldIn, String dungeonId, boolean generateNewDungeon) {
        super(worldIn);
        this.generateNewDungeon = generateNewDungeon;
        this.currentDungeonId = dungeonId;
    }

    @Override
    public void placeEntity(World world, Entity entity, float yaw) {
        if (!world.isRemote) {
            if (!this.hasGenerated) {
                DungeonSavedData data = DungeonSavedData.getInstance(world);

                if(generateNewDungeon) {
                    data.addDungeon();
                }

                int offset = data.getDungeonCount() * 250;
                this.offsetPos = new BlockPos(offset, 40, 0);

                if(generateNewDungeon) {
                    this.doGeneration(world);
                }

                this.generateNewDungeon = false;

                this.hasGenerated = true;
            }

            entity.setLocationAndAngles((double)this.offsetPos.getX() + this.getTeleportOffsetX(), (double)this.offsetPos.up(3).getY(), (double)this.offsetPos.getZ() + this.getTeleportOffsetZ(), entity.rotationYaw, 0.0F);
            entity.motionX = 0.0;
            entity.motionY = 0.0;
            entity.motionZ = 0.0;
        }
    }

    @Override
    public void doGeneration(World world) {
        world.setBlockState(new BlockPos(this.offsetPos.getX() + 6, this.offsetPos.up(3).getY(), this.offsetPos.getZ() + 6), Blocks.BEDROCK.getDefaultState());
        DungeonGeneratorCustom generator = new DungeonGeneratorCustom(world, this.offsetPos, currentDungeonId);
        generator.generateRoom(0, 0);
    }
}
