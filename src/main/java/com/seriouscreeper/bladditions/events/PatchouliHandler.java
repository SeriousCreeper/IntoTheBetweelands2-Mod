package com.seriouscreeper.bladditions.events;

import com.seriouscreeper.bladditions.util.TickScheduler;
import net.darkhax.gamestages.event.GameStageEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.patchouli.client.base.ClientAdvancements;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

@Mod.EventBusSubscriber(Side.CLIENT)
public class PatchouliHandler {
    private static void updateBooks() {
        for (Book book : BookRegistry.INSTANCE.books.values()) {
            for (BookEntry entry : book.contents.entries.values()) {
                entry.updateLockStatus();
            }

            ClientAdvancements.setDoneAdvancements(true, false);
            book.reloadLocks(false);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onStageUnlocked(GameStageEvent.Added event) {
        TickScheduler.schedule(10, (v) -> updateBooks());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onStageRemoved(GameStageEvent.Removed event) {
        TickScheduler.schedule(10, (v) -> updateBooks());
    }
}
