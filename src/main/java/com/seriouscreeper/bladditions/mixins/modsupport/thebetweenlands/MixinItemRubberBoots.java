package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import com.seriouscreeper.bladditions.compat.embers.modifier.ModifierRubberBoots;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.api.itemmod.ModifierBase;
import thebetweenlands.common.item.armor.ItemRubberBoots;

import java.util.List;

@Mixin(value = ItemRubberBoots.class, remap = false)
public class MixinItemRubberBoots {
    /**
     * @author SC
     * @reason allow for more cases
     */
    @Overwrite
    public static boolean isEntityWearingRubberBoots(Entity entity) {
        if (entity == null || !(entity instanceof EntityPlayer)) {
            return false;
        }

        ItemStack boots = ((EntityPlayer)entity).inventory.armorInventory.get(0);

        if (!boots.isEmpty()) {
            List<ModifierBase> modifiers = ItemModUtil.getModifiers(boots);

            for (ModifierBase modifier : modifiers) {
                if (modifier instanceof ModifierRubberBoots) {
                    return true;
                }
            }

            return false;
        }

        return entity instanceof EntityPlayer && !((ItemStack)((EntityPlayer)entity).inventory.armorInventory.get(0)).isEmpty() && ((ItemStack)((EntityPlayer)entity).inventory.armorInventory.get(0)).getItem() instanceof ItemRubberBoots;
    }
}
