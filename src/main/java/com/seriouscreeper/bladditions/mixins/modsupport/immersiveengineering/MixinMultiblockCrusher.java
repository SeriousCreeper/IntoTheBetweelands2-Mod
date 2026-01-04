package com.seriouscreeper.bladditions.mixins.modsupport.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.blocks.multiblocks.MultiblockCrusher;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
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
import thebetweenlands.common.registries.BlockRegistry;

@Mixin(value = MultiblockCrusher.class, remap = false)
public class MixinMultiblockCrusher {
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
        if (block == Blocks.HOPPER) {
            return Utils.isBlockAt(world, pos, BlockRegistry.SYRMORITE_HOPPER, -1);
        }

        return Utils.isBlockAt(world, pos, block, meta);
    }

    @Shadow private static ItemStack[][][] structure;
    @Final @Shadow private static IngredientStack[] materials;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void bladditions$replaceHopperStacks(CallbackInfo ci) {

        // Replace structure preview hoppers
        for (int h = 0; h < 3; h++) {
            for (int l = 0; l < 3; l++) {
                for (int w = 0; w < 5; w++) {
                    ItemStack stack = structure[h][l][w];
                    if (stack != null && stack.getItem() == Item.getItemFromBlock(Blocks.HOPPER)) {
                        structure[h][l][w] = new ItemStack(BlockRegistry.SYRMORITE_HOPPER);
                    }
                }
            }
        }

        // Replace the last "hopper x9" ingredient (it is last in this class)
        materials[materials.length - 1] = new IngredientStack(new ItemStack(BlockRegistry.SYRMORITE_HOPPER, 9));
    }
}
