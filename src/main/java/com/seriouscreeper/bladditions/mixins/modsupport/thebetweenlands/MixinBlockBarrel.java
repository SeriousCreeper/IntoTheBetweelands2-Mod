package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import com.charles445.simpledifficulty.item.ItemCanteen;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thebetweenlands.common.TheBetweenlands;
import thebetweenlands.common.block.BasicBlock;
import thebetweenlands.common.block.container.BlockBarrel;
import thebetweenlands.common.tile.TileEntityBarrel;

@Mixin(value = BlockBarrel.class, remap = false)
public abstract class MixinBlockBarrel extends BasicBlock {
    public MixinBlockBarrel(Material material) {
        super(material);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (world.getTileEntity(pos) instanceof TileEntityBarrel) {
            TileEntityBarrel tile = (TileEntityBarrel)world.getTileEntity(pos);
            if (player.isSneaking()) {
                return false;
            }

            if (!heldItem.isEmpty()) {
                if(heldItem.getItem() instanceof ItemGlassBottle || heldItem.getItem() instanceof ItemCanteen) {
                    return true;
                }

                IFluidHandler handler = (IFluidHandler)heldItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, (EnumFacing)null);
                if (handler != null) {
                    IItemHandler playerInventory = (IItemHandler)player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (EnumFacing)null);
                    if (playerInventory != null) {
                        FluidActionResult fluidActionResult = FluidUtil.tryEmptyContainerAndStow(heldItem, tile, playerInventory, 2147483647, player, !world.isRemote);
                        if (fluidActionResult.isSuccess()) {
                            if (!world.isRemote) {
                                player.setHeldItem(hand, fluidActionResult.getResult());
                            }

                            return true;
                        }

                        fluidActionResult = FluidUtil.tryFillContainerAndStow(heldItem, tile, playerInventory, 2147483647, player, !world.isRemote);
                        if (fluidActionResult.isSuccess()) {
                            if (!world.isRemote) {
                                player.setHeldItem(hand, fluidActionResult.getResult());
                            }

                            return true;
                        }
                    }
                }
            }

            if (!world.isRemote && tile != null) {
                player.openGui(TheBetweenlands.instance, 16, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }

        return true;
    }
}
