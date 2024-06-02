package com.seriouscreeper.bladditions.mixins.modsupport.botania;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.registries.ItemRegistry;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.block.tile.TileSimpleInventory;

import java.util.Iterator;

@Mixin(value = TileAltar.class, remap = false)
public abstract class MixinTileAltarClient extends TileSimpleInventory {
    @Shadow public abstract int getSizeInventory();

    @Shadow
    int recipeKeepTicks;

    @Shadow public boolean hasWater;

    /**
     * @author SC
     * @reason display middle fruit seeds
     */
    @Overwrite
    @SideOnly(Side.CLIENT)
    public void renderHUD(Minecraft mc, ScaledResolution res) {
        int xc = res.getScaledWidth() / 2;
        int yc = res.getScaledHeight() / 2;
        float angle = -90.0F;
        int radius = 24;
        int amt = 0;

        for(int i = 0; i < this.getSizeInventory() && !this.itemHandler.getStackInSlot(i).isEmpty(); ++i) {
            ++amt;
        }

        if (amt > 0) {
            float anglePer = 360.0F / (float)amt;
            Iterator var9 = BotaniaAPI.petalRecipes.iterator();

            while(var9.hasNext()) {
                RecipePetals recipe = (RecipePetals)var9.next();
                if (recipe.matches(this.itemHandler)) {
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    mc.renderEngine.bindTexture(HUDHandler.manaBar);
                    RenderHelper.drawTexturedModalRect(xc + radius + 9, yc - 8, 0.0F, 0, 8, 22, 15);
                    ItemStack stack = recipe.getOutput();
                    net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
                    mc.getRenderItem().renderItemIntoGUI(stack, xc + radius + 32, yc - 8);
                    mc.getRenderItem().renderItemIntoGUI(new ItemStack(ItemRegistry.MIDDLE_FRUIT_BUSH_SEEDS), xc + radius + 16, yc + 6);
                    net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
                    mc.fontRenderer.drawStringWithShadow("+", (float)(xc + radius + 14), (float)(yc + 10), 16777215);
                }
            }

            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();

            for(int i = 0; i < amt; ++i) {
                double xPos = (double)xc + Math.cos((double)angle * Math.PI / 180.0) * (double)radius - 8.0;
                double yPos = (double)yc + Math.sin((double)angle * Math.PI / 180.0) * (double)radius - 8.0;
                GlStateManager.translate(xPos, yPos, 0.0);
                mc.getRenderItem().renderItemIntoGUI(this.itemHandler.getStackInSlot(i), 0, 0);
                GlStateManager.translate(-xPos, -yPos, 0.0);
                angle += anglePer;
            }

            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        } else if (this.recipeKeepTicks > 0 && this.hasWater) {
            String s = I18n.format("botaniamisc.altarRefill0", new Object[0]);
            mc.fontRenderer.drawStringWithShadow(s, (float)(xc - mc.fontRenderer.getStringWidth(s) / 2), (float)(yc + 10), 16777215);
            s = I18n.format("botaniamisc.altarRefill1", new Object[0]);
            mc.fontRenderer.drawStringWithShadow(s, (float)(xc - mc.fontRenderer.getStringWidth(s) / 2), (float)(yc + 20), 16777215);
        }
    }
}
