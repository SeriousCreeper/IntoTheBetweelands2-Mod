package com.seriouscreeper.bladditions.capability;

import com.codetaylor.mc.pyrotech.modules.tech.basic.potion.PotionFocused;
import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import com.seriouscreeper.bladditions.potion.PotionThaumcraftResearch;
import com.seriouscreeper.bladditions.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import thaumcraft.common.lib.utils.EntityUtils;
import thebetweenlands.api.environment.IEnvironmentEvent;
import thebetweenlands.common.entity.mobs.EntityGreebling;
import thebetweenlands.common.world.storage.BetweenlandsWorldStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class WellnessCapability {
    @CapabilityInject(WellnessCapability.class)
    public static Capability<WellnessCapability> INSTANCE;

    private float wellness = 1;

    public float GetWellness() {
        return wellness;
    }

    public void SetWellness(float wellness) {
        this.wellness = wellness;
    }

    public void ResetWellness() {
        wellness = 1;
    }

    public static float CalculateWellness(EntityPlayer player) {
        float wellnessBonus = 1;
        Collection<PotionEffect> effects = player.getActivePotionEffects();

        // greebling nearby
        // well rested buff from pyrotech
        // bl events affecting it in different ways?
        // bookshelf
        // maybe certain blocks should affect different research?
        // like cauldron nearby improves the alchemy one

        if(player.isRiding()) {
            wellnessBonus *= ConfigBLAdditions.configTea.SittingBonus;
        }

        // Find greebling nearby
        List<Entity> l = EntityUtils.getEntitiesInRange(player.world, player.getPosition(), null, Entity.class, 20.0D);

        if (!l.isEmpty()) {
            for (Entity e : l) {
                if(e instanceof EntityGreebling) {
                    wellnessBonus *= ConfigBLAdditions.configTea.GreeblingBonus;
                }
            }
        }

        int blockSearchRadius = 8;

        List<Block> blocksApplied = new ArrayList<>();

        for(int y = -1; y <= 1; y++) {
            for(int x = -blockSearchRadius; x <= blockSearchRadius; x++) {
                for(int z = -blockSearchRadius; z <= blockSearchRadius; z++) {
                    BlockPos playerPos = new BlockPos(Math.floor(player.posX), player.posY, Math.floor(player.posZ));
                    IBlockState state = player.world.getBlockState(playerPos.add(x, y, z));
                    Block block = state.getBlock();

                    for(Block wellnessBlock : CommonProxy.WELLNESS_BLOCKS.keySet()) {
                        if(wellnessBlock == block) {
                            List<PotionThaumcraftResearch.RESEARCH_CATEGORY> researchBonuses = CommonProxy.WELLNESS_BLOCKS.get(block);

                            if(researchBonuses != null) {
                                if(!blocksApplied.contains(block)) {
                                    wellnessBonus *= ConfigBLAdditions.configTea.NearbyBlocksBonus;
                                    blocksApplied.add(block);
                                }

                                break;
                            }
                        }
                    }
                }
            }
        }

        // Pyrotech buff bonus
        for(PotionEffect effect : effects) {
            if(effect.getPotion() instanceof PotionFocused) {
                wellnessBonus *= ConfigBLAdditions.configTea.FocusedBuffBonus;
                break;
            }
        }

        // BL Events
        BetweenlandsWorldStorage storage = BetweenlandsWorldStorage.forWorld(player.world);

        if (storage != null) {
            Map<String, Float> eventBonuses = ConfigBLAdditions.parseBLEvents();

            List<IEnvironmentEvent> activeEvents = storage.getEnvironmentEventRegistry().getActiveEvents();

            for(IEnvironmentEvent event : activeEvents) {
                String eventName = event.getEventName().getPath();

                if(eventBonuses.containsKey(eventName)) {
                    wellnessBonus *= eventBonuses.get(eventName);
                }
            }
        }

        return wellnessBonus;
    }

    public void CopyCapability(WellnessCapability oldCap) {
        wellness = oldCap.wellness;
    }

    public static class WellnessCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound> {
        private WellnessCapability capability = WellnessCapability.INSTANCE.getDefaultInstance();

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing enumFacing) {
            return capability == WellnessCapability.INSTANCE;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing enumFacing) {
            return capability == INSTANCE ? INSTANCE.<T> cast(this.capability) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return this.capability == null ? new NBTTagCompound() : (NBTTagCompound)WellnessCapability.INSTANCE.getStorage().writeNBT(WellnessCapability.INSTANCE, this.capability, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbtTagCompound) {
            if(this.capability == null) {
                this.capability = (WellnessCapability) WellnessCapability.INSTANCE.getDefaultInstance();
            }

            WellnessCapability.INSTANCE.getStorage().readNBT(WellnessCapability.INSTANCE, this.capability, null, nbtTagCompound);
        }
    }

    public static class WellnessCapabilityFactory implements Callable<WellnessCapability> {

        @Override
        public WellnessCapability call() throws Exception {
            return new WellnessCapability();
        }
    }

    public static class WellnessCapabilityStorage implements Capability.IStorage<WellnessCapability> {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<WellnessCapability> capability, WellnessCapability wellnessCapability, EnumFacing enumFacing) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setFloat("wellness", wellnessCapability.wellness);

            return tag;
        }

        @Override
        public void readNBT(Capability<WellnessCapability> capability, WellnessCapability wellnessCapability, EnumFacing enumFacing, NBTBase nbtBase) {
            NBTTagCompound tag = ((NBTTagCompound)nbtBase);
            wellnessCapability.wellness = tag.hasKey("wellness") ? tag.getFloat("wellness") : 1;
        }
    }
}
