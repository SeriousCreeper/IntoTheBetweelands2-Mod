package com.seriouscreeper.bladditions.proxy;

import com.seriouscreeper.bladditions.BLAdditions;
import com.seriouscreeper.bladditions.client.renderers.GreeblingMerchantRender;
import com.seriouscreeper.bladditions.client.renderers.PatchedTileCrucibleRenderer;
import com.seriouscreeper.bladditions.entities.GreeblingMerchantEntity;
import com.seriouscreeper.bladditions.init.ModBlocks;
import com.seriouscreeper.bladditions.init.ModItems;
import com.seriouscreeper.bladditions.tiles.TileCrucibleSwamp;
import hunternif.mc.atlas.api.AtlasAPI;
import hunternif.mc.atlas.registry.MarkerType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        RenderingRegistry.registerEntityRenderingHandler(GreeblingMerchantEntity.class, GreeblingMerchantRender::new);
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        this.registerTESR(TileCrucibleSwamp.class, new PatchedTileCrucibleRenderer());

        registerAntiqueAtlasTextures();
    }

    @Override
    public void registerAntiqueAtlasTextures() {
        AtlasAPI.getMarkerAPI().registerMarker(new MarkerType(CommonProxy.MARKER_CRAGROCK_TOWER, new ResourceLocation(BLAdditions.MODID, "textures/markers/cragrock_tower.png")));
        AtlasAPI.getMarkerAPI().registerMarker(new MarkerType(CommonProxy.MARKER_SPIRIT_TREE, new ResourceLocation(BLAdditions.MODID, "textures/markers/spirit_tree.png")));
        AtlasAPI.getMarkerAPI().registerMarker(new MarkerType(CommonProxy.MARKER_SLUDGEON, new ResourceLocation(BLAdditions.MODID, "textures/markers/sludgeon.png")));
        AtlasAPI.getMarkerAPI().registerMarker(new MarkerType(CommonProxy.MARKER_WIGHT_FORTRESS, new ResourceLocation(BLAdditions.MODID, "textures/markers/wight_fortress.png")));
        AtlasAPI.getMarkerAPI().registerMarker(new MarkerType(CommonProxy.MARKER_GIANT_TREE, new ResourceLocation(BLAdditions.MODID, "textures/markers/giant_tree.png")));
        AtlasAPI.getMarkerAPI().registerMarker(new MarkerType(CommonProxy.MARKER_MENHIR, new ResourceLocation(BLAdditions.MODID, "textures/markers/menhir.png")));
        AtlasAPI.getMarkerAPI().registerMarker(new MarkerType(CommonProxy.MARKER_IDOL_HEAD, new ResourceLocation(BLAdditions.MODID, "textures/markers/idol_head.png")));
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModBlocks.registerRenders();
        ModItems.initModels();
    }

    private void registerTESR(Class tile, TileEntitySpecialRenderer renderer) {
        ClientRegistry.bindTileEntitySpecialRenderer(tile, renderer);
    }
}
