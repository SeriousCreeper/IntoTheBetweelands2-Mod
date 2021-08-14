package com.seriouscreeper.bladditions.items;

import com.seriouscreeper.bladditions.BLAdditions;
import com.seriouscreeper.bladditions.proxy.CommonProxy;
import growthcraft.milk.shared.init.GrowthcraftMilkFluids;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import thebetweenlands.client.tab.BLCreativeTabs;
import thebetweenlands.common.registries.ItemRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class PatchedItemDentrothystFluidVial extends UniversalBucket implements ItemRegistry.IMultipleItemModelDefinition {
    public static final List<Fluid> allowedFluids = new ArrayList<Fluid>() {
    };


    public PatchedItemDentrothystFluidVial() {
        super(250, ItemStack.EMPTY, true);
        this.setHasSubtypes(true);
        this.setMaxStackSize(16);
        this.setMaxDamage(0);
        this.setCreativeTab(BLCreativeTabs.HERBLORE);
        setRegistryName(BLAdditions.MODID, "dentrothyst_fluid_vial");

        allowedFluids.add(GrowthcraftMilkFluids.butterMilk.getFluid());
        allowedFluids.add(GrowthcraftMilkFluids.milk.getFluid());
        allowedFluids.add(GrowthcraftMilkFluids.condensedMilk.getFluid());
        allowedFluids.add(GrowthcraftMilkFluids.skimMilk.getFluid());
        allowedFluids.add(GrowthcraftMilkFluids.cream.getFluid());
        allowedFluids.add(GrowthcraftMilkFluids.curds.getFluid());
        allowedFluids.add(GrowthcraftMilkFluids.rennet.getFluid());
        allowedFluids.add(GrowthcraftMilkFluids.whey.getFluid());
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) {
            nbt = new NBTTagCompound();
        }

        nbt.setTag("Fluid", new NBTTagCompound());
        stack.setTagCompound(nbt);
    }

    @Override
    public Map<Integer, ResourceLocation> getModels() {
        Map<Integer, ResourceLocation> models = new HashMap();
        models.put(0, new ResourceLocation("thebetweenlands:dentrothyst_fluid_vial" + "_green"));
        models.put(1, new ResourceLocation("thebetweenlands:dentrothyst_fluid_vial" + "_orange"));
        return models;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        FluidStack fluidStack = this.getFluid(stack);
        return fluidStack != null ? 1 : super.getItemStackLimit(stack);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        return new PatchedItemDentrothystFluidVial.DentrothystFluidVialFluidHandler(stack, this.getCapacity());
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return stack.getMetadata() >= 2 ? this.getTranslationKey() + ".unknown" : this.getTranslationKey() + (stack.getMetadata() == 0 ? "_green" : "_orange");
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        FluidStack fluidStack = this.getFluid(stack);
        String unlocName = this.getEmpty(stack).getTranslationKey();
        if (fluidStack == null) {
            return I18n.translateToLocal(unlocName + ".name").trim();
        } else {
            String fluidUnlocKey = unlocName + "." + fluidStack.getUnlocalizedName() + ".name";
            return I18n.canTranslate(fluidUnlocKey) ? I18n.translateToLocal(fluidUnlocKey).trim() : I18n.translateToLocalFormatted(this.getEmpty(stack).getTranslationKey() + ".filled.name", new Object[]{fluidStack.getFluid().getRarity(fluidStack).color + fluidStack.getLocalizedName() + TextFormatting.WHITE});
        }
    }

    @Override
    public void getSubItems(@Nullable CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            for(int i = 0; i < 2; ++i) {
                Iterator var4 = FluidRegistry.getRegisteredFluids().values().iterator();

                while(var4.hasNext()) {
                    Fluid fluid = (Fluid)var4.next();
                    FluidStack fs = new FluidStack(fluid, this.getCapacity());
                    ItemStack stack = new ItemStack(this, 1, i);
                    IFluidHandlerItem fluidHandler = (IFluidHandlerItem)stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, (EnumFacing)null);
                    if (fluidHandler != null && fluidHandler.fill(fs, true) == fs.amount) {
                        ItemStack filled = fluidHandler.getContainer();
                        subItems.add(filled);
                    }
                }
            }

        }
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return !this.getEmpty(stack).isEmpty();
    }

    @Nonnull
    @Override
    public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {
        return !this.getEmpty(itemStack).isEmpty() ? this.getEmpty(itemStack).copy() : super.getContainerItem(itemStack);
    }

    @Nullable
    @Override
    public FluidStack getFluid(ItemStack container) {
        NBTTagCompound tagCompound = container.getTagCompound();
        return tagCompound != null && tagCompound.hasKey("Fluid") ? FluidStack.loadFluidStackFromNBT(tagCompound.getCompoundTag("Fluid")) : null;
    }

    public ItemStack getEmpty(ItemStack stack) {
        return stack.getMetadata() == 1 ? new ItemStack(CommonProxy.DENTROTHYST_VIAL, stack.getCount(), 2) : new ItemStack(CommonProxy.DENTROTHYST_VIAL, stack.getCount(), 0);
    }

    public ItemStack withFluid(int meta, Fluid fluid) {
        FluidStack fs = new FluidStack(fluid, this.getCapacity());
        ItemStack stack = new ItemStack(this, 1, meta);
        IFluidHandlerItem fluidHandler = (IFluidHandlerItem)stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, (EnumFacing)null);
        fluidHandler.fill(fs, true);
        return fluidHandler.getContainer();
    }

    public boolean canFillWith(ItemStack stack, FluidStack fluid) {
        return allowedFluids.contains(fluid.getFluid());
    }

    private static final class DentrothystFluidVialFluidHandler extends FluidHandlerItemStackSimple {
        public DentrothystFluidVialFluidHandler(ItemStack container, int capacity) {
            super(container, capacity);
        }

        public boolean canFillFluidType(FluidStack fluid) {
            return ((PatchedItemDentrothystFluidVial)this.container.getItem()).canFillWith(this.container, fluid);
        }

        @Nullable
        public FluidStack getFluid() {
            return this.container.getItem() != CommonProxy.DENTROTHYST_VIAL ? ((PatchedItemDentrothystFluidVial)this.container.getItem()).getFluid(this.container) : null;
        }

        protected void setContainerToEmpty() {
            this.container = ((PatchedItemDentrothystFluidVial)this.container.getItem()).getEmpty(this.container).copy();
        }
    }
}
