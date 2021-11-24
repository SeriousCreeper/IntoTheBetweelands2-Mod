package com.seriouscreeper.bladditions.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thebetweenlands.common.world.gen.biome.decorator.DecorationHelper;
import thebetweenlands.common.world.gen.biome.decorator.DecoratorPositionProvider;
import thebetweenlands.common.world.gen.feature.structure.WorldGenCragrockTower;
import thebetweenlands.common.world.gen.feature.structure.WorldGenWightFortress;

import java.util.Random;

public class BLAdditionsCommands extends CommandBase {
    @Override
    public String getName() {
        return "blgen";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return null;
    }


    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }


    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 0)
            return;

        String structName = args[0];

        EntityPlayer exec;
        World world = sender.getEntityWorld();
        BlockPos pos = sender.getPosition();
        Random rand;

        if(args.length >= 4) {
            int x = parseInt(args[1]);
            int y = parseInt(args[2]);
            int z = parseInt(args[3]);
            pos = new BlockPos(x, y, z);
        }

        if(args.length >= 5) {
            rand = new Random(parseInt(args[4]));
        } else if(args.length == 2) {
            rand = new Random(parseInt(args[1]));
        } else {
            rand = world.rand;
        }

        switch(structName) {
            case "cragrock_tower":
                new WorldGenCragrockTower().generate(world, rand, pos);
                break;

            case "wight_fortress":
                new WorldGenWightFortress().generate(world, rand, pos);
                break;

            case "sludgeon":
                DecoratorPositionProvider provider = new DecoratorPositionProvider();
                provider.init(world, world.getBiome(pos), null, rand, pos.getX(), pos.getY(), pos.getZ());
                DecorationHelper.GEN_SLUDGE_WORM_DUNGEON.generate(world, rand, pos);
                //DecorationHelper.generateSludgePlainsClearingDungeon(provider);
                break;

            case "raised_island":
                break;
        }
    }
}
