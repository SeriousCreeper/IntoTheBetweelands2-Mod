package com.seriouscreeper.bladditions.mixins.modsupport.thaumicaugmentation;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import teamroots.embers.RegistryManager;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.Trajectory;
import thecodex6824.thaumicaugmentation.common.item.foci.FocusEffectLight;

import javax.annotation.Nullable;

@Mixin(value = FocusEffectLight.class, remap = false)
public class MixinFocusEffectLight extends FocusEffect {
    /**
     * @author SC
     * @reason
     */
    @Overwrite
    protected boolean placeLightSource(BlockPos pos, EnumFacing side, int intensity) {
        World world = this.getPackage().world;
        return world.setBlockState(pos, RegistryManager.glow.getDefaultState());
    }

    @Shadow
    public boolean execute(RayTraceResult rayTraceResult, @Nullable Trajectory trajectory, float v, int i) {
        return false;
    }

    @Shadow
    public void renderParticleFX(World world, double v, double v1, double v2, double v3, double v4, double v5) {

    }

    @Shadow
    public int getComplexity() {
        return 0;
    }

    @Shadow
    public Aspect getAspect() {
        return null;
    }

    @Shadow
    public String getKey() {
        return null;
    }

    @Shadow
    public String getResearch() {
        return null;
    }
}
