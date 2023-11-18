package com.seriouscreeper.bladditions.mixins.modsupport.thaumicjei;

import com.buuz135.thaumicjei.ThaumcraftJEIPlugin;
import com.buuz135.thaumicjei.gui.FocalManipulatorAdvancedGuiHandler;
import com.buuz135.thaumicjei.gui.ResearchTableAdvancedGuiHandler;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(value = ThaumcraftJEIPlugin.class, remap = false)
public class MixinThaumcraftJEIPlugin {
    @Inject(method = "Lcom/buuz135/thaumicjei/ThaumcraftJEIPlugin;register(Lmezz/jei/api/IModRegistry;)V", at = @At(value = "INVOKE", target = "Lmezz/jei/api/IModRegistry;addIngredientInfo(Ljava/util/List;Lmezz/jei/api/recipe/IIngredientType;[Ljava/lang/String;)V"), cancellable = true)
    private void injected(IModRegistry registry, CallbackInfo ci) {
        registry.addIngredientInfo(Arrays.asList(new ItemStack(Item.getByNameOrId(new ResourceLocation("thaumcraft", "salis_mundus").toString())),
                new ItemStack(Item.getByNameOrId(new ResourceLocation("thaumcraft", "crystal_essence").toString()))), VanillaTypes.ITEM, "To create Salis Mundus, take 3 Vis Crystals and combine them with Bluedust and Raw Radiant Quartz in a Fey Crafter.");
        registry.addIngredientInfo(new ItemStack(Item.getByNameOrId(new ResourceLocation("thaumcraft", "triple_meat_treat").toString())), ItemStack.class, "To create the Triple Meat Treat, take 3 different kinds of meat nuggets (produced by cooking meat in the Infernal Furnace) and mix them with sugar.");

        registry.addAdvancedGuiHandlers(new ResearchTableAdvancedGuiHandler());
        registry.addAdvancedGuiHandlers(new FocalManipulatorAdvancedGuiHandler());

        ci.cancel();
    }
}
