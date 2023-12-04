package com.seriouscreeper.bladditions.mixins.modsupport.pizzacraft;

import com.tiviacz.pizzacraft.blocks.BlockPizza;
import com.tiviacz.pizzacraft.handlers.ConfigHandler;
import com.tiviacz.pizzacraft.init.ModItems;
import com.tiviacz.pizzacraft.items.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = BlockPizza.class, remap = false)
public class MixinBlockPizza extends BlockBase {
    @Final
    @Shadow public static final PropertyInteger BITES = PropertyInteger.create("bites", 0, 5);
    @Final
    @Shadow private Item pizzaslice;

    public MixinBlockPizza(String name, Material material) {
        super(name, material);
    }

    /**
     * @author SC
     * @reason
     */
    @Overwrite
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack helditem = playerIn.getHeldItem(hand);
        int i = (Integer)state.getValue(BITES);
        if (worldIn.isRemote) {
            return true;
        } else {
            if (helditem.getItem() == ModItems.PEEL && i == 0 && (!playerIn.capabilities.isCreativeMode || playerIn.capabilities.isCreativeMode)) {
                spawnAsEntity(worldIn, pos, new ItemStack(this));
                worldIn.setBlockToAir(pos);
                playerIn.getHeldItem(hand).damageItem(1, playerIn);
            }

            if (ConfigHandler.isKnifeNeeded) {
                if (epicsquid.roots.init.ModItems.knives.contains(helditem.getItem())) {
                    spawnAsEntity(worldIn, pos, new ItemStack(this.pizzaslice));
                    playerIn.getHeldItem(hand).damageItem(1, playerIn);
                    if (i < 5) {
                        worldIn.setBlockState(pos, state.withProperty(BITES, i + 1), 3);
                    } else {
                        worldIn.setBlockToAir(pos);
                    }
                }
            } else if (playerIn.isSneaking()) {
                spawnAsEntity(worldIn, pos, new ItemStack(this.pizzaslice));
                if (i < 5) {
                    worldIn.setBlockState(pos, state.withProperty(BITES, i + 1), 3);
                } else {
                    worldIn.setBlockToAir(pos);
                }
            }

            return this.eatCake(worldIn, pos, state, playerIn);
        }
    }

    @Shadow
    private boolean eatCake(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }
}
