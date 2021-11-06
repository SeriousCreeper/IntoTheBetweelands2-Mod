package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thaumcraft.common.lib.network.fx.PacketFXScanSource;
import thaumcraft.common.lib.utils.BlockUtils;

@Mixin(value = PacketFXScanSource.class, remap = false)
public class MixinPacketFXScanSource {
    /**
     * @author SC
     */
    @Overwrite
    private int getOreColor(World world, BlockPos pos) {
        IBlockState bi = world.getBlockState(pos);
        if (bi.getBlock() != Blocks.AIR && bi.getBlock() != Blocks.BEDROCK) {
            ItemStack is = BlockUtils.getSilkTouchDrop(bi);
            if (is == null || is.isEmpty()) {
                int md = bi.getBlock().getMetaFromState(bi);
                is = new ItemStack(bi.getBlock(), 1, md);
            }

            if (is == null || is.isEmpty() || is.getItem() == null) {
                return 12632256;
            }

            int[] od = OreDictionary.getOreIDs(is);
            if (od != null && od.length > 0) {
                int[] var6 = od;
                int var7 = od.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    int id = var6[var8];
                    if (OreDictionary.getOreName(id) != null) {
                        if (OreDictionary.getOreName(id).toUpperCase().contains("IRON")) {
                            return 14200723;
                        }

                        if (OreDictionary.getOreName(id).toUpperCase().contains("COAL")) {
                            return 1052688;
                        }

                        if (OreDictionary.getOreName(id).toUpperCase().contains("REDSTONE")) {
                            return 16711680;
                        }

                        if (OreDictionary.getOreName(id).toUpperCase().contains("GOLD")) {
                            return 16576075;
                        }

                        if (OreDictionary.getOreName(id).toUpperCase().contains("LAPIS")) {
                            return 1328572;
                        }

                        if (OreDictionary.getOreName(id).toUpperCase().contains("DIAMOND")) {
                            return 6155509;
                        }

                        if (OreDictionary.getOreName(id).toUpperCase().contains("EMERALD")) {
                            return 1564002;
                        }

                        if (OreDictionary.getOreName(id).toUpperCase().contains("QUARTZ")) {
                            return 15064789;
                        }

                        if (OreDictionary.getOreName(id).toUpperCase().contains("SILVER")) {
                            return 14342653;
                        }

                        if (OreDictionary.getOreName(id).toUpperCase().contains("COPPER")) {
                            return 16620629;
                        }

                        if (OreDictionary.getOreName(id).toUpperCase().contains("AMBER")) {
                            return 16626469;
                        }

                        if (OreDictionary.getOreName(id).toUpperCase().contains("CINNABAR")) {
                            return 10159368;
                        }

                        if (OreDictionary.getOreName(id).toUpperCase().contains("LEAD")) {
                            return 4739972;
                        }

                        if (OreDictionary.getOreName(id).toUpperCase().contains("SYRMORITE")) {
                            return 4745215;
                        }

                        if (OreDictionary.getOreName(id).toUpperCase().contains("OCTINE")) {
                            return 16364640;
                        }

                        if (OreDictionary.getOreName(id).toUpperCase().contains("VALONITE")) {
                            return 14729723;
                        }

                        if (OreDictionary.getOreName(id).toUpperCase().contains("SCABYST")) {
                            return 9223650;
                        }

                        if (OreDictionary.getOreName(id).toUpperCase().contains("ALUMINUM")) {
                            return 15514810;
                        }

                        if (OreDictionary.getOreName(id).toUpperCase().contains("NICKEL")) {
                            return 14412223;
                        }

                        if (OreDictionary.getOreName(id).toUpperCase().contains("SULFUR")) {
                            return 16770048;
                        }

                        if (OreDictionary.getOreName(id).toUpperCase().contains("BONE")) {
                            return 9162075;
                        }

                        if (OreDictionary.getOreName(id).toUpperCase().contains("TIN")) {
                            return 15724539;
                        }
                    }
                }
            }
        }

        return 12632256;
    }
}
