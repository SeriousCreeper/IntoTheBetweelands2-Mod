package com.seriouscreeper.bladditions.mixins.modsupport.roots;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualSummonCreatures;
import epicsquid.roots.network.fx.MessageFrostLandsProgressFX;
import epicsquid.roots.ritual.RitualFrostLands;
import epicsquid.roots.ritual.RitualSummonCreatures;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thebetweenlands.common.registries.BlockRegistry;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = EntityRitualSummonCreatures.class, remap = false)
public class MixinEntityRitualSummonCreatures extends EntityRitualBase {
    @Shadow
    private RitualSummonCreatures ritual;

    public MixinEntityRitualSummonCreatures(World worldIn) {
        super(worldIn);
    }


    @Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true)
    public void onUpdate(CallbackInfo ci) {
        super.onUpdate();

        ci.cancel();
    }
}
