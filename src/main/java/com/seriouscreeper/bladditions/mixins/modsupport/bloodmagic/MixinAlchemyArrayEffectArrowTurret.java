package com.seriouscreeper.bladditions.mixins.modsupport.bloodmagic;

import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectArrowTurret;
import com.seriouscreeper.bladditions.interfaces.IBLArrowExpanded;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = AlchemyArrayEffectArrowTurret.class, remap = false)
public class MixinAlchemyArrayEffectArrowTurret {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void fireOnTarget(World world, BlockPos pos, ItemStack arrowStack, EntityLiving targetMob) {
        float vel = 3f;
        double damage = 2;
        if (!world.isRemote) {
            if (arrowStack.getItem() instanceof ItemArrow) {
                ItemArrow arrow = (ItemArrow) arrowStack.getItem();
                EntityArrow entityarrow = null;

                if(arrow instanceof IBLArrowExpanded) {
                    entityarrow = ((IBLArrowExpanded)arrow).createArrow(world, arrowStack);
                } else {
                    entityarrow = new EntityTippedArrow(world);
                    ((EntityTippedArrow)entityarrow).setPotionEffect(arrowStack);
                }

                entityarrow.posX = pos.getX() + 0.5;
                entityarrow.posY = pos.getY() + 0.5;
                entityarrow.posZ = pos.getZ() + 0.5;

                double d0 = targetMob.posX - (pos.getX() + 0.5);
                double d1 = (targetMob.posY - pos.getY()) + targetMob.height / 4f;
                double d2 = targetMob.posZ - (pos.getZ() + 0.5);
                double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
                entityarrow.setDamage(damage);
                entityarrow.shoot(d0, d1 + d3 * 0.05, d2, vel, 0);
                world.spawnEntity(entityarrow);
            }
        }
    }
}
