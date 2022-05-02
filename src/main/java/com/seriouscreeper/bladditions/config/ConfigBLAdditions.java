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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Mod.EventBusSubscriber(modid = BLAdditions.MODID)
@Config(modid = BLAdditions.MODID, name = BLAdditions.NAME)
public class ConfigBLAdditions {
    public static final ConfigGeneral configGeneral = new ConfigGeneral();
    public static final ConfigTea configTea = new ConfigTea();

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
        public String RedstoneOreBlock = "minecraft:redstone_ore";
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
            System.out.println(itemInfo[0]);
            ItemStack input = new ItemStack(Item.getByNameOrId(itemInfo[0] + ":" + itemInfo[1]), 1, Integer.parseInt(itemInfo[itemInfo.length - 1]));

            itemInfo = items[1].split(":");
            System.out.println(itemInfo[0]);
            ItemStack output = new ItemStack(Item.getByNameOrId(itemInfo[0] + ":" + itemInfo[1]), 1, Integer.parseInt(itemInfo[itemInfo.length - 1]));

            CommonProxy.FLUXABLE_ITEMS.put(input, output);
        }

        System.out.println(CommonProxy.FLUXABLE_ITEMS.size());
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
