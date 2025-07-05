package com.seriouscreeper.bladditions.config;

import com.seriouscreeper.bladditions.BLAdditions;
import com.seriouscreeper.bladditions.proxy.CommonProxy;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = BLAdditions.MODID)
@Config(modid = BLAdditions.MODID, name = BLAdditions.NAME)
public class ConfigBLAdditions {
    public static final ConfigGeneral configGeneral = new ConfigGeneral();
    public static final ConfigTea configTea = new ConfigTea();
    public static final ConfigAutoMapping configAutoMapping = new ConfigAutoMapping();
    public static final ConfigSanity configSanity = new ConfigSanity();
    public static final ConfigBotania configBotania = new ConfigBotania();
    public static final ConfigBloodMagic configBloodMagic = new ConfigBloodMagic();
    public static final ConfigThaumcraft configThaumcraft = new ConfigThaumcraft();

    @Config.Ignore
    private static Map<String, Float> blEventBonuses = null;

    @Config.Ignore
    public static int[][] draetonDimensions = null;

    public static class ConfigBotania {
        public int ManaKekimurus = 5000;
        public int ManaNarslimmus = 1200;
        public int ManaSpectrolus = 4800;
        public float DamageChakram = 6;
    }


    public static class ConfigThaumcraft {
        public String[] unnaturalHungerCures = new String[] { "minecraft:rotten_flesh", "thaumcraft:brain" };
        public String stageToUnlockThaumcraft = "knowledge_of_spirit";
        public int taintSlimeSpawnChance = 300;
    }


    public static class ConfigBloodMagic {
        public class ConfigSentientTool {
            public double defaultDamage = 5;
            public double[] defaultDamageAdded = new double[]{1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0};
            public double[] destructiveDamageAdded = new double[]{1.5, 2.25, 3.0, 3.75, 4.5, 5.25, 6.0};
            public double[] vengefulDamageAdded = new double[]{0.0, 0.5, 1.0, 1.5, 2.0, 2.25, 2.5};
            public double[] steadfastDamageAdded = new double[]{0.0, 0.5, 1.0, 1.5, 2.0, 2.25, 2.5};
        }

        public class ConfigSentientArmor {
            public double[] extraProtectionLevel = new double[]{0.0, 0.25, 0.5, 0.6, 0.7, 0.75, 0.85, 0.9};
            public double[] steadfastProtectionLevel = new double[]{0.25, 0.5, 0.6, 0.7, 0.75, 0.85, 0.9, 0.95};
            public double[] knockbackBonus = new double[]{0.2, 0.4, 0.6, 0.8, 1.0, 1.0, 1.0, 1.0};
            public double[] damageBoost = new double[]{0.03, 0.06, 0.09, 0.12, 0.15, 0.18, 0.22, 0.25};
            public double[] attackSpeed = new double[]{-0.02, -0.04, -0.06, -0.08, -0.1, -0.12, -0.14, -0.16};
            public double[] speedBonus = new double[]{0.05, 0.1, 0.15, 0.2, 0.25, 0.3, 0.35, 0.4};
        }

        @Config.Name("Sentient Armor")
        public ConfigSentientArmor configSentientArmor = new ConfigSentientArmor();
        @Config.Name("Sentient Shovel")
        public ConfigSentientTool configShovel = new ConfigSentientTool();
        @Config.Name("Sentient Sword")
        public ConfigSentientTool configSword = new ConfigSentientTool();
        @Config.Name("Sentient Axe")
        public ConfigSentientTool configAxe = new ConfigSentientTool();
        @Config.Name("Sentient Pickaxe")
        public ConfigSentientTool configPickaxe = new ConfigSentientTool();
        {
            configPickaxe.defaultDamage = 3;
            configPickaxe.defaultDamageAdded = new double[]{1.0, 2.0, 3.0, 3.5, 4.0};
            configPickaxe.destructiveDamageAdded = new double[]{2.0, 3.0, 4.0, 5.0, 6.0};
            configPickaxe.vengefulDamageAdded = new double[]{0.0, 0.5, 1.0, 1.5, 2.0};
            configPickaxe.steadfastDamageAdded = new double[]{0.0, 0.5, 1.0, 1.5, 2.0};

            configShovel.defaultDamage = 3;
            configShovel.defaultDamageAdded = new double[]{1.0, 2.0, 3.0, 3.5, 4.0};
            configShovel.destructiveDamageAdded = new double[]{2.0, 3.0, 4.0, 5.0, 6.0};
            configShovel.vengefulDamageAdded = new double[]{0.0, 0.5, 1.0, 1.5, 2.0};
            configShovel.steadfastDamageAdded = new double[]{0.0, 0.5, 1.0, 1.5, 2.0};

            configAxe.defaultDamage = 8;
            configAxe.defaultDamageAdded = new double[]{1.0, 2.0, 3.0, 3.5, 4.0};
            configAxe.destructiveDamageAdded = new double[]{2.0, 3.0, 4.0, 5.0, 6.0};
            configAxe.vengefulDamageAdded = new double[]{0.0, 0.5, 1.0, 1.5, 2.0};
            configAxe.steadfastDamageAdded = new double[]{0.0, 0.5, 1.0, 1.5, 2.0};
        }
    }

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
        @Config.RangeInt(min = 0, max = 9)
        public int MaxPacifistForgiveness = 5;
        public int DungeonDimensionID = -500;
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
        public boolean DebugRecruitmentNames = false;
        public int CrimsonCultSpawnRate = 250;
        public String[] RecruitmentBlacklist = new String[] {
            "thaumcraft.common.entities.monster.cult.EntityCultistKnight",
            "thaumcraft.common.entities.monster.cult.EntityCultistPortalLesser",
            "thaumcraft.common.entities.monster.boss.EntityCultistPortalGreater",
            "thaumcraft.common.entities.monster.cult.EntityCultistCleric",
            "thaumcraft.common.entities.monster.boss.EntityCultistLeader",
            "thaumcraft.common.entities.monster.boss.EntityEldritchWarden",
            "thaumcraft.common.entities.monster.boss.EntityEldritchGolem",
            "thaumcraft.common.entities.monster.boss.EntityTaintacleGiant",
            "thaumcraft.common.entities.monster.EntityMindSpider",
            "thaumcraft.common.entities.monster.EntityEldritchGuardian",
            "thaumcraft.common.entities.monster.EntityEldritchCrab",
            "thaumcraft.common.entities.monster.EntityInhabitedZombie",
            "thaumcraft.common.entities.monster.EntityThaumicSlime",
            "thaumcraft.common.entities.monster.tainted.EntityTaintCrawler",
            "thaumcraft.common.entities.monster.tainted.EntityTaintacle",
            "thaumcraft.common.entities.monster.tainted.EntityTaintSwarm",
            "thaumcraft.common.entities.monster.tainted.EntityTaintSeed",
            "thaumcraft.common.entities.monster.tainted.EntityTaintSeedPrime"
        };

        public String[] BarkAmuletBlocklist = new String[] {
            "gigaherz.eyes.entity.EntityEyes"
        };

        public String[] FoodSicknessWhitelist = new String[] {
                "roots:cooked_pereskia"
        };

        @Config.Comment({"fromDimension, toDimension, minHeight, maxHeight, targetX, targetY, targetZ"})
        public String[] DraetonDimension = new String[] {
                "20,0,300,400,0,128,0",
                "0,20,200,250,0,128,0"
        };
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
        public String[] sanity_lightseeker_whitelist = new String[] { "" };
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


    public static int[][] getParsedDraetonDimension() {
        String[] configArray = ConfigBLAdditions.configGeneral.DraetonDimension;
        int[][] result = new int[configArray.length][];

        for (int i = 0; i < configArray.length; i++) {
            String line = configArray[i];
            String[] tokens = line.split(",");
            int[] row = new int[tokens.length];

            for (int j = 0; j < tokens.length; j++) {
                row[j] = Integer.parseInt(tokens[j].trim());
            }

            result[i] = row;
        }

        return result;
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
            draetonDimensions = getParsedDraetonDimension();

            ConfigManager.sync(BLAdditions.MODID, Config.Type.INSTANCE);
        }
    }
}
