package com.seriouscreeper.bladditions.mixins.modsupport.embers;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.api.upgrades.UpgradeUtil;
import teamroots.embers.recipe.ItemMeltingRecipe;
import teamroots.embers.tileentity.TileEntityFurnaceBottom;
import teamroots.embers.tileentity.TileEntityFurnaceTop;

import java.util.List;

@Mixin(value = TileEntityFurnaceBottom.class)
public class MixinTileEntityFurnaceBottom extends TileEntity {
    @Shadow
    private List<IUpgradeProvider> upgrades;

    @Redirect(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lteamroots/embers/api/upgrades/UpgradeUtil;doWork(Lnet/minecraft/tileentity/TileEntity;Ljava/util/List;)Z"
            )
    )
    private boolean redirectDoWork(TileEntity tile, List<IUpgradeProvider> list) {
        boolean cancel = UpgradeUtil.doWork(tile, upgrades);

        // Check recipe validity before continuing
        TileEntityFurnaceTop top = (TileEntityFurnaceTop) this.getWorld().getTileEntity(this.getPos().up());
        if (top != null && !top.inventory.getStackInSlot(0).isEmpty()) {
            ItemMeltingRecipe recipe = this.getRecipe(top.inventory.getStackInSlot(0));
            if (recipe == null) {
                // Cancel work if no valid recipe exists
                return true;
            }
        }

        return cancel;
    }

    @Shadow
    private ItemMeltingRecipe getRecipe(ItemStack recipeStack) {
        return null;
    }
}
