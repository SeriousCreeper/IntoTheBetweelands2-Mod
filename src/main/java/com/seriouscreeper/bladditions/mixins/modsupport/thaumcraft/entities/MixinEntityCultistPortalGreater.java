package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft.entities;

import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.entities.monster.boss.EntityCultistPortalGreater;
import thebetweenlands.api.entity.IEntityBL;

@Mixin(value = EntityCultistPortalGreater.class, remap = false)
public class MixinEntityCultistPortalGreater implements IEntityBL {
}
