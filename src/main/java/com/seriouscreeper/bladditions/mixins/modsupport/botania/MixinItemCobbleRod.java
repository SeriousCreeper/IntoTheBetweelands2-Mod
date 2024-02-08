package com.seriouscreeper.bladditions.mixins.modsupport.botania;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.registries.BlockRegistry;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.rod.ItemCobbleRod;
import vazkii.botania.common.item.rod.ItemDirtRod;

import javax.annotation.Nonnull;

@Mixin(value = ItemCobbleRod.class, remap = false)
public class MixinItemCobbleRod {
    /**
     * @author
     * @reason
     */
    @Nonnull
    @Overwrite
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) {
        return ItemDirtRod.place(player, world, pos, hand, side, par8, par9, par10, BlockRegistry.BETWEENSTONE, 150, 0.3F, 0.3F, 0.3F);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean provideBlock(EntityPlayer player, ItemStack requestor, ItemStack stack, Block block, int meta, boolean doit) {
        if (block == BlockRegistry.BETWEENSTONE) {
            return !doit || ManaItemHandler.requestManaExactForTool(requestor, player, 150, true);
        } else {
            return false;
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getBlockCount(EntityPlayer player, ItemStack requestor, ItemStack stack, Block block, int meta) {
        return block == BlockRegistry.BETWEENSTONE ? -1 : 0;
    }
}
