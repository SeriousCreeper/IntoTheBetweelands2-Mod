package com.seriouscreeper.bladditions.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = GuiIngameForge.class, remap = false)
public class MixinGuiIngameForge extends GuiIngame {
    @Shadow private FontRenderer fontrenderer = null;

    protected final Minecraft mc = Minecraft.getMinecraft();

    public MixinGuiIngameForge(Minecraft mcIn) {
        super(mcIn);
    }


    /**
     * @author SC
     */
    @Overwrite
    protected void renderToolHighlight(ScaledResolution res) {
        if (this.mc.gameSettings.heldItemTooltips && !this.mc.playerController.isSpectator()) {
            this.mc.profiler.startSection("toolHighlight");
            if (this.remainingHighlightTicks > 0 && !this.highlightingItemStack.isEmpty()) {
                String name = this.highlightingItemStack.getDisplayName();
                if (this.highlightingItemStack.hasDisplayName()) {
                    name = TextFormatting.ITALIC + name;
                }

                name = this.highlightingItemStack.getItem().getHighlightTip(this.highlightingItemStack, name);
                int opacity = (int)((float)this.remainingHighlightTicks * 256.0F / 10.0F);
                if (opacity > 255) {
                    opacity = 255;
                }

                if (opacity > 0) {
                    int y = res.getScaledHeight() - 59 - 10;
                    if (!this.mc.playerController.shouldDrawHUD()) {
                        y += 14;
                    }

                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    FontRenderer font = this.highlightingItemStack.getItem().getFontRenderer(this.highlightingItemStack);
                    int x;
                    if (font != null) {
                        x = (res.getScaledWidth() - font.getStringWidth(name)) / 2;
                        font.drawStringWithShadow(name, (float)x, (float)y, 16777215 | opacity << 24);
                    } else {
                        x = (res.getScaledWidth() - this.fontrenderer.getStringWidth(name)) / 2;
                        this.fontrenderer.drawStringWithShadow(name, (float)x, (float)y, 16777215 | opacity << 24);
                    }

                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                }
            }

            this.mc.profiler.endSection();
        } else if (this.mc.player.isSpectator()) {
            this.spectatorGui.renderSelectedItem(res);
        }

    }
}
