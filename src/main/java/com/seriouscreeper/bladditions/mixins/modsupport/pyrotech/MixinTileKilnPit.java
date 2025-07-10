package com.seriouscreeper.bladditions.mixins.modsupport.pyrotech;

import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileKilnPit;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileKilnPit.class, remap = false)
public class MixinTileKilnPit {
    @Shadow private boolean active;

    @Shadow
    protected boolean isStructureValid() {
        return false;
    }

    @Inject(method = "setActive", at = @At("HEAD"), cancellable = true)
    private void bladditions$setActive(boolean active, CallbackInfo ci) {
        if(this.active || !this.isStructureValid()) {
            ci.cancel();
            return;
        }

        this.active = true;

        // check diagonal neighbors if they are tileKilnPit as well, and if so, set them active if not already
        TileKilnPit tileKilnPit = (TileKilnPit) (Object) this;

        if (tileKilnPit.getWorld().isRemote) {
            return;
        }

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 || z == 0) {
                    continue;
                }

                TileEntity neighbor = tileKilnPit.getWorld().getTileEntity(tileKilnPit.getPos().add(x, 0, z));

                if (neighbor instanceof TileKilnPit) {
                    tileKilnPit.getWorld().setBlockState(neighbor.getPos().up(), Blocks.FIRE.getDefaultState());
                    TileKilnPit neighborKilnPit = (TileKilnPit) neighbor;
                    neighborKilnPit.setActive(active);
                }
            }
        }
    }
}
