package com.seriouscreeper.bladditions.mixins.modsupport.embers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import teamroots.embers.block.BlockBreaker;
import teamroots.embers.tileentity.TileEntityBreaker;
import thebetweenlands.common.world.storage.location.LocationStorage;

import java.lang.ref.WeakReference;
import java.util.List;

@Mixin(value = TileEntityBreaker.class, remap = false)
public class MixinTileEntityBreaker extends TileEntity {
    @Shadow private WeakReference<FakePlayer> fakePlayer;
    @Shadow
    int ticksExisted = 0;

    /**
     * @author SC
     * @reason
     */
    @Overwrite
    public void update() {
        ++this.ticksExisted;
        IBlockState state = this.world.getBlockState(this.pos);

        if (this.ticksExisted % 20 == 0 && this.isActive() && state.getBlock() instanceof BlockBreaker && !this.world.isRemote) {
            FakePlayer player = (FakePlayer)this.fakePlayer.get();
            EnumFacing facing = this.getFacing();
            BlockPos blockPos = this.pos.offset(facing);

            if(player == null) {
                return;
            }

            List<LocationStorage> locations = LocationStorage.getLocations(player.world, new Vec3d(blockPos));

            for(LocationStorage location : locations) {
                if(location != null && location.getGuard() != null && location.getGuard().isGuarded(player.world, player, blockPos)) {
                    return;
                }
            }

            this.mineBlock(blockPos);
        }
    }

    @Shadow
    public boolean isActive() {
        return !this.getWorld().isBlockPowered(this.getPos());
    }

    @Shadow
    public EnumFacing getFacing() {
        IBlockState state = this.world.getBlockState(this.pos);
        return (EnumFacing)state.getValue(BlockBreaker.facing);
    }

    @Shadow
    protected void mineBlock(BlockPos breakPos) {
    }
}
