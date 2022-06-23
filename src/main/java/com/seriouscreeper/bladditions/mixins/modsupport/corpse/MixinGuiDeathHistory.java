package com.seriouscreeper.bladditions.mixins.modsupport.corpse;

import com.mojang.authlib.GameProfile;
import de.maxhenkel.corpse.Death;
import de.maxhenkel.corpse.gui.GUIBase;
import de.maxhenkel.corpse.gui.GUIDeathHistory;
import de.maxhenkel.corpse.net.MessageTeleport;
import de.maxhenkel.corpse.proxy.CommonProxy;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Mixin(value = GUIDeathHistory.class, remap = false)
public class MixinGuiDeathHistory extends GUIBase {
    @Shadow private SimpleDateFormat dateFormat;
    @Shadow private int hSplit;



    public MixinGuiDeathHistory(ResourceLocation texture, Container inventorySlotsIn) {
        super(texture, inventorySlotsIn);
    }

    /**
     * @author SC
     */
    @Overwrite
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }


    /**
     * @author SC
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        Death death = this.getCurrentDeath();
        String title = (new TextComponentTranslation("gui.death_history.title", new Object[0])).getFormattedText();
        int titleWidth = this.fontRenderer.getStringWidth(title);
        this.fontRenderer.drawString(TextFormatting.BLACK + title, this.guiLeft + (this.xSize - titleWidth) / 2, this.guiTop + 7, 0);
        String date = this.dateFormat.format(new Date(death.getTimestamp()));
        int dateWidth = this.fontRenderer.getStringWidth(date);
        this.fontRenderer.drawString(TextFormatting.DARK_GRAY + date, this.guiLeft + (this.xSize - dateWidth) / 2, this.guiTop + 20, 0);
        String textName = (new TextComponentTranslation("gui.death_history.name", new Object[0])).getFormattedText() + ":";
        this.drawLeft(TextFormatting.DARK_GRAY + textName, this.guiTop + 40);
        String name = death.getPlayerName();
        this.drawRight(TextFormatting.GRAY + name, this.guiTop + 40);
        String textDimension = (new TextComponentTranslation("gui.death_history.dimension", new Object[0])).getFormattedText() + ":";
        this.drawLeft(TextFormatting.DARK_GRAY + textDimension, this.guiTop + 55);
        String dimension = "Dim " + death.getDimension();
        this.drawRight(TextFormatting.GRAY + dimension, this.guiTop + 55);
        String textLocation = (new TextComponentTranslation("gui.death_history.location", new Object[0])).getFormattedText() + ":";
        this.drawLeft(TextFormatting.DARK_GRAY + textLocation, this.guiTop + 70);
        this.drawRight(TextFormatting.GRAY + "" + Math.round(death.getPosX()) + " X", this.guiTop + 70);
        this.drawRight(TextFormatting.GRAY + "" + Math.round(death.getPosY()) + " Y", this.guiTop + 85);
        this.drawRight(TextFormatting.GRAY + "" + Math.round(death.getPosZ()) + " Z", this.guiTop + 100);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        EntityPlayer player = new EntityOtherPlayerMP(this.mc.world, new GameProfile(death.getPlayerUUID(), death.getPlayerName()));
        player.height = 3.4028235E38F;
        GuiInventory.drawEntityOnScreen(this.guiLeft + this.xSize - (this.xSize - this.hSplit) / 2, this.guiTop + this.ySize / 2 + 30, 40, (float)(this.guiLeft + this.xSize - (this.xSize - this.hSplit) / 2 - mouseX), (float)(this.guiTop + this.ySize / 2 - mouseY), player);
    }


    @Shadow
    public Death getCurrentDeath() {
        return null;
    }

    @Shadow
    public void drawLeft(String string, int height) {
    }

    @Shadow
    public void drawRight(String string, int height) {
    }
}
