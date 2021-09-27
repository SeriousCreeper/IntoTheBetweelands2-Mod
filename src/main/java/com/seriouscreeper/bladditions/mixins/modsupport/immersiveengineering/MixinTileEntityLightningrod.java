package com.seriouscreeper.bladditions.mixins.modsupport.immersiveengineering;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.IEEnums;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorage;
import blusunrize.immersiveengineering.common.Config;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityLightningrod;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.api.environment.IEnvironmentEvent;
import thebetweenlands.common.entity.EntityBLLightningBolt;
import thebetweenlands.common.world.storage.BetweenlandsWorldStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mixin(value = TileEntityLightningrod.class, remap = false)
public class MixinTileEntityLightningrod extends TileEntityMultiblockPart<TileEntityLightningrod> implements EnergyHelper.IIEInternalFluxHandler {
    @Shadow
    FluxStorage energyStorage;

    @Shadow
    @Nullable
    List<BlockPos> fenceNet;

    @Shadow
    int height;

    protected MixinTileEntityLightningrod(int[] structureDimensions) {
        super(structureDimensions);
    }

    /**
     * @author SC
     */
    @Overwrite
    public void update() {
        ApiUtils.checkForNeedlessTicking(this);

        if (!this.world.isRemote && this.formed && this.pos == 13) {
            if (this.energyStorage.getEnergyStored() > 0) {
                EnumFacing[] var2 = EnumFacing.HORIZONTALS;
                int var3 = var2.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    EnumFacing f = var2[var4];
                    TileEntity tileEntity = Utils.getExistingTileEntity(this.world, this.getPos().offset(f, 2));
                    int output = EnergyHelper.insertFlux(tileEntity, f.getOpposite(), this.energyStorage.getLimitExtract(), true);
                    output = this.energyStorage.extractEnergy(output, false);
                    EnergyHelper.insertFlux(tileEntity, f.getOpposite(), output, false);
                }
            }

            if (this.world.getTotalWorldTime() % 128L == (long)((this.getPos().getX() ^ this.getPos().getZ()) & 127)) {
                this.fenceNet = null;
            }

            if (this.fenceNet == null) {
                this.fenceNet = this.getFenceNet();
            }

            if (this.fenceNet != null && this.fenceNet.size() > 0 && this.world.getTotalWorldTime() % 64L == (long)((this.getPos().getX() ^ this.getPos().getZ()) & 63)) {
                BetweenlandsWorldStorage storage = BetweenlandsWorldStorage.forWorld(world);

                if (storage != null) {
                    List<IEnvironmentEvent> activeEvents = storage.getEnvironmentEventRegistry().getActiveEvents();

                    if((activeEvents.contains(storage.getEnvironmentEventRegistry().thunderstorm) || activeEvents.contains(storage.getEnvironmentEventRegistry().heavyRain) && Utils.RAND.nextInt(7) == 0)) {
                        int i = this.height + this.fenceNet.size();
                        if (Utils.RAND.nextInt(4096 * this.world.getHeight()) < i * (this.getPos().getY() + i)) {
                            this.energyStorage.setEnergy(Config.IEConfig.Machines.lightning_output);
                            BlockPos pos = (BlockPos) this.fenceNet.get(Utils.RAND.nextInt(this.fenceNet.size()));
                            world.spawnEntity(new EntityBLLightningBolt(world, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, 400, false, false));
                        }
                    }
                }
            }
        }
    }

    @Shadow
    @Nullable
    private List<BlockPos> getFenceNet() {
        return null;
    }

    @Shadow
    @Nonnull
    protected IFluidTank[] getAccessibleFluidTanks(@Nullable EnumFacing enumFacing) {
        return new IFluidTank[0];
    }

    @Shadow
    protected boolean canFillTankFrom(int i, EnumFacing enumFacing, FluidStack fluidStack) {
        return false;
    }

    @Shadow
    protected boolean canDrainTankFrom(int i, EnumFacing enumFacing) {
        return false;
    }

    @Shadow
    public ItemStack getOriginalBlock() {
        return null;
    }

    @Shadow
    public float[] getBlockBounds() {
        return new float[0];
    }

    @Shadow
    @Nonnull
    public FluxStorage getFluxStorage() {
        return null;
    }

    @Shadow
    @Nonnull
    public IEEnums.SideConfig getEnergySideConfig(@Nullable EnumFacing enumFacing) {
        return null;
    }

    @Shadow
    public EnergyHelper.IEForgeEnergyWrapper getCapabilityWrapper(EnumFacing enumFacing) {
        return null;
    }
}
