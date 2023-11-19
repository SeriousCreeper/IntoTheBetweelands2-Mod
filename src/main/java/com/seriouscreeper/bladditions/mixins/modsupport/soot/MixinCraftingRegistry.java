package com.seriouscreeper.bladditions.mixins.modsupport.soot;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import soot.Registry;
import soot.brewing.*;
import soot.brewing.deliverytypes.DeliveryBlast;
import soot.compat.jei.ExtraRecipeInfo;
import soot.recipe.*;
import soot.recipe.breweffects.*;
import soot.tile.TileEntityStillBase;
import soot.util.FluidUtil;
import soot.util.MiscUtil;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;
import teamroots.embers.recipe.FluidMixingRecipe;
import teamroots.embers.recipe.ItemMeltingRecipe;
import teamroots.embers.recipe.RecipeRegistry;
import mezz.jei.util.Translator;
import thaumcraft.api.items.ItemsTC;
import thebetweenlands.common.item.misc.ItemMisc;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.ItemRegistry;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(value = CraftingRegistry.class)
public class MixinCraftingRegistry {
    @Shadow public static ArrayList<RecipeStill> stillRecipes = new ArrayList();
    @Shadow public static ArrayList<CatalystInfo> stillCatalysts = new ArrayList();

    /**
     * @author SC
     */
    @Overwrite
    private static void initAlcoholRecipes() {
        FluidUtil.registerModifier((new FluidPotionModifier("ale", 0.0F, FluidModifier.EnumType.PRIMARY, FluidModifier.EffectType.POSITIVE, Registry.POTION_ALE, 4) {
            public EssenceStack toEssence(float amount) {
                return amount > 0.0F ? new EssenceStack(EssenceType.GLASS, (int)Math.ceil((double)(amount / 400.0F))) : super.toEssence(amount);
            }
        }).setFormatType("name_only"));
        FluidUtil.registerModifier((new FluidPotionModifier("stoutness", 0.0F, FluidModifier.EnumType.PRIMARY, FluidModifier.EffectType.POSITIVE, Registry.POTION_STOUTNESS, 4) {
            public EssenceStack toEssence(float amount) {
                return super.toEssence(amount);
            }
        }).setFormatType("name_only"));
        FluidUtil.registerModifier((new FluidPotionModifier("inner_fire", 0.0F, FluidModifier.EnumType.PRIMARY, FluidModifier.EffectType.POSITIVE, Registry.POTION_INNER_FIRE, 2) {
            public EssenceStack toEssence(float amount) {
                return super.toEssence(amount);
            }
        }).setFormatType("name_only"));
        FluidUtil.registerModifier((new FluidPotionModifier("inspiration", 0.0F, FluidModifier.EnumType.PRIMARY, FluidModifier.EffectType.POSITIVE, Registry.POTION_INSPIRATION, 3) {
            public EssenceStack toEssence(float amount) {
                return super.toEssence(amount);
            }
        }).setFormatType("name_only"));
        FluidUtil.registerModifier((new FluidPotionModifier("fire_lung", 0.0F, FluidModifier.EnumType.PRIMARY, FluidModifier.EffectType.POSITIVE, Registry.POTION_FIRE_LUNG, 2) {
            public EssenceStack toEssence(float amount) {
                return super.toEssence(amount);
            }
        }).setFormatType("name_only"));
        FluidUtil.registerModifier((new FluidPotionModifier("snowpoff", 0.0F, FluidModifier.EnumType.PRIMARY, FluidModifier.EffectType.POSITIVE, Registry.POTION_SNOWPOFF, 3) {
            public EssenceStack toEssence(float amount) {
                return super.toEssence(amount);
            }
        }).setFormatType("name_only"));
        FluidUtil.registerModifier((new FluidPotionModifier("lifedrinker", 0.0F, FluidModifier.EnumType.SECONDARY, FluidModifier.EffectType.POSITIVE, Registry.POTION_LIFEDRINKER, 0) {
            public EssenceStack toEssence(float amount) {
                return amount > 0.0F ? new EssenceStack(EssenceType.LIFEDRINKER, (int)Math.ceil((double)(amount / 400.0F))) : super.toEssence(amount);
            }
        }).setFormatType("name_only"));
        FluidUtil.registerModifier((new FluidPotionModifier("steadfast", 0.0F, FluidModifier.EnumType.SECONDARY, FluidModifier.EffectType.POSITIVE, Registry.POTION_STEADFAST, 0) {
            public EssenceStack toEssence(float amount) {
                return amount > 0.0F ? new EssenceStack(EssenceType.SPEED, (int)Math.ceil((double)(amount / 400.0F))) : super.toEssence(amount);
            }
        }).setFormatType("name_only"));
        FluidUtil.registerModifier((new FluidPotionModifier("experience_boost", 0.0F, FluidModifier.EnumType.SECONDARY, FluidModifier.EffectType.POSITIVE, Registry.POTION_EXPERIENCE_BOOST, 0) {
            public EssenceStack toEssence(float amount) {
                return amount > 0.0F ? new EssenceStack(EssenceType.EXPERIENCE, (int)Math.ceil((double)(amount / 400.0F))) : super.toEssence(amount);
            }
        }).setFormatType("name_only"));
        FluidUtil.registerModifier((new FluidPotionModifier("glass", 0.0F, FluidModifier.EnumType.SECONDARY, FluidModifier.EffectType.NEGATIVE, Registry.POTION_GLASS, 9) {
            public EssenceStack toEssence(float amount) {
                return amount > 0.0F ? new EssenceStack(EssenceType.GLASS, (int)Math.ceil((double)(amount / 400.0F))) : super.toEssence(amount);
            }
        }).setFormatType("name_only"));
        FluidUtil.registerModifier((new FluidPotionModifier("resistance", 0.0F, FluidModifier.EnumType.SECONDARY, FluidModifier.EffectType.POSITIVE, MobEffects.RESISTANCE, 2) {
            public EssenceStack toEssence(float amount) {
                return super.toEssence(amount);
            }
        }).setFormatType("name_only"));
        FluidUtil.registerModifier((new FluidPotionModifier("speed", 0.0F, FluidModifier.EnumType.SECONDARY, FluidModifier.EffectType.POSITIVE, MobEffects.SPEED, 3) {
            public EssenceStack toEssence(float amount) {
                return amount > 0.0F ? new EssenceStack(EssenceType.SPEED, (int)Math.ceil((double)(amount / 400.0F))) : super.toEssence(amount);
            }
        }).setFormatType("name_only"));
        FluidUtil.registerModifier((new FluidPotionModifier("slow", 0.0F, FluidModifier.EnumType.SECONDARY, FluidModifier.EffectType.NEGATIVE, MobEffects.SLOWNESS, 3) {
            public EssenceStack toEssence(float amount) {
                return amount > 0.0F ? new EssenceStack(EssenceType.SLOWNESS, (int)Math.ceil((double)(amount / 400.0F))) : super.toEssence(amount);
            }
        }).setFormatType("name_only"));
        FluidUtil.registerModifier((new FluidPotionModifier("regeneration", 0.0F, FluidModifier.EnumType.SECONDARY, FluidModifier.EffectType.POSITIVE, MobEffects.REGENERATION, 3) {
            public EssenceStack toEssence(float amount) {
                return amount > 0.0F ? new EssenceStack(EssenceType.REGENERATION, (int)Math.ceil((double)(amount / 400.0F))) : super.toEssence(amount);
            }
        }).setFormatType("name_only"));
        FluidUtil.registerModifier(new FluidModifier("viscosity", 1000.0F, FluidModifier.EnumType.TERTIARY, FluidModifier.EffectType.NEGATIVE) {
            public String getFormattedText(NBTTagCompound compound, Fluid fluid) {
                float value = this.getOrDefault(compound, fluid);
                return value > 1000.0F ? I18n.format("distilling.modifier.dial.slower_chugging", new Object[]{value}) : I18n.format("distilling.modifier.dial.faster_chugging", new Object[]{value});
            }

            public EssenceStack toEssence(float amount) {
                return amount > 1000.0F ? new EssenceStack(EssenceType.SLOWNESS, (int)Math.ceil((double)((amount - 1000.0F) / 200.0F))) : super.toEssence(amount);
            }
        });
        FluidUtil.registerModifier((new FluidModifier("light", 0.0F, FluidModifier.EnumType.TERTIARY, FluidModifier.EffectType.NEUTRAL)).setFormatType("name_only"));
        FluidUtil.registerModifier(new FluidModifier("health", 0.0F, FluidModifier.EnumType.TERTIARY, FluidModifier.EffectType.POSITIVE) {
            public void applyEffect(EntityLivingBase target, NBTTagCompound compound, Fluid fluid) {
                float value = this.getOrDefault(compound, fluid);
                if (value > 0.0F) {
                    target.heal(value);
                } else if (value < 0.0F) {
                    MiscUtil.damageWithoutInvulnerability(target, new DamageSource("acid"), value);
                }

            }

            public String getFormattedText(NBTTagCompound compound, Fluid fluid) {
                int value = (int)Math.ceil((double)(this.getOrDefault(compound, fluid) / 2.0F));
                DecimalFormat format = Embers.proxy.getDecimalFormat("embers.decimal_format.distilling.health");
                return I18n.format("distilling.modifier.dial.health", new Object[]{this.getLocalizedName(), format.format((long)value)});
            }

            public EssenceStack toEssence(float amount) {
                if (amount > 0.0F) {
                    return new EssenceStack(EssenceType.REGENERATION, (int)Math.ceil((double)(amount / 2.0F)));
                } else {
                    return amount < 0.0F ? new EssenceStack(EssenceType.DEATH, (int)Math.ceil((double)(amount / 10.0F))) : super.toEssence(amount);
                }
            }
        });
        FluidUtil.registerModifier(new FluidModifier("hunger", 0.0F, FluidModifier.EnumType.TERTIARY, FluidModifier.EffectType.POSITIVE) {
            public void applyEffect(EntityLivingBase target, NBTTagCompound compound, Fluid fluid) {
                if (target instanceof EntityPlayer) {
                    int hunger = (int)compound.getFloat("hunger");
                    float saturation = compound.getFloat("saturation");
                    ((EntityPlayer)target).getFoodStats().addStats(hunger, saturation);
                }

            }

            public String getFormattedText(NBTTagCompound compound, Fluid fluid) {
                int value = (int)Math.ceil((double)(this.getOrDefault(compound, fluid) / 2.0F));
                DecimalFormat format = Embers.proxy.getDecimalFormat("embers.decimal_format.distilling.hunger");
                return I18n.format("distilling.modifier.dial.hunger", new Object[]{this.getLocalizedName(), format.format((long)value)});
            }
        });
        FluidUtil.registerModifier((new FluidModifier("saturation", 0.0F, FluidModifier.EnumType.TERTIARY, FluidModifier.EffectType.POSITIVE)).setFormatType((String)null));
        FluidUtil.registerModifier(new FluidModifier("toxicity", 0.0F, FluidModifier.EnumType.TERTIARY, FluidModifier.EffectType.NEGATIVE) {
            public void applyEffect(EntityLivingBase target, NBTTagCompound compound, Fluid fluid) {
                float value = this.getOrDefault(compound, fluid);
                if (value > 0.0F) {
                    target.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, (int)value * 4));
                }

                if (value >= 50.0F) {
                    target.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, (int)value * 2));
                }

                if (value >= 100.0F) {
                    target.addPotionEffect(new PotionEffect(MobEffects.POISON, (int)value - 50));
                }

                if (value >= 200.0F) {
                    target.addPotionEffect(new PotionEffect(MobEffects.WITHER, (int)(value - 150.0F)));
                }

            }

            public EssenceStack toEssence(float amount) {
                if (amount > 200.0F) {
                    return new EssenceStack(EssenceType.WITHER, (int)Math.ceil((double)((amount - 200.0F) / 20.0F)));
                } else {
                    return amount > 0.0F ? new EssenceStack(EssenceType.POISON, (int)Math.ceil((double)(amount / 20.0F))) : EssenceStack.EMPTY;
                }
            }
        });
        FluidUtil.registerModifier(new FluidModifier("sweetness", 0.0F, FluidModifier.EnumType.TERTIARY, FluidModifier.EffectType.POSITIVE) {
            public void applyEffect(EntityLivingBase target, NBTTagCompound compound, Fluid fluid) {
                float value = this.getOrDefault(compound, fluid);
                if (value >= 50.0F) {
                    target.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, (int)value * 5));
                }

            }

            public EssenceStack toEssence(float amount) {
                if (amount >= 50.0F) {
                    return new EssenceStack(EssenceType.REGENERATION, (int)Math.ceil((double)((amount - 50.0F) / 10.0F)));
                } else {
                    return amount > 0.0F ? new EssenceStack(EssenceType.SWEET, (int)Math.ceil((double)(amount / 20.0F))) : EssenceStack.EMPTY;
                }
            }
        });
        FluidUtil.registerModifier(new FluidModifier("heat", 300.0F, FluidModifier.EnumType.TERTIARY, FluidModifier.EffectType.NEUTRAL) {
            public void applyEffect(EntityLivingBase target, NBTTagCompound compound, Fluid fluid) {
                float value = this.getOrDefault(compound, fluid);
                if (value > 400.0F) {
                    MiscUtil.damageWithoutInvulnerability(target, new DamageSource("scalding"), 2.0F);
                    target.playSound(SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE, 1.0F, 1.0F);
                    if (target instanceof EntityPlayer) {
                        ((EntityPlayer)target).sendStatusMessage(new TextComponentTranslation("message.scalding", new Object[0]), true);
                    }
                }

                if (value > 500.0F) {
                    target.setFire((int)((value - 500.0F) / 10.0F));
                }

            }

            public EssenceStack toEssence(float amount) {
                if (amount > 500.0F) {
                    return new EssenceStack(EssenceType.FIRE, (int)Math.ceil((double)((amount - 500.0F) / 20.0F)));
                } else {
                    return amount < 250.0F ? new EssenceStack(EssenceType.ICE, (int)Math.ceil((double)((250.0F - amount) / 20.0F))) : EssenceStack.EMPTY;
                }
            }
        });
        FluidUtil.registerModifier((new FluidModifier("volume", 0.0F, FluidModifier.EnumType.TERTIARY, FluidModifier.EffectType.NEGATIVE) {
            public void applyEffect(EntityLivingBase target, NBTTagCompound compound, Fluid fluid) {
                float value = this.getOrDefault(compound, fluid);
                if (target.getRNG().nextDouble() * 100.0D < (double)value) {
                    target.addPotionEffect(new PotionEffect(Registry.POTION_TIPSY, (int)value * 20));
                }

            }
        }).setFormatType("percent"));
        FluidUtil.registerModifier((new FluidModifier("concentration", 0.0F, FluidModifier.EnumType.TERTIARY, FluidModifier.EffectType.POSITIVE)).setFormatType("percent"));
        FluidUtil.registerModifier((new FluidModifier("duration", 1.0F, FluidModifier.EnumType.TERTIARY, FluidModifier.EffectType.POSITIVE)).setFormatType("multiplier"));
        FluidUtil.registerModifier(new FluidModifier("fuel", 0.0F, FluidModifier.EnumType.TERTIARY, FluidModifier.EffectType.NEUTRAL) {
            public String getFormattedText(NBTTagCompound compound, Fluid fluid) {
                int burntime = (int)this.getOrDefault(compound, fluid);
                return burntime > 0 ? super.getFormattedText(compound, fluid) : I18n.format("distilling.modifier.dial.fire_retardant", new Object[]{burntime});
            }
        });
        FluidUtil.registerModifier((new FluidModifier("alchemy_blast", 0.0F, FluidModifier.EnumType.TERTIARY, FluidModifier.EffectType.POSITIVE)).setFormatType("name_only"));
        FluidUtil.registerModifier(new FluidModifier("alchemy_blast_radius", 8.0F, FluidModifier.EnumType.TERTIARY, FluidModifier.EffectType.POSITIVE));
        CaskManager.register("alchemy_blast", (gauntlet, elixir, user, fluid) -> {
            float radius = FluidUtil.getModifier(fluid, "alchemy_blast_radius");
            return new DeliveryBlast(user, fluid, 8.0D, (double)radius);
        });
        Fluid boiling_wort = FluidRegistry.getFluid("boiling_wort");
        Fluid boiling_potato_juice = FluidRegistry.getFluid("boiling_potato_juice");
        Fluid boiling_beetroot_soup = FluidRegistry.getFluid("boiling_beetroot_soup");
        FluidUtil.setDefaultValue(boiling_beetroot_soup, "hunger", 6.0F);
        FluidUtil.setDefaultValue(boiling_beetroot_soup, "saturation", 0.6F);
        Fluid boiling_verdigris = FluidRegistry.getFluid("boiling_wormwood");
        FluidUtil.setDefaultValue(boiling_verdigris, "toxicity", 100.0F);
        Fluid ale = FluidRegistry.getFluid("dwarven_ale");
        FluidUtil.setDefaultValue(ale, "ale", 1200.0F);
        FluidUtil.setDefaultValue(ale, "volume", 20.0F);
        FluidUtil.setDefaultValue(ale, "fuel", 400.0F);
        Fluid inner_fire = FluidRegistry.getFluid("inner_fire");
        FluidUtil.setDefaultValue(inner_fire, "inner_fire", 1000.0F);
        FluidUtil.setDefaultValue(inner_fire, "heat", 600.0F);
        FluidUtil.setDefaultValue(inner_fire, "volume", 10.0F);
        FluidUtil.setDefaultValue(inner_fire, "fuel", 1600.0F);
        Fluid umber_ale = FluidRegistry.getFluid("umber_ale");
        Fluid vodka = FluidRegistry.getFluid("vodka");
        FluidUtil.setDefaultValue(vodka, "stoutness", 1600.0F);
        FluidUtil.setDefaultValue(vodka, "volume", 30.0F);
        FluidUtil.setDefaultValue(vodka, "fuel", 1200.0F);
        Fluid snowpoff = FluidRegistry.getFluid("snowpoff");
        FluidUtil.setDefaultValue(snowpoff, "snowpoff", 1000.0F);
        FluidUtil.setDefaultValue(snowpoff, "heat", 200.0F);
        FluidUtil.setDefaultValue(snowpoff, "volume", 20.0F);
        FluidUtil.setDefaultValue(snowpoff, "fuel", -2000.0F);
        Fluid absinthe = FluidRegistry.getFluid("absinthe");
        FluidUtil.setDefaultValue(absinthe, "inspiration", 400.0F);
        FluidUtil.setDefaultValue(absinthe, "toxicity", 50.0F);
        FluidUtil.setDefaultValue(absinthe, "volume", 50.0F);
        FluidUtil.setDefaultValue(absinthe, "fuel", 800.0F);
        Fluid methanol = FluidRegistry.getFluid("methanol");
        FluidUtil.setDefaultValue(methanol, "fire_lung", 200.0F);
        FluidUtil.setDefaultValue(methanol, "toxicity", 10.0F);
        FluidUtil.setDefaultValue(methanol, "fuel", 2400.0F);
        ItemStack smallFern = new ItemStack(Blocks.TALLGRASS, 1, net.minecraft.block.BlockTallGrass.EnumType.FERN.getMeta());
        ItemStack bigFern = new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockDoublePlant.EnumPlantType.FERN.getMeta());
        RecipeRegistry.meltingRecipes.add(new ItemMeltingRecipe(new OreIngredient("cropWheat"), new FluidStack(boiling_wort, 100)));
        RecipeRegistry.meltingRecipes.add(new ItemMeltingRecipe(new OreIngredient("cropPotato"), new FluidStack(boiling_potato_juice, 50)));
        RecipeRegistry.meltingRecipes.add(new ItemMeltingRecipe(Ingredient.fromItem(Items.BEETROOT), new FluidStack(boiling_beetroot_soup, 50)));
        RecipeRegistry.meltingRecipes.add(new ItemMeltingRecipe(Ingredient.fromStacks(new ItemStack[]{smallFern}), new FluidStack(boiling_verdigris, 50)));
        RecipeRegistry.meltingRecipes.add(new ItemMeltingRecipe(Ingredient.fromStacks(new ItemStack[]{bigFern}), new FluidStack(boiling_verdigris, 100)));
        RecipeRegistry.mixingRecipes.add(new FluidMixingRecipe(new FluidStack[]{new FluidStack(ale, 4), FluidRegistry.getFluidStack("lava", 1)}, new FluidStack(inner_fire, 4)));
        stillRecipes.add((new RecipeStill(getRL("brew_ale"), new FluidStack(boiling_wort, 1), Ingredient.EMPTY, 0, new FluidStack(ale, 1))).setEssence(Lists.newArrayList(new EssenceStack[]{new EssenceStack(EssenceType.GLASS, 1)})));
        stillRecipes.add((new RecipeStill(getRL("brew_vodka"), new FluidStack(boiling_potato_juice, 3), Ingredient.EMPTY, 0, new FluidStack(vodka, 2))).setEssence(Lists.newArrayList(new EssenceStack[]{new EssenceStack(EssenceType.VILE, 1)})));
        stillRecipes.add((new RecipeStill(getRL("brew_snowpoff"), new FluidStack(vodka, 1), Ingredient.fromItem(Items.SNOWBALL), 1, new FluidStack(snowpoff, 1))).setEssence(Lists.newArrayList(new EssenceStack[]{new EssenceStack(EssenceType.ICE, 5)})));
        stillRecipes.add((new RecipeStill(getRL("brew_absinthe"), new FluidStack(boiling_verdigris, 1), Ingredient.fromItem(ItemRegistry.SAP_SPIT), 0, new FluidStack(absinthe, 1))).setEssence(Lists.newArrayList(new EssenceStack[]{new EssenceStack(EssenceType.POISON, 5), new EssenceStack(EssenceType.EXPERIENCE, 1)})));
        stillRecipes.add((new RecipeStill(getRL("brew_methanol"), (FluidStack)null, new OreIngredient("logWood"), 1, new FluidStack(methanol, 1))).setEssence(Lists.newArrayList(new EssenceStack[]{new EssenceStack(EssenceType.FIRE, 3), new EssenceStack(EssenceType.POISON, 1)})));
        stillRecipes.add((new RecipeStill(getRL("extract_lava"), new FluidStack(FluidRegistry.getFluid("octine"), 3), Ingredient.EMPTY, 1, new FluidStack(FluidRegistry.getFluid("octine"), 1))).setEssence(Lists.newArrayList(new EssenceStack[]{new EssenceStack(EssenceType.FIRE, 10)})));
        stillRecipes.add((new RecipeStill(getRL("extract_iron"), new FluidStack(FluidRegistry.getFluid("syrmorite"), 3), Ingredient.EMPTY, 1, new FluidStack(FluidRegistry.getFluid("syrmorite"), 2))).setEssence(Lists.newArrayList(new EssenceStack[]{new EssenceStack(EssenceType.EXTRACT, 15)})));
        ArrayList<Fluid> allSoups = new ArrayList();
        allSoups.add(boiling_beetroot_soup);
        ArrayList<Fluid> allAlcohols = new ArrayList();
        allAlcohols.add(ale);
        allAlcohols.add(vodka);
        allAlcohols.add(inner_fire);
        allAlcohols.add(umber_ale);
        allAlcohols.add(methanol);
        allAlcohols.add(absinthe);
        allAlcohols.add(snowpoff);
        ArrayList<Fluid> allDrinks = new ArrayList(allAlcohols);
        allDrinks.addAll(allSoups);
        allDrinks.add(boiling_verdigris);
        stillCatalysts.add(new CatalystInfo(Ingredient.fromStacks(new ItemStack[]{smallFern}), 250));
        stillCatalysts.add(new CatalystInfo(Ingredient.fromStacks(new ItemStack[]{bigFern}), 500));
        stillCatalysts.add(new CatalystInfo(new OreIngredient("dustSugar"), 100));
        stillCatalysts.add(new CatalystInfo(Ingredient.fromItem(Items.SNOWBALL), 250));
        stillCatalysts.add(new CatalystInfo(new OreIngredient("logWood"), 750));
        stillCatalysts.add(new CatalystInfo(Ingredient.fromItem(Registry.ESSENCE), 1000));
        stillCatalysts.add(new CatalystInfo(new OreIngredient("dustSulfur"), 100));
        stillCatalysts.add(new CatalystInfoSulfur());
        stillRecipes.add((new RecipeStillDoubleDistillation(getRL("modify_double_distill"), allAlcohols, Ingredient.EMPTY, 0) {
            public int getInputConsumed() {
                return 3;
            }
        }).addEffect(new EffectAdd("concentration", 10.0F, 120.0F, false)).addEffect(new EffectMultiply("concentration", 1.8F, 0.0F, 120.0F, false)).addEffect(new EffectMultiply("volume", 1.1F, false)).addEffect(new EffectLoss(3, 2)));
        stillRecipes.add((new RecipeStillModifier(getRL("modify_lifedrinker"), allAlcohols, Ingredient.fromItem(Items.GHAST_TEAR), 1)).addEffect(new EffectInfo("lifedrinker")).addEffect(new EffectAdd("lifedrinker", 600.0F, 18000.0F, true)).addEffect(new EffectMultiply("lifedrinker", 1.6F, -1.0F, 18000.0F, true)).addEffect(new EffectAdd("toxicity", 10.0F, false)));
        stillRecipes.add((new RecipeStillModifier(getRL("modify_steadfast"), allAlcohols, Ingredient.fromItem(Items.RABBIT_FOOT), 1)).addEffect(new EffectInfo("steadfast")).addEffect(new EffectAdd("steadfast", 600.0F, 18000.0F, true)).addEffect(new EffectMultiply("steadfast", 1.6F, -1.0F, 18000.0F, true)));
        stillRecipes.add((new RecipeStillModifier(getRL("modify_experience_boost"), allDrinks, Ingredient.fromItem(ItemRegistry.ROCK_SNOT_PEARL), 1)).addEffect(new EffectInfo("experience_boost")).addEffect(new EffectAdd("experience_boost", 600.0F, 18000.0F, true)).addEffect(new EffectMultiply("experience_boost", 1.6F, -1.0F, 18000.0F, true)).addEffect(new EffectAdd("toxicity", 10.0F, 50.0F, false)));
        stillRecipes.add((new RecipeStillModifier(getRL("modify_duration_bonus"), allAlcohols, new OreIngredient("dustRedstone"), 1)).addEffect(new EffectAdd("duration", 0.5F, 2.5F, false)).addEffect(new EffectAdd("toxicity", 5.0F, false)));
        stillRecipes.add((new RecipeStillModifier(getRL("modify_sweetness_bonus"), allDrinks, new OreIngredient("materialHoneycomb"), 1)).addEffect(new EffectAdd("sweetness", 15.0F, 80.0F, false)));
        stillRecipes.add((new RecipeStillModifier(getRL("modify_purify"), allDrinks, Ingredient.fromItem(ItemsTC.bathSalts), 1)).addEffect(new EffectMultiply("toxicity", 0.8F, 0.0F, 1.0F, false)).addEffect(new EffectAdd("toxicity", -20.0F, 0.0F, false)));
        stillRecipes.add(new RecipeStillModifier(getRL("modify_taint"), allDrinks, Ingredient.fromStacks(ItemMisc.EnumItemMisc.POISON_GLAND.create(1)), 1) {
            public void modifyOutput(TileEntityStillBase tile, FluidStack output) {
                NBTTagCompound compound = FluidUtil.createModifiers(output);
                Iterator var4 = FluidUtil.SORTED_MODIFIER_KEYS.iterator();

                while(true) {
                    String modifier;
                    FluidModifier.EffectType effectType;
                    float defaultValue;
                    float value;
                    do {
                        do {
                            if (!var4.hasNext()) {
                                return;
                            }

                            modifier = (String)var4.next();
                            effectType = FluidUtil.getEffectType(modifier);
                        } while(effectType == FluidModifier.EffectType.NEUTRAL);

                        defaultValue = FluidUtil.getDefault(modifier);
                        value = this.getModifierOrDefault(modifier, compound, output);
                    } while((!(value > defaultValue) || effectType != FluidModifier.EffectType.POSITIVE) && (!(value < defaultValue) || effectType != FluidModifier.EffectType.NEGATIVE));

                    compound.setFloat(modifier, defaultValue);
                }
            }

            public void modifyTooltip(List<String> tooltip) {
                super.modifyTooltip(tooltip);
                this.addModifier(tooltip, "erase_positive", false);
            }
        });
        stillRecipes.add((new RecipeStillModifier(getRL("modify_heal"), allDrinks, Ingredient.fromItem(ItemRegistry.WIGHT_HEART), 1)).addEffect(new EffectAdd("health", 4.0F, false)).addEffect(new EffectAdd("hunger", -3.0F, false)));
        stillRecipes.add((new RecipeStillModifier(getRL("modify_cool"), allDrinks, Ingredient.fromStacks(new ItemStack[]{new ItemStack(BlockRegistry.BLACK_ICE)}), 1)).addEffect(new EffectMultiply("heat", 0.5F, 200.0F, 1.0F, false)));
        stillRecipes.add(new RecipeStillModifierFood(getRL("soup_potato"), allSoups, new OreIngredient("cropPotato"), 1, 2, 0.3F));
        stillRecipes.add(new RecipeStillModifierFood(getRL("soup_carrot"), allSoups, new OreIngredient("cropCarrot"), 1, 1, 0.2F));
        stillRecipes.add((new RecipeStillModifierFood(getRL("soup_wheat"), allSoups, new OreIngredient("cropWheat"), 1, 4, 0.6F)).addEffect(new EffectInfo("thick_soup", TextFormatting.BLUE)).addEffect(new EffectAdd("viscosity", 1000.0F, true)));
        stillRecipes.add((new RecipeStillModifier(getRL("essence_sweet"), allDrinks, Ingredient.fromStacks(new ItemStack[]{Registry.ESSENCE.getStack(EssenceType.SWEET)}), 1)).addEffect(new EffectAdd("sweetness", 20.0F, false)));
        stillRecipes.add((new RecipeStillModifier(getRL("essence_poison"), allDrinks, Ingredient.fromStacks(new ItemStack[]{Registry.ESSENCE.getStack(EssenceType.POISON)}), 1)).addEffect(new EffectAdd("toxicity", 20.0F, false)));
        stillRecipes.add((new RecipeStillModifier(getRL("essence_wither"), allDrinks, Ingredient.fromStacks(new ItemStack[]{Registry.ESSENCE.getStack(EssenceType.WITHER)}), 1)).addEffect(new EffectInfo("wither", TextFormatting.RED)).addEffect(new EffectMax("toxicity", 200.0F, false)).addEffect(new EffectAdd("toxicity", 50.0F, false)));
        stillRecipes.add((new RecipeStillModifier(getRL("essence_death"), allDrinks, Ingredient.fromStacks(new ItemStack[]{Registry.ESSENCE.getStack(EssenceType.DEATH)}), 1)).addEffect(new EffectInfo("death", TextFormatting.RED)).addEffect(new EffectAdd("health", -10.0F, false)).addEffect(new EffectMin("health", -20.0F, true)));
        stillRecipes.add(new RecipeStillModifier(getRL("essence_vile"), allDrinks, Ingredient.fromStacks(new ItemStack[]{Registry.ESSENCE.getStack(EssenceType.VILE)}), 1) {
            public void modifyOutput(TileEntityStillBase tile, FluidStack output) {
                NBTTagCompound compound = FluidUtil.createModifiers(output);
                float health = this.getModifierOrDefault("health", compound, output);
                float toxicity = this.getModifierOrDefault("toxicity", compound, output);
                float toConvert = Math.max(0.0F, Math.min(toxicity, 20.0F));
                compound.setFloat("health", health - toConvert / 2.0F);
                compound.setFloat("toxicity", toxicity - toConvert);
            }

            public void modifyTooltip(List<String> tooltip) {
                super.modifyTooltip(tooltip);
                this.addModifier(tooltip, "vile", false);
            }
        });
        stillRecipes.add((new RecipeStillModifier(getRL("essence_fire"), allDrinks, Ingredient.fromStacks(new ItemStack[]{Registry.ESSENCE.getStack(EssenceType.FIRE)}), 1)).addEffect(new EffectAdd("heat", 40.0F, false)).addEffect(new EffectAdd("fuel", 500.0F, false)));
        stillRecipes.add((new RecipeStillModifier(getRL("essence_ice"), allDrinks, Ingredient.fromStacks(new ItemStack[]{Registry.ESSENCE.getStack(EssenceType.ICE)}), 1)).addEffect(new EffectAdd("heat", -20.0F, 0.0F, false)));
        stillRecipes.add((new RecipeStillModifier(getRL("essence_lifedrinker"), allDrinks, Ingredient.fromStacks(new ItemStack[]{Registry.ESSENCE.getStack(EssenceType.LIFEDRINKER)}), 1)).addEffect(new EffectInfo("lifedrinker")).addEffect(new EffectAdd("lifedrinker", 800.0F, true)));
        stillRecipes.add((new RecipeStillModifier(getRL("essence_glass"), allDrinks, Ingredient.fromStacks(new ItemStack[]{Registry.ESSENCE.getStack(EssenceType.GLASS)}), 1)).addEffect(new EffectInfo("glass")).addEffect(new EffectAdd("glass", 800.0F, true)));
        stillRecipes.add((new RecipeStillModifier(getRL("essence_speed"), allDrinks, Ingredient.fromStacks(new ItemStack[]{Registry.ESSENCE.getStack(EssenceType.SPEED)}), 1)).addEffect(new EffectInfo("speed")).addEffect(new EffectAdd("speed", 800.0F, true)));
        stillRecipes.add((new RecipeStillModifier(getRL("essence_slowness"), allDrinks, Ingredient.fromStacks(new ItemStack[]{Registry.ESSENCE.getStack(EssenceType.SLOWNESS)}), 1)).addEffect(new EffectInfo("slow")).addEffect(new EffectAdd("slow", 800.0F, true)));
        stillRecipes.add((new RecipeStillModifier(getRL("essence_regeneration"), allDrinks, Ingredient.fromStacks(new ItemStack[]{Registry.ESSENCE.getStack(EssenceType.REGENERATION)}), 1)).addEffect(new EffectInfo("regeneration")).addEffect(new EffectAdd("regeneration", 800.0F, true)));
        stillRecipes.add((new RecipeStillModifier(getRL("essence_experience"), allDrinks, Ingredient.fromStacks(new ItemStack[]{Registry.ESSENCE.getStack(EssenceType.EXPERIENCE)}), 1)).addEffect(new EffectInfo("experience_boost")).addEffect(new EffectAdd("experience_boost", 800.0F, true)));
        stillRecipes.add(new RecipeStillModifier(getRL("essence_extract"), allDrinks, Ingredient.fromStacks(new ItemStack[]{Registry.ESSENCE.getStack(EssenceType.EXTRACT)}), 1) {
            public void modifyOutput(TileEntityStillBase tile, FluidStack output) {
                NBTTagCompound compound = FluidUtil.createModifiers(output);
                String minModifier = this.getSmallestModifier(output, compound);
                if (minModifier != null) {
                    compound.setFloat(minModifier, FluidUtil.getDefault(minModifier));
                }

            }

            public List<EssenceStack> getEssenceOutput(TileEntityStillBase tile, FluidStack input, ItemStack catalyst) {
                NBTTagCompound compound = FluidUtil.createModifiers(input);
                String minModifier = this.getSmallestModifier(input, compound);
                if (minModifier != null) {
                    float value = this.getModifierOrDefault(minModifier, compound, input);
                    return Lists.newArrayList(new EssenceStack[]{FluidUtil.modifierToEssence(minModifier, value)});
                } else {
                    return Lists.newArrayList();
                }
            }

            /*
            public List<ExtraRecipeInfo> getExtraInfo() {
                return Lists.newArrayList(new ExtraRecipeInfo[]{new ExtraRecipeInfo(Lists.newArrayList(new ItemStack[]{Registry.ESSENCE.getStack(EssenceType.CHAOS)})) {
                    public void modifyTooltip(List<String> strings) {
                        strings.clear();
                        strings.add(Translator.translateToLocalFormatted("distilling.effect.decanter", new Object[0]));
                        strings.add(Translator.translateToLocalFormatted("distilling.effect.decanter.desc", new Object[0]));
                        strings.add(Translator.translateToLocalFormatted("distilling.effect.decanter.extract", new Object[0]));
                    }
                }});
            }
             */

            public void modifyTooltip(List<String> tooltip) {
                super.modifyTooltip(tooltip);
                this.addModifier(tooltip, "extract", true);
            }

            private String getSmallestModifier(FluidStack output, NBTTagCompound compound) {
                float minValue = 1.0F;
                String minModifier = null;
                Iterator var5 = FluidUtil.SORTED_MODIFIER_KEYS.iterator();

                while(var5.hasNext()) {
                    String modifier = (String)var5.next();
                    if (FluidUtil.getType(modifier) != FluidModifier.EnumType.PRIMARY) {
                        float value = this.getModifierOrDefault(modifier, compound, output) - FluidUtil.getDefault(modifier);
                        EssenceStack essence = FluidUtil.modifierToEssence(modifier, value);
                        if (!essence.isEmpty() && value != 0.0F && Math.abs(value) < minValue) {
                            minModifier = modifier;
                            minValue = Math.abs(value);
                        }
                    }
                }

                return minModifier;
            }
        });
    }

    @Shadow
    private static ResourceLocation getRL(String name) {
        return null;
    }
}
