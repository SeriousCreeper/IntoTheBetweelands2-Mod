package com.seriouscreeper.bladditions.mixins.modsupport.embers;

import com.seriouscreeper.bladditions.tiles.PatchedTileEntitySteamEngine;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import teamroots.embers.block.BlockSteamEngine;

@Mixin(value = BlockSteamEngine.class)
public class MixinBlockSteamEngine {
    /**
     * @author SC
     */
    @Overwrite
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new PatchedTileEntitySteamEngine();
    }

    /**
     * @author SC
     */
    @Overwrite
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        PatchedTileEntitySteamEngine p = (PatchedTileEntitySteamEngine)world.getTileEntity(pos);
        p.updateNearby();
        p.markDirty();
    }
}
