package com.seriouscreeper.bladditions.mixins.modsupport.mysticallib;

import com.seriouscreeper.bladditions.items.PatchedItemCookedPereskia;
import epicsquid.mysticallib.event.RegisterContentEvent;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = RegisterContentEvent.class, remap = false)
public class MixinRegisterContentEvent {
    @Shadow
    private List<Item> items;


    /**
     * @author
     * @reason
     */
    @Overwrite
    public Item addItem(Item item) {
        if(item.getRegistryName().getPath().equals("cooked_pereskia")) {
            item = new PatchedItemCookedPereskia("cooked_pereskia", 5, 5, false);
        }

        this.items.add(item);
        return item;
    }
}
