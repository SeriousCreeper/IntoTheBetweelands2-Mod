package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.registries.FluidRegistry;
import thebetweenlands.common.registries.LootTableRegistry;
import thebetweenlands.common.tile.FluidTankTile;
import thebetweenlands.common.tile.TileEntityBasicInventory;
import thebetweenlands.common.tile.TileEntityWaterFilter;

import java.util.Random;

@Mixin(value = TileEntityWaterFilter.class, remap = false)
public class MixinTileEntityWaterFilter extends TileEntityBasicInventory implements ITickable {
    @Shadow
    public FluidTankTile tank = new FluidTankTile(4000);

    public MixinTileEntityWaterFilter(int invSize, String name) {
        super(invSize, name);
    }

    @Shadow
    public FluidStack getResultFluid(FluidStack fluid) {
        return null;
    }

    @Shadow
    public boolean hasFilter() {
        return false;
    }

    @Shadow
    public ItemStack chooseRandomItemFromLootTable(ResourceLocation resourceLocation) {
        return null;
    }

    @Shadow
    protected ResourceLocation getStagnantWaterLootTable() {
        return LootTableRegistry.FILTERED_STAGNANT_WATER;
    }

    @Shadow
    protected ResourceLocation getSwampWaterLootTable() {
        return LootTableRegistry.FILTERED_SWAMP_WATER;
    }

    /**
     * @author SC
     */
    @Overwrite
    private void addByProductRandom(Random rand, Fluid fluid) {
        if (rand.nextInt(ConfigBLAdditions.configGeneral.WaterFilterLootChance) == 0) {
            ItemStack stack = ItemStack.EMPTY;
            if (fluid == FluidRegistry.STAGNANT_WATER) {
                stack = this.chooseRandomItemFromLootTable(this.getStagnantWaterLootTable());
            }

            if (fluid == FluidRegistry.SWAMP_WATER) {
                stack = this.chooseRandomItemFromLootTable(this.getSwampWaterLootTable());
            }

            for(int slot = 1; slot < 5 && !this.inventoryHandler.insertItem(slot, stack, false).isEmpty(); ++slot) {
            }
        }
    }

    @Shadow
    private void damageFilter(int damage) {
    }


    @Shadow
    public void setFluidAnimation(boolean showFluid) {
    }

    @Shadow
    public boolean getFluidAnimation() {
        return false;
    }

    @Shadow
    public void markForUpdate() {
    }


    /**
     * @author SC
     */
    @Override
    public void update() {
        if (!this.getWorld().isRemote && this.getWorld().getTotalWorldTime() % ConfigBLAdditions.configGeneral.WaterFilterSpeed == 0L) {
            EnumFacing facing = EnumFacing.DOWN;
            TileEntity tileToFill = this.getWorld().getTileEntity(this.pos.offset(facing));
            if (tileToFill != null && tileToFill.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite())) {
                IFluidHandler recepticle = (IFluidHandler)tileToFill.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
                IFluidTankProperties[] tankProperties = recepticle.getTankProperties();
                if (tankProperties != null) {
                    IFluidTankProperties[] var5 = tankProperties;
                    int var6 = tankProperties.length;

                    for(int var7 = 0; var7 < var6; ++var7) {
                        IFluidTankProperties properties = var5[var7];
                        if (properties != null && (properties.canFill() || properties.getCapacity() > 0)) {
                            FluidStack tankFluid = this.tank.drain(ConfigBLAdditions.configGeneral.WaterFilterAmount, false);
                            if (tankFluid != null) {
                                int filled = recepticle.fill(this.getResultFluid(tankFluid), true);
                                FluidStack drained = this.tank.drain(filled, true);
                                if (drained != null && this.hasFilter()) {
                                    if (drained.amount >= ConfigBLAdditions.configGeneral.WaterFilterAmount) {
                                        this.addByProductRandom(this.getWorld().rand, drained.getFluid());
                                    }

                                    this.damageFilter(1);
                                }

                                if (!this.getFluidAnimation()) {
                                    this.setFluidAnimation(true);
                                }

                                this.markForUpdate();
                            } else if ((tankFluid == null || tankFluid.amount == 0) && this.getFluidAnimation()) {
                                this.setFluidAnimation(false);
                                this.markForUpdate();
                            }
                        }
                    }
                }
            }
        }

        if (!this.getWorld().isRemote && this.tank.getFluid() == null && this.getFluidAnimation()) {
            this.setFluidAnimation(false);
            this.markForUpdate();
        }
    }
}
