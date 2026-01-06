package com.seriouscreeper.bladditions.mixins.modsupport.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.blocks.multiblocks.MultiblockFermenter;
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
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.api.blocks.BlocksTC;

@Mixin(value = MultiblockFermenter.class, remap = false)
public class MixinMultiblockFermenter {
    @Shadow private static ItemStack[][][] structure;
    @Final @Shadow private static IngredientStack[] materials;

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
            // Fermenter passes meta 0; crucible probably ignores meta, so use wildcard
            return Utils.isBlockAt(world, pos, BlocksTC.crucible, 32767);
        }
        return Utils.isBlockAt(world, pos, block, meta);
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void bladditions$replaceCauldronStacks(CallbackInfo ci) {
        Item cauldronItem = Items.CAULDRON;
        Item crucibleItem = Item.getItemFromBlock(BlocksTC.crucible);

        // Replace manual structure cauldron item-stacks
        for (int h = 0; h < 3; h++) {
            for (int l = 0; l < 3; l++) {
                for (int w = 0; w < 3; w++) {
                    ItemStack stack = structure[h][l][w];
                    if (stack != null && !stack.isEmpty() && stack.getItem() == cauldronItem) {
                        structure[h][l][w] = new ItemStack(crucibleItem);
                    }
                }
            }
        }

        materials[materials.length - 2] = new IngredientStack(new ItemStack(crucibleItem, 4, 0));
    }

    @Inject(method = "getBlockstateFromStack", at = @At("HEAD"), cancellable = true)
    public void getBlockstateFromStack(int index, ItemStack stack, CallbackInfoReturnable<IBlockState> cir) {
        if (!stack.isEmpty()) {
            if (stack.getItem() == Items.CAULDRON) {
                cir.setReturnValue(BlocksTC.crucible.getDefaultState());
            }
        }
    }

    @Overwrite
    public boolean isBlockTrigger(IBlockState state) {
        return state.getBlock() == BlocksTC.crucible;
    }
}
