package com.seriouscreeper.bladditions.events;

import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import com.seriouscreeper.bladditions.util.BlankTeleporter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thebetweenlands.common.entity.draeton.DraetonPhysicsPart;
import thebetweenlands.common.entity.draeton.EntityDraeton;
import thebetweenlands.common.item.misc.ItemMob;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class TeleportWorldEventHandler {
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START || event.player.world.isRemote || event.player.ticksExisted % 20 != 0 || ConfigBLAdditions.draetonDimensions == null) {
            return;
        }

        EntityPlayer player = event.player;
        EntityPlayerMP playerMP = (EntityPlayerMP) player;

        int[] teleportInfo = new int[0];

        for(int[] entry : ConfigBLAdditions.draetonDimensions) {
            if(player.dimension == entry[0]) {
                teleportInfo = entry;
                break;
            }
        }

        if(teleportInfo.length == 0) {
            return;
        }

        int targetDim = teleportInfo[1];
        int minHeight = teleportInfo[2];
        int maxHeight = teleportInfo[3];

        int posY = teleportInfo[4];

        // Check if the player is in a teleport world
        if (player.posY >= minHeight && player.posY <= maxHeight) {
            if (player.isRiding()) {
                if (!(player.getRidingEntity() instanceof EntityDraeton)) {
                    return;
                }

                EntityDraeton draeton = (EntityDraeton)player.getRidingEntity();
                player.dismountRidingEntity();

                // Transfer mount to target dimension
                MinecraftServer server = player.getServer();
                WorldServer oldWorld = (WorldServer) draeton.world;
                WorldServer newWorld = server.getWorld(targetDim);

                draeton.dimension = targetDim;
                oldWorld.removeEntityDangerously(draeton);
                draeton.isDead = false;

                // store puller entities in a list
                ArrayList<Entity> pullerEntities = new ArrayList<>();

                for(int i = 0; i < 6; ++i) {
                    ItemStack stack = draeton.getPullersInventory().getStackInSlot(i);

                    if (!stack.isEmpty() && stack.getItem() instanceof ItemMob) {
                        DraetonPhysicsPart puller = draeton.getPhysicsPartBySlot(i, DraetonPhysicsPart.Type.PULLER);
                        Entity pullerEntity = puller.getEntity();

                        if (pullerEntity == null) {
                            continue;
                        }

                        pullerEntity.dimension = targetDim;
                        oldWorld.removeEntityDangerously(pullerEntity);
                        pullerEntity.isDead = false;

                        pullerEntities.add(pullerEntity);
                    }
                }

                // Transfer player
                playerMP.server.getPlayerList().transferPlayerToDimension(playerMP, targetDim, new BlankTeleporter(newWorld, playerMP.posX, posY, playerMP.posZ));

                // Transfer draeton
                draeton.setWorld(newWorld);
                draeton.setPosition(playerMP.posX, posY, playerMP.posZ);
                newWorld.spawnEntity(draeton);

                // Transfer puller entities
                for (Entity pullerEntity : pullerEntities) {
                    pullerEntity.setWorld(newWorld);
                    pullerEntity.setPosition(playerMP.posX, posY, playerMP.posZ);
                    newWorld.spawnEntity(pullerEntity);
                }

                // Remount
                playerMP.startRiding(draeton, true);
            }
        }
    }
}
