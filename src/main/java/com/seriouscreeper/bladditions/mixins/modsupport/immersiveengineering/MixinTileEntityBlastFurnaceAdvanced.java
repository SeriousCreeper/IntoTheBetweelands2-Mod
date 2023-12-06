package com.seriouscreeper.bladditions.mixins.modsupport.immersiveengineering;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.stone.TileEntityBlastFurnace;
import blusunrize.immersiveengineering.common.blocks.stone.TileEntityBlastFurnaceAdvanced;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.registries.BlockRegistry;

@Mixin(value = TileEntityBlastFurnaceAdvanced.class, remap = false)
public class MixinTileEntityBlastFurnaceAdvanced extends TileEntityBlastFurnace {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public ItemStack getOriginalBlock() {
        return this.pos == 31 ? new ItemStack(BlockRegistry.SYRMORITE_HOPPER) : new ItemStack(IEContent.blockStoneDecoration, 1, 2);
    }
}
