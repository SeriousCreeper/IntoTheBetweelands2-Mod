package com.seriouscreeper.bladditions.tiles;

import com.seriouscreeper.bladditions.proxy.CommonProxy;
import mysticalmechanics.api.DefaultMechCapability;
import mysticalmechanics.api.IMechCapability;
import mysticalmechanics.api.MysticalMechanicsAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import thebetweenlands.common.registries.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.Embers;
import teamroots.embers.SoundManager;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.misc.ILiquidFuel;
import teamroots.embers.api.tile.IExtraCapabilityInformation;
import teamroots.embers.block.BlockSteamEngine;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.tileentity.ITileEntityBase;
import teamroots.embers.util.Misc;
import teamroots.embers.util.sound.ISoundController;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashSet;
import java.util.List;

public class PatchedTileEntitySteamEngine extends TileEntity implements ITileEntityBase, ITickable, ISoundController, IExtraCapabilityInformation {
    public static int NORMAL_FLUID_THRESHOLD = 10;
    public static int NORMAL_FLUID_CONSUMPTION = 4;
    public static int GAS_CONSUMPTION = 20;
    public static double MAX_POWER = 50.0D;
    public static int CAPACITY = 8000;
    public static double SOLID_POWER = 20.0D;
    public static double FUEL_MULTIPLIER = 2.0D;
    public static final int SOUND_BURN = 1;
    public static final int SOUND_STEAM = 2;
    public static final int[] SOUND_IDS = new int[]{1, 2};
    PatchedTileEntitySteamEngine.BurningFuel currentFuel = new PatchedTileEntitySteamEngine.BurningFuel();
    int ticksExisted = 0;
    HashSet<Integer> soundsPlaying = new HashSet();
    EnumFacing front;
    public FluidTank tank;
    public DefaultMechCapability capability;
    public ItemStackHandler inventory;

    public PatchedTileEntitySteamEngine() {
        this.front = EnumFacing.UP;
        this.tank = new FluidTank(CAPACITY);
        this.capability = new DefaultMechCapability() {
            public void setPower(double value, EnumFacing from) {
                if (from == null) {
                    super.setPower(value, (EnumFacing)null);
                }
            }
        };
        this.inventory = new ItemStackHandler(1) {
            protected void onContentsChanged(int slot) {
                PatchedTileEntitySteamEngine.this.markDirty();
            }

            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                return TileEntityFurnace.getItemBurnTime(stack) == 0 ? stack : super.insertItem(slot, stack, simulate);
            }

            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                ItemStack currentFuel = super.extractItem(slot, amount, true);
                int burntime = TileEntityFurnace.getItemBurnTime(currentFuel);
                return burntime != 0 ? ItemStack.EMPTY : super.extractItem(slot, amount, simulate);
            }
        };
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setDouble("mech_power", this.capability.power);
        tag.setTag("tank", this.tank.writeToNBT(new NBTTagCompound()));
        tag.setTag("progress", this.currentFuel.writeToNBT(new NBTTagCompound()));
        tag.setInteger("front", this.front.getIndex());
        tag.setTag("inventory", this.inventory.serializeNBT());
        return tag;
    }

    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.capability.power = tag.getDouble("mech_power");
        this.tank.readFromNBT(tag.getCompoundTag("tank"));
        this.currentFuel.readFromNBT(tag.getCompoundTag("progress"));
        this.inventory.deserializeNBT(tag.getCompoundTag("inventory"));
        this.front = EnumFacing.byIndex(tag.getInteger("front"));
    }

    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.getPos(), 0, this.getUpdateTag());
    }

    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == MysticalMechanicsAPI.MECH_CAPABILITY) {
            return facing == this.front;
        } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return true;
        } else {
            return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? true : super.hasCapability(capability, facing);
        }
    }

    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == MysticalMechanicsAPI.MECH_CAPABILITY) {
            return (T) this.capability;
        } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) this.tank;
        } else {
            return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) this.inventory : super.getCapability(capability, facing);
        }
    }

    public void markDirty() {
        super.markDirty();
        Misc.syncTE(this);
    }

    public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        player.getHeldItem(hand);
        boolean didFill = FluidUtil.interactWithFluidHandler(player, hand, this.tank);
        if (didFill) {
            this.markDirty();
            return true;
        } else {
            return false;
        }
    }

    public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        this.invalidate();
        Misc.spawnInventoryInWorld(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, this.inventory);
        this.capability.setPower(0.0D, (EnumFacing)null);
        this.updateNearby();
        world.setTileEntity(pos, (TileEntity)null);
    }

    public void updateNearby() {
        EnumFacing[] var1 = EnumFacing.values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            EnumFacing f = var1[var3];
            TileEntity t = this.world.getTileEntity(this.getPos().offset(f));
            if (t != null && f == this.front && t.hasCapability(MysticalMechanicsAPI.MECH_CAPABILITY, Misc.getOppositeFace(f))) {
                ((IMechCapability)t.getCapability(MysticalMechanicsAPI.MECH_CAPABILITY, Misc.getOppositeFace(f))).setPower(this.capability.getPower(Misc.getOppositeFace(f)), Misc.getOppositeFace(f));
                t.markDirty();
            }
        }

    }

    private ItemStack copyWithSize(ItemStack stack, int size) {
        stack = stack.copy();
        stack.setCount(size);
        return stack;
    }

    public void update() {
        IBlockState state = this.world.getBlockState(this.getPos());
        if (state.getBlock() instanceof BlockSteamEngine) {
            this.front = (EnumFacing)state.getValue(BlockSteamEngine.facing);
        }

        boolean dirty = false;
        double powerGenerated = 0.0D;
        if (this.world.isRemote) {
            this.spawnParticles();
            this.handleSound();
        }

        if (!this.world.isRemote && !this.currentFuel.isEmpty()) {
            this.currentFuel.tick();
            if (this.currentFuel.isEmpty()) {
                this.currentFuel.reset();
                dirty = true;
            }
        }

        FluidStack fluid;
        ILiquidFuel fuelHandler;
        if (this.currentFuel.isEmpty()) {
            fluid = this.tank.getFluid();
            fuelHandler = EmbersAPI.getSteamEngineFuel(fluid);
            if (fluid != null && fuelHandler != null) {
                fluid = this.tank.drain(Math.min(GAS_CONSUMPTION, Math.max(fluid.amount - 1, 1)), false);
                if (!this.world.isRemote) {
                    this.currentFuel = new PatchedTileEntitySteamEngine.BurningFuel(fluid, fuelHandler.getTime(fluid));
                    this.tank.drain(fluid, true);
                    dirty = true;
                }
            } else if (!this.world.isRemote && !this.inventory.getStackInSlot(0).isEmpty() && fluid != null && fluid.getFluid() == CommonProxy.SWAMP_WATER && this.tank.getFluidAmount() >= NORMAL_FLUID_THRESHOLD) {
                ItemStack fuel = this.inventory.getStackInSlot(0);
                if (!fuel.isEmpty()) {
                    ItemStack fuelCopy = fuel.copy();
                    int burnTime = TileEntityFurnace.getItemBurnTime(fuelCopy);
                    if (burnTime > 0) {
                        this.currentFuel = new PatchedTileEntitySteamEngine.BurningFuel(this.copyWithSize(fuelCopy, 1), (int)((double)burnTime * FUEL_MULTIPLIER));
                        fuel.shrink(1);
                        if (fuel.isEmpty()) {
                            this.inventory.setStackInSlot(0, fuelCopy.getItem().getContainerItem(fuelCopy));
                        }

                        dirty = true;
                    }
                }
            }
        }

        if (this.currentFuel.isLiquid()) {
            fluid = this.currentFuel.liquidFuel;
            fuelHandler = EmbersAPI.getSteamEngineFuel(fluid);
            powerGenerated = Misc.getDiminishedPower(fuelHandler.getPower(fluid), MAX_POWER, 1.0D);
        }

        if (this.currentFuel.isSolid()) {
            fluid = this.tank.getFluid();
            if (this.tank.getFluidAmount() >= NORMAL_FLUID_CONSUMPTION && fluid != null && fluid.getFluid() == CommonProxy.SWAMP_WATER) {
                if (!this.world.isRemote) {
                    this.tank.drain(NORMAL_FLUID_CONSUMPTION, true);
                    powerGenerated = SOLID_POWER;
                    dirty = true;
                }
            } else {
                this.currentFuel.reset();
            }
        }

        if (dirty) {
            this.markDirty();
        }

        if (!this.world.isRemote && this.capability.getPower((EnumFacing)null) != powerGenerated) {
            this.capability.setPower(powerGenerated, (EnumFacing)null);
            this.updateNearby();
        }

    }

    private void spawnParticles() {
        if (!this.currentFuel.isEmpty()) {
            boolean vapor = this.currentFuel.isLiquid();

            for(int i = 0; i < 4; ++i) {
                float offX = 0.09375F + 0.8125F * (float)Misc.random.nextInt(2);
                float offZ = 0.28125F + 0.4375F * (float)Misc.random.nextInt(2);
                if (this.front.getAxis() == EnumFacing.Axis.X) {
                    float h = offX;
                    offX = offZ;
                    offZ = h;
                }

                Color color = this.currentFuel.getColor();
                if (vapor) {
                    ParticleUtil.spawnParticleVapor(this.world, (float)this.getPos().getX() + offX, (float)this.getPos().getY() + 1.0F, (float)this.getPos().getZ() + offZ, 0.025F * (Misc.random.nextFloat() - 0.5F), 0.125F * Misc.random.nextFloat(), 0.025F * (Misc.random.nextFloat() - 0.5F), (float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), (float)color.getAlpha() / 255.0F, 0.5F, 2.0F + Misc.random.nextFloat(), 24);
                } else {
                    ParticleUtil.spawnParticleSmoke(this.world, (float)this.getPos().getX() + offX, (float)this.getPos().getY() + 1.0F, (float)this.getPos().getZ() + offZ, 0.025F * (Misc.random.nextFloat() - 0.5F), 0.125F * Misc.random.nextFloat(), 0.025F * (Misc.random.nextFloat() - 0.5F), (float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), (float)color.getAlpha() / 255.0F, 2.0F + Misc.random.nextFloat(), 24);
                }
            }

        }
    }

    public void playSound(int id) {
        float soundX = (float)this.pos.getX() + 0.5F;
        float soundY = (float)this.pos.getY() + 0.5F;
        float soundZ = (float)this.pos.getZ() + 0.5F;
        switch(id) {
            case 1:
                Embers.proxy.playMachineSound(this, 1, SoundManager.STEAM_ENGINE_LOOP_BURN, SoundCategory.BLOCKS, true, 1.0F, 1.0F, soundX, soundY, soundZ);
                this.world.playSound((double)soundX, (double)soundY, (double)soundZ, SoundManager.STEAM_ENGINE_START_BURN, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                break;
            case 2:
                Embers.proxy.playMachineSound(this, 2, SoundManager.STEAM_ENGINE_LOOP_STEAM, SoundCategory.BLOCKS, true, 1.0F, 1.0F, soundX, soundY, soundZ);
                this.world.playSound((double)soundX, (double)soundY, (double)soundZ, SoundManager.STEAM_ENGINE_START_STEAM, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        }

        this.soundsPlaying.add(id);
    }

    public void stopSound(int id) {
        this.world.playSound((double)((float)this.pos.getX() + 0.5F), (double)((float)this.pos.getY() + 0.5F), (double)((float)this.pos.getZ() + 0.5F), SoundManager.STEAM_ENGINE_STOP, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        this.soundsPlaying.remove(id);
    }

    public boolean isSoundPlaying(int id) {
        return this.soundsPlaying.contains(id);
    }

    public int[] getSoundIDs() {
        return SOUND_IDS;
    }

    public boolean shouldPlaySound(int id) {
        switch(id) {
            case 1:
                return this.currentFuel.isSolid();
            case 2:
                return this.currentFuel.isLiquid();
            default:
                return false;
        }
    }

    public boolean hasCapabilityDescription(Capability<?> capability) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }

    public void addCapabilityDescription(List<String> strings, Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            strings.add(IExtraCapabilityInformation.formatCapability(EnumIOType.INPUT, "embers.tooltip.goggles.item", I18n.format("embers.tooltip.goggles.item.fuel", new Object[0])));
        }

        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            strings.add(IExtraCapabilityInformation.formatCapability(EnumIOType.INPUT, "embers.tooltip.goggles.fluid", I18n.format("embers.tooltip.goggles.fluid.water_or_steam", new Object[0])));
        }

    }

    class BurningFuel {
        ItemStack solidFuel;
        FluidStack liquidFuel;
        int timeLeft;

        public BurningFuel() {
            this.solidFuel = ItemStack.EMPTY;
        }

        public BurningFuel(ItemStack solidFuel, int timeLeft) {
            this.solidFuel = ItemStack.EMPTY;
            this.solidFuel = solidFuel;
            this.timeLeft = timeLeft;
        }

        public BurningFuel(FluidStack liquidFuel, int timeLeft) {
            this.solidFuel = ItemStack.EMPTY;
            this.liquidFuel = liquidFuel;
            this.timeLeft = timeLeft;
        }

        public void tick() {
            --this.timeLeft;
        }

        public void reset() {
            this.solidFuel = ItemStack.EMPTY;
            this.liquidFuel = null;
            this.timeLeft = 0;
        }

        public boolean isSolid() {
            return !this.solidFuel.isEmpty();
        }

        public boolean isLiquid() {
            return this.liquidFuel != null;
        }

        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            if (this.liquidFuel != null) {
                tag.setTag("fluid", this.liquidFuel.writeToNBT(new NBTTagCompound()));
            }

            if (!this.solidFuel.isEmpty()) {
                tag.setTag("item", this.solidFuel.serializeNBT());
            }

            tag.setInteger("timeLeft", this.timeLeft);
            return tag;
        }

        public void readFromNBT(NBTTagCompound tag) {
            if (tag.hasKey("fluid")) {
                this.liquidFuel = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("fluid"));
            }

            if (tag.hasKey("item")) {
                this.solidFuel = new ItemStack(tag.getCompoundTag("item"));
            }

            this.timeLeft = tag.getInteger("timeLeft");
        }

        public boolean isEmpty() {
            return this.timeLeft <= 0;
        }

        public Color getColor() {
            if (this.isSolid()) {
                return new Color(72, 72, 72, 128);
            } else {
                if (this.isLiquid()) {
                    ILiquidFuel fuelHandler = EmbersAPI.getSteamEngineFuel(this.liquidFuel);
                    if (fuelHandler != null) {
                        return fuelHandler.getBurnColor(this.liquidFuel);
                    }
                }

                return new Color(0, 0, 0, 0);
            }
        }
    }
}
