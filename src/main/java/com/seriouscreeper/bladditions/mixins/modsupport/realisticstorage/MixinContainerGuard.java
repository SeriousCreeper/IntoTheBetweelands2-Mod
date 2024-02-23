package com.seriouscreeper.bladditions.mixins.modsupport.realisticstorage;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import oethever.realisticstorage.containerguard.ContainerGuard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.regex.Pattern;

@Mixin(value = ContainerGuard.class, remap = false)
public class MixinContainerGuard {
    @Final
    @Shadow
    private ArrayList<Pattern> alwaysEjectedPatterns = new ArrayList();

    @Final
    @Shadow
    private ArrayList<Pattern> neverEjectedPatterns = new ArrayList();

    /**
     * @author
     * @reason
     */
    @Overwrite
    private boolean isBigBlock(ItemStack stack) {
        Item item = stack.getItem();
        ResourceLocation itemRegistryName = item.getRegistryName();

        if (itemRegistryName == null)
            return false;

        String itemName = itemRegistryName.toString();
        String itemNameWithMeta = itemRegistryName.toString() + ":" + stack.getMetadata();

        for (Pattern pattern : alwaysEjectedPatterns) {
            if (pattern.matcher(itemName).matches() || pattern.matcher(itemNameWithMeta).matches())
                return false;
        }

        for (Pattern pattern : neverEjectedPatterns) {
            if (pattern.matcher(itemName).matches() || pattern.matcher(itemNameWithMeta).matches())
                return true;
        }

        return false;
    }


    /**
     * @author SC
     * @reason fix stacking of unstackable items
     */
    @Overwrite
    private static void spawnYeetItem(World world, BlockPos pos, ItemStack item) {
        float f = world.rand.nextFloat() * 0.8F + 0.1F;
        float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
        float f2 = world.rand.nextFloat() * 0.8F + 0.1F;

        while(item.getCount() > 0) {
            ItemStack newStack = item.copy();
            int count = Math.min(item.getMaxStackSize(), item.getCount());
            newStack.setCount(count);
            item.shrink(count);

            EntityItem entityitem = new EntityItem(world, (double)pos.getX() + (double)f, (double)pos.getY() + (double)f1, (double)pos.getZ() + (double)f2, newStack);
            entityitem.setPickupDelay(30);
            entityitem.motionX = world.rand.nextGaussian() * 0.07;
            entityitem.motionY = world.rand.nextGaussian() * 0.07 + 0.20000000298023224;
            entityitem.motionZ = world.rand.nextGaussian() * 0.07;
            world.spawnEntity(entityitem);
        }
    }
}
