package com.seriouscreeper.bladditions.capability;

import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Callable;

public class PacifistCapability {
    @CapabilityInject(PacifistCapability.class)
    public static Capability<PacifistCapability> INSTANCE;

    private boolean isPacifist = true;
    private int pacifismBrokenCount = 0;
    private int forgivenessCount = 0;

    public boolean IsPacifist() {
        return isPacifist;
    }

    public int PacifistBrokenCount() {
        return pacifismBrokenCount;
    }

    public int ForgivenessCount() {
        return forgivenessCount;
    }

    public void CopyCapability(PacifistCapability oldCap) {
        isPacifist = oldCap.isPacifist;
        pacifismBrokenCount = oldCap.pacifismBrokenCount;
        forgivenessCount = oldCap.forgivenessCount;
    }

    public boolean SetPacifist(boolean isPacifist) {
        boolean brokePacifism = false;

        if(!isPacifist) {
            if(this.isPacifist) {
                // we broke our pacifism, notify player
                pacifismBrokenCount++;
                brokePacifism = true;
            }
        }

        this.isPacifist = isPacifist;

        return brokePacifism;
    }

    public boolean Forgive() {
        if(forgivenessCount >= ConfigBLAdditions.configGeneral.MaxPacifistForgiveness) {
            return false;
        }

        SetPacifist(true);

        forgivenessCount++;
        return true;
    }

    public static class PacifistCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound> {
        private PacifistCapability capability = PacifistCapability.INSTANCE.getDefaultInstance();

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing enumFacing) {
            return capability == PacifistCapability.INSTANCE;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing enumFacing) {
            return capability == INSTANCE ? INSTANCE.<T> cast(this.capability) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return this.capability == null ? new NBTTagCompound() : (NBTTagCompound)PacifistCapability.INSTANCE.getStorage().writeNBT(PacifistCapability.INSTANCE, this.capability, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbtTagCompound) {
            if(this.capability == null) {
                this.capability = (PacifistCapability) PacifistCapability.INSTANCE.getDefaultInstance();
            }

            PacifistCapability.INSTANCE.getStorage().readNBT(PacifistCapability.INSTANCE, this.capability, null, nbtTagCompound);
        }
    }

    public static class PacifistCapabilityFactory implements Callable<PacifistCapability> {

        @Override
        public PacifistCapability call() throws Exception {
            return new PacifistCapability();
        }
    }

    public static class PacifistCapabilityStorage implements Capability.IStorage<PacifistCapability> {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<PacifistCapability> capability, PacifistCapability pacifistCapability, EnumFacing enumFacing) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setBoolean("is_pacifist", pacifistCapability.isPacifist);
            tag.setInteger("broken_count", pacifistCapability.pacifismBrokenCount);
            tag.setInteger("forgiveness_count", pacifistCapability.forgivenessCount);

            return tag;
        }

        @Override
        public void readNBT(Capability<PacifistCapability> capability, PacifistCapability pacifistCapability, EnumFacing enumFacing, NBTBase nbtBase) {
            NBTTagCompound tag = ((NBTTagCompound)nbtBase);
            pacifistCapability.isPacifist = tag.hasKey("is_pacifist") ? tag.getBoolean("is_pacifist") : true;
            pacifistCapability.pacifismBrokenCount = tag.getInteger("broken_count");
            pacifistCapability.forgivenessCount = tag.getInteger("forgiveness_count");
        }
    }
}
