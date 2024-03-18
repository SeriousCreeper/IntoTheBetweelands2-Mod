package com.seriouscreeper.bladditions.compat.arcaneworld;

import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import org.spongepowered.asm.mixin.Overwrite;
import party.lemons.arcaneworld.gen.dungeon.generation.Direction;
import party.lemons.arcaneworld.gen.dungeon.generation.DungeonGenerator;
import party.lemons.arcaneworld.gen.dungeon.generation.DungeonRoomProcessor;
import party.lemons.arcaneworld.gen.dungeon.generation.RoomDirection;

import java.lang.reflect.Field;
import java.util.Random;

public class DungeonGeneratorCustom extends DungeonGenerator {
    protected String dungeonId;

    private BlockPos origin;
    private World world;
    private Random random;
    private int width;
    private int height;
    private RoomDirection[][] directions;


    public DungeonGeneratorCustom(World world, BlockPos origin, String dungeonId) {
        super(world, origin);

        this.world = world;
        this.origin = origin;
        this.random = world.rand;
        this.width = 5 + this.random.nextInt(6);
        this.height = 5 + this.random.nextInt(6);
        this.directions = new RoomDirection[this.width][this.height];

        for(int i = 0; i < this.width; ++i) {
            for(int j = 0; j < this.height; ++j) {
                this.directions[i][j] = new RoomDirection();
            }
        }

        this.dungeonId = dungeonId;
    }

    @Override
    public boolean generateRoom(int x, int y) {
        RoomDirection direction = this.directions[x][y];
        direction.withDirection(Direction.SOUTH, true);
        BlockPos generatePos = this.origin.add(x * 13, 0, y * 13);
        Rotation rotation = direction.getRotation();
        int offsetX = 0;
        int offsetZ = 0;

        switch (rotation) {
            case CLOCKWISE_90:
                offsetX = 12;
                break;
            case CLOCKWISE_180:
                offsetX = 12;
                offsetZ = 12;
                break;
            case COUNTERCLOCKWISE_90:
                offsetZ = 12;
        }

        PlacementSettings settings = (new PlacementSettings()).setRotation(Rotation.CLOCKWISE_180).setMirror(Mirror.NONE);
        ResourceLocation layout;
        if (x == 0 && y == 0) {
            layout = new ResourceLocation("bladditions", "dungeon/start/" + dungeonId);
        } else {
            return true;
        }

        Template template = this.world.getSaveHandler().getStructureTemplateManager().getTemplate(this.world.getMinecraftServer(), layout);
        template.addBlocksToWorld(this.world, generatePos.add(offsetX, 0, offsetZ), new DungeonRoomProcessor(), settings, 2);
        return true;
    }


    @Override
    public void generate() {
    }
}
