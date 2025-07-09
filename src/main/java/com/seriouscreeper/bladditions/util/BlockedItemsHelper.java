package com.seriouscreeper.bladditions.util;

import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class BlockedItemsHelper {
    public static final ArrayList<Pattern> alwaysEjectedPatterns = new ArrayList<>();
    public static final ArrayList<Pattern> neverEjectedPatterns = new ArrayList<>();


    public static void updateConfig() {
        //updateRegexList(alwaysEjectedPatterns, ModConfig.alwaysEjectedItems);
        //updateRegexList(neverEjectedPatterns, ModConfig.neverEjectedItems);
    }


    private static void updateRegexList(ArrayList<Pattern> patterns, String[] stringPatterns) {
        patterns.clear();
        String[] var3 = stringPatterns;
        int var4 = stringPatterns.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String pattern = var3[var5];

            try {
                patterns.add(Pattern.compile(pattern));
            } catch (PatternSyntaxException var8) {
            }
        }
    }

    public static boolean isBlocked(ItemStack stack) {
        return false;

        /*
        if(neverEjectedPatterns.isEmpty()) {
            updateConfig();
        }

        Item item = stack.getItem();
        ResourceLocation itemRegistryName = item.getRegistryName();

        if (itemRegistryName == null)
            return false;

        String itemName = itemRegistryName.toString();
        String itemNameWithMeta = itemRegistryName.toString() + ":" + stack.getMetadata();

        for (Pattern pattern : neverEjectedPatterns) {
            if (pattern.matcher(itemName).matches() || pattern.matcher(itemNameWithMeta).matches()) {
                return true;
            }
        }

        return false;
         */
    }
}
