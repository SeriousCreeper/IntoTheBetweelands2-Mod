package com.seriouscreeper.bladditions.mixins.modsupport.quark;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import thebetweenlands.common.block.structure.BlockFenceBetweenlands;
import vazkii.quark.decoration.entity.EntityLeashKnot2TheKnotting;

@Mixin(value = EntityLeashKnot2TheKnotting.class)
public class MixinEntityLeashKnot extends EntityLiving {
    public MixinEntityLeashKnot(World worldIn) {
        super(worldIn);
    }

    /**
     * @author SC
     */
    @Overwrite
    public void func_70071_h_() {
        IBlockState state = world.getBlockState(new BlockPos(posX, posY, posZ));
        if(!(state.getBlock() instanceof BlockFenceBetweenlands)) {
            dismantle(true);
        } else {
            Entity holder = getHolder();
            if(holder == null || holder.isDead)
                dismantle(true);
            else if(holder.posY < posY && holder instanceof EntityLeashKnot) {
                double targetX = holder.posX;
                double targetY = holder.posY;
                double targetZ = holder.posZ;
                holder.setPosition(posX, posY, posZ);
                setPosition(targetX, targetY, targetZ);
            }
        }
    }

    @Shadow
    private Entity getHolder() {
        return null;
    }

    @Shadow
    private void dismantle(boolean b) {

    }
}
