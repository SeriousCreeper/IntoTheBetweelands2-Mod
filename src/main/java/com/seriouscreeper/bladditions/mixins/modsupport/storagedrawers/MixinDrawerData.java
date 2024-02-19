package com.seriouscreeper.bladditions.mixins.modsupport.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawer;
import com.jaquadro.minecraft.storagedrawers.block.tile.tiledata.StandardDrawerGroup;
import com.seriouscreeper.bladditions.util.BlockedItemsHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

@Mixin(value = StandardDrawerGroup.DrawerData.class, remap = false)
public class MixinDrawerData implements IDrawer {
    @Inject(method = "canItemBeStored", at = @At("HEAD"), cancellable = true)
    private void injectCanitemBeStored(ItemStack itemPrototype, Predicate<ItemStack> matchPredicate, CallbackInfoReturnable<Boolean> cir) {
        if(BlockedItemsHelper.isBlocked(itemPrototype)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "setStoredItem(Lnet/minecraft/item/ItemStack;Z)Lcom/jaquadro/minecraft/storagedrawers/api/storage/IDrawer;", at = @At("HEAD"), cancellable = true)
    private void injectSetStoredItem(ItemStack itemPrototype, boolean notify, CallbackInfoReturnable<IDrawer> cir) {
        if(BlockedItemsHelper.isBlocked(itemPrototype)) {
            this.reset(notify);
            cir.setReturnValue(this);
        }
    }

    @Shadow
    protected void reset(boolean notify) {
    }

    @Shadow
    public ItemStack getStoredItemPrototype() {
        return null;

    }

    @Nonnull
    @Shadow
    public IDrawer setStoredItem(@Nonnull ItemStack itemStack) {
        return null;
    }

    @Shadow
    public int getStoredItemCount() {
        return 0;
    }

    @Shadow
    public void setStoredItemCount(int i) {

    }

    @Shadow
    public int getMaxCapacity(@Nonnull ItemStack itemStack) {
        return 0;
    }

    @Shadow
    public int getRemainingCapacity() {
        return 0;
    }

    @Shadow
    public boolean canItemBeStored(@Nonnull ItemStack itemStack, Predicate<ItemStack> predicate) {
        return false;
    }

    @Shadow
    public boolean canItemBeExtracted(@Nonnull ItemStack itemStack, Predicate<ItemStack> predicate) {
        return false;
    }

    @Shadow
    public boolean isEmpty() {
        return false;
    }
}
