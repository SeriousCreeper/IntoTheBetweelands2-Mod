package com.seriouscreeper.bladditions.mixins;

import com.seriouscreeper.bladditions.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thebetweenlands.common.world.storage.location.LocationStorage;

@Mixin(value = BlockPistonBase.class, remap = true)
public class MixinBlockPistonBase {
    @Final
    @Shadow public static final PropertyBool EXTENDED = PropertyBool.create("extended");

    /**
     * @author SC
     */
    @Overwrite
    public static boolean canPush(IBlockState blockStateIn, World worldIn, BlockPos pos, EnumFacing facing, boolean destroyBlocks, EnumFacing p_185646_5_) {
        Block block = blockStateIn.getBlock();

        if(CommonProxy.IsWithinLocation(worldIn, pos, true)) {
            return false;
        }

        if (block == Blocks.OBSIDIAN) {
            return false;
        } else if (!worldIn.getWorldBorder().contains(pos)) {
            return false;
        } else if (pos.getY() < 0 || facing == EnumFacing.DOWN && pos.getY() == 0) {
            return false;
        } else if (pos.getY() > worldIn.getHeight() - 1 || facing == EnumFacing.UP && pos.getY() == worldIn.getHeight() - 1) {
            return false;
        } else {
            if (block != Blocks.PISTON && block != Blocks.STICKY_PISTON) {
                if (blockStateIn.getBlockHardness(worldIn, pos) == -1.0F) {
                    return false;
                }

                switch(blockStateIn.getPushReaction()) {
                    case BLOCK:
                        return false;
                    case DESTROY:
                        return destroyBlocks;
                    case PUSH_ONLY:
                        return facing == p_185646_5_;
                }
            } else if ((Boolean)blockStateIn.getValue(EXTENDED)) {
                return false;
            }

            return !block.hasTileEntity(blockStateIn);
        }
    }
}
