package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft.entities;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.entities.monster.cult.EntityCultistPortalLesser;
import thebetweenlands.api.entity.IEntityBL;

@Mixin(value = EntityCultistPortalLesser.class, remap = false)
public class MixinEntityCultistPortalLesser extends EntityMob implements IEntityBL {
    public MixinEntityCultistPortalLesser(World worldIn) {
        super(worldIn);
    }

    @Override
    public void heal(float healAmount) {
    }
}
