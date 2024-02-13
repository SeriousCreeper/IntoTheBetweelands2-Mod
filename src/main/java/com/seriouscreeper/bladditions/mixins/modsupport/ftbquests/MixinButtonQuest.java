package com.seriouscreeper.bladditions.mixins.modsupport.ftbquests;

import com.feed_the_beast.ftblib.lib.gui.ContextMenuItem;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftbquests.gui.tree.ButtonQuest;
import com.feed_the_beast.ftbquests.quest.Quest;
import com.seriouscreeper.bladditions.ftbquests.CustomFTBTextEditorFrame;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = ButtonQuest.class, remap = false)
public class MixinButtonQuest {
    @Shadow public Quest quest;

    @Redirect(method = "onClicked", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private boolean redirectOnClicked(List<ContextMenuItem> instance, Object e) {
        ContextMenuItem item = (ContextMenuItem) e;

        if(((ContextMenuItem) e).title.equals(I18n.format("ftbquests.gui.edit_text", new Object[0]))) {
            return instance.add(new ContextMenuItem(I18n.format("ftbquests.gui.edit_text", new Object[0]), GuiIcons.INFO, () -> {
                CustomFTBTextEditorFrame.open(this.quest);
            }));
        }

        return instance.add(item);
    }
}
