package com.seriouscreeper.bladditions.mixins.modsupport.roots;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualSummonCreatures;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = RitualSummonCreatures.class, remap = false)
public class MixinRitualSummonCreatures extends RitualBase {
    public MixinRitualSummonCreatures(String name, boolean disabled) {
        super(name, disabled);
    }

    /**
     * @author SC
     */
    @Overwrite
    public void init() {
        this.recipe = new RitualBase.RitualRecipe(this, new Object[]{new ItemStack(Items.WHEAT_SEEDS), new OreIngredient("cropWheat"), new OreIngredient("egg"), new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.WHEAT_SEEDS)});
        this.setIcon(ModItems.ritual_summon_creatures);
        this.setColor(TextFormatting.DARK_PURPLE);
        this.setBold(true);
    }

    @Shadow
    public void doFinalise() {
    }
}
