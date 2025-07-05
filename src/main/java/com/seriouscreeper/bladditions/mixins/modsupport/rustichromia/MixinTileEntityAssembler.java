package com.seriouscreeper.bladditions.mixins.modsupport.rustichromia;

import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rustichromia.tile.TileEntityAssembler;
import rustichromia.util.ItemStackHandlerUnique;

@Mixin(value = TileEntityAssembler.class, remap = false)
public abstract class MixinTileEntityAssembler {
    @Shadow ItemStackHandlerUnique inventory;

    @Inject(method = "<init>(I)V", at = @At("TAIL"))
    private void adjustInventory(int slots, CallbackInfo ci) {
        TileEntityAssembler self = (TileEntityAssembler)(Object)this;
        inventory = new ItemStackHandlerUnique(new ItemStackHandler(3 + slots / 2) {
            @Override
            protected void onContentsChanged(int slot) {
                self.markDirty();
            }
        });
    }
}
