package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft.entities;

import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.entities.monster.cult.EntityCultistPortalLesser;
import thebetweenlands.api.entity.IEntityBL;

@Mixin(value = EntityCultistPortalLesser.class, remap = false)
public class MixinEntityCultistPortalLesser implements IEntityBL {
}
