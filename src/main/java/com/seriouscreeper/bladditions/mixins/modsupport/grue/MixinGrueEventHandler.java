package com.seriouscreeper.bladditions.mixins.modsupport.grue;

import com.shinoow.darknesslib.api.DarknessLibAPI;
import com.shinoow.grue.Grue;
import com.shinoow.grue.common.entity.EntityGrue;
import com.shinoow.grue.common.entity.cap.GrueCapabilityProvider;
import com.shinoow.grue.common.entity.cap.IGrueTimerCapability;
import com.shinoow.grue.common.handlers.GrueEventHandler;
import com.shinoow.grue.common.integrations.gamestages.GameStagesHandler;
import com.shinoow.grue.common.network.PacketDispatcher;
import com.shinoow.grue.common.network.client.GrueTimerMessage;
import com.shinoow.grue.common.util.DimensionData;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.tiffit.sanity.SanityCapability;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(value = GrueEventHandler.class)
public class MixinGrueEventHandler {
    /**
     * @author
     * @reason
     */
    @SubscribeEvent
    @Overwrite
    public void spawnGrue(LivingEvent.LivingUpdateEvent event) {
        if (!event.getEntityLiving().world.isRemote && !event.getEntityLiving().isDead && event.getEntityLiving().world.getDifficulty() != EnumDifficulty.PEACEFUL) {
            if (event.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)event.getEntityLiving();
                if (player.capabilities.isCreativeMode) {
                    return;
                }

                if (player.isSpectator()) {
                    return;
                }

                SanityCapability sanityCapability = event.getEntityLiving().getCapability(SanityCapability.INSTANCE, (EnumFacing)null);

                if(sanityCapability == null || sanityCapability.getSanityExact() > -20) {
                    return;
                }

                if (player.isRiding() && DarknessLibAPI.getInstance().isVehicle(player.getRidingEntity())) {
                    return;
                }

                IGrueTimerCapability cap = (IGrueTimerCapability)player.getCapability(GrueCapabilityProvider.GRUE_TIMER, (EnumFacing)null);
                DimensionData data = Grue.getDimensionConfig(player.dimension);
                if (data.shouldUseGracePeriod() && cap.getGracePeriodTimer() > 0) {
                    cap.decrementGracePeriodTimer();
                } else if (this.isWhitelisted(player.dimension)) {
                    int light = Grue.dynLightPausesTimer ? DarknessLibAPI.getInstance().getLight(player, true) : DarknessLibAPI.getInstance().getLightWithAdditions(player, true);
                    if (light <= data.getMaxLight() && light >= data.getMinLight() && GameStagesHandler.shouldGrueSpawn(player)) {
                        if (this.waterCheck(player) && (Grue.dynLightPausesTimer && !DarknessLibAPI.getInstance().isIlluminatedDynamically(player) || !Grue.dynLightPausesTimer) && (data.isSingleGrue() && player.world.getEntitiesWithinAABB(EntityGrue.class, player.getEntityBoundingBox().grow(10.0)).isEmpty() || !data.isSingleGrue())) {
                            cap.incrementTimer();
                            if (cap.getTimer() % 40 == 0) {
                                player.world.playSound((EntityPlayer)null, player.getPosition(), Grue.grue_whisper, SoundCategory.HOSTILE, 2.0F, 1.0F);
                            }

                            if (cap.getTimer() % 40 == 0) {
                                PacketDispatcher.sendTo(new GrueTimerMessage(player), (EntityPlayerMP)player);
                            }

                            if (cap.getTimer() >= data.getSpawnTimer() * 20 - 40 && this.flyingCloseEnough(player)) {
                                if (!cap.hasSpawnPosition()) {
                                    cap.setSpawnPosition(player.posX, player.posY, player.posZ);
                                }

                                player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 200, 10));
                                player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 200, 10));
                                if (cap.getTimer() % 4 == 0) {
                                    player.world.playSound((EntityPlayer)null, player.getPosition(), Grue.grue_spawn, SoundCategory.HOSTILE, 2.0F, 1.0F);
                                }
                            }

                            if (cap.getTimer() >= data.getSpawnTimer() * 20 && this.flyingCloseEnough(player)) {
                                if (cap.getTimer() - 20 >= data.getSpawnTimer() * 20) {
                                    player.world.playSound((EntityPlayer)null, player.getPosition(), Grue.grue_spawn, SoundCategory.HOSTILE, 2.0F, 1.0F);
                                }

                                cap.resetTimer();
                                EntityGrue grue = new EntityGrue(player.world);
                                grue.copyLocationAndAnglesFrom(player);
                                grue.setPosition(this.stuff(data.shouldSpawnAtPlayer() ? player.posX : cap.getSpawnX()), data.shouldSpawnAtPlayer() ? player.posY : cap.getSpawnY(), this.stuff(data.shouldSpawnAtPlayer() ? player.posZ : cap.getSpawnZ()));
                                cap.resetSpawnPosition();
                                player.world.spawnEntity(grue);
                            }
                        }
                    } else if ((light > data.getMaxLight() || light < data.getMinLight()) && cap.getTimer() > 0) {
                        cap.decrementTimer();
                        if (cap.getTimer() % 10 == 0 || cap.getTimer() == 1) {
                            PacketDispatcher.sendTo(new GrueTimerMessage(player), (EntityPlayerMP)player);
                        }
                    }
                }
            }
        }
    }

@Shadow
    private boolean isWhitelisted(int dim) {
        if (!Grue.useWhitelist) {
            return true;
        } else {
            return !Grue.useBlacklist ? Arrays.stream(Grue.dimWhitelist).anyMatch((id) -> {
                return id == dim;
            }) : Arrays.stream(Grue.dimWhitelist).noneMatch((id) -> {
                return id == dim;
            });
        }
    }


    @Shadow
    private boolean flyingCloseEnough(EntityPlayer player) {
        if (Grue.flyHeight <= 0) {
            return true;
        } else if (!player.capabilities.isFlying && player.onGround) {
            return true;
        } else {
            Vec3d v = new Vec3d(player.posX, player.posY, player.posZ);
            RayTraceResult mop = player.world.rayTraceBlocks(v, new Vec3d(player.posX, player.posY - 3.0, player.posZ), true);
            if (mop != null && mop.typeOfHit == RayTraceResult.Type.BLOCK) {
                return v.y - mop.hitVec.y <= (double)Grue.flyHeight;
            } else {
                return false;
            }
        }
    }

    @Shadow
    private double stuff(double d0) {
        double d1 = (double) MathHelper.floor(d0);
        return d1 > d0 ? d1 - 0.5 : d1 + 0.5;
    }

    @Shadow
    private boolean waterCheck(EntityPlayer player) {
        boolean ret = player.world.getBlockState(player.getPosition()).getMaterial() == Material.WATER && player.world.getBlockState(player.getPosition().down()).getMaterial() != Material.WATER && player.world.getBlockState(player.getPosition().up()) == Blocks.AIR.getDefaultState();
        return player.isInWater() && player.getAir() == 300 && ret || !player.isInWater();
    }
}
