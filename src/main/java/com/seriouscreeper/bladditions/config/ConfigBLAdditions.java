package com.seriouscreeper.bladditions.config;

import com.seriouscreeper.bladditions.BLAdditions;
import epicsquid.mysticallib.util.ConfigUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Mod.EventBusSubscriber(modid = BLAdditions.MODID)
@Config(modid = BLAdditions.MODID, name = BLAdditions.NAME)
public class ConfigBLAdditions {
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


    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(BLAdditions.MODID)) {
            blEventBonuses.clear();
            ConfigManager.sync(BLAdditions.MODID, Config.Type.INSTANCE);
        }
    }
}
