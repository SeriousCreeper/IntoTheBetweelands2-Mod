package com.seriouscreeper.bladditions.mixins.modsupport.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.blocks.multiblocks.MultiblockArcFurnace;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.api.blocks.BlocksTC;

@Mixin(value = MultiblockArcFurnace.class, remap = false)
public class MixinMultiblockArcFurnace {

    @Shadow private static ItemStack[][][] structure;
    @Final @Shadow private static IngredientStack[] materials;

    /**
     * 1) Formation trigger: cauldron -> crucible
     *   original: return state.getBlock() == Blocks.CAULDRON;
     */
    @Redirect(
            method = "isBlockTrigger",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/state/IBlockState;getBlock()Lnet/minecraft/block/Block;"
            )
    )
    private Block bladditions$triggerBlock(IBlockState state) {
        Block b = state.getBlock();
        return (b == Blocks.CAULDRON) ? BlocksTC.crucible : b;
    }

    /**
     * 2) Structure validation: only swap the specific isBlockAt call
     *    when the original "block" arg was Blocks.CAULDRON.
     *
     * ArcFurnace.structureCheck has many isBlockAt calls; do NOT blanket-replace.
     */
    @Redirect(
            method = "structureCheck",
            at = @At(
                    value = "INVOKE",
                    target = "Lblusunrize/immersiveengineering/common/util/Utils;" +
                            "isBlockAt(Lnet/minecraft/world/World;" +
                            "Lnet/minecraft/util/math/BlockPos;" +
                            "Lnet/minecraft/block/Block;I)Z"
            )
    )
    private boolean bladditions$redirectIsBlockAt(World world, BlockPos pos, Block block, int meta) {
        if (block == Blocks.CAULDRON) {
            return Utils.isBlockAt(world, pos, BlocksTC.crucible, 32767);
        }
        return Utils.isBlockAt(world, pos, block, meta);
    }

    /**
     * 3) Manual preview + materials: replace the CAULDRON item stacks after <clinit>.
     */
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void bladditions$replaceCauldronStacks(CallbackInfo ci) {

        // Replace manual structure: Items.CAULDRON -> crucible ItemStack
        Item cauldronItem = Items.CAULDRON;
        Item crucibleItem = Item.getItemFromBlock(BlocksTC.crucible);

        for (int h = 0; h < 5; h++) {
            for (int l = 0; l < 5; l++) {
                for (int w = 0; w < 5; w++) {
                    ItemStack stack = structure[h][l][w];
                    if (!stack.isEmpty() && stack.getItem() == cauldronItem) {
                        structure[h][l][w] = new ItemStack(crucibleItem);
                    }
                }
            }
        }

        // Replace materials[0] which is the cauldron ingredient in this class
        // (it is first in the array as decompiled) :contentReference[oaicite:3]{index=3}
        materials[0] = new IngredientStack(new ItemStack(crucibleItem));
    }

    @Inject(method = "getBlockstateFromStack", at  = @At("HEAD"), cancellable = true)
    private void getBlockstateFromStack(int index, ItemStack stack, CallbackInfoReturnable<IBlockState> cir) {
        if (!stack.isEmpty()) {
            if (stack.getItem() == Items.CAULDRON) {
                cir.setReturnValue(BlocksTC.crucible.getDefaultState());
                cir.cancel();
            }
        }
    }
}
