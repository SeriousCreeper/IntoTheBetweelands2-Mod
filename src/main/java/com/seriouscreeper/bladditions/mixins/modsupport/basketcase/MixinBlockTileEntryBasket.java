package com.seriouscreeper.bladditions.mixins.modsupport.basketcase;

import com.daeruin.basketcase.blocks.BlockTileEntityBasket;
import com.daeruin.basketcase.inventory.ItemStackHandlerBasket;
import com.daeruin.primallib.blocks.BlockTileEntity;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mixin(value = BlockTileEntityBasket.class, remap = false)
public class MixinBlockTileEntryBasket extends BlockTileEntity {
    public MixinBlockTileEntryBasket(String registryName, Material material, float hardness, boolean canBePickedUp) {
        super(registryName, material, hardness, canBePickedUp);
    }

    @Shadow
    private void spillInventoryContents(@Nonnull World world, @Nonnull BlockPos pos, TileEntity tileEntity, IItemHandler inventory) {
        for(int i = 0; i < inventory.getSlots(); ++i) {
            ItemStack itemStackCopy = inventory.getStackInSlot(i);
            if (!itemStackCopy.isEmpty() && itemStackCopy.getCount() != 0) {
                tileEntity.getWorld().spawnEntity(new EntityItem(world, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), itemStackCopy));
            }
        }
    }

    /**
     * @author SC
     */
    @Overwrite
    public void harvestBlock(@Nonnull World world, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, TileEntity tileEntity, ItemStack stack) {
        if (tileEntity != null) {
            IItemHandler inventory = (IItemHandler)tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (EnumFacing)null);
            if (inventory != null && inventory instanceof ItemStackHandlerBasket) {
                this.spillInventoryContents(world, pos, tileEntity, inventory);
            }
        }

        world.setBlockToAir(pos);
        spawnAsEntity(world, pos, new ItemStack(state.getBlock()));
    }

    @Shadow
    @Override
    public Class getTileEntityClass() {
        return null;
    }

    @Shadow
    @Override
    public TileEntity createTileEntity(@Nullable World world, @Nullable IBlockState iBlockState) {
        return null;
    }
}
