package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import epicsquid.roots.item.materials.Materials;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.blocks.world.ore.BlockCrystal;
import thebetweenlands.common.item.BLMaterialRegistry;

import java.util.*;

@Mixin(value = BlockCrystal.class, remap = false)
public class MixinBlockCrystal  extends Block {
    @Shadow
    public Aspect aspect;
    private static final Set<Item.ToolMaterial> materialBlacklist = new HashSet<Item.ToolMaterial>() {
        {
            add(BLMaterialRegistry.TOOL_BONE);
            add(Materials.LIVING);
        }
    };


    public MixinBlockCrystal(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
    }


    /**
     * @author SC
     */
    @Overwrite
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> ret = new ArrayList();
        return ret;
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
        IBlockState state = world.getBlockState(pos);
        state = state.getBlock().getActualState(state, world, pos);
        ItemStack stack = player.getHeldItemMainhand();
        String tool = this.getHarvestTool(state);

        if(stack.getItem() instanceof ItemPickaxe) {
            ItemPickaxe pickaxe = (ItemPickaxe) stack.getItem();

            if(materialBlacklist.contains(pickaxe.toolMaterial)) {
                return false;
            }

            if (!stack.isEmpty() && tool != null) {
                int toolLevel = stack.getItem().getHarvestLevel(stack, tool, player, state);
                if (toolLevel < 0) {
                    return player.canHarvestBlock(state);
                } else {
                    return toolLevel >= this.getHarvestLevel(state);
                }
            } else {
                return player.canHarvestBlock(state);
            }
        }

        return false;
    }

    @Shadow
    public int getGrowth(IBlockState state) {
        return  0;
    }
}
