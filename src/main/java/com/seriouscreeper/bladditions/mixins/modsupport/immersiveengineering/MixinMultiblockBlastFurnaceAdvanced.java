package com.seriouscreeper.bladditions.mixins.modsupport.immersiveengineering;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.multiblocks.MultiblockBlastFurnaceAdvanced;
import blusunrize.immersiveengineering.common.blocks.stone.BlockTypes_StoneDecoration;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thebetweenlands.common.registries.BlockRegistry;

@Mixin(value = MultiblockBlastFurnaceAdvanced.class, remap = false)
public class MixinMultiblockBlastFurnaceAdvanced {
    @Shadow
    static ItemStack[][][] structure = new ItemStack[4][3][3];
    @Shadow static ItemStack renderStack;
    @Mutable
    @Final
    @Shadow static IngredientStack[] materials = new IngredientStack[0];

    @Shadow public static MultiblockBlastFurnaceAdvanced instance;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void injected(CallbackInfo ci) {
        for(int h = 0; h < 4; ++h) {
            for(int l = 0; l < 3; ++l) {
                for(int w = 0; w < 3; ++w) {
                    if (h == 3 && w == 1 && l == 1) {
                        structure[h][l][w] = new ItemStack(BlockRegistry.SYRMORITE_HOPPER);
                    } else if (h < 3) {
                        structure[h][l][w] = new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.BLASTBRICK_REINFORCED.getMeta());
                    }
                }
            }
        }

        renderStack = ItemStack.EMPTY;
        materials = new IngredientStack[]{new IngredientStack(new ItemStack(IEContent.blockStoneDecoration, 27, BlockTypes_StoneDecoration.BLASTBRICK_REINFORCED.getMeta())), ApiUtils.createIngredientStack(BlockRegistry.SYRMORITE_HOPPER)};
    }


    @ModifyArg(method = "createStructure", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/common/util/Utils;isBlockAt(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)Z"), index = 2)
    private Block injected(Block b) {
        if(b == Blocks.HOPPER) {
            return BlockRegistry.SYRMORITE_HOPPER;
        } else {
            return b;
        }
    }
}
