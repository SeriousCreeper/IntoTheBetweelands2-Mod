package com.seriouscreeper.bladditions.items;

import com.seriouscreeper.bladditions.BLAdditions;
import com.seriouscreeper.bladditions.proxy.CommonProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import thebetweenlands.common.item.herblore.ItemDentrothystVial;

import javax.annotation.Nullable;

public class PatchedItemDentrothystVial extends ItemDentrothystVial {
    public PatchedItemDentrothystVial() {
        setRegistryName(BLAdditions.MODID, "dentrothyst_vial");
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new PatchedItemDentrothystVial.DentrothystVialFluidHandler(stack, CommonProxy.DENTROTHYST_FLUID_VIAL.getCapacity());
    }

    private static final class DentrothystVialFluidHandler extends FluidHandlerItemStackSimple {
        public DentrothystVialFluidHandler(ItemStack container, int capacity) {
            super(container, capacity);
        }

        public boolean canFillFluidType(FluidStack fluid) {
            return CommonProxy.DENTROTHYST_FLUID_VIAL.canFillWith(this.container, fluid);
        }

        @Nullable
        public FluidStack getFluid() {
            return null;
        }

        protected void setContainerToEmpty() {
        }

        public int fill(FluidStack resource, boolean doFill) {
            if (this.container.getItemDamage() == 1) {
                return 0;
            } else {
                ItemStack fluidVial = new ItemStack(CommonProxy.DENTROTHYST_FLUID_VIAL, 1, this.container.getItemDamage() == 2 ? 1 : 0);
                IFluidHandlerItem handler = FluidUtil.getFluidHandler(fluidVial);
                int filled = handler.fill(resource, doFill);
                if (filled > 0 && doFill) {
                    this.container = fluidVial;
                }

                return filled;
            }
        }
    }
}
