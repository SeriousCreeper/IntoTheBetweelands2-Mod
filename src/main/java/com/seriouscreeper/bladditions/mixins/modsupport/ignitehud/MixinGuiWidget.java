package com.seriouscreeper.bladditions.mixins.modsupport.ignitehud;

import com.charles445.simpledifficulty.api.SDCapabilities;
import com.charles445.simpledifficulty.api.SDCompatibility;
import com.charles445.simpledifficulty.api.SDPotions;
import com.charles445.simpledifficulty.api.config.QuickConfig;
import com.charles445.simpledifficulty.api.thirst.IThirstCapability;
import com.charles445.simpledifficulty.client.gui.ThirstGui;
import com.charles445.simpledifficulty.config.ModConfig;
import com.charles445.simpledifficulty.util.RenderUtil;
import com.deadzoke.ignitehud.IgniteHUD;
import com.deadzoke.ignitehud.References;
import com.deadzoke.ignitehud.config.Config;
import com.deadzoke.ignitehud.gui.GuiWidget;
import com.deadzoke.ignitehud.util.RenderHelper;
import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

@Mixin(value = GuiWidget.class, remap = false)
public class MixinGuiWidget {
    private int updateCounter = 0;
    private final Random rand = new Random();
    private static final int texturepos_X = 0;
    private static final int texturepos_Y = 0;
    private static final int textureWidth = 9;
    private static final int textureHeight = 9;

    @Shadow
    Minecraft minecraft = Minecraft.getMinecraft();

    /**
     * @author SC
     */
    @Overwrite
    @SubscribeEvent(
            priority = EventPriority.HIGHEST
    )
    public void renderOverlay(RenderGameOverlayEvent.Pre event) {
        RenderGameOverlayEvent.ElementType type = event.getType();
        if (type == RenderGameOverlayEvent.ElementType.POTION_ICONS) {
            event.setCanceled(true);
        }

        World world = this.minecraft.world;
        EntityPlayerSP player = this.minecraft.player;
        ScaledResolution scaled = new ScaledResolution(this.minecraft);

        if (this.minecraft.playerController.gameIsSurvivalOrAdventure()) {
            if (type != RenderGameOverlayEvent.ElementType.TEXT) {
                return;
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPushMatrix();
            //this.getWidgetBase(player);
            //this.getPlayerHealthBar(player);
            //this.getPlayerFoodBar(player);
            //this.getPlayerAirBar(player, scaled);
            //this.getMountInfo(player);
            //this.getFoodValue(player);
            //this.getSatuValue(player);
            //this.getArmorValue(player);


            if (Config.cfgDurabilities) {
                this.getDurabilities(player);
            }

            this.getEffects(player, scaled);

            GL11.glPopMatrix();
        }
    }


    /**
     * @author SC
     */
    @Overwrite
    private void getThirst(EntityPlayerSP player) {
    }


    /**
     * @author SC
     */
    @Overwrite
    public void renderEntityStats(net.minecraftforge.client.event.RenderLivingEvent.Pre<EntityLivingBase> event) {
    }


    @Shadow
    private void getPlayerHealthBar(EntityPlayerSP player){}


    @Shadow
    private void getWidgetBase(EntityPlayerSP player){}


    @Shadow
    private void getPlayerAirBar(EntityPlayerSP player, ScaledResolution scaled) {}

    /**
     * @author SC
     */
    @Overwrite
    private void getDurabilities(EntityPlayerSP player) {
        ItemStack head = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        ItemStack legs = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        ItemStack feet = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        ItemStack mainhand = player.getHeldItemMainhand();
        ItemStack offhand = player.getHeldItemOffhand();
        int pos = 0;
        RenderHelper.addDurabilityDisplay(head, pos);
        if (head != null && head.getItem() != Items.AIR) {
            pos += 25;
        }

        RenderHelper.addDurabilityDisplay(chest, pos);
        if (chest != null && chest.getItem() != Items.AIR) {
            pos += 25;
        }

        RenderHelper.addDurabilityDisplay(legs, pos);
        if (legs != null && legs.getItem() != Items.AIR) {
            pos += 25;
        }

        RenderHelper.addDurabilityDisplay(feet, pos);
        if (feet != null && feet.getItem() != Items.AIR) {
            pos += 25;
        }

        RenderHelper.addDurabilityDisplay(mainhand, pos);
        if (mainhand != null && mainhand.getItem() != Items.AIR) {
            pos += 25;
        }

        RenderHelper.addDurabilityDisplay(offhand, pos);
    }

    /**
     * @author SC
     * @reason
     */
    @Overwrite
    private void getEffects(EntityPlayerSP player, ScaledResolution scaled) {
        int screenWidth = scaled.getScaledWidth();
        int screenHeight = scaled.getScaledHeight();
        Collection<PotionEffect> collection = player.getActivePotionEffects();
        if (!collection.isEmpty()) {
            GlStateManager.enableBlend();
            int i = 0;
            int j = 0;
            Iterator var8 = Ordering.natural().reverse().sortedCopy(collection).iterator();

            while(var8.hasNext()) {
                PotionEffect potioneffect = (PotionEffect)var8.next();
                Potion potion = potioneffect.getPotion();
                if (potioneffect.doesShowParticles()) {
                    int posY = 5;
                    String duration = Potion.getPotionDurationString(potioneffect, 1.0F);

                    int icon = -1;
                    if (potion.getName().equals("effect.moveSpeed")) {
                        icon = 0;
                    }

                    if (potion.getName().equals("effect.moveSlowdown")) {
                        icon = 1;
                    }

                    if (potion.getName().equals("effect.digSpeed")) {
                        icon = 2;
                    }

                    if (potion.getName().equals("effect.digSlowDown")) {
                        icon = 3;
                    }

                    if (potion.getName().equals("effect.damageBoost")) {
                        icon = 4;
                    }

                    if (potion.getName().equals("effect.weakness")) {
                        icon = 5;
                    }

                    if (potion.getName().equals("effect.poison")) {
                        icon = 6;
                    }

                    if (potion.getName().equals("effect.regeneration")) {
                        icon = 7;
                    }

                    if (potion.getName().equals("effect.invisibility")) {
                        icon = 8;
                    }

                    if (potion.getName().equals("effect.hunger")) {
                        icon = 9;
                    }

                    if (potion.getName().equals("effect.jump")) {
                        icon = 10;
                    }

                    if (potion.getName().equals("effect.confusion")) {
                        icon = 11;
                    }

                    if (potion.getName().equals("effect.nightVision")) {
                        icon = 12;
                    }

                    if (potion.getName().equals("effect.blindness")) {
                        icon = 13;
                    }

                    if (potion.getName().equals("effect.resistance")) {
                        icon = 14;
                    }

                    if (potion.getName().equals("effect.fireResistance")) {
                        icon = 15;
                    }

                    if (potion.getName().equals("effect.waterBreathing")) {
                        icon = 16;
                    }

                    if (potion.getName().equals("effect.wither")) {
                        icon = 17;
                    }

                    if (potion.getName().equals("effect.absorption")) {
                        icon = 18;
                    }

                    if (potion.getName().equals("effect.levitation")) {
                        icon = 19;
                    }

                    if (potion.getName().equals("effect.glowing")) {
                        icon = 20;
                    }

                    if (potion.getName().equals("effect.luck")) {
                        icon = 21;
                    }

                    if (potion.getName().equals("effect.unluck")) {
                        icon = 22;
                    }

                    if (potion.getName().equals("effect.healthBoost")) {
                        icon = 23;
                    }

                    if (IgniteHUD.hasToughAsNails) {
                        if (potion.getName().equals("potion.thirst")) {
                            icon = 24;
                        }

                        if (potion.getName().equals("potion.hydration")) {
                            icon = 25;
                        }

                        if (potion.getName().equals("potion.hypothermia")) {
                            icon = 26;
                        }

                        if (potion.getName().equals("potion.hyperthermia")) {
                            icon = 27;
                        }

                        if (potion.getName().equals("potion.heat_resistance")) {
                            icon = 28;
                        }

                        if (potion.getName().equals("potion.cold_resistance")) {
                            icon = 29;
                        }
                    }

                    int posX;
                    ++i;
                    posX = screenWidth - 30 * (i % 5);
                    posY = screenHeight - (27 * Math.floorDiv(i, 5)) - 21;

                    float f = 1.0F;
                    if (potioneffect.getDuration() <= 200) {
                        int j1 = 10 - potioneffect.getDuration() / 20;
                        f = net.minecraft.util.math.MathHelper.clamp((float)potioneffect.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + net.minecraft.util.math.MathHelper.cos((float)potioneffect.getDuration() * 3.1415927F / 5.0F) * net.minecraft.util.math.MathHelper.clamp((float)j1 / 10.0F * 0.25F, 0.0F, 0.25F);
                    }

                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    this.minecraft.renderEngine.bindTexture(References.TEX_HUD_BASE);
                    this.minecraft.ingameGUI.drawTexturedModalRect(posX, posY, 88, 0, 29, 21);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, f);

                    ResourceLocation tempEffectLocation = References.TEX_HUD_EFFECT;
                    int textureX = icon % 14 * 18;
                    int textureY = icon / 14 * 18;
                    int textureWidth = 18;
                    int textureHeight = 18;

                    if(potion.getRegistryName().getNamespace().equals("thaumcraft")) {
                        tempEffectLocation = new ResourceLocation("thaumcraft", "textures/misc/potions.png");
                        icon = potion.getStatusIconIndex();

                        textureX = icon % 8 * 18;
                        textureY = 198 + icon / 8 * 18;
                    }

                    if(potion.getRegistryName().getNamespace().equals("thebetweenlands")) {
                        tempEffectLocation = new ResourceLocation("thebetweenlands", "textures/items/strictly_herblore/misc/vial_green.png");
                        textureX = 0;
                        textureY = 0;
                        textureWidth = 16;
                        textureHeight = 16;
                    }

                    if(icon == -1) {
                        potion.renderHUDEffect(potioneffect, null, posX + 3, posY - 8, 1, f);
                    } else {
                        this.minecraft.renderEngine.bindTexture(tempEffectLocation);
                        this.minecraft.ingameGUI.drawTexturedModalRect(posX + 5, posY - 3, textureX, textureY, textureWidth, textureHeight);
                    }

                    int color = potion.getLiquidColor();
                    RenderHelper.drawFontBoldCentered(duration, posX + 15, posY + 10, color <= 0 ? 16777215 : color, 0);
                }
            }
        }

    }


    public void onPreRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.AIR && QuickConfig.isThirstEnabled() && SDCompatibility.defaultThirstDisplay) {
            //Set the seed to avoid shaking during pausing
            rand.setSeed((long) (updateCounter * 445));

            boolean classic = ModConfig.client.classicHUDThirst;

            //Bind to custom icons image
            if (classic)
                bind(ThirstGui.ICONS);
            else
                bind(ThirstGui.THIRSTHUD);

            //Render thirst at the scaled resolution

            EntityPlayerSP player = Minecraft.getMinecraft().player;
            IThirstCapability capability = SDCapabilities.getThirstData(player);
            ScaledResolution resolution = event.getResolution();
            renderThirst(resolution.getScaledWidth(), resolution.getScaledHeight(), capability.getThirstLevel(), capability.getThirstSaturation());

            //Rebind to old icons image
            bind(Gui.ICONS);

            //Bump up the rendering height so air bubbles draw above thirst
            //TODO does this break any mods?
            GuiIngameForge.right_height += 10;
        }
    }


    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !minecraft.isGamePaused()) {
            ++this.updateCounter;
        }
    }

    private void renderThirst(int width, int height, int thirst, float thirstSaturation) {
        //DebugUtil.startTimer();

        //TODO performance? This probably runs fast enough though
        //Full bar seems to be 3m ns
        //Full bar + saturation is a little over 4m ns

        //thirst is 0 - 20
        GlStateManager.enableBlend();

        //Many mods set this and forget to set it back.
        //I'm setting it back pre-emptively because this has been reported with two mods.
        GlStateManager.color(1.0f, 1.0f, 1.0f);

        int left = width / 2 + 82; //Same x offset as the hunger bar
        int top = height - GuiIngameForge.right_height;

        //Draw the 10 thirst bubbles
        for (int i = 0; i < 10; i++)
        {
            int halfIcon = i * 2 + 1;
            int x = left - i * 8;
            int y = top;

            int bgXOffset = 0;
            int xOffset = 0;

            if (Minecraft.getMinecraft().player.isPotionActive(SDPotions.thirsty))
            {
                xOffset += (textureWidth * 4);
                bgXOffset = (textureWidth * 13);
            }


            //Shake based on saturation and thirst level
            if (thirstSaturation <= 0.0F && updateCounter % (thirst * 3 + 1) == 0)
            {
                y = top + (rand.nextInt(3) - 1);
            }

            //Background
            RenderUtil.drawTexturedModalRect(x, y, texturepos_X + bgXOffset, texturepos_Y, textureWidth, textureHeight);

            //Foreground
            if (halfIcon < thirst) //Full
                RenderUtil.drawTexturedModalRect(x, y, texturepos_X + xOffset + (textureWidth * 4), texturepos_Y, textureWidth, textureHeight);
            else if (halfIcon == thirst) //Half
                RenderUtil.drawTexturedModalRect(x, y, texturepos_X + xOffset + (textureWidth * 5), texturepos_Y, textureWidth, textureHeight);
        }

        //Draw the 10 saturation bubbles
        //Because AppleSkin is awesome and everybody knows it
        int thirstSaturationInt = (int)thirstSaturation;
        if(thirstSaturationInt > 0)
        {
            if(ModConfig.client.drawThirstSaturation)
            {
                for(int i = 0; i < 10; i++)
                {
                    int halfIcon = i * 2 + 1;
                    int x = left - i * 8;
                    int y = top;

                    //Foreground
                    if (halfIcon < thirstSaturationInt) //Full
                        RenderUtil.drawTexturedModalRect(x, y, texturepos_X + (textureWidth * 14), texturepos_Y, textureWidth, textureHeight);
                    else if (halfIcon == thirstSaturationInt) //Half
                        RenderUtil.drawTexturedModalRect(x, y, texturepos_X + (textureWidth * 15), texturepos_Y, textureWidth, textureHeight);
                }
            }
        }
        GlStateManager.disableBlend();

        //DebugUtil.stopTimer(true);
    }

    private void bind(ResourceLocation resource) {
        minecraft.getTextureManager().bindTexture(resource);
    }


    @Shadow
    private void getMountInfo(EntityPlayerSP player){}
}
