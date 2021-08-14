package com.seriouscreeper.bladditions.proxy;

import com.seriouscreeper.bladditions.client.renderers.GreeblingMerchantRender;
import com.seriouscreeper.bladditions.client.renderers.PatchedTileCrucibleRenderer;
import com.seriouscreeper.bladditions.entities.GreeblingMerchantEntity;
import com.seriouscreeper.bladditions.init.ModBlocks;
import com.seriouscreeper.bladditions.init.ModItems;
import com.seriouscreeper.bladditions.tiles.TileCrucibleSwamp;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import party.lemons.deliverymechants.MerchantEntity;
import party.lemons.deliverymechants.MerchantRender;
import thaumcraft.client.renderers.tile.TileCrucibleRenderer;
import thaumcraft.common.tiles.crafting.TileCrucible;
import thebetweenlands.client.render.entity.RenderGreebling;
import thebetweenlands.common.TheBetweenlands;

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
        //ModBlocks.registerRenders();
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
