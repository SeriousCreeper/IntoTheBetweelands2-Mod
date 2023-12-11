package com.seriouscreeper.bladditions.mixins.modsupport.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsAll;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration1;
import blusunrize.immersiveengineering.common.blocks.multiblocks.MultiblockBottlingMachine;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import teamroots.embers.RegistryManager;
import thebetweenlands.common.registries.BlockRegistry;

@Mixin(value = MultiblockBottlingMachine.class, remap = false)
public class MixinMultiblockBottlingMachine {
    @Shadow static ItemStack[][][] structure = new ItemStack[3][2][3];
    @Shadow static ItemStack renderStack;
    @Mutable
    @Final
    @Shadow static IngredientStack[] materials = new IngredientStack[0];


    @Shadow public static MultiblockBottlingMachine instance;

    @ModifyArg(method = "structureCheck", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/common/util/Utils;isBlockAt(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)Z"), index = 2)
    private Block injected(Block b) {
        if(b == IEContent.blockMetalDevice0) {
            return RegistryManager.mechanical_pump;
        } else {
            return b;
        }
    }


    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void injected(CallbackInfo ci) {
        for(int h = 0; h < 3; ++h) {
            for(int l = 0; l < 2; ++l) {
                for(int w = 0; w < 3; ++w) {
                    if (h == 0) {
                        if (l == 0 && w == 1) {
                            structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta());
                        } else if (l == 1 && w == 0) {
                            structure[h][l][w] = new ItemStack(IEContent.blockSheetmetal, 1, BlockTypes_MetalsAll.IRON.getMeta());
                        } else if (l == 1 && w == 2) {
                            structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
                        } else {
                            structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta());
                        }
                    } else if (h == 1) {
                        if (l == 0) {
                            structure[h][l][w] = ConveyorHandler.getConveyorStack("immersiveengineering:conveyor");
                        } else if (l == 1 && w == 0) {
                            structure[h][l][w] = new ItemStack(IEContent.blockSheetmetal, 1, BlockTypes_MetalsAll.IRON.getMeta());
                        } else if (l == 1 && w == 2) {
                            structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
                        } else {
                            structure[h][l][w] = new ItemStack(RegistryManager.mechanical_pump);
                        }
                    } else if (h == 2) {
                        if (l == 0 && w == 1) {
                            structure[h][l][w] = new ItemStack(BlockRegistry.FILTERED_SILT_GLASS);
                        } else if (l == 1 && w == 1) {
                            structure[h][l][w] = new ItemStack(RegistryManager.mechanical_pump);
                        }
                    }
                }
            }
        }

        renderStack = ItemStack.EMPTY;
        materials = new IngredientStack[]{new IngredientStack("scaffoldingSteel", 3), new IngredientStack("blockSheetmetalIron", 2), new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta())), new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 2, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta())), new IngredientStack(Utils.copyStackWithAmount(ConveyorHandler.getConveyorStack("immersiveengineering:conveyor"), 3)), new IngredientStack(new ItemStack(RegistryManager.mechanical_pump)), new IngredientStack(new ItemStack(BlockRegistry.FILTERED_SILT_GLASS))};
    }
}
