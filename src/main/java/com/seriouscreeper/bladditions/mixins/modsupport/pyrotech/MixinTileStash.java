package com.seriouscreeper.bladditions.mixins.modsupport.pyrotech;

import com.codetaylor.mc.pyrotech.modules.storage.tile.TileStash;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.quark.api.IDropoffManager;

import java.util.function.Supplier;

@Mixin(value = TileStash.class, remap = false)
public class MixinTileStash implements IDropoffManager {
    @Inject(method = "hasCapability", at = @At("HEAD"), cancellable = true)
    private void bladditions$hasCapability(Capability<?> capability, EnumFacing facing, CallbackInfoReturnable<Boolean> cir) {
        if (capability == IDropoffManager.CAPABILITY) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getCapability", at = @At("HEAD"), cancellable = true)
    private <T> void bladditions$getCapability(Capability<T> capability, EnumFacing facing,
                                               CallbackInfoReturnable<T> cir) {
        if (capability == IDropoffManager.CAPABILITY) {
            cir.setReturnValue((T) this);
        }
    }

    @Override
    public boolean acceptsDropoff(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public IItemHandler getDropoffItemHandler(Supplier<IItemHandler> defaultSupplier) {
        IItemHandler base = defaultSupplier.get();
        if (base == null) return null;
        return base;
    }
}
