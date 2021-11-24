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
    public TileEntity func_149915_a(World worldIn, int meta) {
        System.out.println("foo2");

        return new PatchedTileEntitySteamEngine();
    }

    /**
     * @author SC
     */
    @Overwrite
    public void func_189540_a(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        System.out.println("bar2");

        PatchedTileEntitySteamEngine p = (PatchedTileEntitySteamEngine)world.getTileEntity(pos);
        p.updateNearby();
        p.markDirty();
    }
}
