package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.block.plant.BlockPlant;
import thebetweenlands.common.registries.ItemRegistry;

@Mixin(value = BlockPlant.class, remap = false)
public class MixinBlockPlant {
    /**
     * @author SC
     */
    @Overwrite
    public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
        return item.getItem() == Items.SHEARS || item.getItem() == ItemRegistry.SYRMORITE_SHEARS || item.getItem() == ItemRegistry.SILT_CRAB_CLAW;
    }
}
