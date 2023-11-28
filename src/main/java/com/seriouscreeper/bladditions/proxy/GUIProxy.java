package com.seriouscreeper.bladditions.proxy;

import com.seriouscreeper.bladditions.containers.PatchedContainerPotionSprayer;
import com.seriouscreeper.bladditions.containers.PatchedContainerResearchTable;
import com.seriouscreeper.bladditions.containers.PatchedContainerSpa;
import com.seriouscreeper.bladditions.guis.PatchedGuiPotionSprayer;
import com.seriouscreeper.bladditions.guis.PatchedGuiResearchTable;
import com.seriouscreeper.bladditions.guis.PatchedGuiSpa;
import com.seriouscreeper.bladditions.tiles.PatchedTilePotionSprayer;
import com.seriouscreeper.bladditions.tiles.PatchedTileResearchTable;
import com.seriouscreeper.bladditions.tiles.PatchedTileSpa;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GUIProxy implements IGuiHandler {
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (world instanceof WorldClient) {
            switch (ID) {
                case 1:
                    return new PatchedGuiPotionSprayer(player.inventory, (PatchedTilePotionSprayer)world.getTileEntity(new BlockPos(x, y, z)));
                case 2:
                    return new PatchedGuiResearchTable(player, (PatchedTileResearchTable)world.getTileEntity(new BlockPos(x, y, z)));
                case 3:
                    return new PatchedGuiSpa(player.inventory, (PatchedTileSpa)world.getTileEntity(new BlockPos(x, y, z)));
            }
        }

        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 1:
                return new PatchedContainerPotionSprayer(player.inventory, (PatchedTilePotionSprayer)world.getTileEntity(new BlockPos(x, y, z)));
            case 2:
                return new PatchedContainerResearchTable(player.inventory, (PatchedTileResearchTable)world.getTileEntity(new BlockPos(x, y, z)));
            case 3:
                return new PatchedContainerSpa(player.inventory, (PatchedTileSpa) world.getTileEntity(new BlockPos(x, y, z)));
        }

        return null;
    }
}
