package com.seriouscreeper.bladditions.mixins.modsupport.growthcraft;

import growthcraft.bees.common.tileentity.TileEntityBeeBox;
import growthcraft.core.shared.tileentity.feature.IItemOperable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = TileEntityBeeBox.class, remap = false)
public class MixinTileEntityBeeBox {
    /**
     * @author SC
     * @reason remove interaction with bee box other than opening UI
     */
    @Overwrite
    public boolean tryPlaceItem(IItemOperable.Action action, EntityPlayer player, ItemStack stack) {
        return false;
    }
}
