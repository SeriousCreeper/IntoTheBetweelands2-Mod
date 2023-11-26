package com.seriouscreeper.bladditions.sanity_consequences;

import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.SoundCategory;
import net.tiffit.sanity.SanityCapability;
import net.tiffit.sanity.consequences.IConsequence;

import java.util.Random;

public class ConsequenceSounds implements IConsequence {
    @Override
    public void run(EntityPlayerMP entityPlayerMP, SanityCapability.SanityLevel sanityLevel) {
        if(!entityPlayerMP.world.isRemote) {
            if(ConfigBLAdditions.configSanity.sanity_sounds_list.length == 0) {
                return;
            }

            Random rand = new Random();

            String soundName = ConfigBLAdditions.configSanity.sanity_sounds_list[rand.nextInt(ConfigBLAdditions.configSanity.sanity_sounds_list.length)];

            double offsetX = rand.nextDouble() * 20 - 10; // Adjust the range as needed
            double offsetY = rand.nextDouble() * 20 - 10;
            double offsetZ = rand.nextDouble() * 20 - 10;

            SPacketCustomSound packet = new SPacketCustomSound(soundName, SoundCategory.AMBIENT,
                    entityPlayerMP.posX + offsetX, entityPlayerMP.posY + offsetY, entityPlayerMP.posZ + offsetZ, 1, 1);

            // Send the sound packet to the player
            entityPlayerMP.connection.sendPacket(packet);
        }
    }

    @Override
    public double getChance(SanityCapability.SanityLevel sanityLevel) {
        int index = sanityLevel.ordinal();

        if(ConfigBLAdditions.configSanity.sanity_chance_sounds.length > index) {
            return ConfigBLAdditions.configSanity.sanity_chance_sounds[index];
        } else {
            return 0;
        }
    }

    @Override
    public int getCooldown() {
        return ConfigBLAdditions.configSanity.sanity_cooldown_sounds;
    }
}
