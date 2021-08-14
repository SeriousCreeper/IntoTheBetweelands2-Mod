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

        if(args.length >= 4) {
            int x = parseInt(args[1]);
            int y = parseInt(args[2]);
            int z = parseInt(args[3]);
            pos = new BlockPos(x, y, z);
        }

        switch(structName) {
            case "cragrock_tower":
                new WorldGenCragrockTower().generate(world, world.rand, pos);
                break;

            case "wight_fortress":
                new WorldGenWightFortress().generate(world, world.rand, pos);
                break;

            case "sludgeon":
                DecoratorPositionProvider provider = new DecoratorPositionProvider();
                provider.init(world, world.getBiome(pos), null, world.rand, pos.getX(), pos.getY(), pos.getZ());
                DecorationHelper.generateSludgePlainsClearingDungeon(provider);
                break;

            case "raised_island":
                break;
        }
    }
}
