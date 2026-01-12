package com.seriouscreeper.bladditions.mixins.modsupport.embers;

import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import teamroots.embers.block.BlockBase;
import teamroots.embers.block.BlockSeedNew;

@Mixin(value = BlockSeedNew.class)
public class MixinBlockSeedNew extends BlockBase {
    public MixinBlockSeedNew(Material material, String name, boolean addToTab) {
        super(material, name, addToTab);
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void ie_changeRenderProps(
            Material material,
            String name,
            boolean addToTab,
            CallbackInfo ci
    ) {
        this.layer = BlockRenderLayer.TRANSLUCENT;
        this.setIsFullCube(false);
        this.setIsOpaqueCube(false);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
