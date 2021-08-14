package com.seriouscreeper.bladditions.blocks;

import com.seriouscreeper.bladditions.client.BLAdditionFX;
import com.seriouscreeper.bladditions.tiles.TileWaterJugSwamp;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.blocks.BlockTCDevice;

import java.util.Random;

public class PatchedBlockWaterJug extends BlockTCDevice {
    public PatchedBlockWaterJug() {
        super(Material.ROCK, TileWaterJugSwamp.class, "everfull_urn");
        this.setSoundType(SoundType.STONE);
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 1.0D, 0.8125D);
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);
            if (te != null && te instanceof TileWaterJugSwamp) {
                TileWaterJugSwamp tile = (TileWaterJugSwamp)te;
                if (FluidUtil.interactWithFluidHandler(player, hand, tile.tank)) {
                    player.inventoryContainer.detectAndSendChanges();
                    te.markDirty();
                    tile.syncTile(false);
                    world.playSound((EntityPlayer)null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 0.33F, 1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.3F);
                }
            }
        }

        return true;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        Block block = state.getBlock();
        if (block.hasTileEntity(state)) {
            TileEntity te = world.getTileEntity(pos);
            if (te != null && te instanceof TileWaterJugSwamp) {
                TileWaterJugSwamp tile = (TileWaterJugSwamp)te;
                if (tile.tank.getFluidAmount() >= tile.tank.getCapacity()) {
                    BLAdditionFX.INSTANCE.jarSplashFx((double)pos.getX() + 0.5D, (double)(pos.getY() + 1), (double)pos.getZ() + 0.5D);
                }
            }
        }

    }
}
