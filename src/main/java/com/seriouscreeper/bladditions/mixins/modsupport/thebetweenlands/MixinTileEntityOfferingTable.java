package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import com.seriouscreeper.bladditions.init.ModItems;
import com.seriouscreeper.bladditions.libs.AdminExecute;
import net.minecraft.command.FunctionObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thebetweenlands.common.item.equipment.ItemRing;
import thebetweenlands.common.registries.SoundRegistry;
import thebetweenlands.common.tile.TileEntityGroundItem;
import thebetweenlands.common.tile.TileEntityOfferingTable;

import java.util.*;

@Mixin(value = TileEntityOfferingTable.class)
public class MixinTileEntityOfferingTable extends TileEntityGroundItem {
    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void injectUpdate(CallbackInfo ci) {
        ItemStack stack = this.getStack();

        if (!stack.isEmpty() && stack.getItem() == ModItems.corrupted_bone_wayfinder) {
            double radius = 2.5;
            AxisAlignedBB aabb = (new AxisAlignedBB(this.getPos())).grow(radius);
            Set<EntityPlayer> teleportingPlayers = null;

            Iterator it;

            EntityPlayer player;

            for(it = this.world.getEntitiesWithinAABB(EntityPlayer.class, aabb, (p) -> {
                return p.isSneaking() && p.getDistanceSq((double)((float)this.pos.getX() + 0.5F), (double)((float)this.pos.getY() + 0.5F), (double)((float)this.pos.getZ() + 0.5F)) <= radius * radius;
            }).iterator(); it.hasNext(); this.setStack(stack)) {
                player = (EntityPlayer)it.next();

                if (teleportingPlayers == null) {
                    teleportingPlayers = new HashSet();
                }

                teleportingPlayers.add(player);

                int ticks = (Integer)this.teleportTicks.getOrDefault(player, 0);

                if (ticks >= 0 && !this.into_the_betweenlands_mod$updateDimensionTeleport(player, ticks, stack)) {
                    this.teleportTicks.put(player, -100);
                } else {
                    this.teleportTicks.put(player, ticks + 1);
                }
            }

            if (teleportingPlayers == null) {
                this.teleportTicks.clear();
            } else if (!this.teleportTicks.isEmpty()) {
                it = this.teleportTicks.keySet().iterator();

                while(it.hasNext()) {
                    player = (EntityPlayer)it.next();

                    if (!teleportingPlayers.contains(player)) {
                        it.remove();
                    }
                }
            }

            ci.cancel();
        }
    }


    @Unique
    public int into_the_betweenlands_mod$getDimension(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            assert nbt != null;
            if (nbt.hasKey("dimension")) {
                return nbt.getInteger("dimension");
            }
        }

        return 20;
    }


    @Unique
    private boolean into_the_betweenlands_mod$updateDimensionTeleport(EntityPlayer entity, int ticks, ItemStack stack) {
        if (ticks >= 100) {
            if (!entity.world.isRemote && stack.getItemDamage() < stack.getMaxDamage()) {
                double radius = 2.5;
                AxisAlignedBB aabb = (new AxisAlignedBB(this.getPos())).grow(radius);

                List<EntityPlayer> it = this.world.getEntitiesWithinAABB(EntityPlayer.class, aabb, (p) -> {
                    return p.getDistanceSq((double)((float)this.pos.getX() + 0.5F), (double)((float)this.pos.getY() + 0.5F), (double)((float)this.pos.getZ() + 0.5F)) <= radius * radius;
                });

                this.playThunderSounds(entity.world, entity.posX, entity.posY, entity.posZ);
                MinecraftServer server = entity.world.getMinecraftServer();

                for(EntityPlayer entityPlayer : it) {
                    if(entityPlayer.isRiding())
                        entityPlayer.dismountRidingEntity();

                    ICommandSender sender = new AdminExecute(entityPlayer, entityPlayer.getPosition());

                    String command = "tpj " + into_the_betweenlands_mod$getDimension(stack);

                    FunctionObject func = FunctionObject.create(server.getFunctionManager(), Arrays.asList(command));

                    server.getFunctionManager().execute(func, sender);

                    this.playThunderSounds(entity.world, entity.posX, entity.posY, entity.posZ);
                }

                stack.shrink(1);
            }
        } else if (stack.getItemDamage() < stack.getMaxDamage()) {
            int removed;
            if (!entity.world.isRemote) {
                if (ticks >= 40) {
                    int count = ticks - 40;
                    if (entity.hurtTime > 0) {
                        entity.world.playSound((EntityPlayer)null, entity.posX, entity.posY, entity.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    }

                    if (entity instanceof EntityPlayer && !entity.isCreative() && count < 60 && entity.ticksExisted % 3 == 0) {
                        removed = ItemRing.removeXp(entity, 1);
                        if (removed == 0) {
                            entity.world.playSound((EntityPlayer)null, entity.posX, entity.posY, entity.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                            return false;
                        }
                    }
                }

                if (ticks >= 15 && ticks < 90 && (ticks - 15) % 20 == 0) {
                    entity.world.playSound((EntityPlayer)null, entity.posX, entity.posY, entity.posZ, SoundRegistry.PORTAL_TRAVEL, SoundCategory.PLAYERS, 0.05F + 0.4F * (float) MathHelper.clamp(80 - ticks, 1, 80) / 80.0F, 0.9F + entity.world.rand.nextFloat() * 0.2F);
                }
            } else {
                if (ticks >= 15 && ticks % 4 == 0) {
                    this.spawnChargingParticles((float)this.world.getTotalWorldTime() * 0.035F, (float)this.pos.getX() + 0.5F, (float)this.pos.getY() + 0.4F, (float)this.pos.getZ() + 0.5F);
                }

                Random rand = entity.world.rand;

                for(removed = 0; removed < MathHelper.clamp(60 - ticks, 1, 60); ++removed) {
                    entity.world.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH, entity.posX + (double)(rand.nextBoolean() ? -1 : 1) * Math.pow((double)rand.nextFloat(), 2.0) * 6.0, entity.posY + (double)(rand.nextFloat() * 4.0F) - 2.0, entity.posZ + (double)(rand.nextBoolean() ? -1 : 1) * Math.pow((double)rand.nextFloat(), 2.0) * 6.0, 0.0, 0.2, 0.0, new int[0]);
                }
            }
        } else if (!entity.world.isRemote) {
            entity.world.playSound((EntityPlayer)null, entity.posX, entity.posY, entity.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
            return false;
        }

        return true;
    }


    @Shadow
    private void playThunderSounds(World world, double x, double y, double z) {
    }


    @Shadow
    @SideOnly(Side.CLIENT)
    private void spawnChargingParticles(float rot, float x, float y, float z) {
    }


    @Shadow
    private Map<EntityPlayer, Integer> teleportTicks = new WeakHashMap();
}
