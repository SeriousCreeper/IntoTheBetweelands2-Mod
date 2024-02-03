package com.seriouscreeper.bladditions.mixins.modsupport.botania.functional_flora;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.block.farming.BlockGenericCrop;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.block.subtile.functional.SubTileAgricarnation;

@Mixin(value = SubTileAgricarnation.class, remap = false)
public class MixinSubTileAgricarnation extends SubTileFunctional {
    /**
     * @author
     * @reason
     */
    @Overwrite
    private boolean isPlant(BlockPos pos) {
        IBlockState state = this.supertile.getWorld().getBlockState(pos);
        Block block = state.getBlock();
        if (block == Blocks.GRASS || block == Blocks.LEAVES || block == Blocks.LEAVES2 || block instanceof BlockBush && !(block instanceof BlockGenericCrop) && !(block instanceof BlockCrops) && !(block instanceof BlockSapling)) {
            return false;
        } else {
            Material mat = state.getMaterial();
            return mat != null && (mat == Material.PLANTS || mat == Material.CACTUS || mat == Material.GRASS || mat == Material.LEAVES || mat == Material.GOURD) && block instanceof IGrowable && ((IGrowable)block).canGrow(this.supertile.getWorld(), pos, this.supertile.getWorld().getBlockState(pos), this.supertile.getWorld().isRemote);
        }
    }
}
