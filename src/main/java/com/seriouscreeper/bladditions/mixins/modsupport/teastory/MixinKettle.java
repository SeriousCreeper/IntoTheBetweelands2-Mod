package com.seriouscreeper.bladditions.mixins.modsupport.teastory;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import roito.teastory.block.Kettle;

@Mixin(value = Kettle.class)
public class MixinKettle extends BlockHorizontal {
    protected MixinKettle(Material materialIn) {
        super(materialIn);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
}
