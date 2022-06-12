package com.seriouscreeper.bladditions.mixins.modsupport.simpledifficulty;

import com.charles445.simpledifficulty.api.SDCapabilities;
import com.charles445.simpledifficulty.api.SDCompatibility;
import com.charles445.simpledifficulty.api.config.QuickConfig;
import com.charles445.simpledifficulty.api.thirst.IThirstCapability;
import com.charles445.simpledifficulty.client.gui.ThirstGui;
import com.charles445.simpledifficulty.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(value = ThirstGui.class, remap = false)
public class MixinThirstGui {
    @Shadow
    private final Random rand = new Random();
    @Shadow
    private int updateCounter = 0;

    @Shadow
    public static final ResourceLocation ICONS = new ResourceLocation("simpledifficulty:textures/gui/icons.png");
    @Shadow
    public static final ResourceLocation THIRSTHUD = new ResourceLocation("simpledifficulty:textures/gui/thirsthud.png");



    @Shadow
    private void bind(ResourceLocation resource) {
    }


    @Shadow
    private void renderThirst(int width, int height, int thirst, float thirstSaturation) {
    }


    /**
     * @author SC
     */
    @Overwrite
    @SubscribeEvent
    public void onPreRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
    }
}
