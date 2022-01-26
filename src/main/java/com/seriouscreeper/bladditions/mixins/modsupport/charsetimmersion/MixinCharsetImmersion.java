package com.seriouscreeper.bladditions.mixins.modsupport.charsetimmersion;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.asie.charset.module.immersion.stacks.BlockStacks;
import pl.asie.charset.module.immersion.stacks.CharsetImmersionStacks;
import thebetweenlands.common.registries.BlockRegistry;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = CharsetImmersionStacks.class, remap = false)
public class MixinCharsetImmersion {
    @Shadow
    private static final Map<Class, Boolean> overridesOnBlockActivated = new HashMap();

    @Shadow
    public BlockStacks blockStacks;


    @Inject(method = "onRightClickBlock", at = @At("HEAD"), cancellable = true)
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event, CallbackInfo ci) {
        IBlockState state = event.getWorld().getBlockState(event.getPos());

        if(state.getBlock() == BlockRegistry.MOSS || state.getBlock() == Blocks.FIRE || event.getWorld().getBlockState(event.getPos().up()).getBlock() == Blocks.FIRE) {
            ci.cancel();
        }
    }
}
