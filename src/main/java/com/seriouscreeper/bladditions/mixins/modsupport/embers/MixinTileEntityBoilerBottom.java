package com.seriouscreeper.bladditions.mixins.modsupport.embers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import teamroots.embers.SoundManager;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.event.EmberEvent;
import teamroots.embers.api.misc.IMetalCoefficient;
import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.api.upgrades.UpgradeUtil;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageEmberActivationFX;
import teamroots.embers.tileentity.TileEntityBoilerBottom;
import teamroots.embers.tileentity.TileEntityBoilerTop;
import thebetweenlands.common.registries.FluidRegistry;

import java.util.List;
import java.util.Random;

@Mixin(value = TileEntityBoilerBottom.class)
public class MixinTileEntityBoilerBottom extends TileFluidHandler {
    @Shadow
    private List<IUpgradeProvider> upgrades;

    @Shadow
    Random random = new Random();

    @Shadow
    public ItemStackHandler inventory;

    @Shadow
    int progress = -1;

    @Shadow
    public double getMultiplier() {
        return 0;
    }

    /**
     * @author SC
     */
    @Overwrite
    public void update() { // func_73660_a
        this.upgrades = UpgradeUtil.getUpgrades(this.world, this.pos, EnumFacing.HORIZONTALS);
        UpgradeUtil.verifyUpgrades(this, this.upgrades);
        if (!UpgradeUtil.doTick(this, this.upgrades)) {
            TileEntity tile = this.getWorld().getTileEntity(this.getPos().up());
            int i = this.random.nextInt(this.inventory.getSlots());
            ItemStack emberStack = this.inventory.getStackInSlot(i);

            if (this.tank.getFluid() != null && this.tank.getFluid().getFluid().getUnlocalizedName().equals("fluid.swamp_water") && this.tank.getFluidAmount() >= 25 && !emberStack.isEmpty()) {
                boolean cancel = UpgradeUtil.doWork(this, this.upgrades);
                if (!cancel && tile instanceof TileEntityBoilerTop) {
                    TileEntityBoilerTop top = (TileEntityBoilerTop)tile;
                    ++this.progress;
                    if (this.progress > UpgradeUtil.getWorkTime(this, 20, this.upgrades)) {
                        this.progress = 0;
                        double emberValue = EmbersAPI.getEmberValue(emberStack);
                        double ember = UpgradeUtil.getTotalEmberProduction(this, emberValue * this.getMultiplier(), this.upgrades);
                        if (ember > 0.0D && top.capability.getEmber() + ember <= top.capability.getEmberCapacity()) {
                            if (!this.world.isRemote) {
                                this.world.playSound((EntityPlayer)null, (double)this.getPos().getX() + 0.5D, (double)this.getPos().getY() + 1.5D, (double)this.getPos().getZ() + 0.5D, SoundManager.PRESSURE_REFINERY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                                PacketHandler.INSTANCE.sendToAll(new MessageEmberActivationFX((double)((float)this.getPos().getX() + 0.5F), (double)((float)this.getPos().getY() + 1.5F), (double)((float)this.getPos().getZ() + 0.5F)));
                            }

                            UpgradeUtil.throwEvent(this, new EmberEvent(this, EmberEvent.EnumType.PRODUCE, ember), this.upgrades);
                            top.capability.addAmount(ember, true);
                            this.inventory.extractItem(i, 1, false);
                            this.tank.drain(25, true);
                            this.markDirty();
                            top.markDirty();
                        }
                    }

                    this.markDirty();
                }
            }
        }
    }
}
