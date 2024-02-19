package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import com.seriouscreeper.bladditions.util.BlockedItemsHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.common.blocks.devices.BlockHungryChest;
import thaumcraft.common.tiles.devices.TileHungryChest;

@Mixin(value = BlockHungryChest.class)
public class MixinBlockHungryChest {
    @Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    private void injectOnEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity, CallbackInfo ci) {
        TileHungryChest tileHungryChest = (TileHungryChest)world.getTileEntity(pos);

        if (tileHungryChest != null) {
            if (!world.isRemote) {
                if (entity instanceof EntityItem && !entity.isDead) {
                    if(BlockedItemsHelper.isBlocked(((EntityItem)entity).getItem())) {
                        ci.cancel();
                    }
                }
            }
        }
    }
}
