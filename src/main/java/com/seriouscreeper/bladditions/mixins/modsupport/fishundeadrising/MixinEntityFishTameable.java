package com.seriouscreeper.bladditions.mixins.modsupport.fishundeadrising;

import com.Fishmod.mod_LavaCow.entities.tameable.EntityFishTameable;
import org.spongepowered.asm.mixin.Mixin;
import thebetweenlands.api.entity.IEntityBL;

@Mixin(value = EntityFishTameable.class, remap = false)
public class MixinEntityFishTameable implements IEntityBL {
}
