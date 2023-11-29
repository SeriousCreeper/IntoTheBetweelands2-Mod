package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import epicsquid.roots.item.materials.Materials;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.blocks.world.ore.BlockCrystal;
import thebetweenlands.common.item.BLMaterialRegistry;

import javax.annotation.Nullable;
import java.util.*;

@Mixin(value = BlockCrystal.class, remap = false)
public class MixinBlockCrystal extends Block {
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


    @Inject(method = "getItemDropped", at = @At("HEAD"))
    private void injectGetItemDropped(IBlockState state, Random rand, int fortune, CallbackInfoReturnable<Item> cir) {
        System.out.println("item dropped");
    }


    @Inject(method = "getDrops", at = @At("HEAD"), cancellable = true)
    private void injectGetDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune, CallbackInfoReturnable<List<ItemStack>> cir) {
        System.out.println("test");
        cir.setReturnValue(new ArrayList<>());
    }


    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        if(stack.getItem() instanceof ItemPickaxe) {
            ItemPickaxe pickaxe = (ItemPickaxe) stack.getItem();

            if(!materialBlacklist.contains(pickaxe.toolMaterial)) {
                super.harvestBlock(worldIn, player, pos, state, te, stack);
            }
        }
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
                    return false;
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
