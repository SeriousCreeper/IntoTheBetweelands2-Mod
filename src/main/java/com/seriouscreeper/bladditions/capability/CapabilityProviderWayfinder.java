package com.seriouscreeper.bladditions.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import thecodex6824.thaumicaugmentation.api.augment.CapabilityAugment;
import thecodex6824.thaumicaugmentation.api.augment.IAugment;
import thecodex6824.thaumicaugmentation.api.impetus.CapabilityImpetusStorage;
import thecodex6824.thaumicaugmentation.api.impetus.ImpetusStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityProviderWayfinder implements ICapabilitySerializable<NBTTagCompound> {
    private ImpetusStorage energy;

    public CapabilityProviderWayfinder(ImpetusStorage e) {
        this.energy = e;
    }

    public void deserializeNBT(NBTTagCompound nbt) {
        this.energy.deserializeNBT(nbt.getCompoundTag("energy"));
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("energy", this.energy.serializeNBT());
        return tag;
    }

    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityImpetusStorage.IMPETUS_STORAGE;
    }

    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityImpetusStorage.IMPETUS_STORAGE ? CapabilityImpetusStorage.IMPETUS_STORAGE.cast(this.energy) : null;
    }
}
