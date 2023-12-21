package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import org.spongepowered.asm.mixin.Mixin;
import thebetweenlands.api.entity.IEntityBL;
import thebetweenlands.common.entity.mobs.EntityDarkDruid;

@Mixin(value = EntityDarkDruid.class)
public class MixinEntityDarkDruid implements IEntityBL {
}
