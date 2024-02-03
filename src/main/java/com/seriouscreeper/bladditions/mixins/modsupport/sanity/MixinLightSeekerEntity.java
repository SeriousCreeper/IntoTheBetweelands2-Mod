package com.seriouscreeper.bladditions.mixins.modsupport.sanity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tiffit.sanity.entity.LightSeekerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = LightSeekerEntity.class, remap = false)
public class MixinLightSeekerEntity extends EntityLiving {
    @Shadow
    private long sound_timer = 0L;

    @Shadow
    private BlockPos target;

    @Shadow
    private int break_progress = 60;

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


    @Shadow
    private double distanceFromTarget() {
        return this.target == null ? 1000.0 : Math.sqrt(this.target.distanceSq(this.posX, this.posY, this.posZ));
    }
}
