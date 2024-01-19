package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft.entities;

import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.entities.monster.tainted.EntityTaintSeed;
import thebetweenlands.api.entity.IEntityBL;

@Mixin(value = EntityTaintSeed.class, remap = false)
public class MixinEntityTaintSeed implements IEntityBL {
}
