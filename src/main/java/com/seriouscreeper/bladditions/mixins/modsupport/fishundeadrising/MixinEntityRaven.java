package com.seriouscreeper.bladditions.mixins.modsupport.fishundeadrising;

import com.Fishmod.mod_LavaCow.entities.tameable.EntityRaven;
import org.spongepowered.asm.mixin.Mixin;
import thebetweenlands.api.entity.IEntityBL;

@Mixin(value = EntityRaven.class, remap = false)
public class MixinEntityRaven implements IEntityBL {
}
