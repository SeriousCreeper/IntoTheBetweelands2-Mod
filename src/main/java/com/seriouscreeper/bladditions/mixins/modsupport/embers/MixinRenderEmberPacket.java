package com.seriouscreeper.bladditions.mixins.modsupport.embers;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import teamroots.embers.entity.RenderEmberPacket;
import thebetweenlands.client.render.shader.LightSource;
import thebetweenlands.client.render.shader.ShaderHelper;

@Mixin(RenderEmberPacket.class)
public class MixinRenderEmberPacket {
    /**
     * @author SC
     */
    @Overwrite
    public void doRender(Entity entity, double x, double y, double z, float yaw, float pTicks) {
        if (ShaderHelper.INSTANCE.isWorldShaderActive()) {
            ShaderHelper.INSTANCE.require();
            double rx = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)pTicks;
            double ry = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)pTicks;
            double rz = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)pTicks;

            ShaderHelper.INSTANCE.getWorldShader().addLight(new LightSource(rx, ry, rz, 8.0F, 1.0F, 0.5F, 0.2F));
        }
    }
}
