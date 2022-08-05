package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.misc.BlockBarrier;
import thebetweenlands.common.block.container.BlockBarrel;

import java.util.List;

@Mixin(value = BlockBarrier.class, remap = false)
public class MixinBlockBarrier extends Block {
    public MixinBlockBarrier(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
    }

    /**
     * @author SC
     */
    @Overwrite
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB mask, List list, Entity collidingEntity, boolean isActualState) {
        if (collidingEntity != null && collidingEntity instanceof EntityLivingBase && !(collidingEntity instanceof EntityPlayer) && !(collidingEntity instanceof EntityMob) && collidingEntity.getRecursivePassengersByType(EntityPlayer.class).isEmpty()) {
            int a = 1;
            if (world.getBlockState(pos.down(a)).getBlock() != BlocksTC.pavingStoneBarrier) {
                ++a;
            }

            if (world.getRedstonePowerFromNeighbors(pos.down(a)) == 0) {
                list.add(FULL_BLOCK_AABB.offset(pos));
            }
        }

    }
}
