package com.seriouscreeper.bladditions.guis;


import com.seriouscreeper.bladditions.containers.PatchedContainerResearchTable;
import com.seriouscreeper.bladditions.tiles.PatchedTileResearchTable;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.theorycraft.ITheorycraftAid;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftManager;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.client.gui.plugins.GuiImageButton;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketStartTheoryToServer;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PatchedGuiResearchTable extends GuiContainer {
    private float xSize_lo;
    private float ySize_lo;
    private PatchedTileResearchTable table;
    private FontRenderer galFontRenderer;
    private String username;
    EntityPlayer player;
    ResourceLocation txBackground = new ResourceLocation("thaumcraft", "textures/gui/gui_research_table.png");
    ResourceLocation txBase = new ResourceLocation("thaumcraft", "textures/gui/gui_base.png");
    ResourceLocation txPaper = new ResourceLocation("thaumcraft", "textures/gui/paper.png");
    ResourceLocation txPaperGilded = new ResourceLocation("thaumcraft", "textures/gui/papergilded.png");
    ResourceLocation txQuestion = new ResourceLocation("thaumcraft", "textures/aspects/_unknown.png");
    ResearchTableData.CardChoice lastDraw;
    float[] cardHover = new float[]{0.0F, 0.0F, 0.0F};
    float[] cardZoomOut = new float[]{0.0F, 0.0F, 0.0F};
    float[] cardZoomIn = new float[]{0.0F, 0.0F, 0.0F};
    boolean[] cardActive = new boolean[]{true, true, true};
    boolean cardSelected = false;
    public HashMap<String, Integer> tempCatTotals = new HashMap();
    long nexCatCheck = 0L;
    long nextCheck = 0L;
    int dummyInspirationStart = 0;
    Set<String> currentAids = new HashSet();
    Set<String> selectedAids = new HashSet();
    GuiImageButton buttonCreate;
    GuiImageButton buttonComplete;
    GuiImageButton buttonScrap;
    public ArrayList<ResearchTableData.CardChoice> cardChoices;

    public PatchedGuiResearchTable(EntityPlayer player, PatchedTileResearchTable e) {
        super(new PatchedContainerResearchTable(player.inventory, e));
        this.buttonCreate = new GuiImageButton(this, 1, this.guiLeft + 128, this.guiTop + 22, 49, 11, "button.create.theory", (String)null, this.txBase, 37, 66, 51, 13, 8978346);
        this.buttonComplete = new GuiImageButton(this, 7, this.guiLeft + 191, this.guiTop + 96, 49, 11, "button.complete.theory", (String)null, this.txBase, 37, 66, 51, 13, 8978346);
        this.buttonScrap = new GuiImageButton(this, 9, this.guiLeft + 128, this.guiTop + 168, 49, 11, "button.scrap.theory", (String)null, this.txBase, 37, 66, 51, 13, 16720418);
        this.cardChoices = new ArrayList();
        this.table = e;
        this.xSize = 255;
        this.ySize = 255;
        this.galFontRenderer = FMLClientHandler.instance().getClient().standardGalacticFontRenderer;
        this.username = player.getName();
        this.player = player;
        if (this.table.data != null) {
            Iterator var3 = this.table.data.categoryTotals.keySet().iterator();

            while(var3.hasNext()) {
                String cat = (String)var3.next();
                this.tempCatTotals.put(cat, this.table.data.categoryTotals.get(cat));
            }

            this.syncFromTableChoices();
            this.lastDraw = this.table.data.lastDraw;
        }

    }

    private void syncFromTableChoices() {
        this.cardChoices.clear();
        Iterator var1 = this.table.data.cardChoices.iterator();

        while(var1.hasNext()) {
            ResearchTableData.CardChoice cc = (ResearchTableData.CardChoice)var1.next();
            this.cardChoices.add(cc);
        }

    }

    protected void drawGuiContainerForegroundLayer(int mx, int my) {
    }

    public void drawScreen(int mx, int my, float par3) {
        this.drawDefaultBackground();
        super.drawScreen(mx, my, par3);
        this.xSize_lo = (float)mx;
        this.ySize_lo = (float)my;
        int xx = this.guiLeft;
        int yy = this.guiTop;
        RenderHelper.disableStandardItemLighting();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int side;
        int r;
        if (this.table.data == null) {
            if (!this.currentAids.isEmpty()) {
                side = Math.min(this.currentAids.size(), 6);
                int c = 0;
                r = 0;
                GL11.glPushMatrix();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.2F);
                this.mc.renderEngine.bindTexture(this.txBase);
                Iterator var9 = this.currentAids.iterator();

                while(var9.hasNext()) {
                    String key = (String)var9.next();
                    ITheorycraftAid mutator = (ITheorycraftAid) TheorycraftManager.aids.get(key);
                    if (mutator != null) {
                        int x = xx + 128 + 20 * c - side * 10;
                        int y = yy + 85 + 35 * r;
                        if (this.isPointInRegion(x - xx, y - yy, 16, 16, mx, my) && !this.selectedAids.contains(key)) {
                            this.drawTexturedModalRect(x, y, 0, 96, 16, 16);
                        }

                        ++c;
                        if (c >= side) {
                            ++r;
                            c = 0;
                        }
                    }
                }

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            }
        } else {
            int sx = 128;
            int cw = 110;
            r = this.cardChoices.size();
            int a = 0;
            if (!this.cardSelected) {
                Iterator var18 = this.cardChoices.iterator();

                while(var18.hasNext()) {
                    ResearchTableData.CardChoice cardChoice = (ResearchTableData.CardChoice)var18.next();
                    if (this.cardZoomOut[a] >= 1.0F) {
                        float dx = (float)(55 + sx - 55 * r + cw * a - 65);
                        float fx = 65.0F + dx * this.cardZoomOut[a];
                        float qx = 191.0F - fx;
                        if (this.cardActive[a]) {
                            fx += qx * this.cardZoomIn[a];
                        }

                        this.drawSheetOverlay((double)fx, 100.0D, cardChoice, mx, my);
                        ++a;
                    }
                }
            }

            int qq = 0;
            if (this.table.getStackInSlot(0) == null || this.table.getStackInSlot(0).isEmpty() || this.table.getStackInSlot(0).getItemDamage() == this.table.getStackInSlot(0).getMaxDamage()) {
                side = Math.max(this.fontRenderer.getStringWidth(I18n.translateToLocal("tile.researchtable.noink.0")), this.fontRenderer.getStringWidth(I18n.translateToLocal("tile.researchtable.noink.1"))) / 2;
                UtilsFX.drawCustomTooltip(this, this.fontRenderer, Arrays.asList(I18n.translateToLocal("tile.researchtable.noink.0"), I18n.translateToLocal("tile.researchtable.noink.1")), xx - side + 116, yy + 60 + qq, 11, true);
                qq += 40;
            }

            if (this.table.getStackInSlot(1) == null || this.table.getStackInSlot(1).isEmpty()) {
                side = this.fontRenderer.getStringWidth(I18n.translateToLocal("tile.researchtable.nopaper.0")) / 2;
                UtilsFX.drawCustomTooltip(this, this.fontRenderer, Arrays.asList(I18n.translateToLocal("tile.researchtable.nopaper.0")), xx - side + 116, yy + 60 + qq, 11, true);
            }
        }

        this.renderHoveredToolTip(mx, my);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mx, int my) {
        this.checkButtons();
        int xx = this.guiLeft;
        int yy = this.guiTop;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        this.mc.renderEngine.bindTexture(this.txBackground);
        this.drawTexturedModalRect(xx, yy, 0, 0, 255, 255);
        this.fontRenderer.drawString(" ", 0, 0, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int side;
        int r;
        Iterator var9;
        String cat;
        int t1;
        if (this.table.data == null) {
            if (this.nextCheck < (long)this.player.ticksExisted) {
                this.currentAids = this.table.checkSurroundingAids();
                this.dummyInspirationStart = ResearchTableData.getAvailableInspiration(this.player);
                this.nextCheck = (long)(this.player.ticksExisted + 100);
            }

            this.mc.renderEngine.bindTexture(this.txBase);
            GL11.glPushMatrix();
            GL11.glTranslated((double)(xx + 128 - this.dummyInspirationStart * 5), (double)(yy + 55), 0.0D);
            GL11.glScaled(0.5D, 0.5D, 0.0D);

            for(side = 0; side < this.dummyInspirationStart; ++side) {
                this.drawTexturedModalRect(20 * side, 0, this.dummyInspirationStart - this.selectedAids.size() <= side ? 48 : 32, 96, 16, 16);
            }

            GL11.glPopMatrix();
            if (!this.currentAids.isEmpty()) {
                side = Math.min(this.currentAids.size(), 6);
                int c = 0;
                r = 0;
                var9 = this.currentAids.iterator();

                label332:
                while(true) {
                    ITheorycraftAid mutator;
                    do {
                        if (!var9.hasNext()) {
                            break label332;
                        }

                        cat = (String)var9.next();
                        mutator = (ITheorycraftAid)TheorycraftManager.aids.get(cat);
                    } while(mutator == null);

                    t1 = xx + 128 + 20 * c - side * 10;
                    int y = yy + 85 + 35 * r;
                    if (this.selectedAids.contains(cat)) {
                        this.mc.renderEngine.bindTexture(this.txBase);
                        this.drawTexturedModalRect(t1, y, 0, 96, 16, 16);
                    }

                    if (mutator.getAidObject() instanceof ItemStack || mutator.getAidObject() instanceof Block) {
                        GL11.glPushMatrix();
                        RenderHelper.enableGUIStandardItemLighting();
                        GlStateManager.disableLighting();
                        GlStateManager.enableRescaleNormal();
                        GlStateManager.enableColorMaterial();
                        GlStateManager.enableLighting();
                        ItemStack s = mutator.getAidObject() instanceof ItemStack ? (ItemStack)mutator.getAidObject() : new ItemStack((Block)mutator.getAidObject());
                        this.itemRender.renderItemAndEffectIntoGUI(s, t1, y);
                        GlStateManager.disableLighting();
                        GlStateManager.depthMask(true);
                        GlStateManager.enableDepth();
                        GL11.glPopMatrix();
                    }

                    ++c;
                    if (c >= side) {
                        ++r;
                        c = 0;
                    }
                }
            }
        } else {
            this.checkCards();
            this.mc.renderEngine.bindTexture(this.txBase);
            GL11.glPushMatrix();
            GL11.glTranslated((double)(xx + 15), (double)(yy + 150), 0.0D);
            if (this.table.data != null) {
                for(side = 0; side < this.table.data.bonusDraws; ++side) {
                    this.drawTexturedModalRect(side * 2, side, 64, 96, 16, 16);
                }
            }

            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated((double)(xx + 128 - this.table.data.inspirationStart * 5), (double)(yy + 16), 0.0D);
            GL11.glScaled(0.5D, 0.5D, 0.0D);

            for(side = 0; side < this.table.data.inspirationStart; ++side) {
                this.drawTexturedModalRect(20 * side, 0, this.table.data.inspiration <= side ? 48 : 32, 96, 16, 16);
            }

            GL11.glPopMatrix();
            side = 0;
            if (this.table.getStackInSlot(1) != null) {
                side = 1 + this.table.getStackInSlot(1).getCount() / 4;
            }

            Random random = new Random(55L);
            if (side > 0 && !this.table.data.isComplete()) {
                for(int i = 0; i < side; ++i) {
                    this.drawSheet((double)(xx + 65), (double)(yy + 100), 6.0D, random, 1.0F, 1.0F, (ResearchTableData.CardChoice)null);
                }

                boolean highlight = false;
                int var7 = mx - (25 + xx);
                int var8 = my - (55 + yy);
                if (this.cardChoices.isEmpty() && var7 >= 0 && var8 >= 0 && var7 < 75 && var8 < 90) {
                    highlight = true;
                }

                GL11.glPushMatrix();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, highlight ? 1.0F : 0.5F);
                GlStateManager.enableBlend();
                this.mc.renderEngine.bindTexture(this.txQuestion);
                GL11.glTranslated((double)(xx + 65), (double)(yy + 100), 0.0D);
                GL11.glScaled(highlight ? 1.75D : 1.5D, highlight ? 1.75D : 1.5D, 0.0D);
                UtilsFX.drawTexturedQuadFull(-8.0F, -8.0F, 0.0D);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            }

            Iterator var31 = this.table.data.savedCards.iterator();

            while(var31.hasNext()) {
                Long seed = (Long)var31.next();
                random = new Random(seed);
                this.drawSheet((double)(xx + 191), (double)(yy + 100), 6.0D, random, 1.0F, 1.0F, (ResearchTableData.CardChoice)null);
            }

            if (this.lastDraw != null) {
                random = new Random(this.lastDraw.card.getSeed());
                this.drawSheet((double)(xx + 191), (double)(yy + 100), 6.0D, random, 1.0F, 1.0F, this.lastDraw);
            }

            ArrayList<String> sparkle = new ArrayList();
            int t0;
            if (this.nexCatCheck < (long)this.player.ticksExisted) {
                var9 = ResearchCategories.researchCategories.keySet().iterator();

                while(true) {
                    while(var9.hasNext()) {
                        cat = (String)var9.next();
                        t0 = 0;
                        if (this.table.data.categoryTotals.containsKey(cat)) {
                            t0 = (Integer)this.table.data.categoryTotals.get(cat);
                        }

                        t1 = 0;
                        if (this.tempCatTotals.containsKey(cat)) {
                            t1 = (Integer)this.tempCatTotals.get(cat);
                        }

                        if (t0 == 0 && t1 == 0) {
                            this.tempCatTotals.remove(cat);
                        } else {
                            if (t1 > t0) {
                                --t1;
                            }

                            if (t1 < t0) {
                                ++t1;
                                sparkle.add(cat);
                            }

                            this.tempCatTotals.put(cat, t1);
                        }
                    }

                    this.nexCatCheck = (long)(this.player.ticksExisted + 1);
                    break;
                }
            }

            HashMap<String, Integer> unsortedMap = new HashMap();
            cat = null;
            t0 = 0;
            Iterator var39 = this.tempCatTotals.keySet().iterator();

            int i;
            while(var39.hasNext()) {
                cat = (String)var39.next();
                i = (Integer)this.tempCatTotals.get(cat);
                if (i != 0) {
                    if (i > t0) {
                        t0 = i;
                        cat = cat;
                    }

                    unsortedMap.put(cat, i);
                }
            }

            if (cat != null) {
                unsortedMap.put(cat, unsortedMap.get(cat));
            }

            Comparator<Map.Entry<String, Integer>> valueComparator = (e1, e2) -> {
                return ((Integer)e2.getValue()).compareTo((Integer)e1.getValue());
            };
            Map<String, Integer> sortedMap = (Map)unsortedMap.entrySet().stream().sorted(valueComparator).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> {
                return e1;
            }, LinkedHashMap::new));
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            i = 0;

            int a;
            for(Iterator var15 = sortedMap.keySet().iterator(); var15.hasNext(); ++i) {
                String field = (String)var15.next();
                GL11.glPushMatrix();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glTranslatef((float)(xx + 253), (float)(yy + 16 + i * 18 + (i > 0 ? 4 : 0)), 0.0F);
                GL11.glScaled(0.0625D, 0.0625D, 0.0625D);
                this.mc.renderEngine.bindTexture(ResearchCategories.getResearchCategory(field).icon);
                this.drawTexturedModalRect(0, 0, 0, 0, 255, 255);
                GL11.glPopMatrix();
                GL11.glTranslatef(0.0F, 0.0F, 5.0F);
                String s = sortedMap.get(field) + "%";
                if (i > this.table.data.penaltyStart) {
                    a = (Integer)sortedMap.get(field) / 3;
                    s = s + " (-" + a + ")";
                }

                this.mc.fontRenderer.drawStringWithShadow(s, (float)(xx + 276), (float)(yy + 20 + i * 18 + (i > this.table.data.penaltyStart ? 4 : 0)), this.table.data.categoriesBlocked.contains(field) ? 6316128 : (i <= this.table.data.penaltyStart ? '\ue0c0' : 16777215));
                if (sparkle.contains(field)) {
                    for(a = 0; a < 2; ++a) {
                        float rr = (float) MathHelper.getInt(this.player.getRNG(), 255, 255) / 255.0F;
                        float gg = (float)MathHelper.getInt(this.player.getRNG(), 189, 255) / 255.0F;
                        float bb = (float)MathHelper.getInt(this.player.getRNG(), 64, 255) / 255.0F;
                        FXDispatcher.INSTANCE.drawSimpleSparkleGui(this.player.getRNG(), (double)((float)(xx + 276) + this.player.getRNG().nextFloat() * (float)this.fontRenderer.getStringWidth(s)), (double)((float)(yy + 20) + this.player.getRNG().nextFloat() * 8.0F + (float)(i * 18) + (float)(i > this.table.data.penaltyStart ? 4 : 0)), this.player.world.rand.nextGaussian() * 0.5D, this.player.world.rand.nextGaussian() * 0.5D, 24.0F, rr, gg, bb, 0, 0.9F, -1.0F);
                    }
                }

                a = mx - (xx + 256);
                int var8 = my - (yy + 16 + i * 18 + (i > this.table.data.penaltyStart ? 4 : 0));
                if (a >= 0 && var8 >= 0 && a < 16 && var8 < 16) {
                    GL11.glPushMatrix();
                    UtilsFX.drawCustomTooltip(this, this.fontRenderer, Arrays.asList(ResearchCategories.getCategoryName(field)), mx + 8, my + 8, 11);
                    GL11.glPopMatrix();
                    RenderHelper.disableStandardItemLighting();
                }
            }

            int sx = 128;
            int cw = 110;
            int sz = this.cardChoices.size();
            a = 0;

            for(Iterator var45 = this.cardChoices.iterator(); var45.hasNext(); ++a) {
                ResearchTableData.CardChoice cardChoice = (ResearchTableData.CardChoice)var45.next();
                random = new Random(cardChoice.card.getSeed());
                int var7 = mx - (5 + sx - 55 * sz + xx + cw * a);
                int var8 = my - (100 + yy - 60);
                float[] var10000;
                if ((double)this.cardZoomOut[a] >= 0.95D && !this.cardSelected) {
                    if (var7 >= 0 && var8 >= 0 && var7 < 100 && var8 < 120) {
                        var10000 = this.cardHover;
                        var10000[a] += Math.max((0.25F - this.cardHover[a]) / 3.0F * partialTicks, 0.0025F);
                    } else {
                        var10000 = this.cardHover;
                        var10000[a] -= 0.1F * partialTicks;
                    }
                }

                float prevZoomIn;
                if (a == sz - 1 || (double)this.cardZoomOut[a + 1] > 0.6D) {
                    prevZoomIn = this.cardZoomOut[a];
                    var10000 = this.cardZoomOut;
                    var10000[a] += Math.max((1.0F - this.cardZoomOut[a]) / 5.0F * partialTicks, 0.0025F);
                    if (this.cardZoomOut[a] > 0.0F && prevZoomIn == 0.0F) {
                        this.playButtonPageFlip();
                    }
                }

                prevZoomIn = this.cardZoomIn[a];
                if (this.cardSelected) {
                    var10000 = this.cardZoomIn;
                    var10000[a] = (float)((double)var10000[a] + (this.cardActive[a] ? Math.max((double)((1.0F - this.cardZoomIn[a]) / 3.0F * partialTicks), 0.0025D) : (double)(0.3F * partialTicks)));
                    this.cardHover[a] = 1.0F - this.cardZoomIn[a];
                }

                this.cardZoomIn[a] = MathHelper.clamp(this.cardZoomIn[a], 0.0F, 1.0F);
                this.cardHover[a] = MathHelper.clamp(this.cardHover[a], 0.0F, 0.25F);
                this.cardZoomOut[a] = MathHelper.clamp(this.cardZoomOut[a], 0.0F, 1.0F);
                float dx = (float)(55 + sx - 55 * sz + xx + cw * a - (xx + 65));
                float fx = (float)(xx + 65) + dx * this.cardZoomOut[a];
                float qx = (float)(xx + 191) - fx;
                if (this.cardActive[a]) {
                    fx += qx * this.cardZoomIn[a];
                }

                this.drawSheet((double)fx, (double)(yy + 100), (double)(6.0F + this.cardZoomOut[a] * 2.0F - this.cardZoomIn[a] * 2.0F + this.cardHover[a]), random, this.cardActive[a] ? 1.0F : 1.0F - this.cardZoomIn[a], Math.max(1.0F - this.cardZoomOut[a], this.cardZoomIn[a]), cardChoice);
                if (this.cardSelected && this.cardActive[a] && this.cardZoomIn[a] >= 1.0F && prevZoomIn < 1.0F) {
                    this.playButtonWrite();
                    this.cardChoices.clear();
                    this.cardSelected = false;
                    this.lastDraw = this.table.data.lastDraw;
                    break;
                }
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelper.disableStandardItemLighting();
    }

    private void drawSheet(double x, double y, double scale, Random r, float alpha, float tilt, ResearchTableData.CardChoice cardChoice) {
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
        GL11.glTranslated(x + r.nextGaussian(), y + r.nextGaussian(), 0.0D);
        GL11.glScaled(scale, scale, 0.0D);
        GL11.glRotated(r.nextGaussian() * (double)tilt, 0.0D, 0.0D, 1.0D);
        GL11.glPushMatrix();
        if (cardChoice != null && cardChoice.fromAid) {
            this.mc.renderEngine.bindTexture(this.txPaperGilded);
        } else {
            this.mc.renderEngine.bindTexture(this.txPaper);
        }

        if (r.nextBoolean()) {
            GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
        }

        if (r.nextBoolean()) {
            GL11.glRotated(180.0D, 0.0D, 1.0D, 0.0D);
        }

        GL11.glDisable(2884);
        UtilsFX.drawTexturedQuadFull(-8.0F, -8.0F, 0.0D);
        GL11.glEnable(2884);
        GL11.glPopMatrix();
        if (cardChoice != null && alpha == 1.0F) {
            if (cardChoice.card.getResearchCategory() != null) {
                ResearchCategory rc = ResearchCategories.getResearchCategory(cardChoice.card.getResearchCategory());
                if (rc != null) {
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha / 6.0F);
                    GL11.glPushMatrix();
                    GL11.glScaled(0.5D, 0.5D, 0.0D);
                    this.mc.renderEngine.bindTexture(rc.icon);
                    UtilsFX.drawTexturedQuadFull(-8.0F, -8.0F, 0.0D);
                    GL11.glPopMatrix();
                }
            }

            GL11.glPushMatrix();
            GL11.glScaled(0.0625D, 0.0625D, 0.0D);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, alpha);
            String name = TextFormatting.BOLD + cardChoice.card.getLocalizedName() + TextFormatting.RESET;
            int sz = this.fontRenderer.getStringWidth(name);
            this.fontRenderer.drawString(name, -sz / 2, -65, 0);
            this.fontRenderer.drawSplitString(cardChoice.card.getLocalizedText(), -70, -48, 140, 0);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            this.mc.renderEngine.bindTexture(this.txBase);
            GL11.glScaled(0.0625D, 0.0625D, 0.0D);
            int cc = cardChoice.card.getInspirationCost();
            boolean add = false;
            if (cc < 0) {
                add = true;
                cc = Math.abs(cc) + 1;
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);

            for(int a = 0; a < cc; ++a) {
                if (a == 0 && add) {
                    this.drawTexturedModalRect(-10 * cc + 20 * a, -95, 48, 0, 16, 16);
                } else {
                    this.drawTexturedModalRect(-10 * cc + 20 * a, -95, 32, 96, 16, 16);
                }
            }

            GL11.glPopMatrix();
            if (cardChoice.card.getRequiredItems() != null) {
                ItemStack[] items = cardChoice.card.getRequiredItems();
                GL11.glPushMatrix();

                for(int a = 0; a < items.length; ++a) {
                    if (items[a] != null && !items[a].isEmpty()) {
                        GL11.glPushMatrix();
                        GL11.glScaled(0.125D, 0.125D, 0.0D);
                        RenderHelper.enableGUIStandardItemLighting();
                        GlStateManager.disableLighting();
                        GlStateManager.enableRescaleNormal();
                        GlStateManager.enableColorMaterial();
                        GlStateManager.enableLighting();
                        this.itemRender.renderItemAndEffectIntoGUI(items[a], -9 * items.length + 18 * a, 35);
                        GlStateManager.disableLighting();
                        GlStateManager.depthMask(true);
                        GlStateManager.enableDepth();
                        GL11.glPopMatrix();

                        try {
                            if (cardChoice.card.getRequiredItemsConsumed()[a]) {
                                GL11.glPushMatrix();
                                this.mc.renderEngine.bindTexture(this.txBase);
                                GL11.glScaled(0.125D, 0.125D, 0.0D);
                                float s = (float)Math.sin((double)(((float)(this.player.ticksExisted + a * 2) + this.mc.getRenderPartialTicks()) / 2.0F)) * 0.03F;
                                GL11.glTranslated((double)(-2 - 9 * items.length + 18 * a), (double)(45.0F + s * 10.0F), 0.0D);
                                GL11.glScaled(0.5D, 0.5D + (double)s, 0.0D);
                                this.drawTexturedModalRect(0, 0, 64, 120, 16, 16);
                                GL11.glPopMatrix();
                            }
                        } catch (Exception var18) {
                        }
                    } else {
                        GL11.glPushMatrix();
                        this.mc.renderEngine.bindTexture(this.txQuestion);
                        GL11.glScaled(0.125D, 0.125D, 0.0D);
                        GL11.glColor4f(0.75F, 0.75F, 0.75F, alpha);
                        GL11.glTranslated((double)(-9 * items.length + 18 * a), 35.0D, 0.0D);
                        UtilsFX.drawTexturedQuadFull(0.0F, 0.0F, 0.0D);
                        GL11.glPopMatrix();
                    }
                }

                GL11.glPopMatrix();
            }
        }

        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    private void drawSheetOverlay(double x, double y, ResearchTableData.CardChoice cardChoice, int mx, int my) {
        GL11.glPushMatrix();
        if (cardChoice != null && cardChoice.card.getRequiredItems() != null) {
            ItemStack[] items = cardChoice.card.getRequiredItems();

            for(int a = 0; a < items.length; ++a) {
                if (this.isPointInRegion((int)(x - (double)(9 * items.length) + (double)(18 * a)), (int)(y + 36.0D), 15, 15, mx, my)) {
                    if (items[a] != null && !items[a].isEmpty()) {
                        this.renderToolTip(items[a], mx, my);
                    } else {
                        this.drawHoveringText(Arrays.asList(I18n.translateToLocal("tc.card.unknown")), mx, my);
                    }
                }
            }
        }

        GL11.glPopMatrix();
    }

    private void drawCards() {
        this.cardSelected = false;
        this.cardHover = new float[]{0.0F, 0.0F, 0.0F};
        this.cardZoomOut = new float[]{0.0F, 0.0F, 0.0F};
        this.cardZoomIn = new float[]{0.0F, 0.0F, 0.0F};
        this.cardActive = new boolean[]{true, true, true};
        int draw = 2;
        if (this.table.data.bonusDraws > 0) {
            ++draw;
            --this.table.data.bonusDraws;
        }

        this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, draw);
        this.cardChoices.clear();
    }

    public void initGui() {
        super.initGui();
        this.buttonList.add(this.buttonCreate);
        this.buttonCreate.x = this.guiLeft + 128;
        this.buttonCreate.y = this.guiTop + 22;
        this.buttonList.add(this.buttonComplete);
        this.buttonComplete.x = this.guiLeft + 191;
        this.buttonComplete.y = this.guiTop + 96;
        this.buttonList.add(this.buttonScrap);
        this.buttonScrap.x = this.guiLeft + 128;
        this.buttonScrap.y = this.guiTop + 168;
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            this.playButtonClick();
            PacketHandler.INSTANCE.sendToServer(new PacketStartTheoryToServer(this.table.getPos(), this.selectedAids));
        } else if (button.id == 7) {
            this.playButtonClick();
            this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, 7);
            this.tempCatTotals.clear();
            this.lastDraw = null;
        } else if (button.id == 9) {
            this.playButtonClick();
            this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, 9);
            this.tempCatTotals.clear();
            this.lastDraw = null;
            this.table.data = null;
            this.cardChoices.clear();
        } else {
            super.actionPerformed(button);
        }

    }

    private void checkButtons() {
        this.buttonComplete.active = false;
        this.buttonComplete.visible = false;
        this.buttonScrap.active = false;
        this.buttonScrap.visible = false;
        if (this.table.data != null) {
            this.buttonCreate.active = false;
            this.buttonCreate.visible = false;
            if (this.table.data.isComplete()) {
                this.buttonComplete.active = true;
                this.buttonComplete.visible = true;
            } else {
                this.buttonScrap.active = true;
                this.buttonScrap.visible = true;
            }
        } else {
            this.buttonCreate.visible = true;
            if (this.table.getStackInSlot(1) != null && this.table.getStackInSlot(0) != null && this.table.getStackInSlot(0).getItemDamage() != this.table.getStackInSlot(0).getMaxDamage()) {
                this.buttonCreate.active = true;
            } else {
                this.buttonCreate.active = false;
            }
        }

    }

    protected void mouseClicked(int mx, int my, int par3) throws IOException {
        super.mouseClicked(mx, my, par3);
        int xx = (this.width - this.xSize) / 2;
        int yy = (this.height - this.ySize) / 2;
        int pressed;
        if (this.table.data == null) {
            if (!this.currentAids.isEmpty()) {
                int side = Math.min(this.currentAids.size(), 6);
                int c = 0;
                pressed = 0;
                Iterator var9 = this.currentAids.iterator();

                while(var9.hasNext()) {
                    String key = (String)var9.next();
                    ITheorycraftAid mutator = (ITheorycraftAid)TheorycraftManager.aids.get(key);
                    if (mutator != null) {
                        int x = 128 + 20 * c - side * 10;
                        int y = 85 + 35 * pressed;
                        if (this.isPointInRegion(x, y, 16, 16, mx, my)) {
                            if (this.selectedAids.contains(key)) {
                                this.selectedAids.remove(key);
                            } else if (this.selectedAids.size() + 1 < this.dummyInspirationStart) {
                                this.selectedAids.add(key);
                            }
                        }

                        ++c;
                        if (c >= side) {
                            ++pressed;
                            c = 0;
                        }
                    }
                }
            }
        } else {
            int sx = 128;
            int cw = 110;
            int a;
            if (this.cardChoices.size() > 0) {
                pressed = -1;

                for(a = 0; a < this.cardChoices.size(); ++a) {
                    int var7 = mx - (5 + sx - 55 * this.cardChoices.size() + xx + cw * a);
                    int var8 = my - (100 + yy - 60);
                    if ((double)this.cardZoomOut[a] >= 0.95D && !this.cardSelected && var7 >= 0 && var8 >= 0 && var7 < 100 && var8 < 120) {
                        pressed = a;
                        break;
                    }
                }

                if (pressed >= 0 && this.table.getStackInSlot(0) != null && this.table.getStackInSlot(0).getItemDamage() != this.table.getStackInSlot(0).getMaxDamage()) {
                    this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, 4 + pressed);
                }
            } else {
                pressed = mx - (25 + xx);
                a = my - (55 + yy);
                if (pressed >= 0 && a >= 0 && pressed < 75 && a < 90 && this.table.getStackInSlot(1) != null) {
                    this.drawCards();
                }
            }
        }

    }

    void checkCards() {
        if (this.table.data.cardChoices.size() > 0 && this.cardChoices.isEmpty()) {
            this.syncFromTableChoices();
        }

        if (!this.cardSelected) {
            for(int a = 0; a < this.cardChoices.size(); ++a) {
                try {
                    if (this.table.data != null && this.table.data.cardChoices.size() > a && ((ResearchTableData.CardChoice)this.table.data.cardChoices.get(a)).selected) {
                        for(int q = 0; q < this.cardChoices.size(); ++q) {
                            this.cardActive[q] = ((ResearchTableData.CardChoice)this.table.data.cardChoices.get(q)).selected;
                        }

                        this.cardSelected = true;
                        this.playButtonPageSelect();
                        this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, 1);
                        break;
                    }
                } catch (Exception var3) {
                }
            }
        }

    }

    private void playButtonPageFlip() {
        this.mc.getRenderViewEntity().playSound(SoundsTC.page, 1.0F, 1.0F);
    }

    private void playButtonPageSelect() {
        this.mc.getRenderViewEntity().playSound(SoundsTC.pageturn, 1.0F, 1.0F);
    }

    private void playButtonClick() {
        this.mc.getRenderViewEntity().playSound(SoundsTC.clack, 0.4F, 1.0F);
    }

    private void playButtonWrite() {
        this.mc.getRenderViewEntity().playSound(SoundsTC.write, 0.3F, 1.0F);
    }
}
