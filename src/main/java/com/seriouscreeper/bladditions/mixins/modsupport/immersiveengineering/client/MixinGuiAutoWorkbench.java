package com.seriouscreeper.bladditions.mixins.modsupport.immersiveengineering.client;

import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiAutoWorkbench;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonItem;
import blusunrize.immersiveengineering.common.gui.ContainerAutoWorkbench;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.items.ItemEngineersBlueprint;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(value = GuiAutoWorkbench.class)
public class MixinGuiAutoWorkbench extends GuiIEContainerBase {
    public MixinGuiAutoWorkbench(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Inject(method = "drawScreen", at = @At("TAIL"))
    private void drawScreen(int mx, int my, float partial, CallbackInfo ci) {
        Slot s = this.inventorySlots.getSlot(0);
        if (s == null || !s.getHasStack()) return;

        ItemStack bp = s.getStack();
        if (bp.isEmpty() || !(bp.getItem() instanceof ItemEngineersBlueprint)) return;

        BlueprintCraftingRecipe[] recipes =
                BlueprintCraftingRecipe.findRecipes(ItemNBTHelper.getString(bp, "blueprint"));
        if (recipes == null || recipes.length == 0) return;

        int l = recipes.length;
        int xx = this.guiLeft + 121;
        int yy = this.guiTop + (l > 6 ? 59 - (l - 3) / 3 * 18 : (l > 3 ? 59 : 68));

        for (int i = 0; i < l; ++i) {
            BlueprintCraftingRecipe r = recipes[i];
            if (r == null || r.output.isEmpty()) continue;

            // These match initGui() / GuiButtonItem placement
            int bx = xx + (i % 3) * 18;
            int by = yy + (i / 3) * 18;

            // Button/icon hitbox is 18x18 (items are 16x16 inside)
            if (mx >= bx && mx < bx + 18 && my >= by && my < by + 18) {

                ArrayList<String> tooltip = new ArrayList<>();
                tooltip.add(r.output.getRarity().color + r.output.getDisplayName());

                ArrayList<ItemStack> inputs = new ArrayList<>();
                for (IngredientStack ing : r.inputs) {
                    ItemStack toAdd = Utils.copyStackWithAmount(
                            ing.getRandomizedExampleStack((long) this.mc.player.ticksExisted),
                            ing.inputSize
                    );
                    if (toAdd.isEmpty()) continue;

                    boolean isNew = true;
                    for (ItemStack ss : inputs) {
                        if (OreDictionary.itemMatches(ss, toAdd, true)) {
                            ss.grow(toAdd.getCount());
                            isNew = false;
                            break;
                        }
                    }
                    if (isNew) inputs.add(toAdd.copy());
                }

                for (ItemStack ss : inputs) {
                    tooltip.add(TextFormatting.GRAY + "" + ss.getCount() + "x " + ss.getDisplayName());
                }

                ClientUtils.drawHoveringText(tooltip, mx, my, this.fontRenderer);
                RenderHelper.enableGUIStandardItemLighting();
                break; // only draw one tooltip
            }
        }
    }

    @Shadow
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    }
}
