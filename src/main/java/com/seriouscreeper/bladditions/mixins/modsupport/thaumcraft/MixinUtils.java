package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import epicsquid.roots.init.ModItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.lib.utils.Utils;
import thebetweenlands.common.registries.ItemRegistry;

import java.util.Random;

@Mixin(value = Utils.class, remap = false)
public class MixinUtils {
    /**
     * @author SC
     */
    @Overwrite
    private static ItemStack genGear(int rarity, Random rand) {
        ItemStack is = ItemStack.EMPTY;
        int quality = rand.nextInt(2);
        if (rand.nextFloat() < 0.2F) {
            ++quality;
        }

        if (rand.nextFloat() < 0.15F) {
            ++quality;
        }

        if (rand.nextFloat() < 0.1F) {
            ++quality;
        }

        if (rand.nextFloat() < 0.095F) {
            ++quality;
        }

        if (rand.nextFloat() < 0.095F) {
            ++quality;
        }

        Item item = getGearItemForSlot(rand.nextInt(5), quality);
        if (item != null) {
            is = new ItemStack(item, 1, rand.nextInt(1 + item.getMaxDamage() / 6));
            if (rand.nextInt(4) < rarity) {
                //EnchantmentHelper.addRandomEnchantment(rand, is, (int)(5.0F + (float)rarity * 0.75F * (float)rand.nextInt(18)), false);
            }

            return is.copy();
        } else {
            return ItemStack.EMPTY;
        }
    }

    /**
     * @author SC
     */
    @Overwrite
    private static Item getGearItemForSlot(int slot, int quality) {
        switch(slot) {
            case 4:
                if (quality == 0) {
                    return ItemRegistry.BONE_HELMET;
                } else if (quality == 1) {
                    return ItemRegistry.LURKER_SKIN_HELMET;
                } else if (quality == 2) {
                    return ItemRegistry.SYRMORITE_HELMET;
                } else if (quality == 3) {
                    return ModItems.wildwood_helmet;
                } else if (quality == 4) {
                    return ItemsTC.thaumiumHelm;
                } else if (quality == 5) {
                    return ItemRegistry.VALONITE_HELMET;
                } else if (quality == 6) {
                    return ItemsTC.voidHelm;
                }
            case 3:
                if (quality == 0) {
                    return ItemRegistry.BONE_CHESTPLATE;
                } else if (quality == 1) {
                    return ItemRegistry.LURKER_SKIN_CHESTPLATE;
                } else if (quality == 2) {
                    return ItemRegistry.SYRMORITE_CHESTPLATE;
                } else if (quality == 3) {
                    return ModItems.wildwood_chestplate;
                } else if (quality == 4) {
                    return ItemsTC.thaumiumChest;
                } else if (quality == 5) {
                    return ItemRegistry.VALONITE_CHESTPLATE;
                } else if (quality == 6) {
                    return ItemsTC.voidChest;
                }
            case 2:
                if (quality == 0) {
                    return ItemRegistry.BONE_LEGGINGS;
                } else if (quality == 1) {
                    return ItemRegistry.LURKER_SKIN_LEGGINGS;
                } else if (quality == 2) {
                    return ItemRegistry.SYRMORITE_LEGGINGS;
                } else if (quality == 3) {
                    return ModItems.wildwood_leggings;
                } else if (quality == 4) {
                    return ItemsTC.thaumiumLegs;
                } else if (quality == 5) {
                    return ItemRegistry.VALONITE_LEGGINGS;
                } else if (quality == 6) {
                    return ItemsTC.voidLegs;
                }
            case 1:
                if (quality == 0) {
                    return ItemRegistry.BONE_BOOTS;
                } else if (quality == 1) {
                    return ItemRegistry.LURKER_SKIN_BOOTS;
                } else if (quality == 2) {
                    return ItemRegistry.SYRMORITE_BOOTS;
                } else if (quality == 3) {
                    return ModItems.wildwood_boots;
                } else if (quality == 4) {
                    return ItemsTC.thaumiumBoots;
                } else if (quality == 5) {
                    return ItemRegistry.VALONITE_BOOTS;
                } else if (quality == 6) {
                    return ItemsTC.voidBoots;
                }
            case 0:
                if (quality == 0) {
                    return ItemRegistry.BONE_AXE;
                } else if (quality == 1) {
                    return ItemRegistry.BONE_SWORD;
                } else if (quality == 2) {
                    return ItemRegistry.OCTINE_AXE;
                } else if (quality == 3) {
                    return ItemRegistry.OCTINE_SWORD;
                } else if (quality == 4) {
                    return ItemsTC.thaumiumSword;
                } else if (quality == 5) {
                    return ItemRegistry.VALONITE_SWORD;
                } else if (quality == 6) {
                    return ItemsTC.voidSword;
                }
            default:
                return null;
        }
    }
}
