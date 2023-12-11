package com.seriouscreeper.bladditions.mixins.modsupport.immersiveengineering;

import blusunrize.immersiveengineering.common.blocks.metal.TileEntityBottlingMachine;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConveyorBelt;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityFluidPump;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import teamroots.embers.tileentity.TileEntityPumpBottom;
import teamroots.embers.tileentity.TileEntityPumpTop;

@Mixin(value = TileEntityBottlingMachine.class, remap = false)
public class MixinTileEntityBottlingMachine extends TileEntity {
    @Inject(method = "replaceStructureBlock", at = @At("RETURN"))
    private void injectReplaceStructureBlock(BlockPos pos, IBlockState state, ItemStack stack, int h, int l, int w, CallbackInfo ci) {

    }
}
