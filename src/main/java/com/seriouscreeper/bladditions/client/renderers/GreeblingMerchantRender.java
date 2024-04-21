package com.seriouscreeper.bladditions.client.renderers;

import com.seriouscreeper.bladditions.client.renderers.models.ModelMerchantGreebling;
import com.seriouscreeper.bladditions.entities.GreeblingMerchantEntity;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class GreeblingMerchantRender  extends RenderLiving<GreeblingMerchantEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("thebetweenlands:textures/entity/greebling_coracle.png");

    public GreeblingMerchantRender(RenderManager rm) {
        super(rm, new ModelMerchantGreebling(), 0.2F);
    }

    @Nullable
    protected ResourceLocation getEntityTexture(GreeblingMerchantEntity merchantEntity) {
        return TEXTURE;
    }
}
