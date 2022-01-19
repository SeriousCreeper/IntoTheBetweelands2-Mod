package com.seriouscreeper.bladditions.mixins.modsupport.eyesinthedarkness;

import com.google.common.collect.Sets;
import gigaherz.eyes.ConfigData;
import gigaherz.eyes.EyesInTheDarkness;
import gigaherz.eyes.entity.EntityEyes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(value = EyesInTheDarkness.class, remap = false)
public class MixinEyesInTheDarkness {
    /**
     * @author SC
     */
    @Overwrite
    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        int entityId = 1;

        EntityEntryBuilder<Entity> builder = EntityEntryBuilder.create().name("eyes")
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
                        System.out.println("BIOME: " + b.getBiomeName());
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
}
