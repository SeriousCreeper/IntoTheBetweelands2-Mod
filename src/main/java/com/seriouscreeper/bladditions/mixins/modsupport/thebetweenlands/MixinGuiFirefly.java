package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.client.gui.GuiFirefly;

@Mixin(value = GuiFirefly.class, remap = false)
public class MixinGuiFirefly extends Gui {
    /**
     * @author SC
     */
    @Overwrite
    public void drawTexturedModalRectWithColor(int x, int y, int textureX, int textureY, int width, int height, int color) {
        float a = 1;
        float r = 222F / 255.0F;
        float g = 34F / 255.0F;
        float b = 215F / 255.0F;


        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        vertexbuffer.pos((double)(x + 0), (double)(y + height), (double)this.zLevel).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).color(r, g, b, a).endVertex();
        vertexbuffer.pos((double)(x + width), (double)(y + height), (double)this.zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).color(r, g, b, a).endVertex();
        vertexbuffer.pos((double)(x + width), (double)(y + 0), (double)this.zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).color(r, g, b, a).endVertex();
        vertexbuffer.pos((double)(x + 0), (double)(y + 0), (double)this.zLevel).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).color(r, g, b, a).endVertex();
        tessellator.draw();
    }
}
