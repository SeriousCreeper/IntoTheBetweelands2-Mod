package com.seriouscreeper.bladditions.compat.embers.modifier;

import net.minecraftforge.common.MinecraftForge;
import teamroots.embers.api.itemmod.ModifierBase;

public class ModifierRubberBoots extends ModifierBase {
    public ModifierRubberBoots() {
        super(EnumType.BOOTS, "rubber_padding", 0.0, false);
        MinecraftForge.EVENT_BUS.register(this);
    }
}
