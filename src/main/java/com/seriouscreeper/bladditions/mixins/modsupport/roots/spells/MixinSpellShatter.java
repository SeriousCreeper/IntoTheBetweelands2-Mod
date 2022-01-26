package com.seriouscreeper.bladditions.mixins.modsupport.roots.spells;

import epicsquid.mysticallib.util.AABBUtil;
import epicsquid.mysticallib.util.RayCastUtil;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.spell.SpellShatter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thebetweenlands.common.world.storage.location.LocationStorage;

import java.util.Iterator;
import java.util.List;

@Mixin(value = SpellShatter.class, remap = false)
public class MixinSpellShatter {
    @Shadow
    public static AxisAlignedBB getBox(EntityPlayer player, StaffModifierInstanceList info, RayTraceResult result) {
        return null;
    }

    @Shadow
    public float distance;

    @Inject(method = "cast", at = @At("HEAD"), cancellable = true)
    public void cast(EntityPlayer player, StaffModifierInstanceList info, int ticks, CallbackInfoReturnable<Boolean> cir) {
        Vec3d eyes = new Vec3d(0.0D, (double)player.getEyeHeight(), 0.0D);
        RayTraceResult result = RayCastUtil.rayTraceBlocks(player.world, player.getPositionVector().add(eyes), player.getLookVec().scale((double)this.distance).add(player.getPositionVector().add(eyes)), false, false, false, false);

        AxisAlignedBB box = getBox(player, info, result);

        if (box != null) {
            for(BlockPos pos : AABBUtil.unique(box)) {
                List<LocationStorage> locations = LocationStorage.getLocations(player.world, new Vec3d(pos));

                for(LocationStorage location : locations) {
                    if(location != null && location.getGuard() != null && location.getGuard().isGuarded(player.world, player, pos)) {
                        cir.cancel();
                    }
                }
            }
        }
    }
}
