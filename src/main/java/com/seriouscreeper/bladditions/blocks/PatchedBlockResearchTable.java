package com.seriouscreeper.bladditions.blocks;

import com.seriouscreeper.bladditions.BLAdditions;
import com.seriouscreeper.bladditions.tiles.PatchedTileResearchTable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXGeneric;
import thaumcraft.common.blocks.BlockTCDevice;
import thaumcraft.common.blocks.IBlockFacingHorizontal;

import java.util.Random;

public class PatchedBlockResearchTable extends BlockTCDevice implements IBlockFacingHorizontal {
    public PatchedBlockResearchTable() {
        super(Material.WOOD, PatchedTileResearchTable.class, "research_table");
        this.setSoundType(SoundType.WOOD);
    }

    public int damageDropped(IBlockState state) {
        return 0;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        } else {
            player.openGui(BLAdditions.instance, 2, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState bs = this.getDefaultState();
        bs = bs.withProperty(IBlockFacingHorizontal.FACING, placer.getHorizontalFacing());
        return bs;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        TileEntity te = world.getTileEntity(pos);
        if (rand.nextInt(5) == 0 && te != null && ((PatchedTileResearchTable)te).data != null) {
            double xx = rand.nextGaussian() / 2.0D;
            double zz = rand.nextGaussian() / 2.0D;
            double yy = 1.5D + (double)rand.nextFloat();
            int a = 40 + rand.nextInt(20);
            FXGeneric fb = new FXGeneric(world, (double)pos.getX() + 0.5D + xx, (double)pos.getY() + yy, (double)pos.getZ() + 0.5D + zz, -xx / (double)a, -(yy - 0.85D) / (double)a, -zz / (double)a);
            fb.setMaxAge(a);
            fb.setRBGColorF(0.5F + rand.nextFloat() * 0.5F, 0.5F + rand.nextFloat() * 0.5F, 0.5F + rand.nextFloat() * 0.5F);
            fb.setAlphaF(new float[]{0.0F, 0.25F, 0.5F, 0.75F, 0.0F});
            fb.setParticles(384 + rand.nextInt(16), 1, 1);
            fb.setScale(new float[]{0.8F + rand.nextFloat() * 0.3F, 0.3F});
            fb.setLayer(0);
            ParticleEngine.addEffect(world, fb);
        }

    }
}
