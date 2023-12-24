package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thebetweenlands.common.world.gen.biome.decorator.BiomeDecoratorBetweenlands;
import thebetweenlands.common.world.gen.biome.decorator.BiomeDecoratorMarsh;
import thebetweenlands.common.world.gen.biome.decorator.DecorationHelper;

@Mixin(value = BiomeDecoratorMarsh.class)
public class MixinBiomeDecoratorMarsh extends BiomeDecoratorBetweenlands {
    public MixinBiomeDecoratorMarsh(Biome biome) {
        super(biome);
    }

    @Inject(method = "decorate", at = @At("RETURN"))
    private void injectDecorate(CallbackInfo ci) {
        this.startProfilerSection("wightFortress");
        this.generate(0.15F, DecorationHelper::generateWightFortress);
        this.endProfilerSection();
    }
}
