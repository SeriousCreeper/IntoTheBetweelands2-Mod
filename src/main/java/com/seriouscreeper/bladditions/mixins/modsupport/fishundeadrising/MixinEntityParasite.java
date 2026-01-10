package com.seriouscreeper.bladditions.mixins.modsupport.fishundeadrising;

import com.Fishmod.mod_LavaCow.entities.EntityParasite;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import thebetweenlands.api.entity.IEntityBL;

@Mixin(value = EntityParasite.class, remap = false)
public class MixinEntityParasite extends EntitySpider implements IEntityBL {
    public MixinEntityParasite(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean getCanSpawnHere() {
        return true;
    }
}
