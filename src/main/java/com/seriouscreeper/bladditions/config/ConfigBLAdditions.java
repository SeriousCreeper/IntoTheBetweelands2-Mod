package com.seriouscreeper.bladditions.config;

import com.seriouscreeper.bladditions.BLAdditions;
import com.seriouscreeper.bladditions.proxy.CommonProxy;
import epicsquid.mysticallib.util.ConfigUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.GameData;
import scala.Int;
import thebetweenlands.common.registries.ItemRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Mod.EventBusSubscriber(modid = BLAdditions.MODID)
@Config(modid = BLAdditions.MODID, name = BLAdditions.NAME)
public class ConfigBLAdditions {
    public static final ConfigGeneral configGeneral = new ConfigGeneral();
    public static final ConfigTea configTea = new ConfigTea();
    public static final ConfigAutoMapping configAutoMapping = new ConfigAutoMapping();

    public static final ConfigSanity configSanity = new ConfigSanity();

    @Config.Ignore
    private static Map<String, Float> blEventBonuses = null;

    public static class ConfigTea {
        @Config.Comment({"Chance that tea gives research point"})
        public int TCPotionChance = 100;
        @Config.Comment({"Does the player need to have the thaumonomicon and scribing tools in their inventory?"})
        public boolean RequiresBookAndQuill = true;
        public float GreeblingBonus = 0.5f;
        public float NearbyBlocksBonus = 0.9f;
        public float FocusedBuffBonus = 0.8f;
        public float SittingBonus = 0.9f;
        public String[] eventBonuses = new String[] { "heavy_rain,1.1", "auroras,0.9" };
    }

    public static class ConfigGeneral {
        public int SulfurExplosionDamage = 4;
        public int SulfurExplosionChance = 5;
        public int SmokingRackRecipeModifier = 5;
        public String[] FluxItems = new String[] { "thebetweenlands:syrmorite_ore:0,thebetweenlands:items_misc:11" };
        public int BarrelFillWithWaterChance = 300;
        public int BarrelFillWithWaterAmount = 10;
        public int WaterFilterSpeed = 50;
        public int WaterFilterAmount = 1;
        public int WaterFilterLootChance = 50;
        public int MothHouseProductionSpeed = 500;
        public String RedstoneOreBlock = "minecraft:redstone_ore";
    }


    public static class ConfigAutoMapping {
        public boolean AutoMapCragrockTower = true;
        public boolean AutoMapGiantWeedwoodTree = true;
        public boolean AutoMapIdolHeads = true;
        public boolean AutoMapSludgeon = true;
        public boolean AutoMapSpiritTree = true;
        public boolean AutoMapWightFortress = true;
        public boolean AutoMapMenhir = true;
    }

    public static class ConfigSanity {
        public double[] sanity_chance_sounds = new double[] { 0, 0, 0, 0, 0 };
        public int sanity_cooldown_sounds = 10;
        public String[] sanity_sounds_list = new String[] { "thebetweenlands:wight_hurt", "thebetweenlands:wight_moan" };
    }


    public static Map<String, Float> parseBLEvents() {
        if (blEventBonuses == null) {
            blEventBonuses = new HashMap<>();

            for(String entry : configTea.eventBonuses) {
                String[] split = entry.split(",");

                if(split.length != 2) {
                    continue;
                }

                blEventBonuses.put(split[0], Float.parseFloat(split[1]));
            }

            return blEventBonuses;
        }

        return blEventBonuses;
    }


    public static void parseFluxItems() {
        CommonProxy.FLUXABLE_ITEMS.clear();

        for(String entry : configGeneral.FluxItems) {
            String[] items = entry.split(",");

            if(items.length != 2) {
                continue;
            }

            String[] itemInfo = items[0].split(":");
            ItemStack input = new ItemStack(Item.getByNameOrId(itemInfo[0] + ":" + itemInfo[1]), 1, Integer.parseInt(itemInfo[itemInfo.length - 1]));

            itemInfo = items[1].split(":");
            ItemStack output = new ItemStack(Item.getByNameOrId(itemInfo[0] + ":" + itemInfo[1]), 1, Integer.parseInt(itemInfo[itemInfo.length - 1]));

            CommonProxy.FLUXABLE_ITEMS.put(input, output);
        }
    }


    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(BLAdditions.MODID)) {
            if(blEventBonuses != null)
                blEventBonuses.clear();

            parseFluxItems();

            ConfigManager.sync(BLAdditions.MODID, Config.Type.INSTANCE);
        }
    }
}
