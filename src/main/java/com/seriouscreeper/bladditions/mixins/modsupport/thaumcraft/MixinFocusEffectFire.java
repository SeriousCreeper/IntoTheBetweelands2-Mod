package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import com.codetaylor.mc.pyrotech.library.spi.block.IBlockIgnitableWithIgniterItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.Trajectory;
import thaumcraft.common.items.casters.foci.FocusEffectFire;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXFocusPartImpact;

@Mixin(value = FocusEffectFire.class, remap = false)
public class MixinFocusEffectFire extends FocusEffect {
    /**
     * @author SC
     */
    @Overwrite
    public boolean execute(RayTraceResult target, Trajectory trajectory, float finalPower, int num) {
        PacketHandler.INSTANCE.sendToAllAround(new PacketFXFocusPartImpact(target.hitVec.x, target.hitVec.y, target.hitVec.z, new String[]{this.getKey()}), new NetworkRegistry.TargetPoint(this.getPackage().world.provider.getDimension(), target.hitVec.x, target.hitVec.y, target.hitVec.z, 64.0D));
        if (target.typeOfHit == RayTraceResult.Type.ENTITY && target.entityHit != null) {
            if (target.entityHit.isImmuneToFire()) {
                return false;
            } else {
                float fire = (float)(1 + this.getSettingValue("duration") * this.getSettingValue("duration"));
                float damage = this.getDamageForDisplay(finalPower);
                fire *= finalPower;
                target.entityHit.attackEntityFrom((new EntityDamageSourceIndirect("fireball", (Entity)(target.entityHit != null ? target.entityHit : this.getPackage().getCaster()), this.getPackage().getCaster())).setFireDamage(), damage);
                if (fire > 0.0F) {
                    target.entityHit.setFire(Math.round(fire));
                }

                return true;
            }
        } else {
            if (target.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos pos = target.getBlockPos();
                pos = pos.offset(target.sideHit);

                IBlockState state = this.getPackage().world.getBlockState(target.getBlockPos());

                if (state.getBlock() instanceof IBlockIgnitableWithIgniterItem) {
                    this.getPackage().world.playSound((EntityPlayer)null, target.getBlockPos(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, this.getPackage().world.rand.nextFloat() * 0.4F + 0.8F);
                    ((IBlockIgnitableWithIgniterItem) state.getBlock()).igniteWithIgniterItem(this.getPackage().world, target.getBlockPos(), state, target.sideHit);
                    return true;
                }

                if (this.getSettingValue("duration") > 0 && this.getPackage().world.isAirBlock(pos) && this.getPackage().world.rand.nextFloat() < finalPower) {
                    this.getPackage().world.playSound((EntityPlayer)null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, this.getPackage().world.rand.nextFloat() * 0.4F + 0.8F);
                    this.getPackage().world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
                    return true;
                }
            }

            return false;
        }
    }

    @Shadow
    @SideOnly(Side.CLIENT)
    public void renderParticleFX(World world, double v, double v1, double v2, double v3, double v4, double v5) {

    }

    @Shadow
    public int getComplexity() {
        return 0;
    }

    @Shadow
    public Aspect getAspect() {
        return null;
    }

    @Shadow
    public String getKey() {
        return null;
    }

    @Shadow
    public String getResearch() {
        return null;
    }
}
