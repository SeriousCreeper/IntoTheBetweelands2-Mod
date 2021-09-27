package com.seriouscreeper.bladditions.mixins.modsupport.quark;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import vazkii.quark.decoration.client.render.RenderLeashKnot2;
import vazkii.quark.decoration.entity.EntityLeashKnot2TheKnotting;
import vazkii.quark.world.client.render.ChainRenderer;

import javax.annotation.Nullable;

@Mixin(value = RenderLeashKnot2.class, remap = false)
public class MixinRenderLeashKnot2 extends Render<EntityLeashKnot2TheKnotting> {
    protected MixinRenderLeashKnot2(RenderManager renderManager) {
        super(renderManager);
    }

    /**
     * @author SC
     */
    @Overwrite
    protected void renderLeash(EntityLeashKnot2TheKnotting leash, double x, double y, double z, float partialTicks) {
        Entity entity = leash.getLeashHolder();
        if (entity == null)
            return;

        y = y - (2.9D - leash.height) * 0.5D;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        double d0 = this.interpolateValue(entity.prevRotationYaw, entity.rotationYaw, (partialTicks * 0.5F)) * 0.01745329238474369D;
        double d1 = this.interpolateValue(entity.prevRotationPitch, entity.rotationPitch, (partialTicks * 0.5F)) * 0.01745329238474369D;
        double d2 = Math.cos(d0);
        double d3 = Math.sin(d0);
        double d4 = Math.sin(d1);

        float off = 1.3F;
        if (entity instanceof EntityHanging) {
            off = 1.1F;
            d2 = 0.0D;
            d3 = 0.0D;
            d4 = -1.0D;
        }


        double d5 = Math.cos(d1);
        double d6 = this.interpolateValue(entity.prevPosX, entity.posX, partialTicks) - d2 * 0.7D - d3 * 0.5D * d5;
        double d7 = this.interpolateValue(entity.prevPosY + entity.getEyeHeight() * 0.7D + off, entity.posY + entity.getEyeHeight() * 0.7D + off, partialTicks) - d4 * 0.5D - 0.25D;
        double d8 = this.interpolateValue(entity.prevPosZ, entity.posZ, partialTicks) - d3 * 0.7D + d2 * 0.5D * d5;
        double d9 = this.interpolateValue(leash.prevRenderYawOffset, leash.renderYawOffset, partialTicks) * 0.01745329238474369D + (Math.PI / 2D);
        d2 = Math.cos(d9) * leash.width * 0.4D;
        d3 = Math.sin(d9) * leash.width * 0.4D;
        double d10 = this.interpolateValue(leash.prevPosX, leash.posX, partialTicks) + d2;
        double d11 = this.interpolateValue(leash.prevPosY, leash.posY, partialTicks);
        double d12 = this.interpolateValue(leash.prevPosZ, leash.posZ, partialTicks) + d3;
        x = x + d2;
        z = z + d3;
        double d13 = ((float) (d6 - d10));
        double d14 = ((float) (d7 - d11));
        double d15 = ((float) (d8 - d12));
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        ChainRenderer.drawChainSegment(x, y, z, bufferbuilder, d13, d14, d15, 0.025, 0, 0.37f, 0.5f, 0.22f, 1);
        ChainRenderer.drawChainSegment(x, y, z, bufferbuilder, d13, d14, d15, 0, 0.025, 0.37f, 0.5f, 0.22f, 1);
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.enableCull();
    }

    @Shadow
    private double interpolateValue(double prevRotationYaw, double rotationYaw, double v) {
        return 0;
    }

    @Shadow
    protected ResourceLocation getEntityTexture(EntityLeashKnot2TheKnotting entityLeashKnot2TheKnotting) {
        return null;
    }
}
