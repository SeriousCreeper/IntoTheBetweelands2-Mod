package com.seriouscreeper.bladditions.mixins.modsupport.embers;

import com.joshiegemfinder.betweenlandsredstone.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import teamroots.embers.research.subtypes.ResearchShowItem;
import thebetweenlands.common.item.misc.ItemMisc;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.ItemRegistry;

import java.util.ArrayList;

@Mixin(value = ResearchShowItem.class, remap = false)
public class MixinResearchShowItem<E> {
    @ModifyArg(method = "addItem", at = @At(value = "INVOKE", target = "Ljava/util/LinkedList;add(Ljava/lang/Object;)Z"))
    private Object injectAddItem(E e) {
        ResearchShowItem.DisplayItem item = (ResearchShowItem.DisplayItem)e;
        ArrayList<ItemStack> itemList = new ArrayList<>();

        for(ItemStack stack : item.stacks) {
            if(stack.getItem() == Items.COAL) {
                stack = ItemMisc.EnumItemMisc.SULFUR.create(1);
            } else if(stack.getItem() == Items.NETHERBRICK) {
                stack = ItemMisc.EnumItemMisc.OCTINE_NUGGET.create(1);
            } else if(stack.getItem() == Items.BLAZE_POWDER) {
                stack = ItemMisc.EnumItemMisc.UNDYING_EMBER.create(1);
            } else if(stack.getItem() == Items.REDSTONE) {
                stack = new ItemStack(ModItems.SCABYST_DUST, 1);
            } else if(stack.getItem() == Items.GUNPOWDER) {
                stack = ItemMisc.EnumItemMisc.CREMAINS.create(1);
            } else if(stack.getItem() == Items.GLOWSTONE_DUST) {
                stack = new ItemStack(BlockRegistry.WISP, 1);
            }

            itemList.add(stack);
        }

        return new ResearchShowItem.DisplayItem(item.sideText, itemList.toArray(new ItemStack[itemList.size()]));
    }
}
