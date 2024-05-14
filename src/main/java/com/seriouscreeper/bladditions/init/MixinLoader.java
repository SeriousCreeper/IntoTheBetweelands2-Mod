package com.seriouscreeper.bladditions.init;

import com.google.common.collect.ImmutableList;
import zone.rong.mixinbooter.ILateMixinLoader;
import java.util.List;

public class MixinLoader implements ILateMixinLoader {
    public static final List<String> modMixins = ImmutableList.of(
        "mixins.bladditions.modsupport.json"
    );

    @Override
    public List<String> getMixinConfigs() {
        return modMixins;
    }
}