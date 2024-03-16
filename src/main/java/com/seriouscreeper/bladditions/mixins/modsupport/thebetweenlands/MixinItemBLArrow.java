package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import com.seriouscreeper.bladditions.interfaces.IBLArrowExpanded;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.entity.projectiles.EntityBLArrow;
import thebetweenlands.common.item.tools.bow.EnumArrowType;
import thebetweenlands.common.item.tools.bow.ItemBLArrow;

@Mixin(value = ItemBLArrow.class, remap = false)
public class MixinItemBLArrow implements IBLArrowExpanded {
    @Shadow
    private EnumArrowType type;

    @Override
    public EntityBLArrow createArrow(World worldIn, ItemStack stack) {
        EntityBLArrow entityArrow = new EntityBLArrow(worldIn);
        entityArrow.setType(this.type);
        return entityArrow;
    }
}
