package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import scala.tools.asm.Opcodes;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.casters.NodeSetting;
import thaumcraft.client.gui.GuiFocalManipulator;
import thaumcraft.client.gui.plugins.GuiFocusSettingSpinnerButton;
import thaumcraft.client.gui.plugins.GuiHoverButton;
import thaumcraft.client.gui.plugins.GuiImageButton;
import thaumcraft.client.gui.plugins.GuiSliderTC;
import thaumcraft.common.items.casters.ItemFocus;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketFocusNodesToServer;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.crafting.FocusElementNode;
import thaumcraft.common.tiles.crafting.TileFocalManipulator;
import thebetweenlands.common.world.gen.feature.structure.WorldGenWightFortress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Mixin(value = GuiFocalManipulator.class, remap = false)
public class MixinGuiFocalManipulator extends GuiContainer {
    @Shadow
    GuiImageButton buttonConfirm;
    @Shadow
    private TileFocalManipulator table;
    @Shadow
    int selectedNode;
    @Shadow
    ArrayList<String> shownParts;
    @Shadow
    ItemStack[] components;
    @Shadow
    int totalComplexity;
    @Shadow
    int maxComplexity;
    @Shadow
    float costCast;
    @Shadow
    int costXp;
    @Shadow
    int costVis;
    @Shadow
    GuiSliderTC scrollbarParts;
    @Shadow
    GuiSliderTC scrollbarMainSide;
    @Shadow
    GuiSliderTC scrollbarMainBottom;
    @Shadow
    boolean valid;
    @Shadow
    int scrollX;
    @Shadow
    int scrollY;
    @Shadow
    int sMinX;
    @Shadow
    int sMinY;
    @Shadow
    int sMaxX;
    @Shadow
    int sMaxY;
    @Shadow
    private GuiTextField nameField;


    public MixinGuiFocalManipulator(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }


    /**
     * @author SC
     */
    @Overwrite
    private void gatherInfo(boolean sync) {
        this.buttonList.clear();
        this.buttonList.add(this.buttonConfirm);
        this.buttonConfirm.x = this.guiLeft + 242;
        this.buttonConfirm.y = this.guiTop + 18;
        this.buttonList.add(new GuiHoverButton(this, 1, this.guiLeft + 241, this.guiTop + 39, 32, 16, I18n.translateToLocal("wandtable.text5"), (String)null, new ResourceLocation("thaumcraft", "textures/gui/complex.png")));
        this.buttonList.add(new GuiHoverButton(this, 2, this.guiLeft + 241, this.guiTop + 53, 32, 16, I18n.translateToLocal("wandtable.text2"), (String)null, new ResourceLocation("thaumcraft", "textures/gui/costxp.png")));
        this.buttonList.add(new GuiHoverButton(this, 3, this.guiLeft + 241, this.guiTop + 67, 32, 16, I18n.translateToLocal("wandtable.text1"), (String)null, new ResourceLocation("thaumcraft", "textures/gui/costvis.png")));
        FocusElementNode fn = (FocusElementNode)this.table.data.get(this.selectedNode);
        int r;
        int i;
        if (fn != null && fn.node != null && !fn.node.getSettingList().isEmpty()) {
            int a = 0;
            Iterator var4 = fn.node.getSettingList().iterator();

            label168:
            while(true) {
                NodeSetting setting;
                do {
                    if (!var4.hasNext()) {
                        break label168;
                    }

                    String sk = (String)var4.next();
                    setting = fn.node.getSetting(sk);
                } while(setting.getResearch() != null && !ThaumcraftCapabilities.knowsResearchStrict(this.mc.player, new String[]{setting.getResearch()}));

                r = this.guiLeft + this.xSize;
                i = this.guiTop + this.ySize + 3 - fn.node.getSettingList().size() * 26 + a * 26;
                int w = 32;
                if (setting.getType() instanceof NodeSetting.NodeSettingIntList) {
                    w = 72;
                }

                this.buttonList.add(new GuiFocusSettingSpinnerButton(10 + a, r, i, w, setting));
                ++a;
            }
        }

        this.shownParts.clear();
        this.components = null;
        if (this.table.getStackInSlot(0) != null && !this.table.getStackInSlot(0).isEmpty()) {
            HashMap<String, Integer> compCount = new HashMap();
            this.totalComplexity = 0;
            this.maxComplexity = 0;
            if (this.inventorySlots.getSlot(0).getHasStack()) {
                ItemStack is = this.inventorySlots.getSlot(0).getStack();
                if (is != null && !is.isEmpty() && is.getItem() instanceof ItemFocus) {
                    this.maxComplexity = ((ItemFocus)is.getItem()).getMaxComplexity();
                }
            }

            boolean emptyNodes = false;
            AspectList crystals = new AspectList();
            if (this.table.data != null && !this.table.data.isEmpty()) {
                Iterator var19 = this.table.data.values().iterator();

                while(var19.hasNext()) {
                    FocusElementNode node = (FocusElementNode)var19.next();
                    if (node.node != null) {
                        i = 0;
                        if (compCount.containsKey(node.node.getKey())) {
                            i = (Integer)compCount.get(node.node.getKey());
                        }

                        ++i;
                        node.complexityMultiplier = 0.5F * (float)(i + 1);
                        compCount.put(node.node.getKey(), i);
                        this.totalComplexity = (int)((float)this.totalComplexity + (float)node.node.getComplexity() * node.complexityMultiplier);
                        if (node.node.getAspect() != null) {
                            crystals.add(node.node.getAspect(), 1);
                        }
                    } else {
                        emptyNodes = true;
                    }
                }
            }

            this.costCast = (float)this.totalComplexity / 5.0F;
            this.costVis = this.totalComplexity * 10 + this.maxComplexity / 5;
            this.costXp = (int)Math.max(1L, Math.round(Math.sqrt((double)this.totalComplexity)));

            boolean validCrystals = false;
            if (crystals.getAspects().length > 0) {
                validCrystals = true;
                this.components = new ItemStack[crystals.getAspects().length];
                r = 0;
                Aspect[] var22 = crystals.getAspects();
                int q = var22.length;

                int z;
                for(z = 0; z < q; ++z) {
                    Aspect as = var22[z];
                    this.components[r] = ThaumcraftApiHelper.makeCrystal(as, crystals.getAmount(as));
                    ++r;
                }

                if (this.components.length >= 0) {
                    boolean[] owns = new boolean[this.components.length];

                    for(q = 0; q < this.components.length; ++q) {
                        owns[q] = InventoryUtils.isPlayerCarryingAmount(this.mc.player, this.components[q], false);
                        if (!owns[q]) {
                            validCrystals = false;
                        }
                    }
                }

                if (this.components != null && this.components.length > 0) {
                    i = 0;
                    q = 0;
                    z = 0;
                    ItemStack[] var25 = this.components;
                    int var12 = var25.length;

                    for(int var13 = 0; var13 < var12; ++var13) {
                        ItemStack stack = var25[var13];
                        this.buttonList.add(new GuiHoverButton(this, 11 + z, this.guiLeft + 234 + i * 16, this.guiTop + 110 + 16 * q, 16, 16, stack.getDisplayName(), (String)null, stack));
                        ++i;
                        if (i > 4) {
                            i = 0;
                            ++q;
                        }

                        ++z;
                    }
                }
            }

            this.gatherPartsList();
            this.scrollbarParts = null;
            if (this.shownParts.size() > 6) {
                this.scrollbarParts = new GuiSliderTC(4, this.guiLeft + 51, this.guiTop + 30, 8, 149, "logistics.scrollbar", 0.0F, (float)(this.shownParts.size() - 6), 0.0F, true);
                this.buttonList.add(this.scrollbarParts);
            }

            this.valid = this.totalComplexity <= this.maxComplexity && !emptyNodes && validCrystals && this.table.xpCost <= this.mc.player.experienceLevel;
            this.calcScrollBounds();
            if (this.scrollY > (this.sMaxY - 3) * 32) {
                this.scrollY = (this.sMaxY - 3) * 32;
            }

            if (this.scrollX > this.sMaxX * 24) {
                this.scrollX = this.sMaxX * 24;
            }

            if (this.scrollX < this.sMinX * 24) {
                this.scrollX = this.sMinX * 24;
            }

            this.scrollbarMainSide = null;
            this.scrollbarMainBottom = null;
            if (this.sMaxY * 32 > 130) {
                this.scrollbarMainSide = new GuiSliderTC(5, this.guiLeft + 203, this.guiTop + 32, 8, 156, "logistics.scrollbar", 0.0F, (float)((this.sMaxY - 3) * 32), (float)this.scrollY, true);
                this.buttonList.add(this.scrollbarMainSide);
            } else {
                this.scrollY = 0;
            }

            if (this.sMinX * 24 >= -70 && this.sMaxX * 24 <= 70) {
                this.scrollX = 0;
            } else {
                this.scrollbarMainBottom = new GuiSliderTC(6, this.guiLeft + 64, this.guiTop + 195, 132, 8, "logistics.scrollbar", (float)(this.sMinX * 24), (float)(this.sMaxX * 24), (float)this.scrollX, false);
                this.buttonList.add(this.scrollbarMainBottom);
            }

            if (this.table.focusName.isEmpty() && !this.table.getStackInSlot(0).isEmpty()) {
                this.table.focusName = this.table.getStackInSlot(0).getDisplayName();
            }

            if (this.nameField == null) {
                this.setupNameField();
            }

            this.nameField.setText(this.table.focusName);
            if (sync) {
                PacketHandler.INSTANCE.sendToServer(new PacketFocusNodesToServer(this.table.getPos(), this.table.data, this.table.focusName));
            }

        }
    }

    @Shadow
    private void setupNameField() {
    }

    @Shadow
    private void calcScrollBounds() {
    }

    @Shadow
    private void gatherPartsList(){}

    @Shadow
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i1) {

    }
}
