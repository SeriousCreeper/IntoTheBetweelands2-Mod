package com.seriouscreeper.bladditions.mixins.fairylights;

import com.pau101.fairylights.server.item.LightVariant;
import com.pau101.fairylights.server.item.crafting.Recipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = Recipes.class, remap = false)
public class MixinFairyLightsRecipes {
    /**
     * @author SC
     */
    @Overwrite
    @SubscribeEvent
    public static void register(RegistryEvent.Register<IRecipe> event) {
    }
}
