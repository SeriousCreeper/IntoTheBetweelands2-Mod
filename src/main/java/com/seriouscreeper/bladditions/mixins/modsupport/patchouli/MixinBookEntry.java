package com.seriouscreeper.bladditions.mixins.modsupport.patchouli;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.patchouli.client.base.ClientAdvancements;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.EntryDisplayState;
import vazkii.patchouli.common.book.Book;

@Mixin(value = BookEntry.class, remap = false)
public abstract class MixinBookEntry {
    @Unique
    private String gamestage;

    @Shadow transient boolean locked;
    @Shadow transient Book book;

    @Shadow public abstract String getName();

    @Shadow public abstract void markReadStateDirty();

    @Inject(method = "updateLockStatus", at = @At("RETURN"))
    private void patchouli_gamestage_lock(CallbackInfo ci) {
        if (gamestage != null && !gamestage.isEmpty()) {
            EntityPlayer player = Minecraft.getMinecraft().player;

            if (!GameStageHelper.hasStage(player, gamestage)) {
                locked = true;
                this.book.markUpdated();
                this.markReadStateDirty();
            }
        }
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initConstructor(CallbackInfo ci) {
        this.gamestage = null;
    }
}
