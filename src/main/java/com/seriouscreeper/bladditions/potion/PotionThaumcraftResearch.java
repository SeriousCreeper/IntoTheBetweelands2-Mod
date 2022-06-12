package com.seriouscreeper.bladditions.potion;

import com.seriouscreeper.bladditions.BLAdditions;
import epicsquid.roots.Roots;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class PotionThaumcraftResearch extends Potion {
    public final RESEARCH_CATEGORY Category;

    public static final ResourceLocation texture = new ResourceLocation("bladditions", "textures/gui/potions.png");


    protected PotionThaumcraftResearch(RESEARCH_CATEGORY category, String name) {
        super(false, category.color);
        this.Category = category;
        this.setPotionName("bladditions.potion." + name);
        this.setRegistryName(BLAdditions.MODID, name);
        this.setIconIndex(this.Category.ordinal(), 0);
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }


    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        return super.getStatusIconIndex();
    }


    @Override
    public boolean shouldRender(PotionEffect effect) {
        return true;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void renderHUDEffect(@Nonnull PotionEffect effect, Gui gui, int x, int y, float z, float alpha) {
        Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.getTextureManager().bindTexture(texture);
        int icon = getStatusIconIndex();
        minecraft.ingameGUI.drawTexturedModalRect(x + 2, y + 3, icon % 8 * 18, 198 + icon / 8 * 18, 18, 18);
    }


    public enum RESEARCH_CATEGORY {
        ARCANE(0x000000),
        AUROMANCY(0x000000),
        GOLEMANCY(0x000000),
        ELDRITCH(0x000000),
        ARTIFICE(0x000000),
        ALCHEMY(0x000000);

        private final int color;

        RESEARCH_CATEGORY(int color) {
            this.color = color;
        }
    }
}
