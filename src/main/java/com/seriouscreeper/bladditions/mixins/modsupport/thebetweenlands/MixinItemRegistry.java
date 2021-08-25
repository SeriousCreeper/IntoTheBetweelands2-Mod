package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;
import com.seriouscreeper.bladditions.proxy.CommonProxy;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import thebetweenlands.common.registries.ItemRegistry;

import java.util.Iterator;
import java.util.List;

@Pseudo
@Mixin(value = ItemRegistry.class)
public abstract class MixinItemRegistry {
    @Shadow @Final private static List<ItemStack> INGOTS;


    @Shadow private static boolean containsItem(List<ItemStack> lst, ItemStack stack) {
        Iterator<ItemStack> var2 = lst.iterator();

        ItemStack s;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            s = (ItemStack)var2.next();
        } while(s.getItem() != stack.getItem() || s.getItemDamage() != stack.getItemDamage());

        return true;
    }

    /**
     * @author SC
     */
    @Overwrite(remap = false)
    public static boolean isOre(ItemStack stack)  {
        return stack.isEmpty() ? false : true;
    }

    /**
     * @author SC
     */
    @Overwrite(remap = false)
    public static boolean isIngot(ItemStack stack) {
        return stack.isEmpty() ? false : (containsItem(INGOTS, stack) || containsItem(CommonProxy.ADDITIONAL_INGOTS, stack));
    }
}
