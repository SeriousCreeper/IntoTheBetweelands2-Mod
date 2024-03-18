package com.seriouscreeper.bladditions.mixins.modsupport.arcaneworld;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import party.lemons.arcaneworld.gen.dungeon.generation.DungeonGenerator;
import party.lemons.arcaneworld.gen.dungeon.generation.DungeonRoomProcessor;
import party.lemons.arcaneworld.gen.dungeon.generation.RoomDirection;
import party.lemons.arcaneworld.gen.dungeon.generation.TickerDungeon;
import party.lemons.lemonlib.ticker.TickerHandler;

@Mixin(value = DungeonGenerator.class, remap = false)
public class MixinDungeonGenerator {
    @Shadow
    private RoomDirection[][] directions;
    @Shadow
    private BlockPos origin;
    @Shadow
    private int width;
    @Shadow
    private int height;
    @Shadow
    private World world;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean generateRoom(int x, int y) {
        RoomDirection direction = this.directions[x][y];
        if (!direction.isSealed()) {
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

            PlacementSettings settings = (new PlacementSettings()).setRotation(direction.getRotation()).setMirror(direction.getMirror());
            ResourceLocation layout;
            if (x == 0 && y == 0) {
                layout = new ResourceLocation("bladditions", "dungeon/start/open_start_1");
            } else {
                return true;
            }

            Template template = this.world.getSaveHandler().getStructureTemplateManager().getTemplate(this.world.getMinecraftServer(), layout);
            template.addBlocksToWorld(this.world, generatePos.add(offsetX, 0, offsetZ), new DungeonRoomProcessor(), settings, 2);
            return true;
        } else {
            return false;
        }
    }


    @Shadow
    public ResourceLocation getRoomLayout(RoomDirection direction) {
        return null;
    }


    /**
     * @author
     * @reason
     */
    @Overwrite
    public void generate() {
    }
}
