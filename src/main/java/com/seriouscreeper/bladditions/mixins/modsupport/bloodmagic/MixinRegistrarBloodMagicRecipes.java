package com.seriouscreeper.bladditions.mixins.modsupport.bloodmagic;

import WayofTime.bloodmagic.api.impl.BloodMagicRecipeRegistrar;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.core.RegistrarBloodMagicRecipes;
import WayofTime.bloodmagic.item.alchemy.ItemCuttingFluid;
import WayofTime.bloodmagic.item.alchemy.ItemLivingArmourPointsUpgrade;
import WayofTime.bloodmagic.item.types.ComponentTypes;
import com.google.common.collect.Sets;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.item.misc.ItemMisc;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.ItemRegistry;

import java.util.List;
import java.util.Set;

@Mixin(value = RegistrarBloodMagicRecipes.class, remap = false)
public class MixinRegistrarBloodMagicRecipes {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public static void registerAlchemyTableRecipes(BloodMagicRecipeRegistrar registrar) {
        registrar.addAlchemyTable(ItemLivingArmourPointsUpgrade.UpgradeType.DRAFT_ANGELUS.getStack(), 20000, 400, 3, new Object[]{ComponentTypes.NEURO_TOXIN.getStack(), ComponentTypes.ANTISEPTIC.getStack(), "dustGold", Items.FERMENTED_SPIDER_EYE, new ItemStack(RegistrarBloodMagicItems.BLOOD_SHARD, 1, 0), Items.GHAST_TEAR});
        registrar.addAlchemyTable(new ItemStack(BlockRegistry.SWAMP_GRASS), 200, 200, 1, new Object[]{BlockRegistry.SWAMP_GRASS, ItemMisc.EnumItemMisc.COMPOST.create(1), ItemRegistry.MIDDLE_FRUIT_BUSH_SEEDS});

        // Original recipes
        /*
        registrar.addAlchemyTable(new ItemStack(Items.STRING, 4), 0, 100, 0, new Object[]{Blocks.WOOL, Items.FLINT});
        registrar.addAlchemyTable(new ItemStack(Items.FLINT, 2), 0, 20, 0, new Object[]{Blocks.GRAVEL, Items.FLINT});
        registrar.addAlchemyTable(new ItemStack(Items.LEATHER, 4), 100, 200, 1, new Object[]{Items.ROTTEN_FLESH, Items.ROTTEN_FLESH, Items.ROTTEN_FLESH, Items.ROTTEN_FLESH, Items.FLINT, Items.WATER_BUCKET});
        registrar.addAlchemyTable(ItemCuttingFluid.FluidType.EXPLOSIVE.getStack(), 500, 200, 1, new Object[]{"gunpowder", "gunpowder", "dustCoal"});
        registrar.addAlchemyTable(new ItemStack(Items.BREAD), 100, 200, 1, new Object[]{Items.WHEAT, Items.SUGAR});
        registrar.addAlchemyTable(new ItemStack(Blocks.GRASS), 200, 200, 1, new Object[]{Blocks.DIRT, new ItemStack(Items.DYE, 1, 15), Items.WHEAT_SEEDS});
        registrar.addAlchemyTable(new ItemStack(Items.CLAY_BALL, 4), 50, 100, 2, new Object[]{Items.WATER_BUCKET, "sand"});
        registrar.addAlchemyTable(new ItemStack(Blocks.CLAY, 5), 200, 200, 1, new Object[]{Items.WATER_BUCKET, Blocks.HARDENED_CLAY, Blocks.HARDENED_CLAY, Blocks.HARDENED_CLAY, Blocks.HARDENED_CLAY, Blocks.HARDENED_CLAY});
        registrar.addAlchemyTable(new ItemStack(Blocks.OBSIDIAN), 50, 50, 1, new Object[]{Items.WATER_BUCKET, Items.LAVA_BUCKET});
        registrar.addAlchemyTable(ComponentTypes.SULFUR.getStack(8), 0, 100, 0, new Object[]{Items.LAVA_BUCKET});
        registrar.addAlchemyTable(ComponentTypes.SALTPETER.getStack(4), 0, 100, 0, new Object[]{ComponentTypes.PLANT_OIL.getStack(), ComponentTypes.PLANT_OIL.getStack(), "dustCoal"});
        registrar.addAlchemyTable(new ItemStack(Items.GUNPOWDER, 3), 0, 100, 0, new Object[]{"dustSaltpeter", "dustSulfur", new ItemStack(Items.COAL, 1, 1)});
        registrar.addAlchemyTable(ComponentTypes.SAND_COAL.getStack(4), 100, 100, 1, new Object[]{new ItemStack(Items.COAL, 1, 0), new ItemStack(Items.COAL, 1, 0), Items.FLINT});
        registrar.addAlchemyTable(ItemCuttingFluid.FluidType.BASIC.getStack(), 1000, 400, 1, new Object[]{"dustCoal", "gunpowder", Items.REDSTONE, Items.SUGAR, ComponentTypes.PLANT_OIL.getStack(), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER)});
        registrar.addAlchemyTable(ComponentTypes.SAND_IRON.getStack(2), 400, 200, 1, new Object[]{"oreIron", ItemCuttingFluid.FluidType.BASIC.getStack()});
        registrar.addAlchemyTable(ComponentTypes.SAND_GOLD.getStack(2), 400, 200, 1, new Object[]{"oreGold", ItemCuttingFluid.FluidType.BASIC.getStack()});
        registrar.addAlchemyTable(new ItemStack(Items.REDSTONE, 8), 400, 200, 1, new Object[]{"oreRedstone", ItemCuttingFluid.FluidType.BASIC.getStack()});
        registrar.addAlchemyTable(new ItemStack(Blocks.GRAVEL), 50, 50, 1, new Object[]{"cobblestone", ItemCuttingFluid.FluidType.EXPLOSIVE.getStack()});
        registrar.addAlchemyTable(new ItemStack(Blocks.SAND), 50, 50, 1, new Object[]{Blocks.GRAVEL, ItemCuttingFluid.FluidType.EXPLOSIVE.getStack()});
        registrar.addAlchemyTable(ComponentTypes.PLANT_OIL.getStack(), 100, 100, 1, new Object[]{"cropCarrot", "cropCarrot", "cropCarrot", new ItemStack(Items.DYE, 1, 15)});
        registrar.addAlchemyTable(ComponentTypes.PLANT_OIL.getStack(), 100, 100, 1, new Object[]{"cropPotato", "cropPotato", new ItemStack(Items.DYE, 1, 15)});
        registrar.addAlchemyTable(ComponentTypes.PLANT_OIL.getStack(), 100, 100, 1, new Object[]{"cropWheat", "cropWheat", new ItemStack(Items.DYE, 1, 15)});
        registrar.addAlchemyTable(ComponentTypes.PLANT_OIL.getStack(), 100, 100, 1, new Object[]{Items.BEETROOT, Items.BEETROOT, Items.BEETROOT, new ItemStack(Items.DYE, 1, 15)});
        registrar.addAlchemyTable(ComponentTypes.NEURO_TOXIN.getStack(), 1000, 100, 2, new Object[]{new ItemStack(Items.FISH, 1, 3)});
        registrar.addAlchemyTable(ComponentTypes.ANTISEPTIC.getStack(2), 1000, 200, 2, new Object[]{ComponentTypes.PLANT_OIL.getStack(), "nuggetGold", "cropWheat", Items.SUGAR, Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM});
        registrar.addAlchemyTable(new ItemStack(RegistrarBloodMagicItems.POTION_FLASK), 1000, 200, 2, new Object[]{PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), "cropNetherWart", "dustRedstone", "dustGlowstone"});
        registrar.addAlchemyTable(ComponentTypes.CATALYST_LENGTH_1.getStack(), 1000, 100, 2, new Object[]{"gunpowder", "cropNetherWart", "gemLapis"});
        registrar.addAlchemyTable(ComponentTypes.CATALYST_POWER_1.getStack(), 1000, 100, 2, new Object[]{"gunpowder", "cropNetherWart", "dustRedstone"});
        Set<String> addedOreRecipeList = Sets.newHashSet(new String[]{"oreIron", "oreGold", "oreCoal", "oreRedstone"});
        String[] oreList = (String[]) OreDictionary.getOreNames().clone();
        String[] var3 = oreList;
        int var4 = oreList.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String ore = var3[var5];
            if (ore.startsWith("ore") && !addedOreRecipeList.contains(ore)) {
                String dustName = ore.replaceFirst("ore", "dust");
                List<ItemStack> discoveredOres = OreDictionary.getOres(ore);
                List<ItemStack> dustList = OreDictionary.getOres(dustName);
                if (dustList != null && !dustList.isEmpty() && discoveredOres != null && !discoveredOres.isEmpty()) {
                    ItemStack dustStack = ((ItemStack)dustList.get(0)).copy();
                    dustStack.setCount(2);
                    registrar.addAlchemyTable(dustStack, 400, 200, 1, new Object[]{ore, ItemCuttingFluid.FluidType.BASIC.getStack()});
                    addedOreRecipeList.add(ore);
                }
            }
        }
        */
    }
}
