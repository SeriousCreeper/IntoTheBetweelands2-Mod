package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft.entities;

import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.entities.monster.cult.EntityCultist;
import thebetweenlands.api.entity.IEntityBL;

@Mixin(value = EntityCultist.class, remap = false)
public class MixinEntityCultist implements IEntityBL {
}
