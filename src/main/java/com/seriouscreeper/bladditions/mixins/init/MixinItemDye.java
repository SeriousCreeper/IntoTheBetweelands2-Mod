package com.seriouscreeper.bladditions.mixins.init;

import com.seriouscreeper.bladditions.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemDye.class)
public class MixinItemDye {
    @Inject(method = "onItemUse", at = @At("HEAD"), cancellable = true)
    private void injectOnItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, CallbackInfoReturnable<EnumActionResult> cir) {
        CommonProxy.DyeBotaniaManaPool(player, world, pos, hand, facing, hitX, hitY, hitZ, cir);
    }
}
