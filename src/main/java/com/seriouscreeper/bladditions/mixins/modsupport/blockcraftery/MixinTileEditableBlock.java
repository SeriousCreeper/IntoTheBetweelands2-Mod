package com.seriouscreeper.bladditions.mixins.modsupport.blockcraftery;

import epicsquid.blockcraftery.tile.TileEditableBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.arcaneworld.gen.dungeon.dimension.DungeonDimensionProvider;

@Mixin(value = TileEditableBlock.class, remap = false)
public class MixinTileEditableBlock {
    @Inject(method = "activate", at = @At("HEAD"), cancellable = true)
    private void injectActivate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> cir) {
        if(world.provider instanceof DungeonDimensionProvider && !player.isCreative()) {
            cir.setReturnValue(false);
        }
    }
}
