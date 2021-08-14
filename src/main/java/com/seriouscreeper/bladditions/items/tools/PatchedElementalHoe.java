package com.seriouscreeper.bladditions.items.tools;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.IThaumcraftItems;
import thaumcraft.common.lib.utils.Utils;
import thebetweenlands.api.item.CorrosionHelper;
import thebetweenlands.api.item.ICorrodible;
import thebetweenlands.common.registries.BlockRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class PatchedElementalHoe extends ItemHoe implements IThaumcraftItems, ICorrodible {
    public PatchedElementalHoe(ToolMaterial enumtoolmaterial) {
        super(enumtoolmaterial);
        this.setCreativeTab(ConfigItems.TABTC);
        this.setRegistryName("thaumcraft:elemental_hoe");
        this.setTranslationKey("elemental_hoe");
        ConfigItems.ITEM_VARIANT_HOLDERS.add(this);

        CorrosionHelper.addCorrosionPropertyOverrides(this);
    }

    public Item getItem() {
        return this;
    }

    public String[] getVariantNames() {
        return new String[]{"normal"};
    }

    public int[] getVariantMeta() {
        return new int[]{0};
    }

    public ItemMeshDefinition getCustomMesh() {
        return null;
    }

    public ModelResourceLocation getCustomModelResourceLocation(String variant) {
        return new ModelResourceLocation("thaumcraft:" + variant);
    }

    public int getItemEnchantability() {
        return 5;
    }

    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.RARE;
    }

    public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {
        return stack2.isItemEqual(new ItemStack(ItemsTC.ingots, 1, 0)) ? true : super.getIsRepairable(stack1, stack2);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        CorrosionHelper.updateCorrosion(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    private EnumActionResult customUseItem(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(facing == EnumFacing.UP) {
            boolean dug = false;
            IBlockState blockState = world.getBlockState(pos);

            if(blockState.getBlock() == BlockRegistry.COARSE_SWAMP_DIRT) {
                world.setBlockState(pos, BlockRegistry.DUG_SWAMP_DIRT.getDefaultState());
                dug = true;
            }

            if(blockState.getBlock() == BlockRegistry.SWAMP_DIRT) {
                world.setBlockState(pos, BlockRegistry.DUG_SWAMP_DIRT.getDefaultState());
                dug = true;
            }

            if(blockState.getBlock() == BlockRegistry.SWAMP_GRASS) {
                world.setBlockState(pos, BlockRegistry.DUG_SWAMP_GRASS.getDefaultState());
                dug = true;
            }

            if(blockState.getBlock() == BlockRegistry.PURIFIED_SWAMP_DIRT) {
                world.setBlockState(pos, BlockRegistry.DUG_PURIFIED_SWAMP_DIRT.getDefaultState());
                dug = true;
            }

            if(dug) {
                if(world.isRemote) {
                    for(int i = 0; i < 80; i++) {
                        world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, pos.getX() + 0.5F, pos.getY() + 1, pos.getZ() + 0.5F, (world.rand.nextFloat() - 0.5F) * 0.1F, world.rand.nextFloat() * 0.3f, (world.rand.nextFloat() - 0.5F) * 0.1F, new int[] {Block.getStateId(blockState)});
                    }
                }

                SoundType sound = blockState.getBlock().getSoundType(blockState, world, pos, player);
                for(int i = 0; i < 3; i++) {
                    world.playSound(null, pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ, sound.getBreakSound(), SoundCategory.PLAYERS, 1, 0.5f + world.rand.nextFloat() * 0.5f);
                }

                player.getHeldItem(hand).damageItem(1, player);

                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }

    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            return customUseItem(player, world, pos, hand, facing, hitX, hitY, hitZ);
        } else {
            boolean did = false;

            for(int xx = -1; xx <= 1; ++xx) {
                for(int zz = -1; zz <= 1; ++zz) {
                    if (customUseItem(player, world, pos.add(xx, 0, zz), hand, facing, hitX, hitY, hitZ) == EnumActionResult.SUCCESS) {
                        if (world.isRemote) {
                            BlockPos pp = pos.add(xx, 0, zz);
                            FXDispatcher.INSTANCE.drawBamf((double)pp.getX() + 0.5D, (double)pp.getY() + 1.01D, (double)pp.getZ() + 0.5D, 0.3F, 0.12F, 0.1F, xx == 0 && zz == 0, false, EnumFacing.UP);
                        }

                        if (!did) {
                            did = true;
                        }
                    }
                }
            }

            if (!did) {
                did = Utils.useBonemealAtLoc(world, player, pos);
                if (did) {
                    player.getHeldItem(hand).damageItem(3, player);
                    if (!world.isRemote) {
                        world.playBroadcastSound(2005, pos, 0);
                    } else {
                        FXDispatcher.INSTANCE.drawBlockMistParticles(pos, 4259648);
                    }
                }
            }

            return EnumActionResult.SUCCESS;
        }
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        CorrosionHelper.addCorrosionTooltips(stack, tooltip, flagIn.isAdvanced());
    }
}
