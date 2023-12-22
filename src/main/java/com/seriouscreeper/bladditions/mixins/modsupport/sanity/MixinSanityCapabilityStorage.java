package com.seriouscreeper.bladditions.mixins.modsupport.sanity;

import com.seriouscreeper.bladditions.interfaces.ISanityExtraInfo;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.tiffit.sanity.SanityCapability;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = SanityCapability.SanityCapabilityStorage.class, remap = false)
public class MixinSanityCapabilityStorage implements Capability.IStorage<SanityCapability> {
    /**
     * @author SC
     * @reason
     */
    @Overwrite
    public NBTBase writeNBT(Capability<SanityCapability> capability, SanityCapability instance, EnumFacing side) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("can_increase",  ((ISanityExtraInfo)instance).getCanIncrease());
        tag.setBoolean("can_decrease",  ((ISanityExtraInfo)instance).getCanDecrease());
        tag.setFloat("sanity", instance.getSanityExact());
        return tag;
    }

    /**
     * @author SC
     * @reason
     */
    @Overwrite
    public void readNBT(Capability<SanityCapability> capability, SanityCapability instance, EnumFacing side, NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound)nbt;
        ISanityExtraInfo extraInfo = (ISanityExtraInfo)instance;
        extraInfo.setCanIncrease(tag.getBoolean("can_increase"));
        extraInfo.setCanDecrease(tag.getBoolean("can_decrease"));
        instance.setSanity(tag.getFloat("sanity"));
    }
}
