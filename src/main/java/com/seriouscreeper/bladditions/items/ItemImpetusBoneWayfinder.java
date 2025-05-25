package com.seriouscreeper.bladditions.items;

import com.seriouscreeper.bladditions.BLAdditions;
import com.seriouscreeper.bladditions.capability.CapabilityProviderWayfinder;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thecodex6824.thaumicaugmentation.api.impetus.CapabilityImpetusStorage;
import thecodex6824.thaumicaugmentation.api.impetus.IImpetusStorage;
import thecodex6824.thaumicaugmentation.api.impetus.ImpetusAPI;
import thecodex6824.thaumicaugmentation.api.impetus.ImpetusStorage;
import thecodex6824.thaumicaugmentation.common.util.ItemHelper;

import javax.annotation.Nullable;
import java.util.List;

public class ItemImpetusBoneWayfinder extends ItemCorruptedBoneWayfinder {
    public ItemImpetusBoneWayfinder(String registryName) {
        super(registryName);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        IImpetusStorage energy = (IImpetusStorage)stack.getCapability(CapabilityImpetusStorage.IMPETUS_STORAGE, (EnumFacing)null);

        if (energy == null) {
            return new ActionResult(EnumActionResult.PASS, stack);
        } else if(energy.getEnergyStored() < energy.getMaxEnergyStored()) {
            player.sendStatusMessage(new TextComponentTranslation("Impetus must be maxed out", new Object[0]), true);
            return new ActionResult(EnumActionResult.PASS, stack);
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entity) {
        if(!worldIn.isRemote) {
            IImpetusStorage energy = (IImpetusStorage)stack.getCapability(CapabilityImpetusStorage.IMPETUS_STORAGE, (EnumFacing)null);
            if(energy.getEnergyStored() < energy.getMaxEnergyStored()) {
                ((EntityPlayer)entity).sendStatusMessage(new TextComponentTranslation("Impetus must be maxed out", new Object[0]), true);

                return stack;
            }
        }

        return super.onItemUseFinish(stack, worldIn, entity);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        CapabilityProviderWayfinder provider = new CapabilityProviderWayfinder(new ImpetusStorage(1500L, 75L, 1L, 0L) {
            public long extractEnergy(long maxToExtract, boolean simulate) {
                long result = super.extractEnergy(maxToExtract, simulate);

                return result;
            }
        });

        if (nbt != null && nbt.hasKey("Parent", 10)) {
            provider.deserializeNBT(nbt.getCompoundTag("Parent"));
        }

        return provider;
    }


    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        IImpetusStorage energy = (IImpetusStorage)stack.getCapability(CapabilityImpetusStorage.IMPETUS_STORAGE, (EnumFacing)null);
        if (energy != null) {
            tooltip.add((new TextComponentTranslation("thaumicaugmentation.text.stored_energy", new Object[]{ImpetusAPI.getSuggestedChatColorForDescriptor(energy) + (new TextComponentTranslation(ImpetusAPI.getEnergyAmountDescriptor(energy), new Object[0])).getFormattedText()})).getFormattedText());
        }
    }

    @Override
    public NBTTagCompound getNBTShareTag(ItemStack stack) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("cap", new NBTTagCompound());

        NBTTagCompound energy = ItemHelper.tryMakeCapabilityTag(stack, CapabilityImpetusStorage.IMPETUS_STORAGE);

        if (energy != null) {
            tag.getCompoundTag("cap").setTag("energy", energy);
        }

        return tag;
    }

    @Override
    public void readNBTShareTag(ItemStack stack, @Nullable NBTTagCompound nbt) {
        if (nbt != null) {
            if (nbt.hasKey("cap", 10)) {
                ((ImpetusStorage)stack.getCapability(CapabilityImpetusStorage.IMPETUS_STORAGE, (EnumFacing)null)).deserializeNBT(nbt.getCompoundTag("cap").getCompoundTag("energy"));
            }
        }
    }
}
