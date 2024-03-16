package com.seriouscreeper.bladditions.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thebetweenlands.common.entity.projectiles.EntityBLArrow;

public interface IBLArrowExpanded {
    EntityBLArrow createArrow(World worldIn, ItemStack stack);
}
