package com.seriouscreeper.bladditions.mixins.modsupport.sanity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.tiffit.sanity.SanityCapability;
import net.tiffit.sanity.consequences.ConsequenceManager;
import net.tiffit.sanity.consequences.IConsequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.*;

@Mixin(value = ConsequenceManager.class, remap = false)
public class MixinConsequenceManager {
    @Shadow
    public static List<IConsequence> CONSEQUENCES;

    @Shadow
    public static int getCooldown(IConsequence con, EntityPlayer p) {
        return 0;
    }

    @Shadow
    public static void reset(IConsequence con, EntityPlayer p) {
    }

    /**
     * @author SC
     * @reason
     */
    @Overwrite
    public static List<IConsequence> roll(EntityPlayer p) {
        List<IConsequence> cons = new ArrayList<>();
        SanityCapability cap = (SanityCapability)p.getCapability(SanityCapability.INSTANCE, (EnumFacing)null);

        if(cap == null) {
            return cons;
        }

        SanityCapability.SanityLevel level = cap.getSanity();

        for (IConsequence con : CONSEQUENCES) {
            if (getCooldown(con, p) == 0) {
                reset(con, p);

                if (Math.random() <= con.getChance(level)) {
                    cons.add(con);
                }
            }
        }

        return cons;
    }
}
