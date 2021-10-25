package com.seriouscreeper.bladditions.mixins.modsupport.ignitehud;

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
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@Mixin(value = GuiWidget.class, remap = false)
public class MixinGuiWidget {
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
        if (type == RenderGameOverlayEvent.ElementType.AIR || type == RenderGameOverlayEvent.ElementType.POTION_ICONS) {
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
            this.getPlayerAirBar(player, scaled);
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
        int pos = -40;
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

    @Shadow
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
                    int posY = screenHeight - 26;
                    String duration = Potion.getPotionDurationString(potioneffect, 1.0F);

                    int icon = 195;
                    if (potion.getName() == "effect.moveSpeed") {
                        icon = 0;
                    }

                    if (potion.getName() == "effect.moveSlowdown") {
                        icon = 1;
                    }

                    if (potion.getName() == "effect.digSpeed") {
                        icon = 2;
                    }

                    if (potion.getName() == "effect.digSlowDown") {
                        icon = 3;
                    }

                    if (potion.getName() == "effect.damageBoost") {
                        icon = 4;
                    }

                    if (potion.getName() == "effect.weakness") {
                        icon = 5;
                    }

                    if (potion.getName() == "effect.poison") {
                        icon = 6;
                    }

                    if (potion.getName() == "effect.regeneration") {
                        icon = 7;
                    }

                    if (potion.getName() == "effect.invisibility") {
                        icon = 8;
                    }

                    if (potion.getName() == "effect.hunger") {
                        icon = 9;
                    }

                    if (potion.getName() == "effect.jump") {
                        icon = 10;
                    }

                    if (potion.getName() == "effect.confusion") {
                        icon = 11;
                    }

                    if (potion.getName() == "effect.nightVision") {
                        icon = 12;
                    }

                    if (potion.getName() == "effect.blindness") {
                        icon = 13;
                    }

                    if (potion.getName() == "effect.resistance") {
                        icon = 14;
                    }

                    if (potion.getName() == "effect.fireResistance") {
                        icon = 15;
                    }

                    if (potion.getName() == "effect.waterBreathing") {
                        icon = 16;
                    }

                    if (potion.getName() == "effect.wither") {
                        icon = 17;
                    }

                    if (potion.getName() == "effect.absorption") {
                        icon = 18;
                    }

                    if (potion.getName() == "effect.levitation") {
                        icon = 19;
                    }

                    if (potion.getName() == "effect.glowing") {
                        icon = 20;
                    }

                    if (potion.getName() == "effect.luck") {
                        icon = 21;
                    }

                    if (potion.getName() == "effect.unluck") {
                        icon = 22;
                    }

                    if (potion.getName() == "effect.healthBoost") {
                        icon = 23;
                    }

                    if (IgniteHUD.hasToughAsNails) {
                        if (potion.getName() == "potion.thirst") {
                            icon = 24;
                        }

                        if (potion.getName() == "potion.hydration") {
                            icon = 25;
                        }

                        if (potion.getName() == "potion.hypothermia") {
                            icon = 26;
                        }

                        if (potion.getName() == "potion.hyperthermia") {
                            icon = 27;
                        }

                        if (potion.getName() == "potion.heat_resistance") {
                            icon = 28;
                        }

                        if (potion.getName() == "potion.cold_resistance") {
                            icon = 29;
                        }
                    }

                    int posX;
                    if (potion.isBeneficial()) {
                        ++i;
                        posX = screenWidth - 33 * i;
                        posY -= 24;
                    } else {
                        ++j;
                        posX = screenWidth - 33 * j;
                    }

                    float f = 1.0F;
                    if (potioneffect.getDuration() <= 200) {
                        int j1 = 10 - potioneffect.getDuration() / 20;
                        f = net.minecraft.util.math.MathHelper.clamp((float)potioneffect.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + net.minecraft.util.math.MathHelper.cos((float)potioneffect.getDuration() * 3.1415927F / 5.0F) * net.minecraft.util.math.MathHelper.clamp((float)j1 / 10.0F * 0.25F, 0.0F, 0.25F);
                    }

                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    this.minecraft.renderEngine.bindTexture(References.TEX_HUD_BASE);
                    this.minecraft.ingameGUI.drawTexturedModalRect(posX, posY, 88, 0, 29, 21);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, f);
                    this.minecraft.renderEngine.bindTexture(References.TEX_HUD_EFFECT);
                    this.minecraft.ingameGUI.drawTexturedModalRect(posX + 6, posY - 3, icon % 14 * 18, icon / 14 * 18, 18, 18);
                    RenderHelper.drawFontBoldCentered(duration, posX + 15, posY + 10, potion.getLiquidColor(), 0);
                }
            }
        }

    }

    @Shadow
    private void getMountInfo(EntityPlayerSP player){}
}
