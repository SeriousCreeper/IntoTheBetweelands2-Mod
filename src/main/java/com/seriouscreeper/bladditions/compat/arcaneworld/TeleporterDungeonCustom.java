package com.seriouscreeper.bladditions.compat.arcaneworld;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import party.lemons.arcaneworld.gen.dungeon.dimension.TeleporterDungeon;
import party.lemons.arcaneworld.gen.dungeon.generation.DungeonGenerator;

public class TeleporterDungeonCustom extends TeleporterDungeon {
    protected String currentDungeonId;

    public TeleporterDungeonCustom(WorldServer worldIn) {
        super(worldIn);
    }

    public TeleporterDungeonCustom(WorldServer worldIn, String dungeonId) {
        super(worldIn);
        this.currentDungeonId = dungeonId;
    }

    @Override
    public void doGeneration(World world) {
        world.setBlockState(new BlockPos(this.offsetPos.getX() + 6, this.offsetPos.up(3).getY(), this.offsetPos.getZ() + 6), Blocks.BEDROCK.getDefaultState());
        DungeonGeneratorCustom generator = new DungeonGeneratorCustom(world, this.offsetPos, currentDungeonId);
        generator.generateRoom(0, 0);
    }
}
