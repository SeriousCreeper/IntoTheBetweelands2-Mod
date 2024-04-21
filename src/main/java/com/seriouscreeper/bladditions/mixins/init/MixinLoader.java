package com.seriouscreeper.bladditions.mixins.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.seriouscreeper.bladditions.BLAdditions;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModClassLoader;
import net.minecraftforge.fml.common.ModContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.transformer.MixinProcessor;
import org.spongepowered.asm.mixin.transformer.Proxy;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MixinLoader implements ILateMixinLoader {
    public static final List<String> modMixins = ImmutableList.of(
        "mixins.bladditions.init.json",
        "mixins.bladditions.modsupport.json"
    );

    @Override
    public List<String> getMixinConfigs() {
        return modMixins;
    }
}