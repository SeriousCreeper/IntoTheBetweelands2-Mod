package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import scala.tools.asm.Opcodes;
import thebetweenlands.common.block.plant.BlockPlant;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.world.gen.feature.structure.WorldGenWightFortress;

import java.util.Random;

@Mixin(value = WorldGenWightFortress.class, remap = false)
public abstract class MixinWorldGenWightFortress {
    //@Shadow private int direction;

    //@Redirect(method = "generateStructure", at = @At(value = "FIELD", target = "Lthebetweenlands/common/world/gen/feature/structure/WorldGenWightFortress;direction:I", opcode = Opcodes.PUTFIELD))
    //public void generateStructure(WorldGenWightFortress wightFortress, int direction) {
    //    this.direction = 0;
    //}

    /**
     * @author SC
     * @reason protect metal blocks
     */
    @Overwrite
    protected boolean isProtectedBlock(IBlockState state) {
        Block block = state.getBlock();
        return block != Blocks.AIR && block != BlockRegistry.MOB_SPAWNER && block != BlockRegistry.LOOT_POT && block != BlockRegistry.ROOT && !(block instanceof BlockPlant) && block != BlockRegistry.WEEDWOOD_CHEST && block != BlockRegistry.ITEM_CAGE && block != BlockRegistry.POSSESSED_BLOCK && block != BlockRegistry.WEAK_POLISHED_LIMESTONE && block != BlockRegistry.WEAK_BETWEENSTONE_TILES && block != BlockRegistry.WEAK_MOSSY_BETWEENSTONE_TILES;
    }
}
