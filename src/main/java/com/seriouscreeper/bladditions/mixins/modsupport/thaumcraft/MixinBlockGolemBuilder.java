package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import com.joshiegemfinder.betweenlandsredstone.ModBlocks;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.crafting.BlockGolemBuilder;
import thebetweenlands.common.registries.BlockRegistry;

@Mixin(value = BlockGolemBuilder.class, remap = false)
public class MixinBlockGolemBuilder {
    @Shadow public static boolean ignore = false;

    /**
     * @author SC
     * @reason
     */
    @Overwrite
    public static void destroy(World worldIn, BlockPos pos, IBlockState state, BlockPos startpos) {
        if (!ignore && !worldIn.isRemote) {
            ignore = true;

            for(int a = -1; a <= 1; ++a) {
                for(int b = 0; b <= 1; ++b) {
                    for(int c = -1; c <= 1; ++c) {
                        if (pos.add(a, b, c) != startpos) {
                            IBlockState bs = worldIn.getBlockState(pos.add(a, b, c));
                            if (bs.getBlock() == BlocksTC.placeholderBars) {
                                worldIn.setBlockState(pos.add(a, b, c), Blocks.IRON_BARS.getDefaultState());
                            }

                            if (bs.getBlock() == BlocksTC.placeholderAnvil) {
                                worldIn.setBlockState(pos.add(a, b, c), BlockRegistry.SYRMORITE_BLOCK.getDefaultState());
                            }

                            if (bs.getBlock() == BlocksTC.placeholderCauldron) {
                                worldIn.setBlockState(pos.add(a, b, c), BlocksTC.crucible.getDefaultState());
                            }

                            if (bs.getBlock() == BlocksTC.placeholderTable) {
                                worldIn.setBlockState(pos.add(a, b, c), BlocksTC.tableStone.getDefaultState());
                            }
                        }
                    }
                }
            }

            if (pos != startpos) {
                worldIn.setBlockState(pos, ModBlocks.SCABYST_PISTON.getDefaultState().withProperty(BlockPistonBase.FACING, EnumFacing.UP));
            }

            ignore = false;
        }
    }
}
