package com.seriouscreeper.bladditions.mixins.modsupport.botania;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.entity.mobs.EntitySludge;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.block.subtile.generating.SubTileNarslimmus;

import java.util.Iterator;
import java.util.List;

@Mixin(value = SubTileNarslimmus.class, remap = false)
public class MixinSubTileNarslimmus extends SubTileGenerating {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onUpdate() {
        super.onUpdate();
        if (this.ticksExisted % 5 == 0) {
            List<EntitySludge> slimes = this.supertile.getWorld().getEntitiesWithinAABB(EntitySludge.class, new AxisAlignedBB(this.supertile.getPos().add(-2, -2, -2), this.supertile.getPos().add(3, 3, 3)));
            Iterator var2 = slimes.iterator();

            while(var2.hasNext()) {
                EntitySludge slime = (EntitySludge)var2.next();
                if (slime.getEntityData().getBoolean("Botania:WorldSpawned") && !slime.isDead) {
                    int size = (int)Math.floor(slime.width * 2);
                    int mul = (int)Math.pow(2.0, (double)size);
                    int mana = 1200 * mul;
                    if (!slime.world.isRemote) {
                        slime.setDead();
                        slime.playSound(size > 1 ? SoundEvents.ENTITY_SLIME_SQUISH : SoundEvents.ENTITY_SMALL_SLIME_SQUISH, 1.0F, 0.02F);
                        this.mana = Math.min(this.getMaxMana(), this.mana + mana);
                        this.sync();
                    }

                    for(int j = 0; j < mul * 8; ++j) {
                        float f = slime.world.rand.nextFloat() * 3.1415927F * 2.0F;
                        float f1 = slime.world.rand.nextFloat() * 0.5F + 0.5F;
                        float f2 = MathHelper.sin(f) * (float)size * 0.5F * f1;
                        float f3 = MathHelper.cos(f) * (float)size * 0.5F * f1;
                        float f4 = slime.world.rand.nextFloat() * (float)size * 0.5F * f1;
                        slime.world.spawnParticle(EnumParticleTypes.SLIME, slime.posX + (double)f2, slime.getEntityBoundingBox().minY + (double)f4, slime.posZ + (double)f3, 0.0, 0.0, 0.0, new int[0]);
                    }

                    return;
                }
            }
        }
    }
}
