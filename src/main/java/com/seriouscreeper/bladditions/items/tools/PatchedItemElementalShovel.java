package com.seriouscreeper.bladditions.items.tools;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.items.IArchitect;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.IThaumcraftItems;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.lib.utils.Utils;
import thebetweenlands.api.item.CorrosionHelper;
import thebetweenlands.api.item.ICorrodible;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PatchedItemElementalShovel extends ItemSpade implements IArchitect, IThaumcraftItems, ICorrodible {
    private static final Block[] isEffective;
    EnumFacing side;

    public PatchedItemElementalShovel(ToolMaterial enumtoolmaterial) {
        super(enumtoolmaterial);
        this.side = EnumFacing.DOWN;
        this.setCreativeTab(ConfigItems.TABTC);
        this.setRegistryName("thaumcraft:elemental_shovel");
        this.setTranslationKey("elemental_shovel");
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

    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("shovel");
    }

    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.RARE;
    }

    public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {
        return stack2.isItemEqual(new ItemStack(ItemsTC.ingots, 1, 0)) ? true : super.getIsRepairable(stack1, stack2);
    }

    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) {
        IBlockState bs = world.getBlockState(pos);
        TileEntity te = world.getTileEntity(pos);
        if (te == null) {
            for(int aa = -1; aa <= 1; ++aa) {
                for(int bb = -1; bb <= 1; ++bb) {
                    int xx = 0;
                    int yy = 0;
                    int zz = 0;
                    byte o = getOrientation(player.getHeldItem(hand));
                    int l;
                    if (o == 1) {
                        yy = bb;
                        if (side.ordinal() <= 1) {
                            l = MathHelper.floor((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
                            if (l != 0 && l != 2) {
                                zz = aa;
                            } else {
                                xx = aa;
                            }
                        } else if (side.ordinal() <= 3) {
                            zz = aa;
                        } else {
                            xx = aa;
                        }
                    } else if (o == 2) {
                        if (side.ordinal() <= 1) {
                            l = MathHelper.floor((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
                            yy = bb;
                            if (l != 0 && l != 2) {
                                zz = aa;
                            } else {
                                xx = aa;
                            }
                        } else {
                            zz = bb;
                            xx = aa;
                        }
                    } else if (side.ordinal() <= 1) {
                        xx = aa;
                        zz = bb;
                    } else if (side.ordinal() <= 3) {
                        xx = aa;
                        yy = bb;
                    } else {
                        zz = aa;
                        yy = bb;
                    }

                    BlockPos p2 = pos.offset(side).add(xx, yy, zz);
                    world.getBlockState(p2);
                    if (bs.getBlock().canPlaceBlockAt(world, p2)) {
                        if (!player.capabilities.isCreativeMode && !InventoryUtils.consumePlayerItem(player, Item.getItemFromBlock(bs.getBlock()), bs.getBlock().getMetaFromState(bs))) {
                            if (bs.getBlock() == Blocks.GRASS && (player.capabilities.isCreativeMode || InventoryUtils.consumePlayerItem(player, Item.getItemFromBlock(Blocks.DIRT), 0))) {
                                world.playSound((double)p2.getX(), (double)p2.getY(), (double)p2.getZ(), bs.getBlock().getSoundType().getBreakSound(), SoundCategory.BLOCKS, 0.6F, 0.9F + world.rand.nextFloat() * 0.2F, false);
                                world.setBlockState(p2, Blocks.DIRT.getDefaultState());
                                player.getHeldItem(hand).damageItem(1, player);
                                if (world.isRemote) {
                                    FXDispatcher.INSTANCE.drawBamf(p2, 8401408, false, false, side);
                                }

                                player.swingArm(hand);
                                if (player.getHeldItem(hand).isEmpty() || player.getHeldItem(hand).getCount() < 1) {
                                    break;
                                }
                            }
                        } else {
                            world.playSound((double)p2.getX(), (double)p2.getY(), (double)p2.getZ(), bs.getBlock().getSoundType().getBreakSound(), SoundCategory.BLOCKS, 0.6F, 0.9F + world.rand.nextFloat() * 0.2F, false);
                            world.setBlockState(p2, bs);
                            player.getHeldItem(hand).damageItem(1, player);
                            if (world.isRemote) {
                                FXDispatcher.INSTANCE.drawBamf(p2, 8401408, false, false, side);
                            }

                            player.swingArm(hand);
                        }
                    }
                }
            }
        }

        return EnumActionResult.FAIL;
    }

    private boolean isEffectiveAgainst(Block block) {
        for(int var3 = 0; var3 < isEffective.length; ++var3) {
            if (isEffective[var3] == block) {
                return true;
            }
        }

        return false;
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab != ConfigItems.TABTC && tab != CreativeTabs.SEARCH) {
            super.getSubItems(tab, items);
        } else {
            ItemStack w1 = new ItemStack(this);
            EnumInfusionEnchantment.addInfusionEnchantment(w1, EnumInfusionEnchantment.DESTRUCTIVE, 1);
            items.add(w1);
        }

    }

    public ArrayList<BlockPos> getArchitectBlocks(ItemStack focusstack, World world, BlockPos pos, EnumFacing side, EntityPlayer player) {
        ArrayList<BlockPos> b = new ArrayList();
        if (!player.isSneaking()) {
            return b;
        } else {
            IBlockState bs = world.getBlockState(pos);

            for(int aa = -1; aa <= 1; ++aa) {
                for(int bb = -1; bb <= 1; ++bb) {
                    int xx = 0;
                    int yy = 0;
                    int zz = 0;
                    byte o = getOrientation(focusstack);
                    int l;
                    if (o == 1) {
                        yy = bb;
                        if (side.ordinal() <= 1) {
                            l = MathHelper.floor((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
                            if (l != 0 && l != 2) {
                                zz = aa;
                            } else {
                                xx = aa;
                            }
                        } else if (side.ordinal() <= 3) {
                            zz = aa;
                        } else {
                            xx = aa;
                        }
                    } else if (o == 2) {
                        if (side.ordinal() <= 1) {
                            l = MathHelper.floor((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
                            yy = bb;
                            if (l != 0 && l != 2) {
                                zz = aa;
                            } else {
                                xx = aa;
                            }
                        } else {
                            zz = bb;
                            xx = aa;
                        }
                    } else if (side.ordinal() <= 1) {
                        xx = aa;
                        zz = bb;
                    } else if (side.ordinal() <= 3) {
                        xx = aa;
                        yy = bb;
                    } else {
                        zz = aa;
                        yy = bb;
                    }

                    BlockPos p2 = pos.offset(side).add(xx, yy, zz);
                    world.getBlockState(p2);
                    if (bs.getBlock().canPlaceBlockAt(world, p2)) {
                        b.add(p2);
                    }
                }
            }

            return b;
        }
    }

    public boolean showAxis(ItemStack stack, World world, EntityPlayer player, EnumFacing side, EnumAxis axis) {
        return false;
    }

    public static byte getOrientation(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("or") ? stack.getTagCompound().getByte("or") : 0;
    }

    public static void setOrientation(ItemStack stack, byte o) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        if (stack.hasTagCompound()) {
            stack.getTagCompound().setByte("or", (byte)(o % 3));
        }

    }

    public RayTraceResult getArchitectMOP(ItemStack stack, World world, EntityLivingBase player) {
        return Utils.rayTrace(world, player, false);
    }

    public boolean useBlockHighlight(ItemStack stack) {
        return true;
    }

    static {
        isEffective = new Block[]{Blocks.GRASS, Blocks.DIRT, Blocks.SAND, Blocks.GRAVEL, Blocks.SNOW_LAYER, Blocks.SNOW, Blocks.CLAY, Blocks.FARMLAND, Blocks.SOUL_SAND, Blocks.MYCELIUM};
    }


    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        CorrosionHelper.updateCorrosion(stack, worldIn, entityIn, itemSlot, isSelected);
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        CorrosionHelper.addCorrosionTooltips(stack, tooltip, flagIn.isAdvanced());
    }


    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return CorrosionHelper.getDestroySpeed(super.getDestroySpeed(stack, state), stack, state);
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
}
