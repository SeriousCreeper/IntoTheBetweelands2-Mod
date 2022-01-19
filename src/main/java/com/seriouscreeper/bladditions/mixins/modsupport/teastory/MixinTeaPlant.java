package com.seriouscreeper.bladditions.mixins.modsupport.teastory;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import roito.teastory.block.Teaplant;
import roito.teastory.helper.EntironmentHelper;
import roito.teastory.item.ItemRegister;
import thebetweenlands.common.block.farming.BlockDugDirt;
import thebetweenlands.common.block.farming.BlockGenericDugSoil;
import thebetweenlands.common.tile.TileEntityDugSoil;

import java.util.Random;

@Mixin(value = Teaplant.class)
public class MixinTeaPlant extends BlockBush {
    @Final
    @Shadow
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);

    private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[]{
            new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.1875D, 0.75D),
            new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.1875D, 0.75D),
            new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.5D, 0.75D),
            new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.5D, 0.75D),
            new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.8125D, 0.75D),
            new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.8125D, 0.75D),
            new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.75D, 0.75D)};


    /**
     * @author SC
     */
    @Overwrite
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && worldIn.getBlockState(pos.down()).getBlock() instanceof BlockGenericDugSoil) {
            TileEntityDugSoil te = BlockGenericDugSoil.getTile(worldIn, pos.down());

            if(te.getCompost() > 0 && !te.isFullyDecayed()) {
                int i = (Integer) state.getValue(AGE);
                float temperature = worldIn.getBiome(pos).getTemperature(pos);
                float f = getGrowthChance(this, worldIn, pos);
                f = f * EntironmentHelper.getTeaPlantGrowPercent(temperature, pos.getY()) * 0.8F;
                if (f != 0.0F && rand.nextInt((int) (25.0F / f) + 1) == 0) {
                    if (i < 11 || i > 11 && i < 15) {
                        worldIn.setBlockState(pos, state.withProperty(AGE, i + 1), 2);
                    } else if (i == 11 || i >= 15) {
                        worldIn.setBlockState(pos, state.withProperty(AGE, 6), 2);
                    }
                }
            }
        }
    }

    /**
     * @author SC
     */
    @Overwrite
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        int age = MathHelper.clamp(state.getValue(this.getAgeProperty()), 0, 6);
        return CROPS_AABB[age];
    }


    /**
     * @author SC
     */
    @Overwrite
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        TileEntityDugSoil te = BlockGenericDugSoil.getTile(worldIn, pos.down());

        if (te != null && te.isComposted()) {
            return !this.isMaxAge(state) && te.isComposted();
        }

        return false;
    }


    /**
     * @author SC
     */
    @Overwrite
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            TileEntityDugSoil te = BlockGenericDugSoil.getTile(worldIn, pos.down());

            switch(this.getMetaFromState(state)) {
                case 8:
                case 9:
                    worldIn.setBlockState(pos, this.getStateFromMeta(10));
                    worldIn.spawnEntity(new EntityItem(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, new ItemStack(this.getCrop(), playerIn.getRNG().nextInt(4) + 1)));
                    worldIn.spawnEntity(new EntityItem(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, new ItemStack(ItemRegister.broken_tea, playerIn.getRNG().nextInt(4) + 1)));

                    te = BlockGenericDugSoil.getTile(worldIn, pos.down());

                    if (te != null && te.isComposted()) {
                        te.setCompost(Math.max(te.getCompost() - 10, 0));

                        IBlockState stateDown = worldIn.getBlockState(pos.down());

                        if (((BlockGenericDugSoil)stateDown.getBlock()).isPurified(worldIn, pos.down(), stateDown)) {
                            te.setPurifiedHarvests(te.getPurifiedHarvests() + 1);
                        }

                        return true;
                    }

                    return false;
                case 10:
                case 11:
                case 12:
                case 13:
                default:
                    return false;
                case 14:
                case 15:
                    worldIn.setBlockState(pos, this.getStateFromMeta(6));
                    worldIn.spawnEntity(new EntityItem(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, new ItemStack(this.getSeed(), playerIn.getRNG().nextInt(4) + 1)));

                    te = BlockGenericDugSoil.getTile(worldIn, pos.down());

                    if (te != null && te.isComposted()) {
                        te.setCompost(Math.max(te.getCompost() - 10, 0));

                        IBlockState stateDown = worldIn.getBlockState(pos.down());

                        if (((BlockGenericDugSoil)stateDown.getBlock()).isPurified(worldIn, pos.down(), stateDown)) {
                            te.setPurifiedHarvests(te.getPurifiedHarvests() + 1);
                        }
                        return true;
                    }

                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * @author SC
     */
    @Overwrite
    protected boolean canSustainBush(IBlockState state) {
        return state.getBlock() instanceof BlockGenericDugSoil;
    }


    @Shadow
    protected static float getGrowthChance(Block blockIn, World worldIn, BlockPos pos){ return 0;}

    @Shadow
    protected Item getCrop() {
        return ItemRegister.tea_leaf;
    }

    @Shadow
    protected Item getSeed() {
        return ItemRegister.tea_seeds;
    }

    @Shadow
    public boolean isMaxAge(IBlockState state) {
        return true;
    }

    @Shadow
    protected PropertyInteger getAgeProperty() {
        return AGE;
    }
}
