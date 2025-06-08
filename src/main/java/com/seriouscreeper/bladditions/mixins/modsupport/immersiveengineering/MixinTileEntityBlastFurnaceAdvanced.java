package com.seriouscreeper.bladditions.mixins.modsupport.immersiveengineering;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityBlastFurnacePreheater;
import blusunrize.immersiveengineering.common.blocks.stone.TileEntityBlastFurnace;
import blusunrize.immersiveengineering.common.blocks.stone.TileEntityBlastFurnaceAdvanced;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.registries.BlockRegistry;

import javax.annotation.Nonnull;

@Mixin(value = TileEntityBlastFurnaceAdvanced.class, remap = false)
public class MixinTileEntityBlastFurnaceAdvanced extends TileEntityBlastFurnace {
    /**
     * @author
     * @reason
     */
    @Nonnull
    @Overwrite
    public ItemStack getOriginalBlock() {
        return this.pos == 31 ? new ItemStack(BlockRegistry.SYRMORITE_HOPPER) : new ItemStack(IEContent.blockStoneDecoration, 1, 2);
    }


    /**
     * @author SC
     * @reason make it not work without preheaters
     */
    @Overwrite
    protected int getProcessSpeed() {
        int i = 0;

        for(int j = 0; j < 2; ++j) {
            EnumFacing phf = j == 0 ? this.facing.rotateY() : this.facing.rotateYCCW();
            BlockPos pos = this.getPos().add(0, -1, 0).offset(phf, 2);
            TileEntity te = Utils.getExistingTileEntity(this.world, pos);
            if (te instanceof TileEntityBlastFurnacePreheater && ((TileEntityBlastFurnacePreheater)te).facing == phf.getOpposite()) {
                i += ((TileEntityBlastFurnacePreheater)te).doSpeedup();
            }
        }

        return i;
    }
}
