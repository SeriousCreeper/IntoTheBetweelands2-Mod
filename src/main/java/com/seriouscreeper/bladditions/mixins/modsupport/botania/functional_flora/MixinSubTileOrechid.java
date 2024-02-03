package com.seriouscreeper.bladditions.mixins.modsupport.botania.functional_flora;

import com.google.common.base.Predicate;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.registries.BlockRegistry;
import vazkii.botania.common.block.subtile.functional.SubTileOrechid;

@Mixin(value = SubTileOrechid.class, remap = false)
public class MixinSubTileOrechid {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public Predicate<IBlockState> getReplaceMatcher() {
        return (state) -> {
            return state.getBlock() == BlockRegistry.BETWEENSTONE;
        };
    }
}
