package com.seriouscreeper.bladditions.mixins.modsupport.asetherworks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.api.environment.IEnvironmentEvent;
import thebetweenlands.common.world.storage.BetweenlandsWorldStorage;
import v0id.aw.common.tile.TilePrism;

import java.util.List;

@Mixin(value = TilePrism.class, remap = false)
public class MixinTilePrism extends TileEntity {
    /**
     * @author SC
     * @reason make it work with auroras
     */
    @Overwrite
    private boolean checkWorkConditions() {
        BetweenlandsWorldStorage storage = BetweenlandsWorldStorage.forWorld(world);
        List<IEnvironmentEvent> activeEvents = storage.getEnvironmentEventRegistry().getActiveEvents();

        for(IEnvironmentEvent activeEvent : activeEvents) {
            String eventName = activeEvent.getEventName().getPath();

            if (!eventName.equals("auroras")) {
                continue;
            }

            World w = this.world;
            BlockPos pos = this.getPos();
            return w.canBlockSeeSky(pos.up()) && w.canBlockSeeSky(pos.north(3).up()) && w.canBlockSeeSky(pos.south(3).up()) && w.canBlockSeeSky(pos.west(3).up()) && w.canBlockSeeSky(pos.east(3).up());
        }

        return false;
    }
}
