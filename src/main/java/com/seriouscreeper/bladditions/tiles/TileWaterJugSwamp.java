package com.seriouscreeper.bladditions.tiles;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.tiles.TileThaumcraft;
import thebetweenlands.common.registries.FluidRegistry;
import vazkii.botania.api.item.IPetalApothecary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class TileWaterJugSwamp extends TileThaumcraft implements ITickable, IFluidHandler {
    int zone = 0;
    int counter = 0;
    ArrayList<Integer> handlers = new ArrayList();
    int zc = 0;
    int tcount = 0;
    public FluidTank tank;

    public TileWaterJugSwamp() {
        this.tank = new FluidTank(new FluidStack(FluidRegistry.SWAMP_WATER, 0), 1000);
    }

    public void readSyncNBT(NBTTagCompound nbttagcompound) {
        this.tank.readFromNBT(nbttagcompound);
    }

    public NBTTagCompound writeSyncNBT(NBTTagCompound nbttagcompound) {
        this.tank.writeToNBT(nbttagcompound);
        return nbttagcompound;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagList nbttaglist = nbt.getTagList("handlers", 3);
        this.handlers = new ArrayList();

        for(int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagInt tag = (NBTTagInt)nbttaglist.get(i);
            this.handlers.add(tag.getInt());
        }

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagList nbttaglist = new NBTTagList();

        for(int i = 0; i < this.handlers.size(); ++i) {
            new NBTTagInt((Integer)this.handlers.get(i));
        }

        nbt.setTag("handlers", nbttaglist);
        return nbt;
    }

    public void update() {
        ++this.counter;
        int x;
        int y;
        int z;
        if (this.world.isRemote) {
            if (this.tcount > 0) {
                if (this.tcount % 5 == 0) {
                    x = this.zc / 5 % 5;
                    y = this.zc / 5 / 5 % 3;
                    z = this.zc % 5;
                    FXDispatcher.INSTANCE.waterTrailFx(this.getPos(), this.getPos().add(x - 2, y - 1, z - 2), this.counter, 7566426, 0.1F);
                }

                --this.tcount;
            }
        } else if (this.counter % 5 == 0) {
            ++this.zone;
            x = this.zone / 5 % 5;
            y = this.zone / 5 / 5 % 3;
            z = this.zone % 5;
            IBlockState bs = this.world.getBlockState(this.getPos().add(x - 2, y - 1, z - 2));
            TileEntity te = this.world.getTileEntity(this.getPos().add(x - 2, y - 1, z - 2));
            if ((te != null && (te instanceof IFluidHandler || te instanceof IPetalApothecary || te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP)) || bs.getBlock() == Blocks.CAULDRON) && !this.handlers.contains(this.zone)) {
                this.handlers.add(this.zone);
                this.markDirty();
            }

            int i = 0;

            label87:
            while(true) {
                while(true) {
                    if (i >= this.handlers.size() || this.tank.getFluidAmount() < 25) {
                        break label87;
                    }

                    int zz = (Integer)this.handlers.get(i);
                    x = zz / 5 % 5;
                    y = zz / 5 / 5 % 3;
                    z = zz % 5;
                    IBlockState bs2 = this.world.getBlockState(this.getPos().add(x - 2, y - 1, z - 2));
                    TileEntity tile = this.world.getTileEntity(this.getPos().add(x - 2, y - 1, z - 2));
                    BlockPos pp;
                    if (tile != null && (tile instanceof IFluidHandler || tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP))) {
                        pp = null;
                        IFluidHandler fh;
                        if (tile instanceof IFluidHandler) {
                            fh = (IFluidHandler)tile;
                        } else {
                            fh = (IFluidHandler)tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
                        }

                        if (fh != null) {
                            int q = fh.fill(new FluidStack(FluidRegistry.SWAMP_WATER, 25), true);
                            if (q > 0) {
                                this.drain(new FluidStack(FluidRegistry.SWAMP_WATER, q), true);
                                this.markDirty();
                                this.world.addBlockEvent(this.getPos(), this.getBlockType(), 1, zz);
                                break label87;
                            }
                        }
                        break;
                    }

                    if (tile != null && tile instanceof IPetalApothecary && this.tank.getFluidAmount() >= 1000) {
                        IPetalApothecary pa = (IPetalApothecary)tile;
                        if (!pa.hasWater()) {
                            pa.setWater(true);
                            this.world.addBlockEvent(this.getPos(), this.getBlockType(), 1, zz);
                            this.drain(new FluidStack(FluidRegistry.SWAMP_WATER, 1000), true);
                        }
                        break;
                    }

                    if (bs2.getBlock() == Blocks.CAULDRON && this.tank.getFluidAmount() >= 333) {
                        if ((Integer)bs2.getValue(BlockCauldron.LEVEL) < 3) {
                            pp = this.getPos().add(x - 2, y - 1, z - 2);
                            this.world.setBlockState(pp, bs2.cycleProperty(BlockCauldron.LEVEL), 2);
                            this.world.updateComparatorOutputLevel(pp, bs2.getBlock());
                            this.world.addBlockEvent(this.getPos(), this.getBlockType(), 1, zz);
                            this.drain(new FluidStack(FluidRegistry.SWAMP_WATER, 333), true);
                        }
                        break;
                    }

                    this.handlers.remove(i);
                    this.markDirty();
                }

                ++i;
            }

            if (this.tank.getFluidAmount() < 1000) {
                float da = (float)(1000 - this.tank.getFluidAmount()) / 1000.0F;
                if (da > 0.1F) {
                    da = 0.1F;
                }

                float dv = AuraHelper.drainVis(this.getWorld(), this.getPos(), da, false);
                int wa = (int)(1000.0F * dv);
                if (wa > 0) {
                    this.tank.fill(new FluidStack(FluidRegistry.SWAMP_WATER, wa), true);
                    this.markDirty();
                    if (this.tank.getFluidAmount() >= this.tank.getCapacity()) {
                        this.syncTile(false);
                    }
                }
            }
        }

    }

    public boolean receiveClientEvent(int i, int j) {
        if (i == 1) {
            if (this.world.isRemote) {
                this.zc = j;
                this.tcount = 5;
            }

            return true;
        } else {
            return super.receiveClientEvent(i, j);
        }
    }

    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return facing == EnumFacing.UP && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return facing == EnumFacing.UP && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? (T) this.tank : super.getCapability(capability, facing);
    }

    public IFluidTankProperties[] getTankProperties() {
        return this.tank.getTankProperties();
    }

    public int fill(FluidStack resource, boolean doFill) {
        return 0;
    }

    public FluidStack drain(FluidStack resource, boolean doDrain) {
        boolean f = this.tank.getFluidAmount() >= this.tank.getCapacity();
        FluidStack fs = this.tank.drain(resource, doDrain);
        this.markDirty();
        if (f && this.tank.getFluidAmount() < this.tank.getCapacity()) {
            this.syncTile(false);
        }

        return fs;
    }

    public FluidStack drain(int maxDrain, boolean doDrain) {
        boolean f = this.tank.getFluidAmount() >= this.tank.getCapacity();
        FluidStack fs = this.tank.drain(maxDrain, doDrain);
        this.markDirty();
        if (f && this.tank.getFluidAmount() < this.tank.getCapacity()) {
            this.syncTile(false);
        }

        return fs;
    }
}