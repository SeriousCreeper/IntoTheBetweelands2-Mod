package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.tiffit.sanity.Sanity;
import net.tiffit.sanity.SanityCapability;
import net.tiffit.sanity.SanityModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thebetweenlands.common.entity.mobs.EntityWight;

import java.util.List;

@Mixin(value = EntityWight.class)
public class MixinEntityWight extends EntityMob {
    public MixinEntityWight(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;"))
    private void injectOnUpdate(CallbackInfo ci) {
        if(this.getAttackTarget() == null) {
            return;
        }

        SanityCapability cap = (SanityCapability)this.getAttackTarget().getCapability(SanityCapability.INSTANCE, (EnumFacing)null);

        if(cap == null) {
            return;
        }

        List<SanityModifier> mods = Sanity.getModifierValues("misc");

        for (SanityModifier mod : mods) {
            if (mod.value.equals("wight_possession")) {
                cap.increaseSanity(mod.amount);
                return;
            }
        }
    }
}
