package com.seriouscreeper.bladditions.capability;

import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.event.DeathEventHandler;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.PacifistEntry;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thebetweenlands.api.capability.IDecayCapability;
import thebetweenlands.common.capability.decay.DecayStats;
import thebetweenlands.common.config.BetweenlandsConfig;
import thebetweenlands.common.registries.CapabilityRegistry;
import thebetweenlands.common.world.storage.BetweenlandsWorldStorage;
import thebetweenlands.util.MathUtils;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class CapabilityEventHandler {
    public static final UUID PACIFIST_HEALTH_MODIFIER_ATTRIBUTE_UUID = UUID.fromString("fa67f2aa-0645-4adc-994b-74d0954ab739");


    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (GeneralConfig.UntruePacifist) {
            EntityLivingBase entity = event.getEntityLiving();
            PacifistEntry entry = ModRecipes.getPacifistEntry(entity);

            if (entry != null && entity.isServerWorld()) {
                DamageSource source = event.getSource();
                if (source instanceof EntityDamageSource) {
                    Entity trueSource = source.getTrueSource();
                    if (trueSource instanceof EntityPlayerMP) {
                        if (entry.matches(entity, (EntityPlayer)trueSource)) {
                            if (entity.getControllingPassenger() == null) {
                                List<Entity> entities = entity.getEntityWorld().getEntitiesInAABBexcluding(entity, DeathEventHandler.BOUNDING_BOX.offset(entity.getPosition()), (o) -> {
                                    return EntityUtil.isHostileTo(entity, (EntityPlayer)trueSource);
                                });

                                if (entities.isEmpty()) {
                                    PacifistCapability cap = trueSource.getCapability(PacifistCapability.INSTANCE, null);

                                    if(cap != null) {
                                        boolean brokePacifism = cap.SetPacifist(false);

                                        if(brokePacifism) {
                                            BlockPos pos = trueSource.getPosition();
                                            trueSource.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE + "You have broken your bond with nature by taking an innocent life. Any benefits from being a pacifist have been revoked until you ask Nature for forgiveness."));
                                            trueSource.world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.PLAYERS, 0.75F, 0.75F);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public static void AdjustPlayerHealth(EntityPlayer player) {
        PacifistCapability cap = player.getCapability(PacifistCapability.INSTANCE, null);
        IAttributeInstance attr = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
        attr.removeModifier(PACIFIST_HEALTH_MODIFIER_ATTRIBUTE_UUID);
        attr.applyModifier(new AttributeModifier(PACIFIST_HEALTH_MODIFIER_ATTRIBUTE_UUID, "Pacifist health modifier", -(cap.ForgivenessCount() / 10f) * player.getMaxHealth(), 0));
    }


    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        EntityPlayer player = (EntityPlayer)event.getEntity();

        PacifistCapability cap = player.getCapability(PacifistCapability.INSTANCE, null);
        PacifistCapability capOld = event.getOriginal().getCapability(PacifistCapability.INSTANCE, null);

        if(cap != null && capOld != null) {
            cap.CopyCapability(capOld);
            AdjustPlayerHealth(player);
        }
    }
}
