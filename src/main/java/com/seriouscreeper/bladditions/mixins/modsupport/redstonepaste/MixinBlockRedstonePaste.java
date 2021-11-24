package com.seriouscreeper.bladditions.mixins.modsupport.redstonepaste;

import net.fybertech.redstonepaste.BlockRedstonePaste;
import net.fybertech.redstonepaste.TileEntityRedstonePaste;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.item.ItemBlockSlab;
import thebetweenlands.common.registries.ItemRegistry;

import javax.annotation.Nullable;

@Mixin(value = BlockRedstonePaste.class, remap = false)
public class MixinBlockRedstonePaste extends BlockContainer {
    protected MixinBlockRedstonePaste(Material materialIn) {
        super(materialIn);
    }

    /**
     * @author SC
     */
    @Overwrite
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        Block bID = state.getBlock();
        if (bID != this) {
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
        } else {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te != null && te instanceof TileEntityRedstonePaste && playerIn != null) {
                TileEntityRedstonePaste terp = (TileEntityRedstonePaste)te;
                ItemStack heldItem = playerIn.getHeldItem(hand);
                boolean isHoldingSlab = heldItem != ItemStack.EMPTY && (heldItem.getItem() instanceof ItemBlockSlab || heldItem.getItem() instanceof ItemSlab || heldItem.getItem() instanceof epicsquid.mysticallib.item.ItemBlockSlab || heldItem.getItem() instanceof teamroots.embers.item.block.ItemBlockSlab);
                int pasteBlockType = getPasteBlockType(worldIn, playerIn);
                int repeaterface;
                int[] var10000;
                int delay;
                if (pasteBlockType == 2) {
                    if (!isHoldingSlab) {
                        repeaterface = 0;

                        for(delay = 0; delay < 6; ++delay) {
                            if (terp.facetype[delay] == 2) {
                                repeaterface = delay;
                                break;
                            }
                        }

                        delay = (terp.facedata[repeaterface] & 24) >> 3;
                        ++delay;
                        delay &= 3;
                        if (!worldIn.isRemote) {
                            var10000 = terp.facedata;
                            var10000[repeaterface] &= -25;
                            var10000 = terp.facedata;
                            var10000[repeaterface] |= delay << 3;
                        }

                        worldIn.markAndNotifyBlock(pos, (Chunk)null, state, state, 3);
                        notifyNeighborCube(worldIn, pos.getX(), pos.getY(), pos.getZ(), this);
                        return true;
                    }
                } else if (pasteBlockType == 3 && !isHoldingSlab) {
                    repeaterface = 0;

                    for(delay = 0; delay < 6; ++delay) {
                        if (terp.facetype[delay] == 3) {
                            repeaterface = delay;
                            break;
                        }
                    }

                    delay = terp.facedata[repeaterface] & 4;
                    SoundEvent sound = (SoundEvent)SoundEvent.REGISTRY.getObject(new ResourceLocation("block.comparator.click"));
                    worldIn.playSound(playerIn, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, sound, SoundCategory.BLOCKS, 0.3F, delay > 0 ? 0.4F : 0.55F);
                    delay = ~delay & 4;
                    if (!worldIn.isRemote) {
                        var10000 = terp.facedata;
                        var10000[repeaterface] &= -5;
                        var10000 = terp.facedata;
                        var10000[repeaterface] |= delay;
                    }

                    worldIn.markAndNotifyBlock(pos, (Chunk)null, state, state, 3);
                    notifyNeighborCube(worldIn, pos.getX(), pos.getY(), pos.getZ(), this);
                    return true;
                }

                if (heldItem == ItemStack.EMPTY) {
                    return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
                } else if (!(heldItem.getItem() instanceof ItemBlockSlab || heldItem.getItem() instanceof ItemSlab || heldItem.getItem() instanceof epicsquid.mysticallib.item.ItemBlockSlab || heldItem.getItem() instanceof teamroots.embers.item.block.ItemBlockSlab)) {
                    return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
                } else {
                    repeaterface = 0;
                    delay = 0;
                    int pasteObjects = 0;

                    for(int n = 0; n < 6; ++n) {
                        if (terp.faces[n] > 0) {
                            ++repeaterface;
                        }

                        if (terp.facetype[n] == 1) {
                            ++delay;
                        }

                        if (terp.facetype[n] == 0 && terp.faces[n] > 0) {
                            ++pasteObjects;
                        }

                        if (terp.facetype[n] > 0) {
                            ++pasteObjects;
                        }
                    }

                    if (pasteObjects > 1) {
                        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
                    } else if (terp.facetype[side.ordinal()] != 0) {
                        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
                    } else {
                        if (!worldIn.isRemote) {
                            terp.facetype[side.ordinal()] = 1;
                            terp.facedata[side.ordinal()] = Block.getIdFromBlock(Block.getBlockFromItem(heldItem.getItem())) | heldItem.getItemDamage() << 16;
                            if (!playerIn.capabilities.isCreativeMode) {
                                heldItem.shrink(1);
                            }
                        }

                        SoundEvent sound = (SoundEvent)SoundEvent.REGISTRY.getObject(new ResourceLocation("block.stone.place"));
                        worldIn.playSound(playerIn, (double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        worldIn.markAndNotifyBlock(pos, (Chunk)null, state, state, 3);
                        notifyNeighborCube(worldIn, pos.getX(), pos.getY(), pos.getZ(), this);
                        return true;
                    }
                }
            } else {
                return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
            }
        }
    }

    @Shadow
    public static int getPasteBlockType(World world, EntityPlayer player) {
        return 0;
    }

    @Shadow
    public static void notifyNeighborCube(World world, int x, int y, int z, Block block){}

    @Shadow
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return null;
    }
}
