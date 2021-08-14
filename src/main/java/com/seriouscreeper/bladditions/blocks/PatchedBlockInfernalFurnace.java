package com.seriouscreeper.bladditions.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.BlockTCDevice;
import thaumcraft.common.blocks.IBlockFacingHorizontal;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.devices.TileInfernalFurnace;
import thebetweenlands.common.registries.BlockRegistry;

import java.util.Random;

public class PatchedBlockInfernalFurnace extends BlockTCDevice implements IBlockFacingHorizontal {
    public static boolean ignore = false;

    public PatchedBlockInfernalFurnace() {
        super(Material.ROCK, TileInfernalFurnace.class, "infernal_furnace");
        this.setSoundType(SoundType.STONE);
        this.setLightLevel(0.9F);
        IBlockState bs = this.blockState.getBaseState();
        bs.withProperty(IBlockFacingHorizontal.FACING, EnumFacing.NORTH);
        this.setDefaultState(bs);
        this.setCreativeTab((CreativeTabs)null);
    }

    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        return false;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState bs = this.getDefaultState();
        bs = bs.withProperty(IBlockFacingHorizontal.FACING, placer.getHorizontalFacing().getOpposite());
        return bs;
    }

    public static void destroyFurnace(World worldIn, BlockPos pos, IBlockState state, BlockPos startpos) {
        if (!ignore && !worldIn.isRemote) {
            ignore = true;

            System.out.println("---- DESTROYING INFERNAL FURNACE");

            for(int a = -1; a <= 1; ++a) {
                for(int b = -1; b <= 1; ++b) {
                    for(int c = -1; c <= 1; ++c) {
                        if (pos.add(a, b, c) != startpos) {
                            IBlockState bs = worldIn.getBlockState(pos.add(a, b, c));
                            if (bs.getBlock() == Blocks.NETHER_BRICK || bs.getBlock() == BlocksTC.placeholderNetherbrick) {
                                worldIn.setBlockState(pos.add(a, b, c), BlocksTC.stoneArcaneBrick.getDefaultState());
                            }

                            if (bs.getBlock() == Blocks.OBSIDIAN || bs.getBlock() == BlocksTC.placeholderObsidian) {
                                worldIn.setBlockState(pos.add(a, b, c), BlockRegistry.MUD_BRICKS.getDefaultState());
                            }
                        }
                    }
                }
            }

            if (worldIn.isAirBlock(pos.offset(BlockStateUtils.getFacing(state).getOpposite()))) {
                worldIn.setBlockState(pos.offset(BlockStateUtils.getFacing(state).getOpposite()), Blocks.IRON_BARS.getDefaultState());
            }

            worldIn.setBlockState(pos, BlockRegistry.OCTINE_BLOCK.getDefaultState());
            ignore = false;
        }
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemById(0);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        destroyFurnace(worldIn, pos, state, pos);
        super.breakBlock(worldIn, pos, state);
    }

    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (entity.posX < (double)((float)pos.getX() + 0.3F)) {
            entity.motionX += 9.999999747378752E-5D;
        }

        if (entity.posX > (double)((float)pos.getX() + 0.7F)) {
            entity.motionX -= 9.999999747378752E-5D;
        }

        if (entity.posZ < (double)((float)pos.getZ() + 0.3F)) {
            entity.motionZ += 9.999999747378752E-5D;
        }

        if (entity.posZ > (double)((float)pos.getZ() + 0.7F)) {
            entity.motionZ -= 9.999999747378752E-5D;
        }

        if (!world.isRemote && entity.ticksExisted % 10 == 0) {
            if (entity instanceof EntityItem) {
                entity.motionY = 0.02500000037252903D;
                if (entity.onGround) {
                    TileInfernalFurnace taf = (TileInfernalFurnace)world.getTileEntity(pos);
                    ((EntityItem)entity).setItem(taf.addItemsToInventory(((EntityItem)entity).getItem()));
                }
            } else if (entity instanceof EntityLivingBase && !entity.isImmuneToFire()) {
                entity.attackEntityFrom(DamageSource.LAVA, 3.0F);
                entity.setFire(10);
            }
        }

        super.onEntityCollision(world, pos, state, entity);
    }
}
