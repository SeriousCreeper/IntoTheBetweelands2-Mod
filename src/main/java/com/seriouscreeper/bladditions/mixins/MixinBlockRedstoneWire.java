package com.seriouscreeper.bladditions.mixins;

import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(value = BlockRedstoneWire.class, remap = true)
public class MixinBlockRedstoneWire {
    @Shadow public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);

    /**
     * @author SC
     */
    @Overwrite
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        int i = (Integer)stateIn.getValue(POWER);
        if (i != 0) {
            double d0 = (double)pos.getX() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            double d1 = (double)((float)pos.getY() + 0.0625F);
            double d2 = (double)pos.getZ() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            float f = (float)i / 15.0F;
            float f1 = f * (0.15F - 0.02f) + 0.02F;
            float f2 = f * (0.67F - 0.145F) + 0.145F;
            float f3 = f * (0.83F - 0.18F) + 0.18F;
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, (double)f1, (double)f2, (double)f3, new int[0]);
        }
    }


    /**
     * @author SC
     */
    @Overwrite
    @SideOnly(Side.CLIENT)
    public static int colorMultiplier(int p_176337_0_) {
        float f = (float)p_176337_0_ / 15.0F;

        float f1 = f * (0.15F - 0.02f) + 0.02F;
        float f2 = f * (0.67F - 0.145F) + 0.145F;
        float f3 = f * (0.83F - 0.18F) + 0.18F;

        int i = MathHelper.clamp((int)(f1 * 255.0F), 0, 255);
        int j = MathHelper.clamp((int)(f2 * 255.0F), 0, 255);
        int k = MathHelper.clamp((int)(f3 * 255.0F), 0, 255);
        return -16777216 | i << 16 | j << 8 | k;
    }
}
