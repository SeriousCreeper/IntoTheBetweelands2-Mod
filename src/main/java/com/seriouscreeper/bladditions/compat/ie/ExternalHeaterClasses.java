package com.seriouscreeper.bladditions.compat.ie;

import blusunrize.immersiveengineering.api.tool.ExternalHeaterHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import thebetweenlands.common.block.container.BlockBLFurnace;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.tile.TileEntityAbstractBLFurnace;


public class ExternalHeaterClasses {
    public static class HeaterBLFurnace extends ExternalHeaterHandler.HeatableAdapter<TileEntityAbstractBLFurnace> {
        public HeaterBLFurnace() {
        }

        boolean canCook(TileEntityAbstractBLFurnace tileEntity) {
            int furnaceAmount = tileEntity.getSizeInventory() / 4;

            for(int i = 0; i < furnaceAmount; i++) {
                TileEntityAbstractBLFurnace.FurnaceData furnaceData = tileEntity.getFurnaceData(i);
                ItemStack input = tileEntity.getStackInSlot(furnaceData.getInputSlot());

                if (input.isEmpty()) {
                    continue;
                } else {
                    ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input);
                    if (output.isEmpty()) {
                        continue;
                    } else {
                        ItemStack existingOutput = tileEntity.getStackInSlot(furnaceData.getOutputSlot());
                        if (existingOutput.isEmpty()) {
                            return true;
                        } else if (!existingOutput.isItemEqual(output)) {
                            continue;
                        } else {
                            int stackSize = existingOutput.getCount() + output.getCount();
                            return stackSize <= tileEntity.getInventoryStackLimit() && stackSize <= output.getMaxStackSize();
                        }
                    }
                }
            }

            return false;
        }

        public int doHeatTick(TileEntityAbstractBLFurnace tileEntity, int energyAvailable, boolean redstone) {
            int energyConsumed = 0;
            boolean canCook = this.canCook(tileEntity);
            if (canCook || redstone) {
                int furnaceAmount = tileEntity.getSizeInventory() / 4;

                for(int i = 0; i < furnaceAmount; i++) {
                    TileEntityAbstractBLFurnace.FurnaceData furnaceData = tileEntity.getFurnaceData(i);

                    boolean burning = tileEntity.isBurning(i);
                    int burnTime = furnaceData.getFurnaceBurnTime();
                    int energyToUse = 0;

                    if (burnTime < 200) {
                        energyToUse = 4;
                        int heatEnergyRatio = Math.max(1, ExternalHeaterHandler.defaultFurnaceEnergyCost);
                        energyToUse = Math.min(energyAvailable, energyToUse * heatEnergyRatio);
                        int heat = energyToUse / heatEnergyRatio;
                        if (heat > 0) {
                            furnaceData.setFurnaceBurnTime(burnTime + heat);
                            energyConsumed += heat * heatEnergyRatio;

                            if (!burning) {
                                this.updateFurnace(tileEntity, furnaceData.getFurnaceBurnTime() > 0);
                            }
                        }
                    }

                    if (canCook && furnaceData.getFurnaceBurnTime() >= 200 && furnaceData.getFurnaceCookTime() < 199) {
                        energyToUse = ExternalHeaterHandler.defaultFurnaceSpeedupCost;
                        if (energyAvailable - energyConsumed > energyToUse) {
                            energyConsumed += energyToUse;
                            furnaceData.setFurnaceCookTime(furnaceData.getFurnaceCookTime() + 1);
                        }
                    }
                }
            }

            return energyConsumed;
        }

        public void updateFurnace(TileEntity tileEntity, boolean active) {
            Block containing = tileEntity.getBlockType();

            System.out.println(containing.getRegistryName());

            if (containing == BlockRegistry.SULFUR_FURNACE) {
                BlockBLFurnace.setState(active, tileEntity.getWorld(), tileEntity.getPos());
            } else if(containing == BlockRegistry.SULFUR_FURNACE_DUAL) {
                //BlockBLDualFurnace.setState(active, tileEntity.getWorld(), tileEntity.getPos());
            }

            IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
            tileEntity.getWorld().notifyBlockUpdate(tileEntity.getPos(), state, state, 3);
        }
    }
}
