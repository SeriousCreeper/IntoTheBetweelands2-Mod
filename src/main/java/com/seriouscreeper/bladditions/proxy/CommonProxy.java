package com.seriouscreeper.bladditions.proxy;

import blusunrize.immersiveengineering.api.tool.ExternalHeaterHandler;
import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.incense.EnumTranquilityType;
import WayofTime.bloodmagic.incense.TranquilityStack;
import com.aranaira.arcanearchives.data.ClientNetwork;
import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.data.HiveSaveData;
import com.aranaira.arcanearchives.init.RecipeLibrary;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipeList;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mrbysco.anotherliquidmilkmod.init.MilkRegistry;
import com.rcx.mystgears.MysticalGears;
import com.rcx.mystgears.block.BlockTurret;
import com.rcx.mystgears.item.ItemGear;
import com.seriouscreeper.bladditions.BLAdditions;
import com.seriouscreeper.bladditions.blocks.*;
import com.seriouscreeper.bladditions.compat.embers.modifier.ModifierRubberBoots;
import com.seriouscreeper.bladditions.compat.ie.ExternalHeaterClasses;
import com.seriouscreeper.bladditions.capability.PacifistCapability;
import com.seriouscreeper.bladditions.capability.WellnessCapability;
import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import com.seriouscreeper.bladditions.crafting.PatchedRecipeMagicDust;
import com.seriouscreeper.bladditions.entities.GreeblingMerchantEntity;
import com.seriouscreeper.bladditions.items.*;
import com.seriouscreeper.bladditions.items.tools.*;
import com.seriouscreeper.bladditions.items.tools.roots.*;
import com.seriouscreeper.bladditions.libs.BLAdditionsUtils;
import com.seriouscreeper.bladditions.potion.PotionRegistery;
import com.seriouscreeper.bladditions.potion.PotionThaumcraftResearch;
import com.seriouscreeper.bladditions.recipes.CustomItemLiverStampingRecipe;
import com.seriouscreeper.bladditions.recipes.CustomItemRenameStampingRecipe;
import com.seriouscreeper.bladditions.sanity_consequences.ConsequenceSounds;
import com.seriouscreeper.bladditions.tiles.PatchedTilePotionSprayer;
import com.seriouscreeper.bladditions.tiles.PatchedTileSpa;
import com.seriouscreeper.bladditions.tiles.TileCrucibleSwamp;
import com.seriouscreeper.bladditions.tiles.TileWaterJugSwamp;
import com.seriouscreeper.bladditions.world.BLBeeHiveWorldGen;
import com.shinoow.darknesslib.api.DarknessLibAPI;
import com.tiviacz.pizzacraft.crafting.bakeware.BaseShapelessOreRecipe;
import com.tiviacz.pizzacraft.crafting.bakeware.IBakewareRecipe;
import com.tiviacz.pizzacraft.crafting.bakeware.PizzaCraftingManager;
import com.tiviacz.pizzacraft.init.ModBlocks;
import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.event.RegisterContentEvent;
import epicsquid.roots.Roots;
import epicsquid.roots.api.CreateToolEvent;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.integration.jei.soil.SoilRecipe;
import epicsquid.roots.item.materials.Materials;
import epicsquid.roots.mechanics.Growth;
import gigaherz.eyes.ConfigData;
import gigaherz.eyes.EyesInTheDarkness;
import gigaherz.eyes.entity.EntityEyes;
import growthcraft.bees.shared.init.GrowthcraftBeesItems;
import growthcraft.core.shared.CoreRegistry;
import growthcraft.core.shared.config.GrowthcraftCoreConfig;
import growthcraft.core.shared.legacy.FluidContainerRegistry;
import growthcraft.core.shared.utils.TickUtils;
import growthcraft.milk.common.Init;
import growthcraft.milk.shared.fluids.MilkFluidTags;
import growthcraft.milk.shared.init.GrowthcraftMilkFluids;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.tiffit.sanity.consequences.ConsequenceManager;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import soot.Registry;
import soot.recipe.ItemRenameStampingRecipe;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.itemmod.ModifierBase;
import teamroots.embers.apiimpl.EmbersAPIImpl;
import teamroots.embers.block.BlockSeedNew;
import teamroots.embers.config.ConfigSeed;
import teamroots.embers.entity.EntityAncientGolem;
import teamroots.embers.recipe.FluidReactionRecipe;
import teamroots.embers.recipe.ItemStampingRecipe;
import teamroots.embers.recipe.RecipeRegistry;
import teamroots.embers.register.BlockRegister;
import teamroots.embers.register.FluidRegister;
import teamroots.embers.register.ItemRegister;
import teamroots.embers.register.Util;
import teamroots.embers.tileentity.TileEntitySeedNew;
import teamroots.embers.util.EmberGenUtil;
import thaumcraft.Thaumcraft;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.*;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.api.research.*;
import thaumcraft.common.blocks.essentia.BlockJarItem;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.monster.cult.EntityCultistPortalLesser;
import thaumcraft.common.golems.seals.SealHandler;
import thaumcraft.common.lib.crafting.DustTriggerMultiblock;
import thaumcraft.common.lib.crafting.InfusionEnchantmentRecipe;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;
import thaumcraft.common.lib.utils.CropUtils;
import thaumcraft.common.tiles.essentia.TileJarFillable;
import thaumicperiphery.ModContent;
import thebetweenlands.api.item.CorrosionHelper;
import thebetweenlands.api.recipes.IDruidAltarRecipe;
import thebetweenlands.api.recipes.ISmokingRackRecipe;
import thebetweenlands.common.block.farming.BlockGenericDugSoil;
import thebetweenlands.common.entity.draeton.EntityDraeton;
import thebetweenlands.common.entity.mobs.*;
import thebetweenlands.common.item.herblore.ItemCrushed;
import thebetweenlands.common.item.misc.ItemMisc;
import thebetweenlands.common.recipe.misc.DruidAltarRecipe;
import thebetweenlands.common.recipe.misc.SmokingRackRecipe;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.FluidRegistry;
import thebetweenlands.common.registries.ItemRegistry;
import thebetweenlands.common.registries.SoundRegistry;
import thebetweenlands.common.tile.TileEntityAbstractBLFurnace;
import thebetweenlands.common.tile.spawner.MobSpawnerLogicBetweenlands;
import thebetweenlands.common.tile.spawner.TileEntityMobSpawnerBetweenlands;
import thebetweenlands.common.world.storage.location.LocationStorage;
import thecodex6824.thaumicaugmentation.api.TAItems;
import thecodex6824.thaumicaugmentation.api.ThaumicAugmentationAPI;
import thecodex6824.thaumicaugmentation.api.item.CapabilityMorphicTool;
import thecodex6824.thaumicaugmentation.api.item.IMorphicItem;
import thecodex6824.thaumicaugmentation.common.item.ItemThaumiumRobes;
import thecodex6824.thaumicaugmentation.common.recipe.InfusionRecipeComplexResearch;
import thecodex6824.thaumicaugmentation.common.util.MorphicArmorHelper;
import vazkii.botania.api.mana.IManaPool;
import vazkii.quark.base.module.Feature;
import vazkii.quark.tweaks.base.BlockStack;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Mod.EventBusSubscriber
public class CommonProxy {
    public static List<ItemStack> ADDITIONAL_ORES = new ArrayList<>();
    public static List<ItemStack> ADDITIONAL_INGOTS = new ArrayList<>();
    public static SoundEvent SORRY_CHARGED;
    public static SoundEvent BEE_SWARM;
    public static PatchedItemDentrothystVial DENTROTHYST_VIAL;
    public static PatchedItemDentrothystFluidVial DENTROTHYST_FLUID_VIAL;
    public static HashMap<Block, List<PotionThaumcraftResearch.RESEARCH_CATEGORY>> WELLNESS_BLOCKS = new HashMap<>();
    public static HashMap<ItemStack, ItemStack> FLUXABLE_ITEMS = new HashMap<>();

    public static List<Block> ALLOWED_FENCES = new ArrayList<>();
    public static final Map<BlockStack, BlockStack> CROPS = Maps.newHashMap();
    public static Fluid SWAMP_WATER;

    public static BlockSeedNew seed_syrmorite;
    public static BlockSeedNew seed_octine;

    public static ResourceLocation MARKER_CRAGROCK_TOWER = new ResourceLocation(BLAdditions.MODID, "tile_cragrock_tower");
    public static ResourceLocation MARKER_SPIRIT_TREE = new ResourceLocation(BLAdditions.MODID, "tile_spirit_tree");
    public static ResourceLocation MARKER_SLUDGEON = new ResourceLocation(BLAdditions.MODID, "tile_sludgeon");
    public static ResourceLocation MARKER_WIGHT_FORTRESS = new ResourceLocation(BLAdditions.MODID, "tile_wight_fortress");
    public static ResourceLocation MARKER_GIANT_TREE = new ResourceLocation(BLAdditions.MODID, "tile_giant_tree");
    public static ResourceLocation MARKER_MENHIR = new ResourceLocation(BLAdditions.MODID, "tile_menhir");
    public static ResourceLocation MARKER_IDOL_HEAD = new ResourceLocation(BLAdditions.MODID, "tile_idol_head");


    public void registerAntiqueAtlasTextures() {
    }


    public static boolean IsPacifistMob(EntityCreature creature) {
        return creature instanceof EntitySwarm || (creature instanceof EntityPyrad && creature.getEntityAttribute(EntityPyrad.AGRESSIVE).getAttributeValue() != 1);
    }


    public static boolean IsSoilDecayed(IBlockState blockState) {
        if (blockState.getBlock() instanceof BlockGenericDugSoil) {
            return (Boolean)blockState.getValue(BlockGenericDugSoil.DECAYED);
        }

        return false;
    }


    public static ItemStack CreateItemStackFromOreDictionary(String oreName) {
        if (OreDictionary.doesOreNameExist(oreName)) {
            List<ItemStack> oreItems = OreDictionary.getOres(oreName);
            if (!oreItems.isEmpty()) {
                ItemStack itemStack = oreItems.get(0).copy();
                itemStack.setCount(1);
                return itemStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static void DyeBotaniaManaPool(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, CallbackInfoReturnable<EnumActionResult> cir) {
        ItemStack stack = player.getHeldItem(hand);
        EnumDyeColor color = EnumDyeColor.byMetadata(15 - stack.getItemDamage());
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IManaPool) {
            IManaPool pool = (IManaPool)tile;

            if (color != pool.getColor()) {
                pool.setColor(color);
                stack.shrink(1);
                cir.setReturnValue(EnumActionResult.SUCCESS);
            }
        }
    }


    public static boolean IsPacifist(EntityPlayerMP player) {
        PacifistCapability cap = player.getCapability(PacifistCapability.INSTANCE, null);

        if(cap == null) {
            return false;
        }

        return cap.IsPacifist();
    }


    public static boolean IsInDungeonWorld(World world) {
        return world.provider.getDimension() == ConfigBLAdditions.configGeneral.DungeonDimensionID;
    }


    @SubscribeEvent
    public static void onCreateTool(CreateToolEvent event) {
        switch(event.getRegistryName()) {
            case "living_shovel":
                LibRegistry.setActiveMod("roots", Roots.CONTAINER);
                event.setItemResult(new PatchedItemLivingShovel(Materials.LIVING, event.getRegistryName()));
            break;

            case "living_sword":
                LibRegistry.setActiveMod("roots", Roots.CONTAINER);
                event.setItemResult(new PatchedItemLivingSword(Materials.LIVING, event.getRegistryName()));
                break;

            case "living_axe":
                LibRegistry.setActiveMod("roots", Roots.CONTAINER);
                event.setItemResult(new PatchedItemLivingAxe(Materials.LIVING, event.getRegistryName()));
                break;

            case "living_pickaxe":
                LibRegistry.setActiveMod("roots", Roots.CONTAINER);
                event.setItemResult(new PatchedItemLivingPickaxe(Materials.LIVING, event.getRegistryName()));
                break;


            case "terrastone_shovel":
                LibRegistry.setActiveMod("roots", Roots.CONTAINER);
                event.setItemResult(new PatchedItemTerrastoneShovel(Materials.TERRASTONE, event.getRegistryName()));
                break;

            case "terrastone_sword":
                LibRegistry.setActiveMod("roots", Roots.CONTAINER);
                event.setItemResult(new PatchedItemTerrastoneSword(Materials.TERRASTONE, event.getRegistryName()));
                break;

            case "terrastone_axe":
                LibRegistry.setActiveMod("roots", Roots.CONTAINER);
                event.setItemResult(new PatchedItemTerrastoneAxe(Materials.TERRASTONE, event.getRegistryName()));
                break;

            case "terrastone_pickaxe":
                LibRegistry.setActiveMod("roots", Roots.CONTAINER);
                event.setItemResult(new PatchedItemTerrastonePickaxe(Materials.TERRASTONE, event.getRegistryName()));
                break;


            case "runed_shovel":
                LibRegistry.setActiveMod("roots", Roots.CONTAINER);
                event.setItemResult(new PatchedItemRunedShovel(Materials.RUNIC, event.getRegistryName()));
                break;

            case "runed_sword":
                LibRegistry.setActiveMod("roots", Roots.CONTAINER);
                event.setItemResult(new PatchedItemRunedSword(Materials.RUNIC, event.getRegistryName()));
                break;

            case "runed_axe":
                LibRegistry.setActiveMod("roots", Roots.CONTAINER);
                event.setItemResult(new PatchedItemRunedAxe(Materials.RUNIC, event.getRegistryName()));
                break;

            case "runed_pickaxe":
                LibRegistry.setActiveMod("roots", Roots.CONTAINER);
                event.setItemResult(new PatchedItemRunedPickaxe(Materials.RUNIC, event.getRegistryName()));
                break;

            case "wildwood_bow":
                LibRegistry.setActiveMod("roots", Roots.CONTAINER);
                event.setItemResult(new PatchedItemWildwoodBow(event.getRegistryName()));
                break;
        }
    }


    private static SoundEvent registerSound(String name) {
        ResourceLocation location = new ResourceLocation(BLAdditions.MODID, name);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(name);

        return event;
    }


    public void preInit(FMLPreInitializationEvent e) {
        PotionRegistery.RegisterPotions();

        DarknessLibAPI.getInstance().addVehicle(EntityDraeton.class);

        SoilRecipe.recipes = Arrays.asList(SoilRecipe.EARTH, SoilRecipe.AIR, SoilRecipe.WATER);

        //RitualRegistry.ritualRegistry.remove("ritual_heavy_storms");
        //RitualRegistry.addRitual(RitualRegistry.ritual_heavy_storms = new PatchedRitualHeavyStorms("ritual_heavy_storms", RitualConfig.disableRitualCategory.disableHeavyStorms));

        // Betweenlands gears
        BlockTurret.metalTextures.put(new OreIngredient("ingotSyrmorite"), "syrmorite");
        BlockTurret.metalTextures.put(new OreIngredient("ingotOctine"), "octine");
        BlockTurret.metalTextures.put(new OreIngredient("gemValonite"), "valonite");

        MysticalGears.items.add(new ItemGear("Weedwood"));
        MysticalGears.items.add(new ItemGear("Cragrock"));
        MysticalGears.items.add(new ItemGear("Syrmorite"));
        MysticalGears.items.add(new ItemGear("Octine"));
        MysticalGears.items.add(new ItemGear("Valonite"));

        BlockRegister.INSTANCE.add(seed_syrmorite = createSimpleSeed(Material.ROCK, "seed_syrmorite", new ResourceLocation("embers", "textures/blocks/material_syrmorite.png"), (tile, i) -> {
            return new ItemStack(Item.getByNameOrId("pyrotech:generated_slag_syrmorite"));
        }));

        BlockRegister.INSTANCE.add(seed_octine = createSimpleSeed(Material.ROCK, "seed_octine", new ResourceLocation("embers", "textures/blocks/material_octine.png"), (tile, i) -> {
            return new ItemStack(Item.getByNameOrId("pyrotech:generated_slag_octine"));
        }));

        registerEntities();
    }


    private static BlockSeedNew createSimpleSeed(Material material, String name, final ResourceLocation texture, final BiFunction<TileEntitySeedNew, Integer, ItemStack> nuggetGenerator) {
        return new BlockSeedNew(material, name, true) {
            public ResourceLocation getTexture(TileEntitySeedNew tile) {
                return texture;
            }

            public ItemStack[] getNuggetDrops(TileEntitySeedNew tile, int n) {
                return (ItemStack[]) IntStream.range(0, n).mapToObj((i) -> {
                    return (ItemStack)nuggetGenerator.apply(tile, n);
                }).toArray((x$0) -> {
                    return new ItemStack[x$0];
                });
            }
        };
    }


    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> register) {
        register.getRegistry().register(SORRY_CHARGED = registerSound("sorry2"));
        register.getRegistry().register(BEE_SWARM = registerSound("insectswarm"));
    }


    private void AddDruidAltarRecipe(ItemStack[] inputs, String entityId) {
        DruidAltarRecipe.addRecipe(new IDruidAltarRecipe() {
            public boolean containsInputItem(ItemStack input) {
                if (input.isEmpty()) {
                    return false;
                }

                for (ItemStack stack : inputs) {
                    if (stack.getItem() == input.getItem()) {
                        return true;
                    }
                }

                return false;
            }

            public boolean matchesInput(ItemStack[] input) {
                List<ItemStack> remainingRecipeInputs = new ArrayList<>(Arrays.asList(inputs));

                for (ItemStack inStack : input) {
                    boolean matched = false;
                    Iterator<ItemStack> iter = remainingRecipeInputs.iterator();

                    while (iter.hasNext()) {
                        ItemStack recipeStack = iter.next();
                        if (ItemStack.areItemsEqual(recipeStack, inStack)) {
                            iter.remove();
                            matched = true;
                            break;
                        }
                    }

                    if (!matched) {
                        // Input item did not match any remaining recipe input
                        return false;
                    }
                }

                // Ensure all recipe items were used exactly once
                return remainingRecipeInputs.isEmpty();
            }


            public ItemStack getOutput(ItemStack[] input) {
                return ItemStack.EMPTY;
            }

            public void onCrafted(World world, BlockPos pos, ItemStack[] input, ItemStack output) {
                ResourceLocation rl = new ResourceLocation(entityId);
                Entity entity = EntityList.createEntityByIDFromName(rl, world);

                if (entity != null) {
                    entity.setPosition(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);
                    world.spawnEntity(entity);
                }
            }
        });
    }

    public static ModifierBase RUBBER_BOOTS = new ModifierRubberBoots();

    private void RegisterEmbersItemModifiers() {
        EmbersAPI.registerModifier(ItemRegistry.RUBBER_BOOTS, RUBBER_BOOTS);
    }

    public void init(FMLInitializationEvent e) {
        RegisterEmbersItemModifiers();

        AddDruidAltarRecipe(new ItemStack[] {
                        new ItemStack(ItemsTC.brain, 1),
                        new ItemStack(ItemsTC.salisMundus, 1),
                        new ItemStack(ItemsTC.bottleTaint, 1),
                        new ItemStack(ModItems.infernal_bulb, 1),
                },
                "thaumcraft:cultistportallesser"
        );


        CapabilityManager.INSTANCE.register(PacifistCapability.class, new PacifistCapability.PacifistCapabilityStorage(), new PacifistCapability.PacifistCapabilityFactory());
        CapabilityManager.INSTANCE.register(WellnessCapability.class, new WellnessCapability.WellnessCapabilityStorage(), new WellnessCapability.WellnessCapabilityFactory());

        // Generate betweenlands bee hives
        GameRegistry.registerWorldGenerator(new BLBeeHiveWorldGen(), 0);

        ConsequenceManager.CONSEQUENCES.add(new ConsequenceSounds());

        ExternalHeaterHandler.registerHeatableAdapter(TileEntityAbstractBLFurnace.class, new ExternalHeaterClasses.HeaterBLFurnace());

        /*
        BoreOutput tcCrystalOutput = new BoreOutput(Sets.newHashSet(20), Sets.newHashSet(new ResourceLocation("thebetweenlands", "sludge_plains"), new ResourceLocation("thebetweenlands", "sludge_plains_clearing")), Lists.newArrayList(
                new WeightedItemStack(ThaumcraftApiHelper.makeCrystal(Aspect.AIR, 2), 15),
                new WeightedItemStack(ThaumcraftApiHelper.makeCrystal(Aspect.FIRE, 2), 15),
                new WeightedItemStack(ThaumcraftApiHelper.makeCrystal(Aspect.WATER, 2), 15),
                new WeightedItemStack(ThaumcraftApiHelper.makeCrystal(Aspect.EARTH, 2), 15),
                new WeightedItemStack(ThaumcraftApiHelper.makeCrystal(Aspect.ORDER, 2), 15),
                new WeightedItemStack(ThaumcraftApiHelper.makeCrystal(Aspect.ENTROPY, 2), 15),

                new WeightedItemStack(ThaumcraftApiHelper.makeCrystal(Aspect.AIR, 3), 10),
                new WeightedItemStack(ThaumcraftApiHelper.makeCrystal(Aspect.FIRE, 3), 10),
                new WeightedItemStack(ThaumcraftApiHelper.makeCrystal(Aspect.WATER, 3), 10),
                new WeightedItemStack(ThaumcraftApiHelper.makeCrystal(Aspect.EARTH, 3), 10),
                new WeightedItemStack(ThaumcraftApiHelper.makeCrystal(Aspect.ORDER, 3), 10),
                new WeightedItemStack(ThaumcraftApiHelper.makeCrystal(Aspect.ENTROPY, 3), 10)
        ));

                RecipeRegistry.boreOutputSets.add(tcCrystalOutput);

         */

        NetworkRegistry.INSTANCE.registerGuiHandler(BLAdditions.instance, new GUIProxy());


        ScanningManager.addScannableThing(new ScanItem("f_DISPENSER", new ItemStack(com.joshiegemfinder.betweenlandsredstone.ModBlocks.SCABYST_DISPENSER)));

        ScanningManager.addScannableThing(new ScanOreDictionary("f_MATIRON", new String[]{"oreSyrmorite", "ingotSyrmorite", "blockSyrmorite", "plateSyrmorite"}));
        ScanningManager.addScannableThing(new ScanItem("f_MATCLAY", new ItemStack(BlockRegistry.MUD)));
        ScanningManager.addScannableThing(new ScanItem("f_MATCLAY", new ItemStack(ItemRegistry.ITEMS_MISC, 1, 10)));

        ScanningManager.addScannableThing(new ScanEntity("f_SPIDER", EntitySludgeWorm.class, true));
        ScanningManager.addScannableThing(new ScanEntity("f_SPIDER", EntityTinySludgeWorm.class, true));
        ScanningManager.addScannableThing(new ScanEntity("f_SPIDER", EntityLargeSludgeWorm.class, true));
        ScanningManager.addScannableThing(new ScanEntity("f_SPIDER", EntityStalker.class, true));

        ScanningManager.addScannableThing(new ScanEntity("f_FLY", EntityFirefly.class, true));
        ScanningManager.addScannableThing(new ScanEntity("f_FLY", EntityDragonFly.class, true));
        ScanningManager.addScannableThing(new ScanEntity("f_FLY", EntityPyrad.class, true));
        ScanningManager.addScannableThing(new ScanEntity("f_FLY", EntityChiromaw.class, true));
        ScanningManager.addScannableThing(new ScanEntity("f_FLY", EntityChiromawMatriarch.class, true));
        ScanningManager.addScannableThing(new ScanEntity("f_FLY", EntityChiromawGreeblingRider.class, true));
        ScanningManager.addScannableThing(new ScanEntity("f_FLY", EntityChiromawTame.class, true));

        ScanningManager.addScannableThing(new ScanBlock("f_TELEPORT", BlockRegistry.WAYSTONE));
        ScanningManager.addScannableThing(new ScanItem("f_TELEPORT", new ItemStack(ItemRegistry.BONE_WAYFINDER, 1, OreDictionary.WILDCARD_VALUE)));

        ScanningManager.addScannableThing(new ScanItem("!DRAGONBREATH", ItemRegistry.DENTROTHYST_FLUID_VIAL.withFluid(0, FluidRegistry.SHALLOWBREATH)));
        ScanningManager.addScannableThing(new ScanItem("!DRAGONBREATH", ItemRegistry.DENTROTHYST_FLUID_VIAL.withFluid(1, FluidRegistry.SHALLOWBREATH)));

        ScanningManager.addScannableThing(new ScanEntity("!Firebat", EntityPyrad.class, true));

        ScanningManager.addScannableThing(new ScanEntity("f_golem", EntityBarrishee.class, true));
        ScanningManager.addScannableThing(new ScanEntity("f_golem", EntityAncientGolem.class, true));

        ScanningManager.addScannableThing(new ScanItem("m_CREEPER", new ItemStack(ItemRegistry.ANGRY_PEBBLE)));

        ScanningManager.addScannableThing(new ScanItem("ORE", new ItemStack(ItemsTC.crystalEssence, 1, OreDictionary.WILDCARD_VALUE)));
        ScanningManager.addScannableThing(new ScanItem("!ORECRYSTAL", new ItemStack(ItemsTC.crystalEssence, 1, OreDictionary.WILDCARD_VALUE)));

        CoreRegistry.instance().fluidDictionary().addFluidTags(MilkRegistry.liquid_milk, MilkFluidTags.MILK);
        growthcraft.milk.shared.MilkRegistry.instance().pancheon().addRecipe(
                new FluidStack(MilkRegistry.liquid_milk, 1000),
                GrowthcraftMilkFluids.cream.asFluidStack(2 * GrowthcraftCoreConfig.BOTTLE_CAPACITY), GrowthcraftMilkFluids.skimMilk.asFluidStack(Init.roundToBottles(FluidContainerRegistry.BUCKET_VOLUME - 2 * GrowthcraftCoreConfig.BOTTLE_CAPACITY)),
                TickUtils.minutes(1));

        ALLOWED_FENCES.add(BlockRegistry.HEARTHGROVE_PLANK_FENCE);
        ALLOWED_FENCES.add(BlockRegistry.NIBBLETWIG_PLANK_FENCE);
        ALLOWED_FENCES.add(BlockRegistry.ROTTEN_PLANK_FENCE);
        ALLOWED_FENCES.add(BlockRegistry.WEEDWOOD_LOG_FENCE);
        ALLOWED_FENCES.add(BlockRegistry.WEEDWOOD_PLANK_FENCE);
        ALLOWED_FENCES.add(BlockRegistry.GIANT_ROOT_PLANK_FENCE);
        ALLOWED_FENCES.add(BlockRegistry.RUBBER_TREE_PLANK_FENCE);

        fillCropList();

        SealHandler.types.put("thaumcraft:harvest", new PatchedSealHarvest());

        CropUtils.addClickableCrop(new ItemStack(ItemRegistry.MIDDLE_FRUIT_BUSH_SEEDS), 15);
        ThaumcraftApi.registerSeed(BlockRegistry.MIDDLE_FRUIT_BUSH, new ItemStack(ItemRegistry.MIDDLE_FRUIT_BUSH_SEEDS));


        // Embers efficiencies
        EmberGenUtil.registerMetalCoefficient("blockOctine",1.0f);
        EmberGenUtil.registerMetalCoefficient("blockSyrmorite",0.75f);

        // Arcane Archives
        RecipeLibrary.LETTER_OF_INVITATION_RECIPE = GCTRecipeList.instance.makeAndAddRecipeWithCreatorAndCondition("letter_invitation", new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.LETTER_OF_INVITATION, 1), new Object[]{ItemMisc.EnumItemMisc.PARCHMENT.create(1), new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.COMPONENT_RADIANTDUST, 1), ItemCrushed.EnumItemCrushed.GROUND_AQUA_MIDDLE_GEM.create(1)}).addCondition((player, tile) -> {
            if (!player.world.isRemote) {
                HiveSaveData saveData = DataHelper.getHiveData();
                HiveSaveData.Hive hive = saveData.getHiveByMember(player.getUniqueID());
                return hive == null || hive.owner.equals(player.getUniqueID());
            } else {
                ClientNetwork network = DataHelper.getClientNetwork();
                return network.ownsHive() || !network.inHive();
            }
        });
        RecipeLibrary.LETTER_OF_RESIGNATION_RECIPE = GCTRecipeList.instance.makeAndAddRecipeWithCreatorAndCondition("letter_resignation", new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.LETTER_OF_RESIGNATION, 1), new Object[]{ItemMisc.EnumItemMisc.PARCHMENT.create(1), new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.COMPONENT_RADIANTDUST, 1), ItemCrushed.EnumItemCrushed.GROUND_GREEN_MIDDLE_GEM.create(1)}).addCondition((player, tile) -> {
            if (!player.world.isRemote) {
                HiveSaveData saveData = DataHelper.getHiveData();
                HiveSaveData.Hive hive = saveData.getHiveByMember(player.getUniqueID());
                return hive != null;
            } else {
                ClientNetwork network = DataHelper.getClientNetwork();
                return network.inHive();
            }
        });
        RecipeLibrary.WRIT_OF_EXPULSION_RECIPE = GCTRecipeList.instance.makeAndAddRecipeWithCreatorAndCondition("writ_expulsion", new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.WRIT_OF_EXPULSION, 1), new Object[]{ItemMisc.EnumItemMisc.PARCHMENT.create(1), new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.COMPONENT_RADIANTDUST, 1), ItemCrushed.EnumItemCrushed.GROUND_CRIMSON_MIDDLE_GEM.create(1)}).addCondition((player, tile) -> {
            if (!player.world.isRemote) {
                HiveSaveData saveData = DataHelper.getHiveData();
                HiveSaveData.Hive hive = saveData.getHiveByMember(player.getUniqueID());
                return hive != null && hive.owner.equals(player.getUniqueID());
            } else {
                ClientNetwork network = DataHelper.getClientNetwork();
                return network.inHive() && network.ownsHive();
            }
        });
    }


    private void fillCropList() {
        CROPS.clear();

        ForgeRegistries.BLOCKS.getValuesCollection().stream()
                .filter(b -> !Feature.isVanilla(b) && b instanceof BlockCrops)
                .forEach(b -> CROPS.put(new BlockStack(b, ((BlockCrops) b).getMaxAge()), new BlockStack(b)));

        CROPS.put(new BlockStack(BlockRegistry.FUNGUS_CROP, 15), new BlockStack(BlockRegistry.FUNGUS_CROP));
    }


    private void setupPizzaRecipes() {
        PizzaCraftingManager manager = PizzaCraftingManager.getPizzaCraftingInstance();
        List<IBakewareRecipe> recipes = manager.getRecipeList();

        for(int i = recipes.size() - 1; i >= 0; i--) {
            manager.removeRecipe(recipes.get(i));
        }

        manager.addRecipe(new BaseShapelessOreRecipe(new ItemStack(ModBlocks.RAW_PIZZA_0, 1), "foodPizzaDough", "foodCheese"));
        manager.addRecipe(new BaseShapelessOreRecipe(new ItemStack(ModBlocks.RAW_PIZZA_1, 1), "foodPizzaDough", "foodCheese", "foodMushroom"));
        manager.addRecipe(new BaseShapelessOreRecipe(new ItemStack(ModBlocks.RAW_PIZZA_2, 1), "foodPizzaDough", "foodCheese", new ItemStack(ItemRegistry.ANADIA_MEAT_RAW), new ItemStack(ItemRegistry.SWAMP_KELP_ITEM)));
        manager.addRecipe(new BaseShapelessOreRecipe(new ItemStack(ModBlocks.RAW_PIZZA_3, 1), "foodPizzaDough", "foodCheese", new ItemStack(ItemRegistry.SNAIL_FLESH_RAW), new ItemStack(ItemRegistry.MIRE_SNAIL_EGG_COOKED)));
        manager.addRecipe(new BaseShapelessOreRecipe(new ItemStack(ModBlocks.RAW_PIZZA_4, 1), "foodPizzaDough", "foodCheese", "foodMushroom", new ItemStack(ItemRegistry.FROG_LEGS_RAW), new ItemStack(BlockRegistry.NETTLE)));
        manager.addRecipe(new BaseShapelessOreRecipe(new ItemStack(ModBlocks.RAW_PIZZA_5, 1), "foodPizzaDough", "foodCheese", "foodMushroom", new ItemStack(ItemRegistry.SAP_BALL), new ItemStack(ItemRegistry.SAP_SPIT), new ItemStack(ItemRegistry.SLUDGE_BALL)));
        manager.addRecipe(new BaseShapelessOreRecipe(new ItemStack(ModBlocks.RAW_PIZZA_6, 1), "foodPizzaDough", "foodCheese", "foodMushroom", new ItemStack(ItemRegistry.SWAMP_KELP_ITEM), new ItemStack(ItemRegistry.MIDDLE_FRUIT)));
        manager.addRecipe(new BaseShapelessOreRecipe(new ItemStack(ModBlocks.RAW_PIZZA_7, 1), "foodPizzaDough", "foodCheese", "foodMushroom", new ItemStack(ItemRegistry.SWAMP_KELP_ITEM), new ItemStack(ItemRegistry.SNAIL_FLESH_RAW), new ItemStack(ItemRegistry.FROG_LEGS_RAW), new ItemStack(BlockRegistry.MUD)));
        manager.addRecipe(new BaseShapelessOreRecipe(new ItemStack(ModBlocks.RAW_PIZZA_8, 1), "foodPizzaDough", "foodCheese", "foodMushroom", new ItemStack(ItemRegistry.SWAMP_KELP_ITEM), new ItemStack(ItemRegistry.ITEMS_CRUSHED, 1, 34)));
        manager.addRecipe(new BaseShapelessOreRecipe(new ItemStack(ModBlocks.RAW_PIZZA_9, 1), "foodPizzaDough", "foodCheese", new ItemStack(ItemRegistry.FORBIDDEN_FIG), new ItemStack(ItemRegistry.WIGHT_HEART), new ItemStack(ItemRegistry.SPIRIT_FRUIT), new ItemStack(ItemRegistry.WEEPING_BLUE_PETAL)));
        //manager.addRecipe(new BaseShapelessOreRecipe(new ItemStack(ModBlocks.RAW_PIZZA_10, 1), "foodPizzaDough", "foodCheese"));
    }


    public void postInit(FMLPostInitializationEvent e) {
        Growth.addBlacklist(epicsquid.roots.init.ModBlocks.cloud_berry);
        Growth.addBlacklist(epicsquid.roots.init.ModBlocks.infernal_bulb);
        Growth.addBlacklist(epicsquid.roots.init.ModBlocks.stalicripe);
        Growth.addBlacklist(epicsquid.roots.init.ModBlocks.dewgonia);

        BloodMagicAPI.INSTANCE.unregisterAltarComponent(Blocks.GLOWSTONE.getDefaultState(), "GLOWSTONE");
        BloodMagicAPI.INSTANCE.registerAltarComponent(BlockRegister.BLOCK_DAWNSTONE.getDefaultState(), "GLOWSTONE");

        BloodMagicAPI.INSTANCE.unregisterAltarComponent(Blocks.BEACON.getDefaultState(), "BEACON");
        BloodMagicAPI.INSTANCE.registerAltarComponent(BlocksTC.metalBlockVoid.getDefaultState(), "BEACON");

        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(BlockRegistry.DUG_PURIFIED_SWAMP_DIRT, new TranquilityStack(EnumTranquilityType.EARTHEN, 1.0));
        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(BlockRegistry.DUG_PURIFIED_SWAMP_GRASS, new TranquilityStack(EnumTranquilityType.EARTHEN, 1.0));
        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(BlockRegistry.SWAMP_DIRT, new TranquilityStack(EnumTranquilityType.EARTHEN, 0.25));
        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(BlockRegistry.SWAMP_GRASS, new TranquilityStack(EnumTranquilityType.EARTHEN, 0.5));

        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(BlockRegistry.WEEDWOOD_BUSH, new TranquilityStack(EnumTranquilityType.PLANT, 1.0));

        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(BlockRegistry.MIDDLE_FRUIT_BUSH, new TranquilityStack(EnumTranquilityType.CROP, 1.0));
        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(BlockRegistry.FUNGUS_CROP, new TranquilityStack(EnumTranquilityType.CROP, 1.0));
        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(epicsquid.roots.init.ModBlocks.cloud_berry, new TranquilityStack(EnumTranquilityType.CROP, 1.0));
        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(epicsquid.roots.init.ModBlocks.dewgonia, new TranquilityStack(EnumTranquilityType.CROP, 1.0));
        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(epicsquid.roots.init.ModBlocks.stalicripe, new TranquilityStack(EnumTranquilityType.CROP, 1.0));
        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(epicsquid.roots.init.ModBlocks.infernal_bulb, new TranquilityStack(EnumTranquilityType.CROP, 1.0));
        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(epicsquid.roots.init.ModBlocks.spirit_herb, new TranquilityStack(EnumTranquilityType.CROP, 1.0));
        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(epicsquid.roots.init.ModBlocks.pereskia, new TranquilityStack(EnumTranquilityType.CROP, 1.0));
        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(epicsquid.roots.init.ModBlocks.moonglow, new TranquilityStack(EnumTranquilityType.CROP, 1.0));

        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(BlockRegistry.SWAMP_WATER, new TranquilityStack(EnumTranquilityType.WATER, 1.0));

        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(BlockRegistry.LOG_SPIRIT_TREE, new TranquilityStack(EnumTranquilityType.TREE, 1.2));
        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(BlocksTC.logSilverwood, new TranquilityStack(EnumTranquilityType.TREE, 1.2));
        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(BlocksTC.logGreatwood, new TranquilityStack(EnumTranquilityType.TREE, 1.1));
        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(epicsquid.roots.init.ModBlocks.wildwood_log, new TranquilityStack(EnumTranquilityType.TREE, 1.1));

        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(BlockRegistry.PEAT_SMOULDERING, new TranquilityStack(EnumTranquilityType.FIRE, 0.5));

        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(BlockRegistry.WISP.getDefaultState(), "magical", 1.0);
        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(BlockRegistry.SIMULACRUM_DEEPMAN.getDefaultState(), "magical", 1.1);
        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(BlockRegistry.SIMULACRUM_ROOTMAN.getDefaultState(), "magical", 1.1);
        BloodMagicAPI.INSTANCE.getValueManager().setTranquility(BlockRegistry.SIMULACRUM_LAKE_CAVERN.getDefaultState(), "magical", 1.1);


        /*
        api.getValueManager().setTranquility(Blocks.LAVA, new TranquilityStack(EnumTranquilityType.LAVA, 1.2));
        api.getValueManager().setTranquility(Blocks.FLOWING_LAVA, new TranquilityStack(EnumTranquilityType.LAVA, 1.2));
        api.getValueManager().setTranquility(Blocks.WATER, new TranquilityStack(EnumTranquilityType.WATER, 1.0));
        api.getValueManager().setTranquility(Blocks.FLOWING_WATER, new TranquilityStack(EnumTranquilityType.WATER, 1.0));
        api.getValueManager().setTranquility(RegistrarBloodMagicBlocks.LIFE_ESSENCE, new TranquilityStack(EnumTranquilityType.WATER, 1.5));
        api.getValueManager().setTranquility(Blocks.NETHERRACK, new TranquilityStack(EnumTranquilityType.FIRE, 0.5));
        api.getValueManager().setTranquility(Blocks.DIRT, new TranquilityStack(EnumTranquilityType.EARTHEN, 0.25));
        api.getValueManager().setTranquility(Blocks.FARMLAND, new TranquilityStack(EnumTranquilityType.EARTHEN, 1.0));
        api.getValueManager().setTranquility(Blocks.POTATOES, new TranquilityStack(EnumTranquilityType.CROP, 1.0));
        api.getValueManager().setTranquility(Blocks.CARROTS, new TranquilityStack(EnumTranquilityType.CROP, 1.0));
        api.getValueManager().setTranquility(Blocks.WHEAT, new TranquilityStack(EnumTranquilityType.CROP, 1.0));
        api.getValueManager().setTranquility(Blocks.NETHER_WART, new TranquilityStack(EnumTranquilityType.CROP, 1.0));
        api.getValueManager().setTranquility(Blocks.BEETROOTS, new TranquilityStack(EnumTranquilityType.CROP, 1.0));
         */

        registerMultiblocks();
        registerInfusionRecipes();
        registerSmeltingRecipes();
        registerAdditionalBLFurnaceIngots();
        setupPizzaRecipes();
        setupWellnessBlocks();
        SWAMP_WATER = FluidRegistry.SWAMP_WATER;

        CorrosionHelper.addCorrosionPropertyOverrides(vazkii.botania.common.item.ModItems.manasteelAxe);
        CorrosionHelper.addCorrosionPropertyOverrides(vazkii.botania.common.item.ModItems.manasteelShovel);
        CorrosionHelper.addCorrosionPropertyOverrides(vazkii.botania.common.item.ModItems.manasteelPick);
        CorrosionHelper.addCorrosionPropertyOverrides(vazkii.botania.common.item.ModItems.manasteelSword);

        CorrosionHelper.addCorrosionPropertyOverrides(vazkii.botania.common.item.ModItems.elementiumAxe);
        CorrosionHelper.addCorrosionPropertyOverrides(vazkii.botania.common.item.ModItems.elementiumShovel);
        CorrosionHelper.addCorrosionPropertyOverrides(vazkii.botania.common.item.ModItems.elementiumPick);
        CorrosionHelper.addCorrosionPropertyOverrides(vazkii.botania.common.item.ModItems.elementiumSword);

        CorrosionHelper.addCorrosionPropertyOverrides(vazkii.botania.common.item.ModItems.terraSword);

        CorrosionHelper.addCorrosionPropertyOverrides(RegistrarBloodMagicItems.BOUND_SWORD);
        CorrosionHelper.addCorrosionPropertyOverrides(RegistrarBloodMagicItems.BOUND_AXE);
        CorrosionHelper.addCorrosionPropertyOverrides(RegistrarBloodMagicItems.BOUND_SHOVEL);
        CorrosionHelper.addCorrosionPropertyOverrides(RegistrarBloodMagicItems.BOUND_PICKAXE);

        CorrosionHelper.addCorrosionPropertyOverrides(RegistrarBloodMagicItems.SENTIENT_SWORD);
        CorrosionHelper.addCorrosionPropertyOverrides(RegistrarBloodMagicItems.SENTIENT_AXE);
        CorrosionHelper.addCorrosionPropertyOverrides(RegistrarBloodMagicItems.SENTIENT_SHOVEL);
        CorrosionHelper.addCorrosionPropertyOverrides(RegistrarBloodMagicItems.SENTIENT_PICKAXE);

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:LiquidDeath"));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("thaumcraft:LiquidDeath"), new CrucibleRecipe("LIQUIDDEATH", ItemRegistry.BL_BUCKET.withFluid(1, ConfigBlocks.FluidDeath.instance), new ItemStack(ItemRegistry.BL_BUCKET, 1, 1), (new AspectList()).add(Aspect.DEATH, 100).add(Aspect.ALCHEMY, 20).add(Aspect.ENTROPY, 50)));

//        FLUXABLE_ITEMS.put(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 1), new ItemStack(Items.APPLE));

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Item.getItemFromBlock(BlocksTC.jarNormal), new BehaviorDefaultDispenseItem()
        {
            @Override
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
            {
                if(stack.getItem() instanceof BlockJarItem) {
                    BlockPos blockPos = (source.getBlockState().getValue(BlockDispenser.FACING) == EnumFacing.UP) ? source.getBlockPos().up() : source.getBlockPos().offset(source.getBlockState().getValue(BlockDispenser.FACING));
                    World world = source.getWorld();

                    if(world.getBlockState(blockPos).getBlock() instanceof BlockAir) {
                        world.setBlockState(blockPos, BlocksTC.jarNormal.getDefaultState());
                        TileEntity te = source.getWorld().getTileEntity(blockPos);

                        if (te instanceof TileJarFillable) {
                            BlockJarItem blockJarItem = (BlockJarItem)stack.getItem();
                            TileJarFillable jar = (TileJarFillable)te;
                            jar.setAspects(blockJarItem.getAspects(stack));

                            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("AspectFilter")) {
                                jar.aspectFilter = Aspect.getAspect(stack.getTagCompound().getString("AspectFilter"));
                            }

                            te.markDirty();
                            source.getWorld().markAndNotifyBlock(blockPos, source.getWorld().getChunk(blockPos), BlocksTC.jarNormal.getDefaultState(), BlocksTC.jarNormal.getDefaultState(), 3);

                            stack.shrink(1);
                            return stack;
                        }
                    }
                }

                return super.dispenseStack(source, stack);
            }
        });

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Item.getItemFromBlock(BlocksTC.jarVoid), new BehaviorDefaultDispenseItem()
        {
            @Override
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
            {
                if(stack.getItem() instanceof BlockJarItem) {
                    BlockPos blockPos = (source.getBlockState().getValue(BlockDispenser.FACING) == EnumFacing.UP) ? source.getBlockPos().up() : source.getBlockPos().offset(source.getBlockState().getValue(BlockDispenser.FACING));
                    World world = source.getWorld();

                    if(world.getBlockState(blockPos).getBlock() instanceof BlockAir) {
                        world.setBlockState(blockPos, BlocksTC.jarVoid.getDefaultState());
                        TileEntity te = source.getWorld().getTileEntity(blockPos);

                        if (te instanceof TileJarFillable) {
                            BlockJarItem blockJarItem = (BlockJarItem)stack.getItem();
                            TileJarFillable jar = (TileJarFillable)te;
                            jar.setAspects(blockJarItem.getAspects(stack));

                            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("AspectFilter")) {
                                jar.aspectFilter = Aspect.getAspect(stack.getTagCompound().getString("AspectFilter"));
                            }

                            te.markDirty();
                            source.getWorld().markAndNotifyBlock(blockPos, source.getWorld().getChunk(blockPos), BlocksTC.jarNormal.getDefaultState(), BlocksTC.jarNormal.getDefaultState(), 3);

                            stack.shrink(1);
                            return stack;
                        }
                    }
                }

                return super.dispenseStack(source, stack);
            }
        });
    }


    private static void overrideThaumcraftBook () {
        NBTTagCompound contents = new NBTTagCompound();
        contents.setInteger("generation", 3);
        contents.setString("title", I18n.translateToLocal("book.custom.start.title"));
        NBTTagList pages = new NBTTagList();
        pages.appendTag(new NBTTagString(I18n.translateToLocal("book.custom.start.1")));
        pages.appendTag(new NBTTagString(I18n.translateToLocal("book.custom.start.2")));
        pages.appendTag(new NBTTagString(I18n.translateToLocal("book.custom.start.3")));
        contents.setTag("pages", pages);
        ConfigItems.startBook.setTagCompound(contents);
    }


    private void setupWellnessBlocks() {
        // add more so there is at least 1 per type
        WELLNESS_BLOCKS.put(BlocksTC.crucible, Collections.singletonList(PotionThaumcraftResearch.RESEARCH_CATEGORY.ALCHEMY));
        WELLNESS_BLOCKS.put(BlocksTC.golemBuilder, Collections.singletonList(PotionThaumcraftResearch.RESEARCH_CATEGORY.GOLEMANCY));
        WELLNESS_BLOCKS.put(BlocksTC.nitor.get(EnumDyeColor.YELLOW), Collections.singletonList(PotionThaumcraftResearch.RESEARCH_CATEGORY.AUROMANCY));
        WELLNESS_BLOCKS.put(BlocksTC.jarBrain, Collections.singletonList(PotionThaumcraftResearch.RESEARCH_CATEGORY.ELDRITCH));
        WELLNESS_BLOCKS.put(BlocksTC.jarNormal, Collections.singletonList(PotionThaumcraftResearch.RESEARCH_CATEGORY.ARCANE));
        WELLNESS_BLOCKS.put(BlocksTC.mirror, Collections.singletonList(PotionThaumcraftResearch.RESEARCH_CATEGORY.ARTIFICE));

        WELLNESS_BLOCKS.put(Blocks.BOOKSHELF, Stream.of(
                PotionThaumcraftResearch.RESEARCH_CATEGORY.ALCHEMY,
                PotionThaumcraftResearch.RESEARCH_CATEGORY.ARCANE,
                PotionThaumcraftResearch.RESEARCH_CATEGORY.ARTIFICE,
                PotionThaumcraftResearch.RESEARCH_CATEGORY.AUROMANCY,
                PotionThaumcraftResearch.RESEARCH_CATEGORY.GOLEMANCY,
                PotionThaumcraftResearch.RESEARCH_CATEGORY.ELDRITCH
        ).collect(Collectors.toList()));

        WELLNESS_BLOCKS.put(BlocksTC.researchTable, Stream.of(
                PotionThaumcraftResearch.RESEARCH_CATEGORY.ALCHEMY,
                PotionThaumcraftResearch.RESEARCH_CATEGORY.ARCANE,
                PotionThaumcraftResearch.RESEARCH_CATEGORY.ARTIFICE,
                PotionThaumcraftResearch.RESEARCH_CATEGORY.AUROMANCY,
                PotionThaumcraftResearch.RESEARCH_CATEGORY.GOLEMANCY,
                PotionThaumcraftResearch.RESEARCH_CATEGORY.ELDRITCH
        ).collect(Collectors.toList()));
    }


    public void registerAdditionalBLFurnaceIngots() {
        CommonProxy.ADDITIONAL_INGOTS.add(new ItemStack(Items.IRON_INGOT));
        CommonProxy.ADDITIONAL_INGOTS.add(new ItemStack(BLAdditionsUtils.getStackFromOredict("ingotCopper").getItem()));
        CommonProxy.ADDITIONAL_INGOTS.add(new ItemStack(BLAdditionsUtils.getStackFromOredict("ingotAluminum").getItem()));
        CommonProxy.ADDITIONAL_INGOTS.add(new ItemStack(BLAdditionsUtils.getStackFromOredict("ingotLead").getItem()));
        CommonProxy.ADDITIONAL_INGOTS.add(new ItemStack(BLAdditionsUtils.getStackFromOredict("ingotNickel").getItem()));
        CommonProxy.ADDITIONAL_INGOTS.add(new ItemStack(BLAdditionsUtils.getStackFromOredict("ingotSilver").getItem()));
        CommonProxy.ADDITIONAL_INGOTS.add(new ItemStack(BLAdditionsUtils.getStackFromOredict("ingotTin").getItem()));
    }


    private void registerSmeltingRecipes() {
        ThaumcraftApi.addSmeltingBonus("oreSyrmorite", new ItemStack(ItemRegistry.ITEMS_MISC, 1, 41));
        ThaumcraftApi.addSmeltingBonus("oreOctine", new ItemStack(ItemRegistry.ITEMS_MISC, 1, 42));

        // rare earth drops
        ThaumcraftApi.addSmeltingBonus("oreSyrmorite", new ItemStack(ItemsTC.nuggets, 1, 10), 0.01F);
        ThaumcraftApi.addSmeltingBonus("oreOctine", new ItemStack(ItemsTC.nuggets, 1, 10), 0.02F);
    }


    private void registerInfusionRecipes() {
        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:HelmGoggles"));
        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:MaskAngryGhost"));
        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:MaskSippingFiend"));

        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:HelmGoggles"), new InfusionRecipe("FORTRESSMASK", new Object[]{"goggles", new NBTTagByte((byte)1)}, 5, (new AspectList()).add(Aspect.SENSES, 40).add(Aspect.AURA, 20).add(Aspect.PROTECT, 20), new ItemStack(ItemsTC.fortressHelm, 1, 32767), new Object[]{new ItemStack(ItemRegistry.SAP_SPIT), new ItemStack(ItemsTC.goggles, 1, 32767)}));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:MaskAngryGhost"), new InfusionRecipe("FORTRESSMASK", new Object[]{"mask", new NBTTagInt(1)}, 8, (new AspectList()).add(Aspect.ENTROPY, 80).add(Aspect.DEATH, 80).add(Aspect.PROTECT, 20), new ItemStack(ItemsTC.fortressHelm, 1, 32767), new Object[]{new ItemStack(Items.DYE, 1, 15), "plateIron", "leather", new ItemStack(ItemRegistry.ANGRY_PEBBLE), new ItemStack(ItemRegistry.ITEMS_MISC, 1, 44), "plateIron"}));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:MaskSippingFiend"), new InfusionRecipe("FORTRESSMASK", new Object[]{"mask", new NBTTagInt(2)}, 8, (new AspectList()).add(Aspect.UNDEAD, 80).add(Aspect.LIFE, 80).add(Aspect.PROTECT, 20), new ItemStack(ItemsTC.fortressHelm, 1, 32767), new Object[]{new ItemStack(Items.DYE, 1, 1), "plateIron", "leather", new ItemStack(ItemRegistry.WIGHT_HEART), ItemRegistry.BL_BUCKET.withFluid(1, MilkRegistry.liquid_milk), "plateIron"}));

        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumicaugmentation", "thaumium_robes_hood_warp"), new InfusionRecipeComplexResearch("THAUMIUM_ROBES&&FORTRESSMASK", new Object[]{"maskType", new NBTTagInt(ItemThaumiumRobes.MaskType.WARP_REDUCTION.getID())}, 8, (new AspectList()).add(Aspect.MIND, 80).add(Aspect.LIFE, 80).add(Aspect.PROTECT, 20), new ItemStack(TAItems.THAUMIUM_ROBES_HOOD, 1, 32767), new Object[]{"plateIron", "dyeBlack", "plateIron", "leather", BlocksTC.shimmerleaf, ItemsTC.brain}));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumicaugmentation", "thaumium_robes_hood_wither"), new InfusionRecipeComplexResearch("THAUMIUM_ROBES&&FORTRESSMASK", new Object[]{"maskType", new NBTTagInt(ItemThaumiumRobes.MaskType.WITHER.getID())}, 8, (new AspectList()).add(Aspect.ENTROPY, 80).add(Aspect.DEATH, 80).add(Aspect.PROTECT, 20), new ItemStack(TAItems.THAUMIUM_ROBES_HOOD, 1, 32767), new Object[]{new ItemStack(Items.DYE, 1, 15), "plateIron", "leather", new ItemStack(ItemRegistry.ANGRY_PEBBLE), new ItemStack(ItemRegistry.ITEMS_MISC, 1, 44), "plateIron"}));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumicaugmentation", "thaumium_robes_hood_lifesteal"), new InfusionRecipeComplexResearch("THAUMIUM_ROBES&&FORTRESSMASK", new Object[]{"maskType", new NBTTagInt(ItemThaumiumRobes.MaskType.LIFESTEAL.getID())}, 8, (new AspectList()).add(Aspect.UNDEAD, 80).add(Aspect.LIFE, 80).add(Aspect.PROTECT, 20), new ItemStack(TAItems.THAUMIUM_ROBES_HOOD, 1, 32767), new Object[]{new ItemStack(Items.DYE, 1, 1), "plateIron", "leather", new ItemStack(ItemRegistry.WIGHT_HEART), ItemRegistry.BL_BUCKET.withFluid(1, MilkRegistry.liquid_milk), "plateIron"}));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:focus_2"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:focus_2"), new InfusionRecipe("FOCUSADVANCED@1", new ItemStack(ItemsTC.focus2), 3, (new AspectList()).add(Aspect.MAGIC, 25).add(Aspect.ORDER, 50), new ItemStack(ItemsTC.focus1), new Object[]{new ItemStack(ItemsTC.quicksilver), new ItemStack(ItemRegistry.ITEMS_MISC, 1, 19), new ItemStack(ItemsTC.quicksilver), "shimmerstone"}));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:LampGrowth"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:LampGrowth"), new InfusionRecipe("LAMPGROWTH", new ItemStack(BlocksTC.lampGrowth), 4, (new AspectList()).add(Aspect.PLANT, 20).add(Aspect.LIGHT, 15).add(Aspect.LIFE, 15).add(Aspect.TOOL, 15), new ItemStack(BlocksTC.lampArcane), new Object[]{new ItemStack(ItemRegistry.OCTINE_INGOT), new ItemStack(ItemRegistry.ITEMS_CRUSHED, 1, 17), ConfigItems.EARTH_CRYSTAL, new ItemStack(ItemRegistry.OCTINE_INGOT), new ItemStack(ItemRegistry.ITEMS_CRUSHED, 1, 17), ConfigItems.EARTH_CRYSTAL}));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:LampFertility"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:LampFertility"), new InfusionRecipe("LAMPFERTILITY", new ItemStack(BlocksTC.lampFertility), 4, (new AspectList()).add(Aspect.BEAST, 20).add(Aspect.LIGHT, 15).add(Aspect.LIFE, 15).add(Aspect.DESIRE, 15), new ItemStack(BlocksTC.lampArcane), new Object[]{new ItemStack(ItemRegistry.OCTINE_INGOT), new ItemStack(ItemRegistry.SWAMP_REED_ITEM), ConfigItems.FIRE_CRYSTAL, new ItemStack(ItemRegistry.OCTINE_INGOT), new ItemStack(ItemRegistry.MIDDLE_FRUIT), ConfigItems.FIRE_CRYSTAL}));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:JarBrain"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:JarBrain"), new InfusionRecipe("JARBRAIN", new ItemStack(BlocksTC.jarBrain), 4, (new AspectList()).add(Aspect.MIND, 25).add(Aspect.SENSES, 25).add(Aspect.UNDEAD, 25), new ItemStack(BlocksTC.jarNormal), new Object[]{new ItemStack(ItemsTC.brain), new ItemStack(ItemRegistry.ITEMS_MISC, 1,30), ItemRegistry.BL_BUCKET.withFluid(1, FluidRegistry.CLEAN_WATER), new ItemStack(ItemRegistry.ITEMS_MISC, 1,30)}));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:SealBreak"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:SealBreak"), new InfusionRecipe("SEALBREAK", GolemHelper.getSealStack("thaumcraft:breaker"), 1, (new AspectList()).add(Aspect.TOOL, 10).add(Aspect.ENTROPY, 10).add(Aspect.MAN, 10), new ItemStack(ItemsTC.seals), new Object[]{Ingredient.fromItem(ItemRegistry.OCTINE_AXE), Ingredient.fromItem(ItemRegistry.OCTINE_PICKAXE), Ingredient.fromItem(ItemRegistry.OCTINE_SHOVEL)}));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:SealHarvest"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:SealHarvest"), new InfusionRecipe("SEALHARVEST", GolemHelper.getSealStack("thaumcraft:harvest"), 0, (new AspectList()).add(Aspect.PLANT, 10).add(Aspect.SENSES, 10).add(Aspect.MAN, 10), new ItemStack(ItemsTC.seals), new Object[]{new ItemStack(ItemRegistry.MIDDLE_FRUIT_BUSH_SEEDS), new ItemStack(ItemRegistry.SWAMP_REED_ITEM), new ItemStack(ItemRegistry.SWAMP_KELP_ITEM), new ItemStack(ItemRegistry.BLACK_HAT_MUSHROOM_ITEM), new ItemStack(ItemRegistry.FLAT_HEAD_MUSHROOM_ITEM), new ItemStack(ItemRegistry.BULB_CAPPED_MUSHROOM_ITEM)}));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:SealButcher"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:SealButcher"), new InfusionRecipe("SEALBUTCHER", GolemHelper.getSealStack("thaumcraft:butcher"), 0, (new AspectList()).add(Aspect.BEAST, 10).add(Aspect.SENSES, 10).add(Aspect.MAN, 10), GolemHelper.getSealStack("thaumcraft:guard"), new Object[]{"leather", new ItemStack(ItemRegistry.SNAIL_FLESH_RAW), new ItemStack(ItemRegistry.ITEMS_MISC, 1, 1), new ItemStack(ItemRegistry.ANADIA_MEAT_RAW), "feather", new ItemStack(ItemRegistry.ITEMS_MISC, 1, 14)}));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:ArcaneBore"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:ArcaneBore"), new InfusionRecipe("ARCANEBORE", new ItemStack(ItemsTC.turretPlacer, 1, 2), 4, (new AspectList()).add(Aspect.ENERGY, 25).add(Aspect.EARTH, 25).add(Aspect.MECHANISM, 100).add(Aspect.VOID, 25).add(Aspect.MOTION, 25), new ItemStack(ItemsTC.turretPlacer), new Object[]{new ItemStack(BlocksTC.plankGreatwood), new ItemStack(BlocksTC.plankGreatwood), new ItemStack(ItemsTC.mechanismComplex), "plateBrass", Ingredient.fromItem(ItemRegistry.OCTINE_PICKAXE), Ingredient.fromItem(ItemRegistry.OCTINE_SHOVEL), new ItemStack(ItemsTC.morphicResonator), new ItemStack(ItemsTC.nuggets, 1, 10)}));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:Mirror"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:Mirror"), new InfusionRecipe("MIRROR", new ItemStack(BlocksTC.mirror), 1, (new AspectList()).add(Aspect.MOTION, 25).add(Aspect.DARKNESS, 25).add(Aspect.EXCHANGE, 25), new ItemStack(ItemsTC.mirroredGlass), new Object[]{"ingotOctine", "ingotOctine", "ingotOctine", "shimmerstone"}));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:MirrorHand"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:MirrorHand"), new InfusionRecipe("MIRRORHAND", new ItemStack(ItemsTC.handMirror), 5, (new AspectList()).add(Aspect.TOOL, 50).add(Aspect.MOTION, 50), new ItemStack(BlocksTC.mirror), new Object[]{"stickWood", new ItemStack(ItemRegistry.ITEMS_MISC, 1, 47), new ItemStack(ItemRegistry.EMPTY_AMATE_MAP)}));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:MirrorEssentia"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:MirrorEssentia"), new InfusionRecipe("MIRRORESSENTIA", new ItemStack(BlocksTC.mirrorEssentia), 2, (new AspectList()).add(Aspect.MOTION, 25).add(Aspect.WATER, 25).add(Aspect.EXCHANGE, 25), new ItemStack(ItemsTC.mirroredGlass), new Object[]{"ingotSyrmorite", "ingotSyrmorite", "ingotSyrmorite", "shimmerstone"}));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:VoidSiphon"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:VoidSiphon"), new InfusionRecipe("VOIDSIPHON", new ItemStack(BlocksTC.voidSiphon), 7, (new AspectList()).add(Aspect.ELDRITCH, 50).add(Aspect.ENTROPY, 50).add(Aspect.VOID, 100).add(Aspect.CRAFT, 50), new ItemStack(BlocksTC.metalBlockVoid), new Object[]{new ItemStack(BlocksTC.stoneArcane), new ItemStack(BlocksTC.stoneArcane), new ItemStack(ItemsTC.mechanismComplex), "plateBrass", "plateBrass", new ItemStack(ItemRegistry.LIFE_CRYSTAL)}));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:focus_3"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:focus_3"), new InfusionRecipe("FOCUSGREATER@1", new ItemStack(ItemsTC.focus3), 5, (new AspectList()).add(Aspect.MAGIC, 25).add(Aspect.ORDER, 50).add(Aspect.VOID, 100), new ItemStack(ItemsTC.focus2), new Object[]{new ItemStack(ItemsTC.quicksilver), Ingredient.fromItem(ItemsTC.primordialPearl), new ItemStack(ItemsTC.quicksilver), new ItemStack(ItemRegistry.LIFE_CRYSTAL)}));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumicperiphery:magic_quiver"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumicperiphery", "magic_quiver"), new InfusionRecipe("MAGICQUIVER", new ItemStack(ModContent.magic_quiver), 4, (new AspectList()).add(Aspect.VOID, 100).add(Aspect.ORDER, 25).add(Aspect.DESIRE, 25).add(Aspect.MAGIC, 15).add(Aspect.AURA, 15), new ItemStack(ItemsTC.baubles, 1, 2), new Object[]{new ItemStack(ItemsTC.visResonator), "leather", new ItemStack(ItemsTC.fabric), new ItemStack(ItemRegistry.ANGLER_TOOTH_ARROW), new ItemStack(ItemsTC.fabric), "leather"}));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumicperiphery:malignant_heart"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumicperiphery", "malignant_heart"), new InfusionRecipe("MALIGNANTHEART", new ItemStack(ModContent.malignant_heart), 6, (new AspectList()).add(Aspect.AVERSION, 50).add(Aspect.DEATH, 50).add(Aspect.UNDEAD, 25).add(Aspect.FLUX, 15).add(Aspect.ENTROPY, 10), new ItemStack(ItemRegistry.ITEMS_MISC, 1, 25), new Object[]{new ItemStack(ItemsTC.brain), new ItemStack(ItemRegistry.ITEMS_MISC, 1, 44), new ItemStack(GrowthcraftBeesItems.beesWax.getItem(), 1, OreDictionary.WILDCARD_VALUE), new ItemStack(ItemRegistry.ITEMS_MISC, 1, 56), new ItemStack(GrowthcraftBeesItems.beesWax.getItem(), 1, OreDictionary.WILDCARD_VALUE), new ItemStack(ItemRegistry.WIGHT_HEART)}));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:CausalityCollapser"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:CausalityCollapser"), new InfusionRecipe("RIFTCLOSER", new ItemStack(ItemsTC.causalityCollapser), 8, (new AspectList()).add(Aspect.ELDRITCH, 50).add(Aspect.FLUX, 50), new ItemStack(ItemRegistry.ANGRY_PEBBLE), new Object[]{new ItemStack(ItemsTC.morphicResonator), "blockRedstone", new ItemStack(ItemsTC.alumentum), "nitor", new ItemStack(ItemsTC.visResonator), "blockRedstone", new ItemStack(ItemsTC.alumentum), "nitor"}));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumicperiphery:pauldron_repulsion"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumicperiphery", "pauldron_repulsion"), new InfusionRecipe("PAULDRONREPULSION", new ItemStack(ModContent.pauldron_repulsion), 1, (new AspectList()).add(Aspect.AIR, 50).add(Aspect.MOTION, 50).add(Aspect.PROTECT, 10), new ItemStack(ModContent.pauldron), new Object[]{new ItemStack(ItemRegistry.SHIMMER_STONE), ConfigItems.AIR_CRYSTAL}));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:CrystalClusterAir"));
        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:CrystalClusterFire"));
        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:CrystalClusterWater"));
        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:CrystalClusterEarth"));
        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:CrystalClusterOrder"));
        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:CrystalClusterEntropy"));
        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:CrystalClusterFlux"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:CrystalClusterAir"), new InfusionRecipe("CRYSTALFARMER", new ItemStack(BlocksTC.crystalAir), 0, (new AspectList()).add(Aspect.AIR, 10).add(Aspect.CRYSTAL, 10).add(Aspect.TRAP, 5), ThaumcraftApiHelper.makeCrystal(Aspect.AIR), new Object[]{new ItemStack(ItemRegistry.MIDDLE_FRUIT_BUSH_SEEDS), new ItemStack(ItemsTC.salisMundus)}));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:CrystalClusterFire"), new InfusionRecipe("CRYSTALFARMER", new ItemStack(BlocksTC.crystalFire), 0, (new AspectList()).add(Aspect.FIRE, 10).add(Aspect.CRYSTAL, 10).add(Aspect.TRAP, 5), ThaumcraftApiHelper.makeCrystal(Aspect.FIRE), new Object[]{new ItemStack(ItemRegistry.MIDDLE_FRUIT_BUSH_SEEDS), new ItemStack(ItemsTC.salisMundus)}));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:CrystalClusterWater"), new InfusionRecipe("CRYSTALFARMER", new ItemStack(BlocksTC.crystalWater), 0, (new AspectList()).add(Aspect.WATER, 10).add(Aspect.CRYSTAL, 10).add(Aspect.TRAP, 5), ThaumcraftApiHelper.makeCrystal(Aspect.WATER), new Object[]{new ItemStack(ItemRegistry.MIDDLE_FRUIT_BUSH_SEEDS), new ItemStack(ItemsTC.salisMundus)}));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:CrystalClusterEarth"), new InfusionRecipe("CRYSTALFARMER", new ItemStack(BlocksTC.crystalEarth), 0, (new AspectList()).add(Aspect.EARTH, 10).add(Aspect.CRYSTAL, 10).add(Aspect.TRAP, 5), ThaumcraftApiHelper.makeCrystal(Aspect.EARTH), new Object[]{new ItemStack(ItemRegistry.MIDDLE_FRUIT_BUSH_SEEDS), new ItemStack(ItemsTC.salisMundus)}));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:CrystalClusterOrder"), new InfusionRecipe("CRYSTALFARMER", new ItemStack(BlocksTC.crystalOrder), 0, (new AspectList()).add(Aspect.ORDER, 10).add(Aspect.CRYSTAL, 10).add(Aspect.TRAP, 5), ThaumcraftApiHelper.makeCrystal(Aspect.ORDER), new Object[]{new ItemStack(ItemRegistry.MIDDLE_FRUIT_BUSH_SEEDS), new ItemStack(ItemsTC.salisMundus)}));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:CrystalClusterEntropy"), new InfusionRecipe("CRYSTALFARMER", new ItemStack(BlocksTC.crystalEntropy), 0, (new AspectList()).add(Aspect.ENTROPY, 10).add(Aspect.CRYSTAL, 10).add(Aspect.TRAP, 5), ThaumcraftApiHelper.makeCrystal(Aspect.ENTROPY), new Object[]{new ItemStack(ItemRegistry.MIDDLE_FRUIT_BUSH_SEEDS), new ItemStack(ItemsTC.salisMundus)}));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:CrystalClusterFlux"), new InfusionRecipe("CRYSTALFARMER", new ItemStack(BlocksTC.crystalTaint), 4, (new AspectList()).add(Aspect.FLUX, 10).add(Aspect.CRYSTAL, 10).add(Aspect.TRAP, 5), ThaumcraftApiHelper.makeCrystal(Aspect.FLUX), new Object[]{new ItemStack(ItemRegistry.MIDDLE_FRUIT_BUSH_SEEDS), new ItemStack(ItemsTC.salisMundus)}));



        // THAUMIC AUGMENTATION
        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumicaugmentation:morphic_tool_binding_fake"));
        ItemStack morphicSample = new ItemStack(TAItems.MORPHIC_TOOL);
        IMorphicItem tool = morphicSample.getCapability(CapabilityMorphicTool.MORPHIC_TOOL, null);
        tool.setDisplayStack(new ItemStack(ItemRegistry.OCTINE_SHOVEL));
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "morphic_tool_binding_fake"),
            new InfusionRecipe("MORPHIC_TOOL", morphicSample, 5, new AspectList().add(Aspect.VOID, 15),
                    ItemRegistry.VALONITE_SWORD, new Object[] {
                    ItemsTC.primordialPearl, ItemRegistry.OCTINE_SHOVEL, ItemsTC.quicksilver
            }
        ));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumicaugmentation:morphic_armor_binding_fake"));
        morphicSample = new ItemStack(Items.DIAMOND_CHESTPLATE);
        MorphicArmorHelper.setMorphicArmor(morphicSample, new ItemStack(ItemRegistry.SYRMORITE_CHESTPLATE));
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "morphic_armor_binding_fake"),
            new InfusionRecipe("MORPHIC_ARMOR", morphicSample, 8, new AspectList().add(Aspect.VOID, 100),
                    ItemRegistry.VALONITE_CHESTPLATE, new Object[] {
                    ItemsTC.primordialPearl, ItemRegistry.SYRMORITE_CHESTPLATE, ItemsTC.quicksilver
            }
        ));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumicaugmentation:boots_void"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "boots_void"),
            new InfusionRecipe("BOOTS_VOID", new ItemStack(TAItems.VOID_BOOTS), 6,
                    new AspectList().add(Aspect.VOID, 50).add(Aspect.ELDRITCH, 50).add(Aspect.MOTION, 150).add(Aspect.FLIGHT, 150),
                    ItemsTC.travellerBoots, new Object[] {
                    ItemsTC.fabric, ItemsTC.fabric, "plateVoid", "plateVoid", "feather",
                    new ItemStack(ItemRegistry.ITEMS_MISC, 1, 4), ItemsTC.primordialPearl, "quicksilver"
            }
        ));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumicaugmentation:rift_energy_cell"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "rift_energy_cell"),
            new InfusionRecipe("RIFT_POWER@2", new ItemStack(TAItems.MATERIAL, 1, 3), 8, new AspectList().add(Aspect.ELDRITCH, 25).add(Aspect.VOID, 50).add(Aspect.ENERGY, 100),
                    new ItemStack(ItemsTC.voidSeed), new Object[] {
                    "plateVoid", ItemsTC.primordialPearl, "plateVoid", ThaumcraftApiHelper.makeCrystal(Aspect.ELDRITCH),
                    "plateVoid", "dustRedstone", "plateVoid", "gemAmber"
            }
        ));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumicaugmentation:impetus_mirror"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "impetus_mirror"), new InfusionRecipe(
                "IMPETUS_MIRROR", new ItemStack(TAItems.IMPETUS_MIRROR), 4, new AspectList().add(Aspect.MOTION, 25).add(Aspect.EXCHANGE, 25).add(Aspect.ENERGY, 25), ItemsTC.mirroredGlass, new Object[] {
                "shimmerstone", BlocksTC.stoneEldritchTile, new ItemStack(TAItems.MATERIAL, 1, 5), "plateVoid"
            }
        ));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumicaugmentation:thaumostatic_harness"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "thaumostatic_harness"), new InfusionRecipe(
                "THAUMOSTATIC_HARNESS", new ItemStack(TAItems.THAUMOSTATIC_HARNESS), 6, new AspectList().add(Aspect.MECHANISM, 50).add(Aspect.MOTION, 25).add(Aspect.ENERGY, 50).add(Aspect.FLIGHT, 50),
                new ItemStack(TAItems.MATERIAL, 1, 4), new Object[] {
                ThaumcraftApiHelper.makeCrystal(Aspect.AIR), ThaumcraftApiHelper.makeCrystal(Aspect.AIR), BlocksTC.levitator, ItemRegistry.DRAETON_BURNER, BlocksTC.plankGreatwood,
                ItemsTC.morphicResonator
            }
        ));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumicaugmentation:thaumostatic_gyroscope"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "thaumostatic_gyroscope"), new InfusionRecipe(
                "THAUMOSTATIC_GYROSCOPE", new ItemStack(TAItems.THAUMOSTATIC_HARNESS_AUGMENT, 1, 0), 5, new AspectList().add(Aspect.TRAP, 35).add(Aspect.AIR, 25).add(Aspect.FLIGHT, 25),
                new ItemStack(ItemsTC.baubles, 1, 2), new Object[] {
                "dustRedstone", "plateThaumium", ThaumcraftApiHelper.makeCrystal(Aspect.TRAP), "plateThaumium"
        }
        ));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumicaugmentation:thaumostatic_girdle"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "thaumostatic_girdle"), new InfusionRecipe(
                "THAUMOSTATIC_GIRDLE", new ItemStack(TAItems.THAUMOSTATIC_HARNESS_AUGMENT, 1, 1), 8, new AspectList().add(Aspect.AIR, 50).add(Aspect.MOTION, 25).add(Aspect.FLIGHT, 25),
                new ItemStack(ItemsTC.baubles, 1, 2), new Object[] {
                "feather", ThaumcraftApiHelper.makeCrystal(Aspect.FLIGHT), "ingotOctine", "feather", ThaumcraftApiHelper.makeCrystal(Aspect.AIR), "ingotOctine"
        }
        ));

        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumicperiphery", "caster_ember"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumicperiphery", "caster_ember"), new InfusionRecipe(
            "CASTEREMBER", new ItemStack(ModContent.caster_ember), 6, (new AspectList()).add(Aspect.FIRE, 50).add(Aspect.MAGIC, 15).add(Aspect.EXCHANGE, 25).add(Aspect.MECHANISM, 25).add(Aspect.ENERGY, 50),
            new ItemStack(ItemRegister.WILDFIRE_CORE),
            new Object[]{new ItemStack(ItemRegister.SHARD_EMBER), "ingotDawnstone", "plateSyrmorite", new ItemStack(ItemsTC.mechanismComplex), "ingotOctine", new ItemStack(ItemsTC.morphicResonator), "plateSyrmorite", "ingotDawnstone"
        }
        ));



        // Enchanting recipes
        InfusionEnchantmentRecipe IEBURROWING = new InfusionEnchantmentRecipe(EnumInfusionEnchantment.BURROWING, (new AspectList()).add(Aspect.SENSES, 80).add(Aspect.EARTH, 150), new Object[]{
                new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.COMPONENT_CONTAINMENTFIELD),
                new ItemStack(ItemsTC.salisMundus),
                new ItemStack(ItemRegistry.SILT_CRAB_CLAW),
                new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.COMPONENT_CONTAINMENTFIELD),
                new ItemStack(ItemsTC.salisMundus),
                new ItemStack(ItemRegistry.SILT_CRAB_CLAW)
        });
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:IEBURROWING"), IEBURROWING);
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation("thaumcraft:IEBURROWINGFAKE"), new InfusionEnchantmentRecipe(IEBURROWING, new ItemStack(ItemRegistry.WEEDWOOD_PICKAXE)));

        InfusionEnchantmentRecipe IECOLLECTOR = new InfusionEnchantmentRecipe(EnumInfusionEnchantment.COLLECTOR, (new AspectList()).add(Aspect.DESIRE, 80).add(Aspect.WATER, 100), new Object[]{
                new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.COMPONENT_CONTAINMENTFIELD),
                new ItemStack(ItemsTC.salisMundus),
                new ItemStack(BlockRegister.VACUUM),
                new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.COMPONENT_CONTAINMENTFIELD),
                new ItemStack(ItemsTC.salisMundus),
                new ItemStack(BlockRegister.VACUUM)
        });
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:IECOLLECTOR"), IECOLLECTOR);
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation("thaumcraft:IECOLLECTORFAKE"), new InfusionEnchantmentRecipe(IECOLLECTOR, new ItemStack(ItemRegistry.BONE_AXE)));

        InfusionEnchantmentRecipe IEDESTRUCTIVE = new InfusionEnchantmentRecipe(EnumInfusionEnchantment.DESTRUCTIVE, (new AspectList()).add(Aspect.AVERSION, 200).add(Aspect.ENTROPY, 250), new Object[]{
                new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.COMPONENT_CONTAINMENTFIELD),
                new ItemStack(ItemsTC.salisMundus),
                new ItemStack(ItemRegistry.ANGRY_PEBBLE),
                new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.COMPONENT_CONTAINMENTFIELD),
                new ItemStack(ItemsTC.salisMundus),
                new ItemStack(ItemRegistry.ANGRY_PEBBLE)
        });
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:IEDESTRUCTIVE"), IEDESTRUCTIVE);
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation("thaumcraft:IEDESTRUCTIVEFAKE"), new InfusionEnchantmentRecipe(IEDESTRUCTIVE, new ItemStack(ItemRegistry.BONE_PICKAXE)));

        InfusionEnchantmentRecipe IEREFINING = new InfusionEnchantmentRecipe(EnumInfusionEnchantment.REFINING, (new AspectList()).add(Aspect.ORDER, 80).add(Aspect.EXCHANGE, 60), new Object[]{
                new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.COMPONENT_CONTAINMENTFIELD),
                new ItemStack(ItemsTC.salisMundus),
                new ItemStack(ItemMisc.EnumItemMisc.LIMESTONE_FLUX.getItem()),
                new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.COMPONENT_CONTAINMENTFIELD),
                new ItemStack(ItemsTC.salisMundus),
                new ItemStack(ItemMisc.EnumItemMisc.LIMESTONE_FLUX.getItem())
        });
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:IEREFINING"), IEREFINING);
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation("thaumcraft:IEREFININGFAKE"), new InfusionEnchantmentRecipe(IEREFINING, new ItemStack(ItemRegistry.OCTINE_PICKAXE)));

        InfusionEnchantmentRecipe IESOUNDING = new InfusionEnchantmentRecipe(EnumInfusionEnchantment.SOUNDING, (new AspectList()).add(Aspect.SENSES, 40).add(Aspect.FIRE, 60), new Object[]{
                new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.COMPONENT_CONTAINMENTFIELD),
                new ItemStack(ItemsTC.salisMundus),
                new ItemStack(ItemRegister.RESONATING_BELL),
                new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.COMPONENT_CONTAINMENTFIELD),
                new ItemStack(ItemsTC.salisMundus),
                new ItemStack(ItemRegister.RESONATING_BELL)
        });
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:IESOUNDING"), IESOUNDING);
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation("thaumcraft:IESOUNDINGFAKE"), new InfusionEnchantmentRecipe(IESOUNDING, new ItemStack(ItemRegistry.VALONITE_PICKAXE)));

        InfusionEnchantmentRecipe IEARCING = new InfusionEnchantmentRecipe(EnumInfusionEnchantment.ARCING, (new AspectList()).add(Aspect.ENERGY, 40).add(Aspect.AIR, 60), new Object[]{
                new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.COMPONENT_CONTAINMENTFIELD),
                new ItemStack(ItemsTC.salisMundus),
                new ItemStack(com.joshiegemfinder.betweenlandsredstone.ModBlocks.SCABYST_BLOCK),
                new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.COMPONENT_CONTAINMENTFIELD),
                new ItemStack(ItemsTC.salisMundus),
                new ItemStack(com.joshiegemfinder.betweenlandsredstone.ModBlocks.SCABYST_BLOCK)
        });
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:IEARCING"), IEARCING);
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation("thaumcraft:IEARCINGFAKE"), new InfusionEnchantmentRecipe(IEARCING, new ItemStack(ItemRegistry.WEEDWOOD_SWORD)));

        InfusionEnchantmentRecipe IEESSENCE = new InfusionEnchantmentRecipe(EnumInfusionEnchantment.ESSENCE, (new AspectList()).add(Aspect.BEAST, 40).add(Aspect.FLUX, 60), new Object[]{
                new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.COMPONENT_CONTAINMENTFIELD),
                new ItemStack(ItemsTC.salisMundus),
                new ItemStack(ItemsTC.crystalEssence),
                new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.COMPONENT_CONTAINMENTFIELD),
                new ItemStack(ItemsTC.salisMundus),
                new ItemStack(ItemsTC.crystalEssence)
        });
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:IEESSENCE"), IEESSENCE);
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation("thaumcraft:IEESSENCEFAKE"), new InfusionEnchantmentRecipe(IEESSENCE, new ItemStack(ItemRegistry.BONE_SWORD)));

        InfusionEnchantmentRecipe IELAMPLIGHT = new InfusionEnchantmentRecipe(EnumInfusionEnchantment.LAMPLIGHT, (new AspectList()).add(Aspect.LIGHT, 80).add(Aspect.AIR, 20), new Object[]{
                new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.COMPONENT_CONTAINMENTFIELD),
                new ItemStack(ItemsTC.salisMundus),
                "nitor",
                new ItemStack(com.aranaira.arcanearchives.init.ItemRegistry.COMPONENT_CONTAINMENTFIELD),
                new ItemStack(ItemsTC.salisMundus),
                "nitor"
        });
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("thaumcraft:IELAMPLIGHT"), IELAMPLIGHT);
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation("thaumcraft:IELAMPLIGHTFAKE"), new InfusionEnchantmentRecipe(IELAMPLIGHT, new ItemStack(ItemRegistry.OCTINE_PICKAXE)));
    }


    private void registerMultiblocks() {
        IThaumcraftRecipe recipe = ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:infernalfurnace"));

        Part NB = new Part(BlocksTC.stoneArcaneBrick, new ItemStack(BlocksTC.placeholderNetherbrick));
        Part OB = new Part(BlockRegistry.MUD_BRICKS, new ItemStack(BlocksTC.placeholderObsidian));
        Part IB = new Part(Blocks.IRON_BARS, "AIR");
        Part LA = new Part(BlockRegistry.OCTINE_BLOCK, BlocksTC.infernalFurnace, true);
        Part[][][] infernalFurnaceBlueprint = new Part[][][]{{{NB, OB, NB}, {OB, null, OB}, {NB, OB, NB}}, {{NB, OB, NB}, {OB, LA, OB}, {NB, IB, NB}}, {{NB, OB, NB}, {OB, OB, OB}, {NB, OB, NB}}};
        IDustTrigger.registerDustTrigger(new DustTriggerMultiblock("INFERNALFURNACE", infernalFurnaceBlueprint));
        ThaumcraftApi.addMultiblockRecipeToCatalog(new ResourceLocation("thaumcraft:infernalfurnace"), new ThaumcraftApi.BluePrint("INFERNALFURNACE", infernalFurnaceBlueprint, new ItemStack[]{new ItemStack(BlocksTC.stoneArcaneBrick, 12), new ItemStack(BlockRegistry.MUD_BRICKS, 12), new ItemStack(Blocks.IRON_BARS), new ItemStack(BlockRegistry.OCTINE_BLOCK)}));


        ThaumcraftApi.getCraftingRecipes().remove(new ResourceLocation("thaumcraft:GolemPress"));

        Part GP1 = new Part(Blocks.IRON_BARS, new ItemStack(BlocksTC.placeholderBars));
        Part GP2 = new Part(BlocksTC.crucible, new ItemStack(BlocksTC.placeholderCauldron));
        Part GP3 = new Part(com.joshiegemfinder.betweenlandsredstone.ModBlocks.SCABYST_PISTON.getDefaultState().withProperty(BlockPistonBase.FACING, EnumFacing.UP), BlocksTC.golemBuilder);
        Part GP4 = new Part(BlockRegistry.SYRMORITE_BLOCK, new ItemStack(BlocksTC.placeholderAnvil));
        Part GP5 = new Part(BlocksTC.tableStone, new ItemStack(BlocksTC.placeholderTable));
        Part[][][] golempressBlueprint = new Part[][][]{{{null, null}, {GP1, null}}, {{GP2, GP4}, {GP3, GP5}}};
        IDustTrigger.registerDustTrigger(new DustTriggerMultiblock("MINDCLOCKWORK", golempressBlueprint));
        ThaumcraftApi.addMultiblockRecipeToCatalog(new ResourceLocation("thaumcraft:GolemPress"), new ThaumcraftApi.BluePrint("MINDCLOCKWORK", new ItemStack(BlocksTC.golemBuilder), golempressBlueprint, new ItemStack[]{new ItemStack(Blocks.IRON_BARS), new ItemStack(BlocksTC.crucible), new ItemStack(com.joshiegemfinder.betweenlandsredstone.ModBlocks.SCABYST_PISTON), new ItemStack(BlockRegistry.SYRMORITE_BLOCK), new ItemStack(BlocksTC.tableStone)}));
    }


    @SubscribeEvent
    public static void registerBlocks(@Nonnull RegisterContentEvent event) {
        //event.addBlock(epicsquid.roots.init.ModBlocks.baffle_cap_mushroom = new BlockCustomBaffleCap());
    }


    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        GameRegistry.registerTileEntity(TileCrucibleSwamp.class, "thaumcraft:tilecrucibleswamp");
        GameRegistry.registerTileEntity(TileWaterJugSwamp.class, "thaumcraft:tilewaterjug");
        //GameRegistry.registerTileEntity(PatchedTileResearchTable.class, "thaumcraft:researchtable");
        GameRegistry.registerTileEntity(PatchedTilePotionSprayer.class, "thaumcraft:TilePotionSprayer");
        GameRegistry.registerTileEntity(PatchedTileSpa.class, "thaumcraft:TileSpa");

        //ModBlocks.blockShelf = new BlockItemShelf();
        //event.getRegistry().register(ModBlocks.blockShelf);

        BlocksTC.crucible = registerBlock(new BlockCrucibleSwamp());
        BlocksTC.everfullUrn = registerBlock(new PatchedBlockWaterJug());

        BlocksTC.placeholderNetherbrick = registerBlock(new PatchedBlockPlaceHolder("placeholder_brick"));
        BlocksTC.placeholderObsidian = registerBlock(new PatchedBlockPlaceHolder("placeholder_obsidian"));
        BlocksTC.placeholderBars = registerBlock(new PatchedBlockPlaceHolder("placeholder_bars"));
        BlocksTC.placeholderAnvil = registerBlock(new PatchedBlockPlaceHolder("placeholder_anvil"));
        BlocksTC.placeholderCauldron = registerBlock(new PatchedBlockPlaceHolder("placeholder_cauldron"));
        BlocksTC.placeholderTable = registerBlock(new PatchedBlockPlaceHolder("placeholder_table"));
        BlocksTC.potionSprayer = registerBlock(new PatchedBlockPotionSprayer());
        BlocksTC.spa = registerBlock(new PatchedBlockSpa());
       // BlocksTC.researchTable = registerBlock(new PatchedBlockResearchTable());

        //GrowthcraftBeesBlocks.beeHive = new BlockDefinition((new PatchedBeeHive("beehive")));

        BlocksTC.infernalFurnace = registerBlock(new PatchedBlockInfernalFurnace());

    }


    private static Block registerBlock(Block block) {
        ItemBlock itemBlock = new ItemBlock(block);
        ForgeRegistries.BLOCKS.register(block);
        itemBlock.setRegistryName(block.getRegistryName());
        ForgeRegistries.ITEMS.register(itemBlock);
        Thaumcraft.proxy.registerModel(itemBlock);
        return block;
    }


    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        //event.getRegistry().register(new ItemBlock(ModBlocks.blockShelf).setRegistryName(ModBlocks.blockShelf.getRegistryName()));
        event.getRegistry().register(ItemsTC.charmVerdant = new PatchedVerdantCharm());

        event.getRegistry().register(ItemsTC.elementalHoe = new PatchedElementalHoe(ThaumcraftMaterials.TOOLMAT_ELEMENTAL));
        event.getRegistry().register(ItemsTC.elementalAxe = new PatchedItemElementalAxe(ThaumcraftMaterials.TOOLMAT_ELEMENTAL));
        event.getRegistry().register(ItemsTC.elementalPick = new PatchedItemElementalPickaxe(ThaumcraftMaterials.TOOLMAT_ELEMENTAL));
        event.getRegistry().register(ItemsTC.elementalShovel = new PatchedItemElementalShovel(ThaumcraftMaterials.TOOLMAT_ELEMENTAL));
        event.getRegistry().register(ItemsTC.elementalSword = new PatchedItemElementalSword(ThaumcraftMaterials.TOOLMAT_ELEMENTAL));

        event.getRegistry().register(ItemsTC.thaumiumAxe = new PatchedItemThaumiumAxe(ThaumcraftMaterials.TOOLMAT_THAUMIUM));
        event.getRegistry().register(ItemsTC.thaumiumPick = new PatchedItemThaumiumPickaxe(ThaumcraftMaterials.TOOLMAT_THAUMIUM));
        event.getRegistry().register(ItemsTC.thaumiumShovel = new PatchedItemThaumiumShovel(ThaumcraftMaterials.TOOLMAT_THAUMIUM));
        event.getRegistry().register(ItemsTC.thaumiumSword = new PatchedItemThaumiumSword(ThaumcraftMaterials.TOOLMAT_THAUMIUM));

        event.getRegistry().register(ItemsTC.voidAxe = new PatchedItemVoidAxe(ThaumcraftMaterials.TOOLMAT_VOID));
        event.getRegistry().register(ItemsTC.voidPick = new PatchedItemVoidPickaxe(ThaumcraftMaterials.TOOLMAT_VOID));
        event.getRegistry().register(ItemsTC.voidShovel = new PatchedItemVoidShovel(ThaumcraftMaterials.TOOLMAT_VOID));
        event.getRegistry().register(ItemsTC.voidSword = new PatchedItemVoidSword(ThaumcraftMaterials.TOOLMAT_VOID));

        //event.getRegistry().register(DENTROTHYST_VIAL = new PatchedItemDentrothystVial());
        //event.getRegistry().register(DENTROTHYST_FLUID_VIAL = new PatchedItemDentrothystFluidVial());




        //event.getRegistry().register(ModItems.living_shovel = new PatchedItemLivingShovel(Materials.LIVING, "living_shovel"));

        event.getRegistry().register(new ItemAngrierPebble());
        event.getRegistry().register(new ItemCorruptedBoneWayfinder("corrupted_bone_wayfinder"));
        event.getRegistry().register(new ItemImpetusBoneWayfinder("impetus_bone_wayfinder"));
        //event.getRegistry().register(new ItemWaterBowl());

        ModItems.baffle_cap = ItemRegistry.YELLOW_DOTTED_FUNGUS;
        ModItems.wildewheet = ItemRegistry.WEEPING_BLUE_PETAL;

        overrideThaumcraftBook();
    }


    @SubscribeEvent(
            priority = EventPriority.LOWEST
    )
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().register(new PatchedRecipeMagicDust().setRegistryName("thaumcraft:salismundus"));

        int recipeCount = SmokingRackRecipe.RECIPES.size();

        for(int i = 0; i < recipeCount; i++) {
            ISmokingRackRecipe recipe = SmokingRackRecipe.RECIPES.get(i);
            int time = recipe.getSmokingTime(recipe.getInput());
            time *= ConfigBLAdditions.configGeneral.SmokingRackRecipeModifier;
            SmokingRackRecipe.addRecipe(recipe.getOutput(recipe.getInput()), time, recipe.getInput());
        }

        for(int i = 0; i < recipeCount; i++) {
            ISmokingRackRecipe recipe = SmokingRackRecipe.RECIPES.get(0);
            SmokingRackRecipe.removeRecipe(recipe);
        }

        RecipeRegistry.stampingRecipes.remove(new ItemRenameStampingRecipe());

        RecipeRegistry.stampingRecipes.removeAll(getEmbersRecipesByOutput(new ItemStack(Registry.SULFUR)));

        RecipeRegistry.stampingRecipes.add(new CustomItemLiverStampingRecipe());
        RecipeRegistry.stampingRecipes.add(new CustomItemRenameStampingRecipe());

        RecipeRegistry.fluidReactionRecipes.clear();
        RecipeRegistry.fluidReactionRecipes.add(new FluidReactionRecipe(new FluidStack(FluidRegister.FLUID_STEAM, 5), new FluidStack(FluidRegistry.CLEAN_WATER, 1), new Color(255,255,255)));
        RecipeRegistry.fluidReactionRecipes.add(new FluidReactionRecipe(new FluidStack(FluidRegister.FLUID_GAS, 1), new FluidStack(FluidRegister.FLUID_STEAM, 5), new Color(128,192,255)));
    }


    private static List<ItemStampingRecipe> getEmbersRecipesByOutput(ItemStack stack) {
        return (List)RecipeRegistry.stampingRecipes.stream().filter((recipe) -> {
            return ItemStack.areItemStacksEqual(stack, recipe.result);
        }).collect(Collectors.toCollection(ArrayList::new));
    }


    @SubscribeEvent
    public static void onRegisterEntity(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().registerAll(new EntityEntry[]{EntityEntryBuilder.create().entity(GreeblingMerchantEntity.class).name("greeblingmerchant").tracker(120, 3, true).egg(16777215, 65280).id(new ResourceLocation("deliverymerchants", "merchant"), 0).build()});
    }


    public static void registerEntities() {
        //net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(new ResourceLocation(BLAdditions.MODID, "angrier_pebble"), EntityAngrierPebble.class, "bladditions." + "angrier_pebble", 0, BLAdditions.instance, 64, 3, true);
    }


    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        int entityId = 1;

        EntityEntryBuilder<Entity> builder = EntityEntryBuilder.create().name("eyes_fixed")
                .id(EyesInTheDarkness.location("eyes"), entityId++)
                .entity(EntityEyes.class).factory(EntityEyes::new)
                .tracker(80, 3, true)
                .egg(0x000000, 0x7F0000);

        if(ConfigData.EnableNaturalSpawn)
        {
            int currentWeight = ConfigData.OverrideWeight;

            if (currentWeight > 0)
            {
                Collection<Biome> biomes = ForgeRegistries.BIOMES.getValuesCollection();

                if (ConfigData.BiomeWhitelist != null && ConfigData.BiomeWhitelist.length > 0)
                {
                    Set<String> whitelist = Sets.newHashSet(ConfigData.BiomeWhitelist);
                    biomes = biomes.stream().filter(b -> {
                        return whitelist.contains(b.getRegistryName().toString());
                    }).collect(Collectors.toList());
                }
                else if (ConfigData.BiomeBlacklist != null && ConfigData.BiomeBlacklist.length > 0)
                {
                    Set<String> blacklist = Sets.newHashSet(ConfigData.BiomeBlacklist);
                    biomes = biomes.stream().filter(b -> !blacklist.contains(b.getRegistryName().toString())).collect(Collectors.toList());
                }

                builder = builder.spawn(EnumCreatureType.MONSTER, currentWeight,
                        ConfigData.MinimumPackSize, ConfigData.MaximumPackSize,
                        biomes);
            }
        }

        event.getRegistry().registerAll(
                builder
                        .build()
        );

    }

    public static boolean IsWithinLocation(World world, BlockPos pos) {
        return IsWithinLocation(world, pos, false);
    }
    public static boolean IsWithinLocation(World world, BlockPos pos, boolean isPiston) {
        IBlockState blockState = world.getBlockState(pos);
        return LocationStorage.isLocationGuarded(world, null, pos) || (!isPiston && world.provider.getDimension() == ConfigBLAdditions.configGeneral.DungeonDimensionID);
    }
}
