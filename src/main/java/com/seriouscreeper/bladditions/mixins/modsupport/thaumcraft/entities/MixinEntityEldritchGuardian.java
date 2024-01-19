package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft.entities;

import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thebetweenlands.api.entity.IEntityBL;

@Mixin(value = EntityEldritchGuardian.class, remap = false)
public class MixinEntityEldritchGuardian implements IEntityBL {
}
