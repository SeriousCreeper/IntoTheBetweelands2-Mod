package com.seriouscreeper.bladditions.mixins.modsupport.growthcraft;

import growthcraft.core.shared.fluids.TaggedFluidStacks;
import growthcraft.core.shared.item.OreItemStacks;
import growthcraft.milk.common.Init;
import growthcraft.milk.shared.init.GrowthcraftMilkItems;
import growthcraft.milk.shared.utils.CheeseVatRecipeBuilder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.item.herblore.ItemCrushed;
import thebetweenlands.common.item.misc.ItemMisc;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.ItemRegistry;

@Mixin(value = Init.class, remap = false)
public class MixinInitMilk {
    /**
     * @author SC
     * @reason custom recipes
     */
    @Overwrite
    private static void registerCheeseVatRecipes() {
        String[] saltOres = new String[]{"salt", "itemSalt", "dustSalt", "foodSalt", "listAllSalt", "ingredientSalt", "pinchSalt", "portionSalt", "lumpSalt", "materialSalt"};
        String[] var1 = saltOres;
        int var2 = saltOres.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String saltOre = var1[var3];
            CheeseVatRecipeBuilder.buildRecipe("CHEDDAR Recipe").outputFluids(new FluidStack[]{GrowthcraftMilkItems.WaxedCheeseTypes.CHEDDAR.getFluids().asFluidStack(5000)}).inputFluids(new Object[]{new TaggedFluidStacks(5000, new String[]{"milk_curds"})})
                    .inputItems(new Object[]{
                            new OreItemStacks(saltOre, 1),
                            ItemMisc.EnumItemMisc.DRIED_SWAMP_REED.create(1),
                            ItemMisc.EnumItemMisc.COMPOST.create(1)
                    }).register();

            CheeseVatRecipeBuilder.buildRecipe("ASIAGO Recipe").outputFluids(new FluidStack[]{GrowthcraftMilkItems.AgedCheeseTypes.ASIAGO.getFluids().asFluidStack(5000)}).inputFluids(new Object[]{new TaggedFluidStacks(5000, new String[]{"milk_curds"})})
                    .inputItems(new Object[]{
                            new OreItemStacks(saltOre, 1),
                            new ItemStack(BlockRegistry.FUNGUS_CROP, 1, 0),
                            new ItemStack(BlockRegistry.SHELF_FUNGUS, 1, 0)
                    }).register();

            CheeseVatRecipeBuilder.buildRecipe("PARMESAN Recipe").outputFluids(new FluidStack[]{GrowthcraftMilkItems.AgedCheeseTypes.PARMESAN.getFluids().asFluidStack(5000)}).inputFluids(new Object[]{new TaggedFluidStacks(5000, new String[]{"milk_curds"})})
                    .inputItems(new Object[]{
                            new OreItemStacks(saltOre, 1),
                            ItemMisc.EnumItemMisc.SULFUR.create(1),
                            new ItemStack(BlockRegistry.PEAT, 1, 0)
                    }).register();

            CheeseVatRecipeBuilder.buildRecipe("MONTEREY Recipe").outputFluids(new FluidStack[]{GrowthcraftMilkItems.WaxedCheeseTypes.MONTEREY.getFluids().asFluidStack(5000)}).inputFluids(new Object[]{new TaggedFluidStacks(5000, new String[]{"milk_curds"})})
                    .inputItems(new Object[]{
                            new OreItemStacks(saltOre, 1),
                            new ItemStack(BlockRegistry.MARSH_MALLOW, 1, 0),
                            ItemCrushed.EnumItemCrushed.GROUND_DRIED_SWAMP_REED.create(1)
                    }).register();

            CheeseVatRecipeBuilder.buildRecipe("PROVOLONE Recipe").outputFluids(new FluidStack[]{GrowthcraftMilkItems.WaxedCheeseTypes.PROVOLONE.getFluids().asFluidStack(5000)}).inputFluids(new Object[]{new TaggedFluidStacks(5000, new String[]{"milk_curds"})})
                    .inputItems(new Object[]{
                            new OreItemStacks(saltOre, 1),
                            new ItemStack(ItemRegistry.TANGLED_ROOT, 1, 0),
                            ItemMisc.EnumItemMisc.DRY_BARK.create(1)
                    }).register();

            CheeseVatRecipeBuilder.buildRecipe("GOUDA Recipe").outputFluids(new FluidStack[]{GrowthcraftMilkItems.WaxedCheeseTypes.GOUDA.getFluids().asFluidStack(5000)}).inputFluids(new Object[]{new TaggedFluidStacks(5000, new String[]{"milk_curds"})})
                    .inputItems(new Object[]{
                            new OreItemStacks(saltOre, 1),
                            new ItemStack(BlockRegistry.ALGAE, 1, 0),
                            new ItemStack(BlockRegistry.SWAMP_REED, 1, 0)
                    }).register();
        }
    }
}
