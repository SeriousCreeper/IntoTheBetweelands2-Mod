package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import epicsquid.roots.item.ItemRunicShears;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thebetweenlands.common.entity.EntityTameableBL;
import thebetweenlands.common.entity.mobs.EntityGiantToad;

import javax.annotation.Nullable;

@Mixin(value = EntityGiantToad.class)
public class MixinEntityGiantToad extends EntityTameableBL {
    public MixinEntityGiantToad(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "processInteract", at = @At("HEAD"), cancellable = true)
    private void processInteract(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> cir) {
        boolean holdsEquipment = hand == EnumHand.MAIN_HAND && !player.getHeldItem(hand).isEmpty() && (player.getHeldItem(hand).getItem() instanceof ItemRunicShears);

        if (holdsEquipment) {
            player.getHeldItem(hand).interactWithEntity(player, this, hand);
            cir.setReturnValue(true);
        }
    }

    @Shadow
    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable entityAgeable) {
        return null;
    }
}
