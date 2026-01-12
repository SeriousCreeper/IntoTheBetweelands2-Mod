package com.seriouscreeper.bladditions.mixins.modsupport.embers;

import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import teamroots.embers.tileentity.TileEntitySeedNew;
import teamroots.embers.tileentity.TileEntitySeedNewRenderer;

@Mixin(value = TileEntitySeedNewRenderer.class, remap = false)
public abstract class MixinTileSeedRenderer {
    @Inject(method = "render(Lteamroots/embers/tileentity/TileEntitySeedNew;DDDFIF)V", at = @At("HEAD"))
    private void seed$enableTranslucency(TileEntitySeedNew tile, double x, double y, double z,
                                         float partialTicks, int destroyStage, float tileAlpha,
                                         CallbackInfo ci) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );

        GlStateManager.depthMask(false);
    }

    @Inject(method = "render(Lteamroots/embers/tileentity/TileEntitySeedNew;DDDFIF)V", at = @At("RETURN"))
    private void seed$restoreState(TileEntitySeedNew tile, double x, double y, double z,
                                   float partialTicks, int destroyStage, float tileAlpha,
                                   CallbackInfo ci) {
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
    }
}
