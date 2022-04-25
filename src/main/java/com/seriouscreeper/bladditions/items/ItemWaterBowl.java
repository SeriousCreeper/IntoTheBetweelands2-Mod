package com.seriouscreeper.bladditions.items;

import com.seriouscreeper.bladditions.BLAdditions;
import com.seriouscreeper.bladditions.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.common.item.misc.ItemMisc;
import thebetweenlands.common.registries.FluidRegistry;
import thebetweenlands.common.registries.ItemRegistry;

import javax.annotation.Nonnull;

public class ItemWaterBowl extends Item {
    public ItemWaterBowl() {
        setCreativeTab(CreativeTabs.BREWING);
        setRegistryName(new ResourceLocation(BLAdditions.MODID, "water_bowl"));
        setTranslationKey(BLAdditions.MODID + ".water_bowl");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Nonnull
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new Handler(stack);
    }

    private static class Handler extends FluidHandlerItemStackSimple.SwapEmpty {

        private Handler(ItemStack stack) {
            super(stack, new ItemStack(ItemMisc.EnumItemMisc.WEEDWOOD_BOWL.getItem()), 250);
            setFluid(new FluidStack(FluidRegistry.SWAMP_WATER, 250));
        }

        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return false;
        }

        @Override
        public boolean canDrainFluidType(FluidStack fluid) {
            return fluid.getFluid() == FluidRegistry.SWAMP_WATER;
        }
    }
}
