package com.seriouscreeper.bladditions.mixins.modsupport.realisticstorage;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import oethever.realisticstorage.containerguard.ContainerGuard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.regex.Pattern;

@Mixin(value = ContainerGuard.class, remap = false)
public class MixinContainerGuard {
    @Final
    @Shadow
    private ArrayList<Pattern> alwaysEjectedPatterns = new ArrayList();

    @Final
    @Shadow
    private ArrayList<Pattern> neverEjectedPatterns = new ArrayList();

    /**
     * @author
     * @reason
     */
    @Overwrite
    private boolean isBigBlock(ItemStack stack) {
        Item item = stack.getItem();
        ResourceLocation itemRegistryName = item.getRegistryName();

        if (itemRegistryName == null)
            return false;

        String itemName = itemRegistryName.toString();
        String itemNameWithMeta = itemRegistryName.toString() + ":" + stack.getMetadata();

        for (Pattern pattern : alwaysEjectedPatterns) {
            if (pattern.matcher(itemName).matches() || pattern.matcher(itemNameWithMeta).matches())
                return true;
        }

        for (Pattern pattern : neverEjectedPatterns) {
            if (pattern.matcher(itemName).matches() || pattern.matcher(itemNameWithMeta).matches())
                return true;
        }

        return false;
    }
}
