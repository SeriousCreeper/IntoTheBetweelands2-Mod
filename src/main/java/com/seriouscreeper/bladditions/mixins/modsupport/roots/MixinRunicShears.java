package com.seriouscreeper.bladditions.mixins.modsupport.roots;

import com.seriouscreeper.bladditions.proxy.CommonProxy;
import epicsquid.mysticallib.item.ItemShearsBase;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.advancements.Advancements;
import epicsquid.roots.capability.life_essence.LifeEssenceCapability;
import epicsquid.roots.capability.life_essence.LifeEssenceCapabilityProvider;
import epicsquid.roots.capability.runic_shears.RunicShearsCapability;
import epicsquid.roots.capability.runic_shears.RunicShearsCapabilityProvider;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.event.AdvancementHandler;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.item.ItemRunicShears;
import epicsquid.roots.network.fx.MessageRunicShearsFX;
import epicsquid.roots.recipe.RunicShearEntityRecipe;
import epicsquid.roots.recipe.RunicShearRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

@Mixin(value = ItemRunicShears.class, remap = false)
public class MixinRunicShears extends ItemShearsBase {
    public MixinRunicShears(String name, Supplier<Ingredient> repairIngredient) {
        super(name, repairIngredient);
    }

    /**
     * @author SC
     */
    @Overwrite
    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
        World world = player.world;
        Random rand = itemRand;

        if (entity.isChild()) {
            return true;
        } else {
            if (!player.isSneaking()) {
                RunicShearEntityRecipe recipe = ModRecipes.getRunicShearRecipe(entity);
                if (recipe != null) {
                    player.swingArm(hand);

                    if (!world.isRemote) {
                        RunicShearsCapability cap = (RunicShearsCapability)entity.getCapability(RunicShearsCapabilityProvider.RUNIC_SHEARS_CAPABILITY, (EnumFacing)null);
                        if (cap != null) {
                            if (cap.canHarvest()) {
                                long cooldown = (long)recipe.getCooldown();

                                boolean isPacifist = CommonProxy.IsPacifist((EntityPlayerMP) player);

                                if(isPacifist) {
                                    cooldown /= 4;
                                }

                                cap.setCooldown(cooldown);
                                EntityItem ent = entity.entityDropItem(recipe.getDrop(entity).copy(), 1.0F);
                                ent.motionY += (double)(rand.nextFloat() * 0.05F);
                                ent.motionX += (double)((rand.nextFloat() - rand.nextFloat()) * 0.1F);
                                ent.motionZ += (double)((rand.nextFloat() - rand.nextFloat()) * 0.1F);
                                if (!player.capabilities.isCreativeMode) {
                                    itemstack.damageItem(1, entity);
                                }

                                world.playSound((EntityPlayer)null, entity.getPosition(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
                                IMessage packet = new MessageRunicShearsFX(entity);
                                PacketHandler.sendToAllTracking(packet, entity);
                                return true;
                            }

                            player.sendStatusMessage((new TextComponentTranslation("roots.runic_shears.cooldown", new Object[0])).setStyle((new Style()).setColor(TextFormatting.DARK_PURPLE)), true);
                        }
                    }
                }
            }

            if (entity instanceof IShearable) {
                int count = 0;
                if (Items.SHEARS.itemInteractionForEntity(itemstack, player, entity, hand)) {
                    ++count;
                }

                float radius = (float) GeneralConfig.RunicShearsRadius;
                List<EntityLiving> entities = Util.getEntitiesWithinRadius(entity.world, (ex) -> {
                    return ex instanceof IShearable;
                }, entity.getPosition(), radius, radius / 2.0F, radius);
                Iterator var21 = entities.iterator();

                while(true) {
                    EntityLiving e;
                    do {
                        if (!var21.hasNext()) {
                            if (count > 0) {
                                player.swingArm(hand);
                                return true;
                            }

                            return false;
                        }

                        e = (EntityLiving)var21.next();
                        e.captureDrops = true;
                        if (Items.SHEARS.itemInteractionForEntity(itemstack, player, e, hand)) {
                            ++count;
                        }

                        e.captureDrops = false;
                    } while(world.isRemote);

                    Iterator var12 = e.capturedDrops.iterator();

                    while(var12.hasNext()) {
                        EntityItem ent = (EntityItem)var12.next();
                        ent.setPosition(entity.posX, entity.posY, entity.posZ);
                        ent.motionY = 0.0D;
                        ent.motionX = 0.0D;
                        ent.motionZ = 0.0D;
                        ent.world.spawnEntity(ent);
                    }
                }
            } else {
                return false;
            }
        }
    }
}
