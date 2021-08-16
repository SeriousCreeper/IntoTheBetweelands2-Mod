package com.seriouscreeper.bladditions.mixins.modsupport;

import com.tiviacz.pizzacraft.PizzaCraft;
import com.tiviacz.pizzacraft.blocks.BlockPizzaOven;
import com.tiviacz.pizzacraft.blocks.BlockPizzaOvenBurning;
import com.tiviacz.pizzacraft.init.ModBlocks;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.ItemRegistry;

import static net.minecraft.block.Block.spawnAsEntity;

@Mixin(value = BlockPizzaOven.class, remap = false)
public class MixinPizzaOven {
    @Shadow
    public static final PropertyInteger STATE = PropertyInteger.create("wood", 0, 4);


    @Inject(method = "Lcom/tiviacz/pizzacraft/blocks/BlockPizzaOven;func_180639_a(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;Lnet/minecraft/util/EnumFacing;FFF)Z", at = @At("HEAD"), cancellable = true)
    public void onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> cir)
    {
        if(!worldIn.isRemote)
        {
            ItemStack helditem = playerIn.getHeldItem(hand);
            int a = state.getValue(STATE).intValue();

            if(helditem.getItem() == ItemRegistry.ITEMS_MISC && helditem.getMetadata() == 20 && (a == 0 || a == 1 || a == 2))
            {
                worldIn.setBlockState(pos, state.withProperty(STATE, a + 1), 3);
                helditem.shrink(1);
            }

            if(helditem.getItem() == Item.getItemFromBlock(BlockRegistry.PEAT) && a == 0)
            {
                worldIn.setBlockState(pos, state.withProperty(STATE, 4), 3);
                helditem.shrink(1);
            }

            if(helditem.isEmpty() && a > 0)
            {
                if(a == 4)
                {
                    worldIn.setBlockState(pos, state.withProperty(STATE, 0), 3);
                    playerIn.inventory.addItemStackToInventory(new ItemStack(BlockRegistry.PEAT));
                }
                else
                {
                    worldIn.setBlockState(pos, state.withProperty(STATE, a - 1), 3);
                    playerIn.inventory.addItemStackToInventory(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 20));
                }
            }

            if(helditem.getItem() == Items.FLINT_AND_STEEL || helditem.getItem() == ItemRegistry.OCTINE_INGOT)
            {
                if(a != 0)
                {
                    worldIn.setBlockState(pos, ModBlocks.BURNING_PIZZA_OVEN.getDefaultState().withProperty(BlockPizzaOvenBurning.FIRE, a - 1), 3);
                }
            }

            if(worldIn.isAirBlock(pos.up()))
            {
                if(helditem.getItem() == Item.getItemFromBlock(ModBlocks.RAW_PIZZA_0))
                {
                    worldIn.setBlockState(pos.up(), ModBlocks.RAW_PIZZA_0.getDefaultState(), 3);
                    helditem.shrink(1);
                }

                else if(helditem.getItem() == Item.getItemFromBlock(ModBlocks.RAW_PIZZA_1))
                {
                    worldIn.setBlockState(pos.up(), ModBlocks.RAW_PIZZA_1.getDefaultState(), 3);
                    helditem.shrink(1);
                }

                else if(helditem.getItem() == Item.getItemFromBlock(ModBlocks.RAW_PIZZA_2))
                {
                    worldIn.setBlockState(pos.up(), ModBlocks.RAW_PIZZA_2.getDefaultState(), 3);
                    helditem.shrink(1);
                }

                else if(helditem.getItem() == Item.getItemFromBlock(ModBlocks.RAW_PIZZA_3))
                {
                    worldIn.setBlockState(pos.up(), ModBlocks.RAW_PIZZA_3.getDefaultState(), 3);
                    helditem.shrink(1);
                }

                else if(helditem.getItem() == Item.getItemFromBlock(ModBlocks.RAW_PIZZA_4))
                {
                    worldIn.setBlockState(pos.up(), ModBlocks.RAW_PIZZA_4.getDefaultState(), 3);
                    helditem.shrink(1);
                }

                else if(helditem.getItem() == Item.getItemFromBlock(ModBlocks.RAW_PIZZA_5))
                {
                    worldIn.setBlockState(pos.up(), ModBlocks.RAW_PIZZA_5.getDefaultState(), 3);
                    helditem.shrink(1);
                }

                else if(helditem.getItem() == Item.getItemFromBlock(ModBlocks.RAW_PIZZA_6))
                {
                    worldIn.setBlockState(pos.up(), ModBlocks.RAW_PIZZA_6.getDefaultState(), 3);
                    helditem.shrink(1);
                }

                else if(helditem.getItem() == Item.getItemFromBlock(ModBlocks.RAW_PIZZA_7))
                {
                    worldIn.setBlockState(pos.up(), ModBlocks.RAW_PIZZA_7.getDefaultState(), 3);
                    helditem.shrink(1);
                }

                else if(helditem.getItem() == Item.getItemFromBlock(ModBlocks.RAW_PIZZA_8))
                {
                    worldIn.setBlockState(pos.up(), ModBlocks.RAW_PIZZA_8.getDefaultState(), 3);
                    helditem.shrink(1);
                }

                else if(helditem.getItem() == Item.getItemFromBlock(ModBlocks.RAW_PIZZA_9))
                {
                    worldIn.setBlockState(pos.up(), ModBlocks.RAW_PIZZA_9.getDefaultState(), 3);
                    helditem.shrink(1);
                }

                else if(helditem.getItem() == Item.getItemFromBlock(ModBlocks.RAW_PIZZA_10))
                {
                    worldIn.setBlockState(pos.up(), ModBlocks.RAW_PIZZA_10.getDefaultState(), 3);
                    helditem.shrink(1);
                }
            }
        }

        cir.setReturnValue(true);
    }


    @Inject(method = "Lcom/tiviacz/pizzacraft/blocks/BlockPizzaOven;func_176208_a(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/player/EntityPlayer;)V", at = @At("HEAD"), cancellable = true)
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, CallbackInfo ci) {
        int a = (Integer)state.getValue(STATE);
        if (!worldIn.isRemote && !player.capabilities.isCreativeMode) {
            if (a != 4) {
                if (a != 0) {
                    spawnAsEntity(worldIn, pos, new ItemStack(ItemRegistry.ITEMS_MISC, a, 20));
                }
            }
            else
            {
                spawnAsEntity(worldIn, pos, new ItemStack(BlockRegistry.PEAT));
            }
        }

        ci.cancel();
    }
}
