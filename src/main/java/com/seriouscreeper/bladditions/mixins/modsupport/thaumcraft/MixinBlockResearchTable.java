package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thaumcraft.Thaumcraft;
import thaumcraft.common.blocks.crafting.BlockResearchTable;

@Mixin(value = BlockResearchTable.class, remap = true)
public class MixinBlockResearchTable {
    /**
     * @author SC
     */
    @Overwrite
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }
}
