package com.seriouscreeper.bladditions.mixins.modsupport.simpledifficulty;

import com.charles445.simpledifficulty.api.SDFluids;
import com.charles445.simpledifficulty.api.config.ServerConfig;
import com.charles445.simpledifficulty.api.config.ServerOptions;
import com.charles445.simpledifficulty.api.thirst.ThirstEnum;
import com.charles445.simpledifficulty.api.thirst.ThirstEnumBlockPos;
import com.charles445.simpledifficulty.util.internal.ThirstUtilInternal;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.registries.BlockRegistry;

import javax.annotation.Nullable;

@Mixin(value = ThirstUtilInternal.class, remap = false)
public class MixinThirstUtilInternal {
    /**
     * @author SC
     */
    @Overwrite
    @Nullable
    public ThirstEnumBlockPos traceWater(EntityPlayer player) {
        if (player.rotationPitch < -75.0F && player.world.isRainingAt(player.getPosition()) && player.world.canSeeSky(player.getPosition()) && ServerConfig.instance.getBoolean(ServerOptions.THIRST_DRINK_RAIN)) {
            return new ThirstEnumBlockPos(ThirstEnum.RAIN, player.getPosition());
        } else {
            double reach = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() * 0.5D;
            Vec3d eyevec = player.getPositionEyes(1.0F);
            Vec3d lookvec = player.getLook(1.0F);
            Vec3d targetvec = eyevec.add(lookvec.x * reach, lookvec.y * reach, lookvec.z * reach);
            RayTraceResult trace = player.getEntityWorld().rayTraceBlocks(eyevec, targetvec, true);
            if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK) {
                Block traceBlock = player.getEntityWorld().getBlockState(trace.getBlockPos()).getBlock();
                if (traceBlock == BlockRegistry.SWAMP_WATER) {
                    return new ThirstEnumBlockPos(ThirstEnum.NORMAL, trace.getBlockPos());
                } else {
                    return traceBlock == SDFluids.blockPurifiedWater ? new ThirstEnumBlockPos(ThirstEnum.PURIFIED, trace.getBlockPos()) : null;
                }
            } else {
                return null;
            }
        }
    }
}
