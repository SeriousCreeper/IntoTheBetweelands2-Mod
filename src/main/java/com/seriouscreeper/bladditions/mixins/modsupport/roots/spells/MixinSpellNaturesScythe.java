package com.seriouscreeper.bladditions.mixins.modsupport.roots.spells;

import epicsquid.roots.spell.SpellNaturesScythe;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thebetweenlands.common.TheBetweenlands;
import thebetweenlands.common.registries.ItemRegistry;

@Pseudo
@Mixin(value = SpellNaturesScythe.class, remap = false, priority = -9999)
public class MixinSpellNaturesScythe {
    @Mutable
    @Final
    @Shadow public static ItemStack SHEARS;

    static {
        SHEARS = new ItemStack(ItemRegistry.SYRMORITE_SHEARS);
    }
}
