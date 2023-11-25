package com.seriouscreeper.bladditions.mixins.modsupport.simpledifficulty;

import com.charles445.simpledifficulty.api.temperature.TemperatureEnum;
import com.charles445.simpledifficulty.capability.TemperatureCapability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.tiffit.sanity.SanityCapability;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TemperatureCapability.class, remap = false)
public abstract class MixinTemperatureCapability {
    private static TemperatureEnum ConvertSanityToTemp(SanityCapability.SanityLevel sanityLevel) {
        switch(sanityLevel) {
            case VERY_HEALTHY:
                return TemperatureEnum.BURNING;
            case HEALTHY:
                return TemperatureEnum.HOT;
            case DAMAGED:
                return TemperatureEnum.NORMAL;
            case VERY_DAMAGED:
                return TemperatureEnum.COLD;
            case INSANE:
                return TemperatureEnum.FREEZING;
        }

        return TemperatureEnum.NORMAL;
    }


    @Shadow
    private int packetTimer = 0;

    @Shadow
    private int updatetimer = 500;

    @Shadow
    private int targettemp = 0;

    @Shadow
    private boolean manualDirty = false;

    @Shadow
    public void addTemperatureTickTimer(int ticktimer) {
    }

    @Shadow
    public int getTemperatureTickTimer() {
        return 0;
    }

    @Shadow
    private int getTemperatureTickLimit() {
        return 0;
    }

    @Shadow
    public void setTemperatureTickTimer(int ticktimer) {
    }

    @Shadow public abstract void setTemperatureLevel(int temperature);

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void tickUpdate(EntityPlayer player, World world, TickEvent.Phase phase) {

        if (phase == TickEvent.Phase.START) {
            ++this.packetTimer;
        } else {
            ++this.updatetimer;
            if (this.updatetimer >= 5) {
                this.updatetimer = 0;
            }

            this.addTemperatureTickTimer(1);

            if (this.getTemperatureTickTimer() >= this.getTemperatureTickLimit()) {
                this.setTemperatureTickTimer(0);

                SanityCapability cap = (SanityCapability)player.getCapability(SanityCapability.INSTANCE, (EnumFacing)null);

                if(cap != null) {
                    setTemperatureLevel(ConvertSanityToTemp(cap.getSanity()).getLowerBound());
                }

                this.manualDirty = true;
            }
        }
    }
}
