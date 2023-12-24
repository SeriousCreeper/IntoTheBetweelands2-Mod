package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.tiffit.sanity.SanityCapability;
import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.items.tools.ItemSanityChecker;

@Mixin(value = ItemSanityChecker.class, remap = false)
public class MixinItemSanityChecker extends Item {
    @Override
    public ActionResult<ItemStack> onItemRightClick(World w, EntityPlayer p, EnumHand h) {
        if (!w.isRemote) {
            SanityCapability cap = (SanityCapability)p.getCapability(SanityCapability.INSTANCE, (EnumFacing)null);
            p.sendMessage(new TextComponentString(TextFormatting.GOLD + "Mental State: " + cap.getSanity().toColoredString() + " [" + ItemStack.DECIMALFORMAT.format((double)cap.getSanityExact()) + "]"));
        }

        return new ActionResult(EnumActionResult.SUCCESS, p.getHeldItem(h));
    }
}
