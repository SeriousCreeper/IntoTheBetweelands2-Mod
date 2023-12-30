package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import com.seriouscreeper.bladditions.BLAdditions;
import com.seriouscreeper.bladditions.proxy.CommonProxy;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.world.gen.feature.structure.utils.SludgeWormMazeBlockHelper;

import java.util.Random;

@Mixin(value = SludgeWormMazeBlockHelper.class, remap = false)
public class MixinSludgeWormMazeBlockHelper {
    /**
     * @author SC
     * @reason Replace ancient armor
     */
    @Overwrite
    public void placeArmourStandLoot(World world, BlockPos pos, EnumFacing facing, Random rand) {
        world.setBlockState(pos, BlockRegistry.WEEDWOOD_CHEST.getDefaultState(), 2);
        TileEntity tile = world.getTileEntity(pos);
        ((TileEntityChest)tile).setLootTable(BLAdditions.ANCIENT_ARMOR_CHEST, world.rand.nextLong());
    }
}
