package com.seriouscreeper.bladditions.mixins.modsupport.fishundeadrising;

import com.Fishmod.mod_LavaCow.entities.EntityZombieMushroom;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(value = EntityZombieMushroom.class, remap = false)
public class MixinEntityZombieMushroom extends EntityZombie {
    public MixinEntityZombieMushroom(World worldIn) {
        super(worldIn);
    }

    @Shadow
    public void setSkin(int skinType) {
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData entityLivingData) {
        if(this.world.rand.nextFloat() >= 0.5f) {
            this.setSkin(1);
        }

        return super.onInitialSpawn(difficulty, entityLivingData);
    }
}
