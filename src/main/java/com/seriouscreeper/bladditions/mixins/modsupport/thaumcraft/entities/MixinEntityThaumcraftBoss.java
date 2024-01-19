package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft.entities;

import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.entities.monster.boss.EntityThaumcraftBoss;
import thebetweenlands.api.entity.IEntityBL;

@Mixin(value = EntityThaumcraftBoss.class, remap = false)
public class MixinEntityThaumcraftBoss implements IEntityBL {
}
