package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft.entities;

import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.entities.construct.EntityOwnedConstruct;
import thebetweenlands.api.entity.IEntityBL;

@Mixin(value = EntityOwnedConstruct.class, remap = false)
public class MixinEntityOwnedConstruct implements IEntityBL {

}
