package com.seriouscreeper.bladditions.mixins;

import crafttweaker.mc1120.brackets.BracketHandlerBlockState;
import net.minecraft.block.BlockFence;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.block.structure.BlockFenceBetweenlands;

import javax.annotation.Nullable;

@Mixin(value = EntityLeashKnot.class)
public class MixinLeashKnot extends EntityHanging {
    public MixinLeashKnot(World worldIn) {
        super(worldIn);
    }

    /**
     * @author SC
     */
    @Overwrite
    public boolean onValidSurface() {
        return this.world.getBlockState(this.hangingPosition).getBlock() instanceof BlockFence || this.world.getBlockState(this.hangingPosition).getBlock() instanceof BlockFenceBetweenlands;
    }

    @Shadow
    public int getWidthPixels() {
        return 0;
    }

    @Shadow
    public int getHeightPixels() {
        return 0;
    }

    @Shadow
    public void onBroken(@Nullable Entity entity) {

    }

    @Shadow
    public void playPlaceSound() {

    }
}
