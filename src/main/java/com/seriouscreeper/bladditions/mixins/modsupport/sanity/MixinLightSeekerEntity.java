package com.seriouscreeper.bladditions.mixins.modsupport.sanity;

import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCampfire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.tiffit.sanity.entity.LightSeekerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.lang.reflect.Field;
import java.util.List;

@Mixin(value = LightSeekerEntity.class, remap = false)
public class MixinLightSeekerEntity extends EntityLiving {
    @Shadow
    private long sound_timer = 0L;
    @Shadow
    private BlockPos target;
    @Shadow
    private int break_progress = 60;
    @Shadow
    private IBlockState state;

    private int nextRandomSound = 30;

    public MixinLightSeekerEntity(World worldIn) {
        super(worldIn);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void updateSound() {
        ++this.sound_timer;
        if (this.sound_timer % nextRandomSound == 0L) {
            nextRandomSound = Math.round(this.getEntityWorld().rand.nextInt(15) + 15);
            this.playSound(SoundEvents.BLOCK_NOTE_XYLOPHONE, 1.0F, this.getEntityWorld().rand.nextFloat() + 0.5f);
        }

        if (this.getPosition().equals(this.target)) {
            if (this.sound_timer % 4L == 0L) {
                this.playSound(SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, 1.0F, (float)this.break_progress / 60.0F * 0.7F + 0.3F);
            }
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onLivingUpdate() {
        if (!this.world.isRemote) {
            if (!this.hasNoGravity()) {
                this.setNoGravity(true);
            }

            WorldServer ws = (WorldServer)this.world;
            if (this.state != null && ws.getBlockState(this.target) != this.state) {
                this.dieWithPassion();
            }

            double offDiv = 2.0;
            double xoff = (Math.random() - 0.5) / offDiv;
            double yoff = (Math.random() - 0.5) / offDiv;
            double zoff = (Math.random() - 0.5) / offDiv;
            ws.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 1.0, this.posZ, 3, xoff, yoff, zoff, 0.0, new int[0]);
            this.updateSound();
            this.updateMove();
            if (this.getPosition().equals(this.target)) {
                --this.break_progress;
            }

            if (this.break_progress <= 0) {
                TileEntity te = ws.getTileEntity(this.getPosition());

                if(te != null) {
                    if(te instanceof TileCampfire) {
                        TileCampfire campfire = (TileCampfire)te;
                        Field field = null;
                        try {
                            field = TileCampfire.class.getDeclaredField("extinguishedByRain");
                            field.setAccessible(true);

                            try {
                                boolean b = false;
                                b = field.getBoolean(campfire);
                                b = true;
                            } catch (IllegalAccessException e) {
                            }
                        } catch (NoSuchFieldException e) {
                        }

                        campfire.workerSetActive(false);
                    }
                } else {
                    ws.destroyBlock(this.getPosition(), true);
                }

                this.setDead();
                this.playSound(SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, 1.0F, 1.3F);
                ws.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY + 1.0, this.posZ, 3, 0.0, 0.0, 0.0, 0.0, new int[0]);
            } else {
                this.searchArea();
            }
        }

        super.onLivingUpdate();
    }

    @Shadow
    private void dieWithPassion() {
    }

    @Shadow
    private void updateMove() {
    }

    @Shadow
    private void searchArea() {
    }
}
