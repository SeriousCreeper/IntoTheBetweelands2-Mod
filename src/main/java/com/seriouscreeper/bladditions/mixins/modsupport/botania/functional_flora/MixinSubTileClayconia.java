package com.seriouscreeper.bladditions.mixins.modsupport.botania.functional_flora;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.registries.BlockRegistry;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.block.subtile.functional.SubTileClayconia;
import vazkii.botania.common.core.handler.ConfigHandler;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = SubTileClayconia.class, remap = false)
public class MixinSubTileClayconia extends SubTileFunctional {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onUpdate() {
        super.onUpdate();
        if (!this.supertile.getWorld().isRemote && this.ticksExisted % 5 == 0 && this.mana >= 80) {
            BlockPos coords = this.getCoordsToPut();
            if (coords != null) {
                if (ConfigHandler.blockBreakParticles) {
                    this.supertile.getWorld().playEvent(2001, coords, Block.getStateId(BlockRegistry.SILT.getDefaultState()));
                }

                this.supertile.getWorld().setBlockState(coords, BlockRegistry.MUD.getDefaultState());
                this.mana -= 80;
            }
        }
    }

    @Shadow
    public int getRange() {
        return 5;
    }

    @Shadow
    public int getRangeY() {
        return 3;
    }


    /**
     * @author
     * @reason
     */
    @Overwrite
    public BlockPos getCoordsToPut() {
        List<BlockPos> possibleCoords = new ArrayList();
        int range = this.getRange();
        int rangeY = this.getRangeY();

        for(int i = -range; i < range + 1; ++i) {
            for(int j = -rangeY; j < rangeY + 1; ++j) {
                for(int k = -range; k < range + 1; ++k) {
                    BlockPos pos = this.supertile.getPos().add(i, j, k);
                    Block block = this.supertile.getWorld().getBlockState(pos).getBlock();
                    if (block == BlockRegistry.SILT) {
                        possibleCoords.add(pos);
                    }
                }
            }
        }

        if (possibleCoords.isEmpty()) {
            return null;
        } else {
            return (BlockPos)possibleCoords.get(this.supertile.getWorld().rand.nextInt(possibleCoords.size()));
        }
    }
}
