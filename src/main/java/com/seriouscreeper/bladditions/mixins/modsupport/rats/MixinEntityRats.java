package com.seriouscreeper.bladditions.mixins.modsupport.rats;

import com.github.alexthe666.rats.server.entity.EntityRat;
import org.spongepowered.asm.mixin.Mixin;
import thebetweenlands.api.entity.IEntityBL;

@Mixin(value = EntityRat.class, remap = false)
public class MixinEntityRats implements IEntityBL {
}
