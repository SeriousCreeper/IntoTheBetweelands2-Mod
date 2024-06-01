package com.seriouscreeper.bladditions.init;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public class BLContainer extends DummyModContainer {
    public BLContainer() {
        super(new ModMetadata());
        ModMetadata meta = this.getMetadata();
        meta.modId = "bladditionscore";
        meta.name = "ITBL2 Additions Core";
        meta.description = "Core functionality of ITBL2 Utility Mod";
        meta.version = "1.12.2-1.0.0";
        meta.authorList.add("SeriousCreeper");
    }


    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }
}
