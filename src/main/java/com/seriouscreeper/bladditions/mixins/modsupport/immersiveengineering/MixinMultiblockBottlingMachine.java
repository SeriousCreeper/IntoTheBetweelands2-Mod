package com.seriouscreeper.bladditions.mixins.modsupport.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsAll;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration1;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice0;
import blusunrize.immersiveengineering.common.blocks.multiblocks.MultiblockBottlingMachine;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
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

    /**
     * @author SC
     * @reason
     */
    @Overwrite

    boolean structureCheck(World world, BlockPos startPos, EnumFacing dir, boolean mirror) {
        EnumFacing conveyorDir = mirror ? dir.rotateYCCW() : dir.rotateY();

        for(int l = 0; l < 2; ++l) {
            for(int h = -1; h <= 1; ++h) {
                for(int w = -1; w <= 1; ++w) {
                    BlockPos pos2 = startPos.offset(dir, l).offset(conveyorDir, w).add(0, h, 0);
                    if (h == -1) {
                        if (l == 0 && w == 0) {
                            if (!Utils.isBlockAt(world, pos2, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta())) {
                                return false;
                            }
                        } else if (w == -1 && l == 1) {
                            if (!Utils.isOreBlockAt(world, pos2, "blockSheetmetalIron")) {
                                return false;
                            }
                        } else if (w == 1 && l == 1) {
                            if (!Utils.isBlockAt(world, pos2, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta())) {
                                return false;
                            }
                        } else if (!Utils.isOreBlockAt(world, pos2, "scaffoldingSteel")) {
                            return false;
                        }
                    } else if (h == 0) {
                        if (l == 0) {
                            if (!ConveyorHandler.isConveyor(world, pos2, "immersiveengineering:conveyor", conveyorDir)) {
                                return false;
                            }
                        } else if (w == -1 && l == 1) {
                            if (!Utils.isOreBlockAt(world, pos2, "blockSheetmetalIron")) {
                                return false;
                            }
                        } else if (w == 1 && l == 1) {
                            if (!Utils.isBlockAt(world, pos2, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta())) {
                                return false;
                            }
                        } else if (!Utils.isBlockAt(world, pos2, IEContent.blockMetalDevice0, BlockTypes_MetalDevice0.FLUID_PUMP.getMeta())) {
                            return false;
                        }
                    } else if (h == 1 && w == 0 && l == 0 && !Utils.isBlockAt(world, pos2, BlockRegistry.FILTERED_SILT_GLASS, 0)) {
                        return false;
                    }
                }
            }
        }

        return true;
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
                            structure[h][l][w] = new ItemStack(IEContent.blockMetalDevice0, 1, BlockTypes_MetalDevice0.FLUID_PUMP.getMeta());
                        }
                    } else if (h == 2) {
                        if (l == 0 && w == 1) {
                            structure[h][l][w] = new ItemStack(BlockRegistry.FILTERED_SILT_GLASS);
                        } else if (l == 1 && w == 1) {
                            structure[h][l][w] = new ItemStack(IEContent.blockMetalDevice0, 1, BlockTypes_MetalDevice0.FLUID_PUMP.getMeta());
                        }
                    }
                }
            }
        }

        renderStack = ItemStack.EMPTY;
        materials = new IngredientStack[]{new IngredientStack("scaffoldingSteel", 3), new IngredientStack("blockSheetmetalIron", 2), new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta())), new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 2, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta())), new IngredientStack(Utils.copyStackWithAmount(ConveyorHandler.getConveyorStack("immersiveengineering:conveyor"), 3)), new IngredientStack(new ItemStack(IEContent.blockMetalDevice0, 1, BlockTypes_MetalDevice0.FLUID_PUMP.getMeta())), new IngredientStack(new ItemStack(BlockRegistry.FILTERED_SILT_GLASS))};
    }
}
