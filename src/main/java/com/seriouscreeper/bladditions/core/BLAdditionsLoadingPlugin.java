package com.seriouscreeper.bladditions.core;

import com.seriouscreeper.bladditions.BLAdditions;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

@IFMLLoadingPlugin.Name(BLAdditions.MODID)
@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
@IFMLLoadingPlugin.SortingIndex(-8000)
public class BLAdditionsLoadingPlugin implements IFMLLoadingPlugin {
    public BLAdditionsLoadingPlugin() {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.bladditions.init.json");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
