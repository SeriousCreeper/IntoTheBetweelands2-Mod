package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.config.ModConfig;
import thebetweenlands.common.item.misc.ItemMisc;
import thebetweenlands.common.registries.ItemRegistry;

import java.util.Iterator;
import java.util.Random;

@Mixin(value = ModConfig.class, remap = false)
public class MixinModConfig {
    /**
     * @author SC
     */
    @Overwrite
    public static void postInitLoot() {
        new Random(System.currentTimeMillis());
        ThaumcraftApi.addLootBagItem(ItemMisc.EnumItemMisc.OCTINE_NUGGET.create(1), 2500, new int[]{0});
        ThaumcraftApi.addLootBagItem(ItemMisc.EnumItemMisc.OCTINE_NUGGET.create(2), 2250, new int[]{1});
        ThaumcraftApi.addLootBagItem(ItemMisc.EnumItemMisc.OCTINE_NUGGET.create(3), 2000, new int[]{2});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.salisMundus), 3, new int[]{0});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.salisMundus), 6, new int[]{1});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.salisMundus), 9, new int[]{2});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemRegistry.MARSHMALLOW), 5, new int[]{0, 1, 2});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemRegistry.MARSHMALLOW_PINK), 5, new int[]{0, 1, 2});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemRegistry.EMPTY_AMATE_MAP), 5, new int[]{0, 1, 2});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemRegistry.NIBBLESTICK), 5, new int[]{0, 1, 2});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.primordialPearl, 1, 7), 1, new int[]{0});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.primordialPearl, 1, 7), 3, new int[]{1});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.primordialPearl, 1, 6), 1, new int[]{1});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.primordialPearl, 1, 5), 9, new int[]{2});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.primordialPearl, 1, 3), 3, new int[]{2});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.primordialPearl, 1), 1, new int[]{2});
        ThaumcraftApi.addLootBagItem(ItemMisc.EnumItemMisc.LOOT_SCRAPS.create(1), 1, new int[]{2});
        ThaumcraftApi.addLootBagItem(ItemMisc.EnumItemMisc.VALONITE_SHARD.create(1), 10, new int[]{0});
        ThaumcraftApi.addLootBagItem(ItemMisc.EnumItemMisc.VALONITE_SHARD.create(1), 50, new int[]{1, 2});
        ThaumcraftApi.addLootBagItem(ItemMisc.EnumItemMisc.SCABYST.create(1), 15, new int[]{0});
        ThaumcraftApi.addLootBagItem(ItemMisc.EnumItemMisc.SCABYST.create(1), 75, new int[]{1, 2});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemRegistry.OCTINE_INGOT), 100, new int[]{0, 1, 2});
        ThaumcraftApi.addLootBagItem(ItemMisc.EnumItemMisc.ANCIENT_REMNANT.create(1), 100, new int[]{0, 1, 2});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.amuletVis, 1, 0), 6, new int[]{1, 2});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.baubles, 1, 0), 10, new int[]{0});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.baubles, 1, 1), 10, new int[]{0});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.baubles, 1, 2), 10, new int[]{0});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.baubles, 1, 3), 5, new int[]{2});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.baubles, 1, 4), 5, new int[]{1});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.baubles, 1, 5), 5, new int[]{1});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.baubles, 1, 6), 5, new int[]{1});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemRegistry.ROCK_SNOT_PEARL), 5, new int[]{0});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemRegistry.ROCK_SNOT_PEARL), 10, new int[]{1});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemRegistry.ROCK_SNOT_PEARL), 20, new int[]{2});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemRegistry.SPIRIT_FRUIT, 1, 1), 1, new int[]{0});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemRegistry.SPIRIT_FRUIT, 1, 1), 2, new int[]{1});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemRegistry.SPIRIT_FRUIT, 1, 1), 3, new int[]{2});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemRegistry.SPIRIT_FRUIT, 1, 0), 3, new int[]{0});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemRegistry.SPIRIT_FRUIT, 1, 0), 6, new int[]{1});
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemRegistry.SPIRIT_FRUIT, 1, 0), 9, new int[]{2});
        /*
        ThaumcraftApi.addLootBagItem(new ItemStack(ItemRegistry.WIGHT_HEART), 10, new int[]{0, 1, 2});
        Iterator var4 = PotionType.REGISTRY.iterator();

        while(var4.hasNext()) {
            PotionType pt = (PotionType)var4.next();
            ThaumcraftApi.addLootBagItem(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), pt), 2, new int[]{0, 1, 2});
            ThaumcraftApi.addLootBagItem(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), pt), 2, new int[]{0, 1, 2});
            ThaumcraftApi.addLootBagItem(PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), pt), 2, new int[]{1, 2});
        }
        */

        ItemStack[] var10000 = new ItemStack[]{new ItemStack(ItemsTC.lootBag, 1, 0), new ItemStack(ItemsTC.ingots), new ItemStack(ItemsTC.amber)};
        var10000 = new ItemStack[]{new ItemStack(ItemsTC.lootBag, 1, 1), new ItemStack(ItemsTC.baubles, 1, 0), new ItemStack(ItemsTC.baubles, 1, 1), new ItemStack(ItemsTC.baubles, 1, 2)};
        var10000 = new ItemStack[]{new ItemStack(ItemsTC.lootBag, 1, 2), new ItemStack(ItemsTC.thaumonomicon), new ItemStack(ItemsTC.thaumiumSword), new ItemStack(ItemsTC.thaumiumAxe), new ItemStack(ItemsTC.thaumiumPick), new ItemStack(ItemsTC.baubles, 1, 3), new ItemStack(ItemsTC.baubles, 1, 4), new ItemStack(ItemsTC.baubles, 1, 5), new ItemStack(ItemsTC.baubles, 1, 6), new ItemStack(ItemsTC.amuletVis, 1, 0)};
    }
}
