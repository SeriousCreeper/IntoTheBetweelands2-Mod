package com.seriouscreeper.bladditions.mixins.modsupport.fishundeadrising;

import com.Fishmod.mod_LavaCow.entities.tameable.EntityMimic;
import org.spongepowered.asm.mixin.Mixin;
import thebetweenlands.api.entity.IEntityBL;

@Mixin(value = EntityMimic.class, remap = false)
public class MixinEntityMimic implements IEntityBL {
}
