package com.seriouscreeper.bladditions.mixins.modsupport.firespreadtweaks;

import com.natamus.firespreadtweaks.events.FireSpreadEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thebetweenlands.common.registries.BlockRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(value = FireSpreadEvent.class)
public class MixinFireSpreadEvent {
    @Shadow private static List<Block> fireblocks;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void injected(CallbackInfo ci) {
        fireblocks = new ArrayList<>(Arrays.asList(Blocks.NETHERRACK, Blocks.MAGMA, Blocks.SOUL_SAND, BlockRegistry.PEAT_SMOULDERING, BlockRegistry.PEAT, BlockRegistry.MUD_TOWER_BRAZIER));
    }
}
