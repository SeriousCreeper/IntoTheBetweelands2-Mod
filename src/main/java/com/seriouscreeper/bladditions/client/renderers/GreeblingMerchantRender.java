package com.seriouscreeper.bladditions.client.renderers;

import com.seriouscreeper.bladditions.entities.GreeblingMerchantEntity;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import party.lemons.deliverymechants.MerchantEntity;
import party.lemons.deliverymechants.MerchantType;
import thebetweenlands.client.render.model.entity.ModelGreebling;

import javax.annotation.Nullable;

public class GreeblingMerchantRender  extends RenderLiving<GreeblingMerchantEntity> {
    public static final ResourceLocation TEXTURE_0 = new ResourceLocation("thebetweenlands:textures/entity/greebling_0.png");
    public static final ResourceLocation TEXTURE_1 = new ResourceLocation("thebetweenlands:textures/entity/greebling_1.png");

    public GreeblingMerchantRender(RenderManager rm) {
        super(rm, new ModelGreebling(), 0.2F);
    }

    @Nullable
    protected ResourceLocation getEntityTexture(GreeblingMerchantEntity merchantEntity) {
        return merchantEntity.getType() == 0 ? TEXTURE_0 : TEXTURE_1;
    }
}
