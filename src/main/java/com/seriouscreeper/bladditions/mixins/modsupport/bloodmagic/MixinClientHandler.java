package com.seriouscreeper.bladditions.mixins.modsupport.bloodmagic;

import WayofTime.bloodmagic.util.handler.event.ClientHandler;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientHandler.class, remap = false)
public class MixinClientHandler {
    @Shadow
    public static TextureAtlasSprite glowstone;
    @Shadow
    public static TextureAtlasSprite stoneBrick;
    @Shadow
    public static TextureAtlasSprite beacon;

    @Inject(method = "onTextureStitch(Lnet/minecraftforge/client/event/TextureStitchEvent$Pre;)V", at = @At("RETURN"))
    private static void inject(TextureStitchEvent.Pre event, CallbackInfo ci) {
        glowstone = event.getMap().registerSprite(new ResourceLocation("embers:blocks/block_dawnstone"));
        stoneBrick = event.getMap().registerSprite(new ResourceLocation("thebetweenlands:blocks/betweenstone_bricks"));
        beacon = event.getMap().registerSprite(new ResourceLocation("thaumcraft:blocks/metal_void"));
    }
}
