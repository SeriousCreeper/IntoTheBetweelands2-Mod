package com.seriouscreeper.bladditions.mixins.modsupport.botania.functional_flora;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import thebetweenlands.common.registries.BlockRegistry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.BiomeStoneVariant;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.subtile.functional.SubTileMarimorphosis;

@Mixin(value = SubTileMarimorphosis.class, remap = false)
public class MixinSubTileMarimorphosis extends SubTileFunctional {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public IBlockState getStoneToPut(BlockPos coords) {
        int i = this.supertile.getWorld().rand.nextInt(8);
        BiomeStoneVariant variant = BiomeStoneVariant.values()[i];
        return ModFluffBlocks.biomeStoneA.getDefaultState().withProperty(BotaniaStateProps.BIOMESTONE_VARIANT, variant);
    }

    @ModifyArg(method = "getCoordsToPut", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/pattern/BlockStateMatcher;forBlock(Lnet/minecraft/block/Block;)Lnet/minecraft/block/state/pattern/BlockStateMatcher;"), index = 0)
    private Block modifyDeadBush(Block blockIn) {
        return BlockRegistry.BETWEENSTONE;
    }
}
