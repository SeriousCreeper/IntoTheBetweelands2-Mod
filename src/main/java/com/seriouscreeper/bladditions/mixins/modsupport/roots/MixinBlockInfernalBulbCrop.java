package com.seriouscreeper.bladditions.mixins.modsupport.roots;

import com.seriouscreeper.bladditions.mixins.modsupport.mysticallib.MixinBlockCropBase;
import epicsquid.roots.block.BlockInfernalBulbCrop;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import thebetweenlands.api.aspect.IAspectType;
import thebetweenlands.api.block.IAspectFogBlock;
import thebetweenlands.common.registries.AspectRegistry;

import java.util.Iterator;
import java.util.Random;

@Mixin(value = BlockInfernalBulbCrop.class, remap = false)
public class MixinBlockInfernalBulbCrop  extends MixinBlockCropBase {
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if(!canGrow(worldIn, pos, state, worldIn.isRemote)) {
            return;
        }

        super.updateTick(worldIn, pos, state, rand);
        if (worldIn.isAreaLoaded(pos, 1)) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                float f = getGrowthChance(this, worldIn, pos);
                if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int)(10.0F / f) + 1) == 0)) {
                    worldIn.setBlockState(pos, this.withAge(i + 1), 2);
                    ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
                }
            }
        }
    }

    @Override
    public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient) {
        boolean hasSource = false;
        Iterator var6 = BlockPos.getAllInBoxMutable(pos.add(-6, -1, -6), pos.add(6, 0, 6)).iterator();

        while(var6.hasNext()) {
            BlockPos.MutableBlockPos checkPos = (BlockPos.MutableBlockPos)var6.next();
            if (world.isBlockLoaded(checkPos)) {
                IBlockState offsetState = world.getBlockState(checkPos);
                Block offsetBlock = offsetState.getBlock();
                if (offsetBlock instanceof IAspectFogBlock) {
                    IAspectType aspectType = ((IAspectFogBlock)offsetBlock).getAspectFogType(world, checkPos, offsetState);
                    if (aspectType != null) {
                        if (aspectType != AspectRegistry.FIRNALAZ) {
                            continue;
                        }

                        hasSource = true;
                        break;
                    }
                }
            }
        }

        return !this.isMaxAge(state) && !isDecayed(world, pos) && hasSource;
    }
}