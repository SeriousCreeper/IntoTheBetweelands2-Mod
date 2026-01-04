package com.seriouscreeper.bladditions.mixins.modsupport.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.blocks.multiblocks.MultiblockSqueezer;
import blusunrize.immersiveengineering.common.util.Utils;
import com.joshiegemfinder.betweenlandsredstone.ModBlocks;
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

@Mixin(value = MultiblockSqueezer.class, remap = false)
public class MixinMultiblockSqueezer {
    @Shadow private static ItemStack[][][] structure;
    @Final @Shadow private static IngredientStack[] materials;

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
        if (block == Blocks.PISTON) {
            return Utils.isBlockAt(world, pos, ModBlocks.SCABYST_PISTON, -1);
        }
        return Utils.isBlockAt(world, pos, block, meta);
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void bladditions$replacePistonStacks(CallbackInfo ci) {

        Item pistonItem = Item.getItemFromBlock(Blocks.PISTON);
        Item scabystItem = Item.getItemFromBlock(ModBlocks.SCABYST_PISTON);

        // Replace the one piston in the 3x3x3 manual structure
        for (int h = 0; h < 3; h++) {
            for (int l = 0; l < 3; l++) {
                for (int w = 0; w < 3; w++) {
                    ItemStack stack = structure[h][l][w];
                    if (stack != null && !stack.isEmpty() && stack.getItem() == pistonItem) {
                        structure[h][l][w] = new ItemStack(scabystItem);
                    }
                }
            }
        }

        materials[materials.length - 2] = new IngredientStack(new ItemStack(scabystItem));
    }
}
