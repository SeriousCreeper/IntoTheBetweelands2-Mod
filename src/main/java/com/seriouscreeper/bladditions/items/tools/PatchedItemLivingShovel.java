package com.seriouscreeper.bladditions.items.tools;

import com.google.common.collect.Multimap;
import epicsquid.roots.item.ILivingRepair;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.api.item.CorrosionHelper;
import thebetweenlands.api.item.ICorrodible;
import thebetweenlands.common.registries.BlockRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class PatchedItemLivingShovel extends PatchedRootsItemShovelBase implements ICorrodible, ILivingRepair {
    public PatchedItemLivingShovel(ToolMaterial material, String name) {
        super(material, name, 3, 192, () -> Ingredient.EMPTY);

        CorrosionHelper.addCorrosionPropertyOverrides(this);
    }


    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        CorrosionHelper.updateCorrosion(stack, worldIn, entityIn, itemSlot, isSelected);
        update(stack, worldIn, entityIn, itemSlot, isSelected, 40);
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing == EnumFacing.UP) {
            boolean dug = false;
            IBlockState blockState = world.getBlockState(pos);
            if (blockState.getBlock() == BlockRegistry.COARSE_SWAMP_DIRT) {
                world.setBlockState(pos, BlockRegistry.DUG_SWAMP_DIRT.getDefaultState());
                dug = true;
            }

            if (blockState.getBlock() == BlockRegistry.SWAMP_DIRT) {
                world.setBlockState(pos, BlockRegistry.DUG_SWAMP_DIRT.getDefaultState());
                dug = true;
            }

            if (blockState.getBlock() == BlockRegistry.SWAMP_GRASS) {
                world.setBlockState(pos, BlockRegistry.DUG_SWAMP_GRASS.getDefaultState());
                dug = true;
            }

            if (blockState.getBlock() == BlockRegistry.PURIFIED_SWAMP_DIRT) {
                world.setBlockState(pos, BlockRegistry.DUG_PURIFIED_SWAMP_DIRT.getDefaultState());
                dug = true;
            }

            if (dug) {
                if (world.isRemote) {
                    for(int i = 0; i < 80; ++i) {
                        world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, (double)((float)pos.getX() + 0.5F), (double)(pos.getY() + 1), (double)((float)pos.getZ() + 0.5F), (double)((world.rand.nextFloat() - 0.5F) * 0.1F), (double)(world.rand.nextFloat() * 0.3F), (double)((world.rand.nextFloat() - 0.5F) * 0.1F), new int[]{Block.getStateId(blockState)});
                    }
                }

                SoundType sound = blockState.getBlock().getSoundType(blockState, world, pos, player);

                for(int i = 0; i < 3; ++i) {
                    world.playSound((EntityPlayer)null, (double)((float)pos.getX() + hitX), (double)((float)pos.getY() + hitY), (double)((float)pos.getZ() + hitZ), sound.getBreakSound(), SoundCategory.PLAYERS, 1.0F, 0.5F + world.rand.nextFloat() * 0.5F);
                }

                player.getHeldItem(hand).damageItem(1, player);
                return EnumActionResult.SUCCESS;
            }
        }

        ItemStack stack = player.getHeldItem(hand);
        Block block = world.getBlockState(pos).getBlock();

        if (facing != EnumFacing.DOWN && world.isAirBlock(pos.up()) && (block == Blocks.GRASS || block == Blocks.DIRT)) {
            if (!world.isRemote) {
                world.playSound(null, pos, Blocks.GRASS_PATH.getSoundType().getStepSound(), SoundCategory.BLOCKS, 1F, 1F);
                world.setBlockState(pos, Blocks.GRASS_PATH.getDefaultState());
                if (!player.capabilities.isCreativeMode) {
                    stack.damageItem(1, player);
                }
            }
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        CorrosionHelper.addCorrosionTooltips(stack, tooltip, flagIn.isAdvanced());
    }


    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        Material material = state.getMaterial();
        float str = material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getDestroySpeed(stack, state) : this.efficiency;
        str = CorrosionHelper.getDestroySpeed(str, stack, state);
        return str;
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return CorrosionHelper.shouldCauseBlockBreakReset(oldStack, newStack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return CorrosionHelper.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        return CorrosionHelper.getAttributeModifiers(super.getAttributeModifiers(slot, stack), slot, stack, ATTACK_DAMAGE_MODIFIER, this.attackDamage);
    }


    /*
    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return toRepair.getItem() == this && RootsIngredients.BARK.test(repair);
    }
     */
}
