package com.seriouscreeper.bladditions.blocks;

import com.seriouscreeper.bladditions.BLAdditions;
import com.seriouscreeper.bladditions.tiles.PatchedTilePotionSprayer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.common.blocks.BlockTCDevice;
import thaumcraft.common.blocks.IBlockEnabled;
import thaumcraft.common.blocks.IBlockFacing;

public class PatchedBlockPotionSprayer extends BlockTCDevice implements IBlockFacing, IBlockEnabled {
    public PatchedBlockPotionSprayer() {
        super(Material.IRON, PatchedTilePotionSprayer.class, "potion_sprayer");
        this.setSoundType(SoundType.METAL);
    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        } else {
            player.openGui(BLAdditions.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
    }

    public int damageDropped(IBlockState state) {
        return 0;
    }
}
