package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import com.seriouscreeper.bladditions.capability.WellnessCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.client.lib.events.HudHandler;

@Mixin(value = HudHandler.class, remap = false)
public class MixinHudHandler {
    @Final
    @Shadow
    final ResourceLocation HUD = new ResourceLocation("thaumcraft", "textures/gui/hud.png");

    @Inject(method = "renderThaumometerHud", at = @At("HEAD"))
    private void injectRenderThaumometerHud(Minecraft mc, float partialTicks, EntityPlayer player, long time, int ww, int hh, int shifty, CallbackInfo ci) {
        WellnessCapability cap = player.getCapability(WellnessCapability.INSTANCE, null);

        if(cap == null) {
            return;
        }

        float wellnessBuff = WellnessCapability.CalculateWellness(player);
        wellnessBuff = Math.round((1f - wellnessBuff) * 100);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glPushMatrix();
        GL11.glTranslated(20.0, 5, 0.0);
        GL11.glScaled(1, 1, 1);
        mc.ingameGUI.drawString(mc.fontRenderer, "Wellness Bonus: " + wellnessBuff + "%" , 10, 0, 7325720);
        GL11.glPopMatrix();
        mc.renderEngine.bindTexture(this.HUD);
        GL11.glPopMatrix();
    }
}
