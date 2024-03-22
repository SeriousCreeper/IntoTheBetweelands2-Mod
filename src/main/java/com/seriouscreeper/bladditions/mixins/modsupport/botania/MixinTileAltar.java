package com.seriouscreeper.bladditions.mixins.modsupport.botania;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thebetweenlands.common.registries.BlockRegistry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AltarVariant;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.block.tile.TileSimpleInventory;

@Mixin(value = TileAltar.class, remap = false)
public class MixinTileAltar extends TileSimpleInventory {
    @Shadow
    public boolean isMossy = false;

    @Inject(method = "collideEntityItem", at = @At("HEAD"), cancellable = true)
    private void colliderEntityItem(EntityItem item, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = item.getItem();

        if (!this.world.isRemote && !stack.isEmpty() && !item.isDead) {
            if (!this.isMossy && this.world.getBlockState(this.getPos()).getValue(BotaniaStateProps.ALTAR_VARIANT) == AltarVariant.DEFAULT && stack.getItem() == Item.getItemFromBlock(BlockRegistry.MOSS)) {
                this.isMossy = true;
                this.world.updateComparatorOutputLevel(this.pos, this.world.getBlockState(this.pos).getBlock());
                stack.shrink(1);
                cir.setReturnValue(true);
            }
        }
    }

    @Shadow
    public int getSizeInventory() {
        return 0;
    }
}
