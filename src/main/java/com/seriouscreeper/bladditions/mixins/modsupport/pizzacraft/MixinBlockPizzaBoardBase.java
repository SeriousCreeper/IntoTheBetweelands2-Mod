package com.seriouscreeper.bladditions.mixins.modsupport.pizzacraft;

import com.tiviacz.pizzacraft.blocks.BlockPizza;
import com.tiviacz.pizzacraft.blocks.BlockPizzaBoardBase;
import com.tiviacz.pizzacraft.handlers.ConfigHandler;
import com.tiviacz.pizzacraft.init.ModBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = BlockPizzaBoardBase.class, remap = false)
public class MixinBlockPizzaBoardBase extends BlockPizza {
    public MixinBlockPizzaBoardBase(String name, Material material, int foodstats, Float saturation, Item pizzaslice) {
        super(name, material, foodstats, saturation, pizzaslice);
    }

    @Shadow
    private Item pizzaslice;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack helditem = playerIn.getHeldItem(hand);
        int i = (Integer)state.getValue(BITES);
        if (worldIn.isRemote) {
            return true;
        } else {
            if (helditem.isEmpty() && playerIn.isSneaking() && i == 0 && (playerIn.canEat(true) || playerIn.canEat(false))) {
                spawnAsEntity(worldIn, pos, new ItemStack(this));
                worldIn.setBlockToAir(pos);
            }

            if (ConfigHandler.isKnifeNeeded) {
                if (epicsquid.roots.init.ModItems.knives.contains(helditem.getItem())) {
                    spawnAsEntity(worldIn, pos, new ItemStack(this.pizzaslice));
                    helditem.damageItem(1, playerIn);
                    if (i < 5) {
                        worldIn.setBlockState(pos, state.withProperty(BITES, i + 1), 3);
                    } else {
                        worldIn.setBlockState(pos, ModBlocks.PIZZA_BOARD.getDefaultState(), 3);
                    }
                }
            } else if (playerIn.isSneaking()) {
                spawnAsEntity(worldIn, pos, new ItemStack(this.pizzaslice));
                if (i < 5) {
                    worldIn.setBlockState(pos, state.withProperty(BITES, i + 1), 3);
                } else {
                    worldIn.setBlockState(pos, ModBlocks.PIZZA_BOARD.getDefaultState(), 3);
                }
            }

            return this.eatCake(worldIn, pos, state, playerIn);
        }
    }

    @Shadow
    private boolean eatCake(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }
}
