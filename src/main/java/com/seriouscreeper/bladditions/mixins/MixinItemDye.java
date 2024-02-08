package com.seriouscreeper.bladditions.mixins;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.api.mana.IManaPool;

@Mixin(value = ItemDye.class)
public class MixinItemDye {
    @Inject(method = "onItemUse", at = @At("HEAD"), cancellable = true)
    private void injectOnItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, CallbackInfoReturnable<EnumActionResult> cir) {
        ItemStack stack = player.getHeldItem(hand);
        EnumDyeColor color = EnumDyeColor.byMetadata(15 - stack.getItemDamage());
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IManaPool) {
            IManaPool pool = (IManaPool)tile;

            if (color != pool.getColor()) {
                pool.setColor(color);
                stack.shrink(1);
                cir.setReturnValue(EnumActionResult.SUCCESS);
            }
        }
    }
}
