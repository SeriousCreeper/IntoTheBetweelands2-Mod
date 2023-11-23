package com.seriouscreeper.bladditions.mixins.modsupport.corpse;

import de.maxhenkel.corpse.entities.EntityCorpse;
import org.spongepowered.asm.mixin.Mixin;
import thebetweenlands.api.entity.IEntityBL;

@Mixin(value = EntityCorpse.class, remap = false)
public class MixinEntityCorpse implements IEntityBL {
}
