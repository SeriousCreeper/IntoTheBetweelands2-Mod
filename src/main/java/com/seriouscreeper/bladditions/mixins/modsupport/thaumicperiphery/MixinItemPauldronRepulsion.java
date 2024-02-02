package com.seriouscreeper.bladditions.mixins.modsupport.thaumicperiphery;

import baubles.api.BaublesApi;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thaumcraft.common.lib.SoundsTC;
import thaumicperiphery.items.ItemPauldronRepulsion;
import thebetweenlands.common.entity.mobs.EntityGiantToad;

import java.util.List;

@Mixin(value = ItemPauldronRepulsion.class, remap = false)
public class MixinItemPauldronRepulsion {
    /**
     * @author SC
     * @reason fix pauldron bug
     */
    @SubscribeEvent
    @Overwrite
    public void onPlayerDamaged(LivingDamageEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        Entity source = event.getSource().getImmediateSource();
        if (source != null && source instanceof EntityLivingBase && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack body = BaublesApi.getBaublesHandler(player).getStackInSlot(5);

            if (!body.isEmpty() && body.getItem() instanceof ItemPauldronRepulsion && getCooldown(body) <= 0) {
                if (!player.world.isRemote) {
                    player.world.playSound(null, player.posX, player.posY, player.posZ,
                            SoundsTC.poof, SoundCategory.PLAYERS, 1F,
                            1.0F + (float) player.getEntityWorld().rand.nextGaussian() * 0.05F);

                    List<Entity> entities = player.world.getEntitiesWithinAABBExcludingEntity(player,
                            player.getEntityBoundingBox().grow(4.5, 2, 4.5));

                    for (Entity e : entities) {
                        if (e instanceof EntityLivingBase) {
                            if(e instanceof EntityGiantToad) {
                                continue;
                            }

                            EntityLivingBase mob = (EntityLivingBase) e;
                            mob.knockBack(player, 2F, player.posX - mob.posX, player.posZ - mob.posZ);
                        }
                    }
                }
                setCooldown(body, 50);
            }
        }
    }


    @Shadow
    protected int getCooldown(ItemStack stack) {
        return 0;
    }

    @Shadow
    protected void setCooldown(ItemStack stack, int cooldown) {}
}
