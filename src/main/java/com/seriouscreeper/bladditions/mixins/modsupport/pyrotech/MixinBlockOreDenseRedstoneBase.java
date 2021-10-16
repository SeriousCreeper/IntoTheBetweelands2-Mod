package com.seriouscreeper.bladditions.mixins.modsupport.pyrotech;

import com.codetaylor.mc.pyrotech.modules.core.block.spi.BlockOreDenseRedstoneBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import pyre.coloredredstone.util.EnumColor;

import java.util.Random;

@Mixin(value = BlockOreDenseRedstoneBase.class, remap = false)
public class MixinBlockOreDenseRedstoneBase {
    /**
     * @author SC
     */
    @Overwrite
    protected void spawnParticles(World world, BlockPos pos, int particleCount) {
        Random random = world.rand;

        for(int i = 0; i < particleCount; ++i) {
            double d1 = (double)((float)pos.getX() + random.nextFloat());
            double d2 = (double)((float)pos.getY() + random.nextFloat());
            double d3 = (double)((float)pos.getZ() + random.nextFloat());
            if (i == 0 && !world.getBlockState(pos.up()).isOpaqueCube()) {
                d2 = (double)pos.getY() + 0.0625D + 1.0D;
            }

            if (i == 1 && !world.getBlockState(pos.down()).isOpaqueCube()) {
                d2 = (double)pos.getY() - 0.0625D;
            }

            if (i == 2 && !world.getBlockState(pos.south()).isOpaqueCube()) {
                d3 = (double)pos.getZ() + 0.0625D + 1.0D;
            }

            if (i == 3 && !world.getBlockState(pos.north()).isOpaqueCube()) {
                d3 = (double)pos.getZ() - 0.0625D;
            }

            if (i == 4 && !world.getBlockState(pos.east()).isOpaqueCube()) {
                d1 = (double)pos.getX() + 0.0625D + 1.0D;
            }

            if (i == 5 && !world.getBlockState(pos.west()).isOpaqueCube()) {
                d1 = (double)pos.getX() - 0.0625D;
            }

            if (d1 < (double)pos.getX() || d1 > (double)(pos.getX() + 1) || d2 < 0.0D || d2 > (double)(pos.getY() + 1) || d3 < (double)pos.getZ() || d3 > (double)(pos.getZ() + 1)) {
                EnumColor color = EnumColor.LIGHT_BLUE;
                double red = (double)((float)color.getShades()[15].getR() / 255.0F);
                double green = (double)((float)color.getShades()[15].getG() / 255.0F);
                double blue = (double)((float)color.getShades()[15].getB() / 255.0F);

                world.spawnParticle(EnumParticleTypes.REDSTONE, d1, d2, d3, red, green, blue, new int[0]);
            }
        }

    }
}
