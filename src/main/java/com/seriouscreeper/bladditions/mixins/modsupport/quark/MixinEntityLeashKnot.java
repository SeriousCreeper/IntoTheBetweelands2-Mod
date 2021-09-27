package com.seriouscreeper.bladditions.mixins.modsupport.quark;

import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import thebetweenlands.common.block.structure.BlockFenceBetweenlands;
import vazkii.quark.decoration.entity.EntityLeashKnot2TheKnotting;

import javax.annotation.Nullable;

@Mixin(value = EntityLeashKnot2TheKnotting.class)
public class MixinEntityLeashKnot extends EntityLiving {
    private NBTTagCompound nbtTagCompound;

    public MixinEntityLeashKnot(World worldIn) {
        super(worldIn);
        this.setNoAI(true);
        this.width = 0.375F;
        this.height = 0.5F;
    }

    /**
     * @author SC
     */
    @Overwrite
    public void onUpdate() {
        super.onUpdate();

        IBlockState state = this.world.getBlockState(new BlockPos(this.posX, this.posY, this.posZ));

        if (!(state.getBlock() instanceof BlockFenceBetweenlands || state.getBlock() instanceof BlockFence)) {
           this.dismantle(true);
        } else {
            Entity holder = this.getHolder();
            if (holder != null && !holder.isDead) {
                if (holder.posY < this.posY && holder instanceof EntityLeashKnot) {
                    double targetX = holder.posX;
                    double targetY = holder.posY;
                    double targetZ = holder.posZ;
                    holder.setPosition(this.posX, this.posY, this.posZ);
                    this.setPosition(targetX, targetY, targetZ);
                }
            } else {
                if(!getLeashed()) {
                    return;
                }

                this.dismantle(true);
            }
        }
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (!this.world.isRemote) {
            Entity holder = this.getLeashHolder();

            if(holder == null || !getLeashed()) {
                dismantle(true);
                return true;
            }

            holder.setDead();
            this.dismantle(!player.isCreative());
        }

        return true;
    }

    @Shadow
    @Nullable
    private Entity getHolder() {
        return this.getLeashHolder();
    }

    @Shadow
    private void dismantle(boolean b) {
    }
}
