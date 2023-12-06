package com.seriouscreeper.bladditions.mixins.modsupport.livingchest;

import com.syllient.livingchest.entity.ChesterEntity;
import org.spongepowered.asm.mixin.Mixin;
import thebetweenlands.api.entity.IEntityBL;

@Mixin(value = ChesterEntity.class, remap = false)
public class MixinChesterEntity implements IEntityBL {
}
