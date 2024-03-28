package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import com.seriouscreeper.bladditions.capability.PacifistCapability;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thebetweenlands.common.handler.BlockBreakHandler;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.world.storage.BetweenlandsWorldStorage;
import thebetweenlands.common.world.storage.location.EnumLocationType;
import thebetweenlands.common.world.storage.location.LocationStorage;

import java.util.List;

@Mixin(value = BlockBreakHandler.class, remap = false)
public class MixinBlockBreakHandler {
    @Inject(method = "onBlockBreak", at = @At("HEAD"), cancellable = true)
    private static void injectOnBlockBreak(BlockEvent.BreakEvent event, CallbackInfo ci) {
        EntityPlayer player = event.getPlayer();
        if(player != null && !event.getWorld().isRemote) {
            BlockPos pos = event.getPos();
            IBlockState blockState = event.getState();

            if (!player.isCreative()) {
                if (blockState.getBlock() == BlockRegistry.WEEDWOOD || blockState.getBlock() == BlockRegistry.LOG_WEEDWOOD) {
                    BetweenlandsWorldStorage worldStorage = BetweenlandsWorldStorage.forWorld(event.getWorld());
                    List<LocationStorage> locations = worldStorage.getLocalStorageHandler().getLocalStorages(LocationStorage.class, pos.getX(), pos.getZ(), location -> location.isInside(pos));

                    for (LocationStorage location : locations) {
                        if (location.getType() == EnumLocationType.GIANT_TREE) {
                            PacifistCapability cap = player.getCapability(PacifistCapability.INSTANCE, null);

                            if(cap != null && cap.IsPacifist()) {
                                ci.cancel();
                            }
                        }
                    }
                }
            }
        }
    }
}
