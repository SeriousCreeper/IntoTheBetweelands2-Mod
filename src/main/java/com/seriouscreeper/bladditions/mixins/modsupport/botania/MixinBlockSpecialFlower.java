package com.seriouscreeper.bladditions.mixins.modsupport.botania;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.registries.BlockRegistry;
import vazkii.botania.common.block.BlockSpecialFlower;
import vazkii.botania.common.block.ModBlocks;

@Mixin(value = BlockSpecialFlower.class, remap = false)
public class MixinBlockSpecialFlower extends BlockFlower {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return world.getBlockState(pos.down()).getBlock() == ModBlocks.redStringRelay || world.getBlockState(pos.down()).getBlock() == BlockRegistry.MUD || super.canPlaceBlockAt(world, pos);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    protected boolean canSustainBush(IBlockState state) {
        return state.getBlock() == ModBlocks.redStringRelay || state.getBlock() == BlockRegistry.MUD || super.canSustainBush(state);
    }

    @Shadow
    public EnumFlowerColor getBlockType() {
        return null;
    }
}
