package com.seriouscreeper.bladditions.mixins.modsupport.roots;

import epicsquid.roots.world.data.UUIDRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;
import java.util.function.Function;

@Mixin(value = UUIDRegistry.class, remap = false)
public class MixinUUIDRegistry<T extends WorldSavedData> {
    @Shadow private Class<? extends T> clazz;
    @Shadow private Function<UUID, String> nameConverter;
    @Shadow private Function<UUID, T> builder;


    /**
     * @author
     * @reason
     */
    @Overwrite
    public T getDataInternal(UUID id) {
        MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();

        if(mcServer == null) {
            return null;
        }

        WorldServer server = mcServer.getWorld(0);
        MapStorage storage = server.getMapStorage();

        if (storage == null) {
            throw new NullPointerException("Map storage is null");
        }

        T data = (T) storage.getOrLoadData(clazz, nameConverter.apply(id));

        if (data == null) {
            data = builder.apply(id);
            server.getMapStorage().setData(nameConverter.apply(id), data);
        }

        return data;
    }
}
