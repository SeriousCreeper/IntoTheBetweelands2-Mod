package com.seriouscreeper.bladditions.mixins.modsupport;

import com.tiviacz.pizzacraft.blocks.BlockChoppingBoard;
import com.tiviacz.pizzacraft.crafting.chopping.ChoppingBoardRecipes;
import com.tiviacz.pizzacraft.crafting.chopping.ChoppingBoardUtils;
import com.tiviacz.pizzacraft.init.ModItems;
import com.tiviacz.pizzacraft.init.ModPotions;
import com.tiviacz.pizzacraft.tileentity.TileEntityChoppingBoard;
import growthcraft.core.shared.item.ItemTest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static com.tiviacz.pizzacraft.blocks.BlockChoppingBoard.FACING;

@Mixin(value = BlockChoppingBoard.class, remap = false)
public class MixinBlockChoppingBoard {
    @Shadow
    private boolean canHit(EnumFacing facing, float hitX, float hitZ, int slot) {
        return false;
    }

    @Inject(method = "Lcom/tiviacz/pizzacraft/blocks/BlockChoppingBoard;func_180639_a(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;Lnet/minecraft/util/EnumFacing;FFF)Z", at = @At("HEAD"), cancellable = true)
    public void onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> cir) {
        if (hand == EnumHand.MAIN_HAND) {
            TileEntityChoppingBoard tile = (TileEntityChoppingBoard)worldIn.getTileEntity(pos);
            IItemHandler inv = (IItemHandler)tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (EnumFacing)null);
            ItemStack stack = playerIn.getHeldItem(hand);
            if (ChoppingBoardUtils.isItemValid(stack)) {
                if (inv.getStackInSlot(0).isEmpty() && this.canHit((EnumFacing)state.getValue(FACING), hitX, hitZ, 0)) {
                    playerIn.setHeldItem(hand, inv.insertItem(0, stack, false));
                    cir.setReturnValue(true);
                    return;
                }
            } else {
                if (!inv.getStackInSlot(0).isEmpty() && stack.isEmpty() && this.canHit((EnumFacing)state.getValue(FACING), hitX, hitZ, 0)) {
                    playerIn.setHeldItem(hand, inv.extractItem(0, 64, false));
                    cir.setReturnValue(true);
                    return;
                }

                if (ItemTest.itemMatchesOre(stack, "toolKnife") && this.canHit((EnumFacing)state.getValue(FACING), hitX, hitZ, 0) && !inv.getStackInSlot(0).isEmpty()) {
                    if (tile.chop()) {
                        if (ChoppingBoardRecipes.instance().getChoppingResult(tile.inventory.getStackInSlot(0)).getItem() == ModItems.ONION_SLICE) {
                            playerIn.addPotionEffect(new PotionEffect(ModPotions.EYE_IRRITATION_EFFECT, 100, 0, true, true));
                        }

                        stack.damageItem(1, playerIn);
                        worldIn.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        cir.setReturnValue(true);
                        return;
                    }
                } else {
                    if (!inv.getStackInSlot(1).isEmpty() && this.canHit((EnumFacing)state.getValue(FACING), hitX, hitZ, 1) && stack.isEmpty()) {
                        playerIn.setHeldItem(hand, inv.extractItem(1, 64, false));
                        cir.setReturnValue(true);
                        return;
                    }

                    if (inv.getStackInSlot(2).isEmpty() && ItemTest.itemMatchesOre(stack, "toolKnife") && this.canHit((EnumFacing)state.getValue(FACING), hitX, hitZ, 1)) {
                        playerIn.setHeldItem(hand, inv.insertItem(2, stack, false));
                        cir.setReturnValue(true);
                        return;
                    }

                    if (!inv.getStackInSlot(2).isEmpty() && stack.isEmpty() && this.canHit((EnumFacing)state.getValue(FACING), hitX, hitZ, 1)) {
                        playerIn.setHeldItem(hand, inv.extractItem(2, 64, false));
                        cir.setReturnValue(true);
                        return;
                    }
                }
            }
        }

        cir.setReturnValue(false);
        return;
    }
}
