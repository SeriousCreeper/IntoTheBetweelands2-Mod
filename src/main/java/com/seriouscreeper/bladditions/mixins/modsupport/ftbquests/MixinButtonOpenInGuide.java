package com.seriouscreeper.bladditions.mixins.modsupport.ftbquests;

import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.feed_the_beast.ftbquests.gui.tree.ButtonOpenInGuide;
import com.feed_the_beast.ftbquests.quest.Quest;
import epicsquid.roots.integration.IntegrationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Mixin(value = ButtonOpenInGuide.class, remap = false)
public class MixinButtonOpenInGuide {
    @Shadow @Final private Quest quest;

    /**
     * @author SC
     */
    @Overwrite
    public void onClicked(MouseButton button) {
        String[] split = this.quest.guidePage.split(":");

        if(split.length < 3) {
            return;
        }

        ResourceLocation bookPath = new ResourceLocation(split[0], split[1]);
        ResourceLocation entryPath = new ResourceLocation(split[0], split[2]);

        Book book = (Book) BookRegistry.INSTANCE.books.get(bookPath);
        BookEntry entry = null;

        if (book != null) {
            PatchouliAPI.instance.openBookGUI(bookPath);
            SoundEvent sfx = SoundEvent.REGISTRY.getObject(new ResourceLocation("patchouli", "book_open"));

            if (sfx != null) {
                Minecraft.getMinecraft().world.playSound(null, Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY, Minecraft.getMinecraft().player.posZ, sfx, SoundCategory.PLAYERS, 1F, (float) (0.7 + Math.random() * 0.4));
            }

            Iterator var8 = book.contents.entries.keySet().iterator();

            while(var8.hasNext()) {
                ResourceLocation location = (ResourceLocation)var8.next();
                if (entryPath.equals(location)) {
                    entry = (BookEntry)book.contents.entries.get(location);
                    break;
                }
            }

            if (entry != null && !entry.isLocked()) {
                GuiBookEntry pageToOpen = new GuiBookEntry(book, entry, 0);
                book.contents.openLexiconGui(pageToOpen, true);
            }
        }
    }
}
