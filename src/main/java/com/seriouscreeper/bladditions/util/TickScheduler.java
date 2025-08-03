package com.seriouscreeper.bladditions.util;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.function.Consumer;

public class TickScheduler {
    private static int ticksRemaining = -1;
    private static Consumer<Void> task = null;

    public static void schedule(int ticks, Consumer<Void> run) {
        ticksRemaining = ticks;
        task = run;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && ticksRemaining > 0) {
            ticksRemaining--;
            if (ticksRemaining == 0 && task != null) {
                task.accept(null);
                task = null;
            }
        }
    }
}
