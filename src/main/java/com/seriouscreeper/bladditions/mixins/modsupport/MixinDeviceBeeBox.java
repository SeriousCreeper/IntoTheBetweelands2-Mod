package com.seriouscreeper.bladditions.mixins.modsupport;

import growthcraft.bees.common.tileentity.TileEntityBeeBox;
import growthcraft.bees.common.tileentity.device.DeviceBeeBox;
import growthcraft.bees.shared.config.GrowthcraftBeesConfig;
import net.darkhax.bookshelf.util.OreDictUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.registries.BlockRegistry;

@Pseudo
@Mixin(value = DeviceBeeBox.class, remap = false)
public class MixinDeviceBeeBox {
    private final float bonus = GrowthcraftBeesConfig.beeBoxBonusMultiplier;

    /**
     * @author SC
     */
    @Overwrite
    private float calcGrowthRate(World world, BlockPos pos) {
        final int checkSize = 5;
        final int i = pos.getX() - ((checkSize - 1) / 2);
        final int k = pos.getZ() - ((checkSize - 1) / 2);
        float f = 1.0F;

        BlockPos.MutableBlockPos fpos = new BlockPos.MutableBlockPos();
        for (int loopx = -checkSize; loopx < checkSize; loopx++) {
            for (int loopz = -checkSize; loopz < checkSize; loopz++) {
                fpos.setPos(i + loopx, pos.getY(), k + loopz);

                final IBlockState flower = world.getBlockState(fpos);
                final Block flowerBlock = flower.getBlock();
                final IBlockState soil = world.getBlockState(fpos.down());
                float f1 = 0.0F;

                if (soil.getMaterial() == Material.GRASS) {
                    //f1 = 1.0F;
                    f1 = 0.36F;

                    if (isBlockFlower(flower)) {
                        //f1 = 3.0F;
                        f1 = 1.08F;
                    }
                }

                f1 /= 4.0F;

                f += f1;
            }
        }

        final TileEntityBeeBox te = getParentTile();

        if (te != null) {
            final int bees = te.countBees();
            final float div = 2.0F - (0.015625F * bees);

            f /= div;

            if (te.hasBonus()) {
                f *= this.bonus;
            }
        }

        return f;
    }

    @Shadow
    protected TileEntityBeeBox getParentTile() {
        return null;
    }

    @Shadow
    private boolean isBlockFlower(IBlockState flower) {
        return false;
    }
}
