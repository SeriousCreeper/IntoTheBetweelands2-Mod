package com.seriouscreeper.bladditions.mixins.modsupport.bloodmagic;

import WayofTime.bloodmagic.alchemyArray.*;
import WayofTime.bloodmagic.client.render.alchemyArray.*;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.core.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.iface.ISigil;
import WayofTime.bloodmagic.item.types.ComponentTypes;
import WayofTime.bloodmagic.registry.ModRecipes;
import WayofTime.bloodmagic.util.Utils;
import com.joshiegemfinder.betweenlandsredstone.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import teamroots.embers.register.ItemRegister;
import thaumcraft.api.items.ItemsTC;
import thebetweenlands.common.item.misc.ItemMisc;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.ItemRegistry;

@Mixin(value = ModRecipes.class)
public class MixinModRecipes {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public static void addAlchemyArrayRecipes() {
        AlchemyArrayRecipeRegistry.registerRecipe(ComponentTypes.REAGENT_BINDING.getStack(), new ItemStack(ItemRegistry.OCTINE_SWORD), new AlchemyArrayEffectBinding("boundSword", Utils.setUnbreakable(new ItemStack(RegistrarBloodMagicItems.BOUND_SWORD))), new BindingAlchemyCircleRenderer());
        AlchemyArrayRecipeRegistry.registerRecipe(ComponentTypes.REAGENT_BINDING.getStack(), new ItemStack(ItemRegistry.OCTINE_AXE), new AlchemyArrayEffectBinding("boundAxe", Utils.setUnbreakable(new ItemStack(RegistrarBloodMagicItems.BOUND_AXE))));
        AlchemyArrayRecipeRegistry.registerRecipe(ComponentTypes.REAGENT_BINDING.getStack(), new ItemStack(ItemRegistry.OCTINE_PICKAXE), new AlchemyArrayEffectBinding("boundPickaxe", Utils.setUnbreakable(new ItemStack(RegistrarBloodMagicItems.BOUND_PICKAXE))));
        AlchemyArrayRecipeRegistry.registerRecipe(ComponentTypes.REAGENT_BINDING.getStack(), new ItemStack(ItemRegistry.OCTINE_SHOVEL), new AlchemyArrayEffectBinding("boundShovel", Utils.setUnbreakable(new ItemStack(RegistrarBloodMagicItems.BOUND_SHOVEL))));
        AlchemyArrayRecipeRegistry.registerRecipe(ComponentTypes.REAGENT_BINDING.getStack(), new ItemStack(ItemRegister.ASHEN_CLOAK_HEAD), new AlchemyArrayEffectBinding("livingHelmet", new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_HELMET)));
        AlchemyArrayRecipeRegistry.registerRecipe(ComponentTypes.REAGENT_BINDING.getStack(), new ItemStack(ItemRegister.ASHEN_CLOAK_CHEST), new AlchemyArrayEffectBinding("livingChest", new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_CHEST)));
        AlchemyArrayRecipeRegistry.registerRecipe(ComponentTypes.REAGENT_BINDING.getStack(), new ItemStack(ItemRegister.ASHEN_CLOAK_LEGS), new AlchemyArrayEffectBinding("livingLegs", new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_LEGGINGS)));
        AlchemyArrayRecipeRegistry.registerRecipe(ComponentTypes.REAGENT_BINDING.getStack(), new ItemStack(ItemRegister.ASHEN_CLOAK_BOOTS), new AlchemyArrayEffectBinding("livingBoots", new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_BOOTS)));
        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(ItemsTC.brain), new ItemStack(ItemsTC.brain), new AlchemyArrayEffectAttractor("attractor"), new AttractorAlchemyCircleRenderer());
        AlchemyArrayRecipeRegistry.registerRecipe(ItemMisc.EnumItemMisc.DRAGONFLY_WING.create(1), new ItemStack(ModItems.SCABYST_DUST), new AlchemyArrayEffectMovement("movement"), new StaticAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/MovementArray.png")));
        AlchemyArrayRecipeRegistry.registerRecipe(ItemMisc.EnumItemMisc.DRAGONFLY_WING.create(1), new ItemStack(BlockRegistry.WISP), new AlchemyArrayEffectUpdraft("updraft"), new AttractorAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/UpdraftArray.png")));
        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(ModItems.SCABYST_SLIME_1), new ItemStack(ModItems.SCABYST_DUST), new AlchemyArrayEffectBounce("bounce"), new SingleAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/BounceArray.png")));
        //AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Items.COAL), new ItemStack(Items.REDSTONE), new AlchemyArrayEffectFurnaceFuel("furnace"), new LowAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/FurnaceArray.png")));
        //AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Items.ARROW), new ItemStack(Items.FEATHER), new AlchemyArrayEffectSkeletonTurret("skeletonTurret"), new DualAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SkeletonTurret1.png"), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SkeletonTurret2.png")));
        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(ItemRegistry.SHIMMER_STONE), new ItemStack(ModItems.SCABYST_BLOCK), new AlchemyArrayEffectTeleport("teleport"), new StaticAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/teleportation.png")));
        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(ItemRegistry.WEEDWOOD_BOW), new ItemStack(ItemRegistry.ANGLER_TOOTH_ARROW), new AlchemyArrayEffectArrowTurret("turret"), new TurretAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SkeletonTurret1.png")));
        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(ModItems.SCABYST_BLOCK), new ItemStack(epicsquid.roots.init.ModItems.cloud_berry), new AlchemyArrayEffectLaputa("laputa"), new AttractorAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/shardoflaputa.png")));
        //AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Blocks.COBBLESTONE), new ItemStack(Items.IRON_INGOT), new AlchemyArrayEffectSpike("spike"), new LowStaticAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/spikearray.png")));
        //AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Blocks.REDSTONE_BLOCK), new ItemStack(Items.REDSTONE), new AlchemyArrayEffectMobSacrifice("mobSacrifice"), new MobSacrificeAlchemyCircleRenderer());
        AlchemyArrayRecipeRegistry.registerRecipe(ComponentTypes.REAGENT_FAST_MINER.getStack(), new ItemStack(ItemRegistry.BONE_PICKAXE), new AlchemyArrayEffectSigil("fastMiner", (ISigil)RegistrarBloodMagicItems.SIGIL_FAST_MINER), new SingleAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/FastMinerSigil.png")));
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static void addPotionRecipes() {
    }
}
