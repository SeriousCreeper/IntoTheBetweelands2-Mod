package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.tiffit.sanity.Sanity;
import net.tiffit.sanity.SanityCapability;
import net.tiffit.sanity.SanityModifier;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.item.misc.ItemMob;
import thebetweenlands.common.item.misc.ItemMobAnadia;
import thebetweenlands.common.item.tools.ItemNet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

@Mixin(value = ItemNet.class, remap = false)
public class MixinItemNet {
    /**
     * @author SC
     */
    @Overwrite
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        Collection<Pair<Supplier<? extends ItemMob>, BiPredicate<EntityPlayer, Entity>>> entries = ItemNet.CATCHABLE_ENTITIES.get(target.getClass());
        if (entries != null) {
            Iterator var6 = entries.iterator();

            while(var6.hasNext()) {
                Pair<Supplier<? extends ItemMob>, BiPredicate<EntityPlayer, Entity>> entry = (Pair)var6.next();
                if (((BiPredicate)entry.getRight()).test(player, target)) {
                    ItemMob item = (ItemMob)((Supplier)entry.getLeft()).get();
                    player.swingArm(hand);

                    if(item instanceof ItemMobAnadia) {
                        player.addStat(StatList.FISH_CAUGHT, 1);
                    }

                    if (player.world.isRemote) {
                        return true;
                    } else {
                        SanityCapability cap = (SanityCapability)player.getCapability(SanityCapability.INSTANCE, (EnumFacing)null);
                        List<SanityModifier> mods = Sanity.getModifierValues("misc");
                        Iterator var3 = mods.iterator();

                        while(var3.hasNext()) {
                            SanityModifier mod = (SanityModifier)var3.next();
                            if (mod.value.equals("fish")) {
                                cap.increaseSanity(mod.amount);
                            }
                        }
                    }

                    ItemStack mobItemStack = item.capture(target);
                    if (!mobItemStack.isEmpty()) {
                        target.setDropItemsWhenDead(false);
                        target.setDead();
                        player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, mobItemStack));
                        stack.damageItem(1, player);
                        item.onCapturedByPlayer(player, hand, mobItemStack, target);
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
