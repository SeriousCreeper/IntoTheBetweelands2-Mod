package com.seriouscreeper.bladditions.mixins.modsupport.botania;

import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.registries.BlockRegistry;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.common.block.tile.TileSpecialFlower;

@Mixin(value = TileSpecialFlower.class, remap = false)
public class MixinTileSpecialFlower extends TileMod {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getSlowdownFactor() {
        Block below = this.world.getBlockState(this.getPos().down()).getBlock();
        if (below == BlockRegistry.MUD) {
            return 10;
        } else {
            if (below == BlockRegistry.DEAD_GRASS) {
                return 5;
            }

            return 0;
        }
    }
}
