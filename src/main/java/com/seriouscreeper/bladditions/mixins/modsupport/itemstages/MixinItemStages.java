package com.seriouscreeper.bladditions.mixins.modsupport.itemstages;

import com.google.common.collect.ListMultimap;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.itemstages.ConfigurationHandler;
import net.darkhax.itemstages.ItemStages;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;

@Mixin(value = ItemStages.class, remap = false)
public class MixinItemStages {
    @Shadow
    public static String getStage(ItemStack stack) {
        return null;
    }

    @Shadow
    public static String getEnchantStage(ItemStack stack) {
        return null;
    }

    @Shadow
    private static String getUnfamiliarName(ItemStack stack) {
        return "Unfamiliar Item";
    }

    @Shadow public static ListMultimap<String, String> tooltipStages;

    /**
     * @author
     * @reason
     */
    @Overwrite
    @SideOnly(Side.CLIENT)
    @SubscribeEvent(
            priority = EventPriority.LOWEST
    )
    public void onTooltip(ItemTooltipEvent event) {
        EntityPlayerSP player = PlayerUtils.getClientPlayerSP();
        if (player != null) {
            String itemsStage = getStage(event.getItemStack());
            String enchantStage = getEnchantStage(event.getItemStack());
            if (itemsStage != null && !GameStageHelper.hasStage(player, itemsStage) && ConfigurationHandler.changeRestrictionTooltip) {
                event.getToolTip().clear();
                event.getToolTip().add(TextFormatting.WHITE + getUnfamiliarName(event.getItemStack()));
                event.getToolTip().add(" ");
                event.getToolTip().add(TextFormatting.RED + "" + TextFormatting.ITALIC + I18n.format("tooltip.itemstages.description", new Object[0]));
                event.getToolTip().add(TextFormatting.RED + I18n.format("tooltip.itemstages.info", new Object[]{makeStageReadable(itemsStage)}));
            }

            if (enchantStage != null && !GameStageHelper.hasStage(player, enchantStage) && ConfigurationHandler.changeRestrictionTooltip) {
                event.getToolTip().add(" ");
                event.getToolTip().add(TextFormatting.RED + "" + TextFormatting.ITALIC + I18n.format("tooltip.itemstages.enchant", new Object[0]));
                event.getToolTip().add(TextFormatting.RED + I18n.format("tooltip.itemstages.info", new Object[]{enchantStage}));
            } else if (itemsStage != null && (event.getEntityPlayer() != null && event.getEntityPlayer().isCreative() || event.getFlags() == ITooltipFlag.TooltipFlags.ADVANCED)) {
                event.getToolTip().add(TextFormatting.BLUE + I18n.format("tooltip.itemstages.stage", new Object[0]) + " " + TextFormatting.WHITE + makeStageReadable(itemsStage));
            }

            Iterator var5 = tooltipStages.keySet().iterator();

            while(true) {
                String tipStage;
                do {
                    if (!var5.hasNext()) {
                        return;
                    }

                    tipStage = (String)var5.next();
                } while(GameStageHelper.hasStage(player, tipStage));

                Iterator<String> iterator = event.getToolTip().iterator();

                while(iterator.hasNext()) {
                    String tooltipLine = (String)iterator.next();
                    Iterator var9 = tooltipStages.get(tipStage).iterator();

                    while(var9.hasNext()) {
                        String restricted = (String)var9.next();
                        if (tooltipLine.startsWith(restricted)) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }


    private String makeStageReadable(String stage) {
        StringBuilder newStage = new StringBuilder();
        String[] split = stage.split("_");

        for(String part : split) {
            if(!part.equals("of")) {
                part = capitalize(part);
            }

            newStage.append(part);
            newStage.append(" ");
        }

        if(newStage.length() > 0)
            newStage.deleteCharAt(newStage.length() - 1);

        return newStage.toString();
    }


    private String capitalize(String str)
    {
        if (str == null || str.length() == 0) return str;

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
