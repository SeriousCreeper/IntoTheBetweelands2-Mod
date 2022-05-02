package com.seriouscreeper.bladditions.mixins.modsupport.waterstrainer;

import mods.waterstrainer.gui.GuiStrainer;
import mods.waterstrainer.tileentity.TileEntityStrainer;
import mods.waterstrainer.util.APIUtils;
import mods.waterstrainer.util.Patcher;
import mods.waterstrainer.util.WaterStrainerUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Arrays;

@Mixin(value = GuiStrainer.class, remap = false)
public class MixinGuiStrainer extends GuiContainer {
    @Final
    @Shadow
    private static final ResourceLocation guiTexture = new ResourceLocation("waterstrainer", "textures/gui/strainer_base.png");

    @Shadow
    private final TileEntityStrainer tile;


    public MixinGuiStrainer(Container inventorySlotsIn, TileEntityStrainer tile) {
        super(inventorySlotsIn);
        this.tile = tile;
    }

    /**
     * @author SC
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        Slot slot = this.getSlotUnderMouse();
        int slotIndex;
        if (slot != null) {
            slotIndex = slot.slotNumber;
            if (slot.getHasStack()) {
                return;
            }

            if (slotIndex == 0) {
                this.drawHoveringText(Arrays.asList("Strainer Slot"), mouseX, mouseY, this.fontRenderer);
            }
        }

        slotIndex = (this.width - this.xSize) / 2;
        int guiY = (this.height - this.ySize) / 2;

        if (this.isMouseHovering(mouseX, mouseY, slotIndex + 155, guiY + 5, 16, 16) && !APIUtils.isJEILoaded()) {
            this.drawHoveringText(Arrays.asList(WaterStrainerUtils.ctext("#RED#Show Recipes"), WaterStrainerUtils.ctext("This function is only available if you"), WaterStrainerUtils.ctext("have #BLUE#Just Enough Items (JEI)#RESET# installed.")), mouseX, mouseY, this.fontRenderer);
        }
    }

    /**
     * @author SC
     */
    @Overwrite
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(guiTexture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        if (Patcher.isStackEmpty(this.tile.getStackInSlot(0))) {
            this.drawTexturedModalRect(k + 80, l + 21, 176, 0, 16, 16);
        }
    }

    @Shadow
    public boolean isMouseHovering(int mouse_x, int mouse_y, int x, int y, int xsize, int ysize) {
        return mouse_x >= x && mouse_x < x + xsize && mouse_y >= y && mouse_y < y + ysize;
    }
}
