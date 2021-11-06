package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.Trajectory;
import thaumcraft.common.items.casters.foci.FocusEffectFrost;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXFocusPartImpact;
import thebetweenlands.common.registries.BlockRegistry;

import java.util.Iterator;

@Mixin(value = FocusEffectFrost.class, remap = false)
public class MixinFocusEffectFrost extends FocusEffect {
    /**
     * @author SC
     */
    @Overwrite
    public boolean execute(RayTraceResult target, Trajectory trajectory, float finalPower, int num) {
        PacketHandler.INSTANCE.sendToAllAround(new PacketFXFocusPartImpact(target.hitVec.x, target.hitVec.y, target.hitVec.z, new String[]{this.getKey()}), new NetworkRegistry.TargetPoint(this.getPackage().world.provider.getDimension(), target.hitVec.x, target.hitVec.y, target.hitVec.z, 64.0D));
        float f;
        if (target.typeOfHit == RayTraceResult.Type.ENTITY && target.entityHit != null) {
            f = this.getDamageForDisplay(finalPower);
            int duration = 20 * this.getSettingValue("duration");
            int potency = (int)(1.0F + (float)this.getSettingValue("power") * finalPower / 3.0F);
            target.entityHit.attackEntityFrom(DamageSource.causeThrownDamage((Entity)(target.entityHit != null ? target.entityHit : this.getPackage().getCaster()), this.getPackage().getCaster()), f);
            if (target.entityHit instanceof EntityLivingBase) {
                ((EntityLivingBase)target.entityHit).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, duration, potency));
            }
        } else if (target.typeOfHit == RayTraceResult.Type.BLOCK) {
            f = Math.min(16.0F, (float)(2 * this.getSettingValue("power")) * finalPower);
            Iterator var6 = BlockPos.getAllInBoxMutable(target.getBlockPos().add((double)(-f), (double)(-f), (double)(-f)), target.getBlockPos().add((double)f, (double)f, (double)f)).iterator();

            while(var6.hasNext()) {
                BlockPos.MutableBlockPos blockpos$mutableblockpos1 = (BlockPos.MutableBlockPos)var6.next();
                if (blockpos$mutableblockpos1.distanceSqToCenter(target.hitVec.x, target.hitVec.y, target.hitVec.z) <= (double)(f * f)) {
                    IBlockState iblockstate1 = this.getPackage().world.getBlockState(blockpos$mutableblockpos1);
                    if (iblockstate1.getMaterial() == Material.WATER && (Integer)iblockstate1.getValue(BlockLiquid.LEVEL) == 0 && this.getPackage().world.mayPlace(BlockRegistry.BLACK_ICE, blockpos$mutableblockpos1, false, EnumFacing.DOWN, (Entity)null)) {
                        this.getPackage().world.setBlockState(blockpos$mutableblockpos1, BlockRegistry.BLACK_ICE.getDefaultState());
                        this.getPackage().world.scheduleUpdate(blockpos$mutableblockpos1.toImmutable(), BlockRegistry.BLACK_ICE, MathHelper.getInt(this.getPackage().world.rand, 60, 120));
                    }
                }
            }
        }

        return false;
    }

    @Shadow
    public float getDamageForDisplay(float finalPower){return 0;}

    @Override
    public void renderParticleFX(World world, double v, double v1, double v2, double v3, double v4, double v5) {

    }

    @Shadow
    public String getResearch() {
        return "FOCUSELEMENTAL";
    }

    @Shadow
    public String getKey() {
        return "thaumcraft.FROST";
    }

    @Override
    public int getComplexity() {
        return 0;
    }

    @Shadow
    public Aspect getAspect() {
        return Aspect.COLD;
    }
}
