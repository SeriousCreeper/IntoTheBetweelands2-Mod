package com.seriouscreeper.bladditions.mixins.modsupport.reccomplex;

import ivorius.reccomplex.world.gen.feature.structure.generic.transformers.TransformerRuins;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.registries.BlockRegistry;

import javax.annotation.Nonnull;
import java.util.Random;

@Mixin(value = TransformerRuins.class, remap = false)
public class MixinTransformerRuins {
    @Shadow public float blockErosion;
    @Shadow public float vineGrowth;
    @Shadow public float cobwebGrowth;

    /**
     * @author SC
     * @reason replace with non-vanilla blocks
     */
    @Overwrite
    public void decayBlock(World world, Random random, IBlockState state, BlockPos pos, StructureBoundingBox boundingBox) {
        IBlockState newState = state;
        if (random.nextFloat() < this.blockErosion) {
            if (state.getBlock() == Blocks.STONEBRICK && state.getProperties().get(BlockStoneBrick.VARIANT) != BlockStoneBrick.EnumType.MOSSY) {
                newState = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED);
            } else if (state.getBlock() == Blocks.SANDSTONE) {
                newState = Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, net.minecraft.block.BlockSandStone.EnumType.DEFAULT);
            }
        }

        newState = this.maybeErodeShape(random, newState, Blocks.STONEBRICK, BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT, Blocks.STONE_BRICK_STAIRS);
        newState = this.maybeErodeShape(random, newState, Blocks.PLANKS, BlockPlanks.VARIANT, net.minecraft.block.BlockPlanks.EnumType.OAK, Blocks.OAK_STAIRS);
        newState = this.maybeErodeShape(random, newState, Blocks.PLANKS, BlockPlanks.VARIANT, net.minecraft.block.BlockPlanks.EnumType.SPRUCE, Blocks.SPRUCE_STAIRS);
        newState = this.maybeErodeShape(random, newState, Blocks.PLANKS, BlockPlanks.VARIANT, net.minecraft.block.BlockPlanks.EnumType.BIRCH, Blocks.BIRCH_STAIRS);
        newState = this.maybeErodeShape(random, newState, Blocks.PLANKS, BlockPlanks.VARIANT, net.minecraft.block.BlockPlanks.EnumType.JUNGLE, Blocks.JUNGLE_STAIRS);
        newState = this.maybeErodeShape(random, newState, Blocks.PLANKS, BlockPlanks.VARIANT, net.minecraft.block.BlockPlanks.EnumType.ACACIA, Blocks.ACACIA_STAIRS);
        newState = this.maybeErodeShape(random, newState, Blocks.PLANKS, BlockPlanks.VARIANT, net.minecraft.block.BlockPlanks.EnumType.DARK_OAK, Blocks.DARK_OAK_STAIRS);
        newState = this.maybeErodeShape(random, newState, Blocks.SANDSTONE, null, null, Blocks.SANDSTONE_STAIRS);
        newState = this.maybeErodeShape(random, newState, Blocks.COBBLESTONE, null, null, Blocks.STONE_STAIRS);
        newState = this.maybeErodeShape(random, newState, Blocks.QUARTZ_BLOCK, null, null, Blocks.QUARTZ_STAIRS);
        newState = this.maybeErodeShape(random, newState, Blocks.RED_SANDSTONE, null, null, Blocks.RED_SANDSTONE_STAIRS);
        newState = this.maybeErodeShape(random, newState, Blocks.NETHER_BRICK, null, null, Blocks.NETHER_BRICK_STAIRS);
        if (random.nextFloat() < this.vineGrowth) {
            if (newState.getBlock() == Blocks.STONEBRICK) {
                newState = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY);
            } else if (newState.getBlock() == Blocks.COBBLESTONE) {
                newState = Blocks.MOSSY_COBBLESTONE.getDefaultState();
            } else if (newState.getBlock() == Blocks.COBBLESTONE_WALL) {
                newState = Blocks.COBBLESTONE_WALL.getDefaultState().withProperty(BlockWall.VARIANT, net.minecraft.block.BlockWall.EnumType.MOSSY);
            }
        }

        if (newState.getBlock() == Blocks.AIR) {
            newState = null;

            label66:
            for(EnumFacing direction : EnumFacing.HORIZONTALS) {
                if (random.nextFloat() < this.vineGrowth && boundingBox.isVecInside(pos.offset(direction.getOpposite())) && BlockRegistry.MOSS.canPlaceBlockOnSide(world, pos, direction)) {
                    IBlockState downState = world.getBlockState(pos.offset(EnumFacing.DOWN));
                    downState = downState.getBlock() == BlockRegistry.MOSS ? downState : BlockRegistry.MOSS.getDefaultState();
                    downState = downState.withProperty(BlockVine.getPropertyFor(direction.getOpposite()), true);
                    int length = 1;

                    for(int y = 0; y < length; ++y) {
                        BlockPos downPos = pos.offset(EnumFacing.DOWN, y);
                        if (world.getBlockState(downPos).getMaterial() != Material.AIR) {
                            break label66;
                        }

                        world.setBlockState(downPos, downState, 3);
                    }
                    break;
                }

                if (random.nextFloat() < this.cobwebGrowth && this.hasAirNeighbors(world, pos, 3)) {
                    newState = null;
                    world.setBlockState(pos, Blocks.WEB.getDefaultState(), 3);
                }
            }
        }

        if (newState != null && state != newState) {
            world.setBlockState(pos, newState, 3);
        }
    }

    @Shadow
    public boolean hasAirNeighbors(World world, BlockPos pos, int sides) {
        return false;
    }

    @Shadow
    @Nonnull
    protected IBlockState maybeErodeShape(Random random, IBlockState newState, Block block, PropertyEnum<?> variant, Object value, Block oakStairs) {
        return null;
    }
}
