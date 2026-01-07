package com.seriouscreeper.bladditions.mixins.modsupport.basketcase;

import com.daeruin.basketcase.inventory.ItemStackHandlerBasket;
import com.daeruin.basketcase.tileentity.TileEntityBasket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import vazkii.quark.api.IDropoffManager;

import java.util.function.Supplier;

@Mixin(value = TileEntityBasket.class)
public class MixinTileEntityBasket implements IDropoffManager {
    @Shadow
    private ItemStackHandlerBasket inventory;

    @Override
    public boolean acceptsDropoff(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public IItemHandler getDropoffItemHandler(Supplier<IItemHandler> defaultSupplier) {
        return inventory;
    }
}
