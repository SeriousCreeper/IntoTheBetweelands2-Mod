package com.seriouscreeper.bladditions.mixins.modsupport.roots;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualFrostLands;
import epicsquid.roots.network.fx.MessageFrostLandsProgressFX;
import epicsquid.roots.ritual.IColdRitual;
import epicsquid.roots.ritual.RitualFrostLands;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thebetweenlands.common.registries.BlockRegistry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(value = EntityRitualFrostLands.class, remap = false)
public class MixinEntityRitualFrostLands extends EntityRitualBase implements IColdRitual {
    @Shadow private RitualFrostLands ritual;

    public MixinEntityRitualFrostLands(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true)
    public void onUpdate(CallbackInfo ci) {
        super.onUpdate();
        if (!this.world.isRemote) {
            List<BlockPos> affectedPositions = new ArrayList();
            if (this.ticksExisted % this.ritual.interval_heal == 0) {
                List<BlockPos> positions = Util.getBlocksWithinRadius(this.world, this.getPosition(), (float)this.ritual.radius_x, (float)this.ritual.radius_y, (float)this.ritual.radius_z, (posx) -> {
                    return this.world.isAirBlock(posx.up()) && !this.world.isAirBlock(posx) && Blocks.SNOW_LAYER.canPlaceBlockAt(this.world, posx);
                });

                BlockPos choice;
                IBlockState state;
                for(int breakout = 0; !positions.isEmpty() && breakout < 50; ++breakout) {
                    choice = (BlockPos)positions.get(this.rand.nextInt(positions.size()));
                    state = this.world.getBlockState(choice);
                    if (state.getBlock() != Blocks.SNOW_LAYER) {
                        if (Blocks.SNOW_LAYER.canPlaceBlockAt(this.world, choice.up())) {
                            this.world.setBlockState(choice.up(), Blocks.SNOW_LAYER.getDefaultState());
                            affectedPositions.add(choice.up());
                            break;
                        }
                    } else if ((Integer)state.getValue(BlockSnow.LAYERS) < 8 && Util.rand.nextInt(5) == 0) {
                        this.world.setBlockState(choice, state.withProperty(BlockSnow.LAYERS, Math.min((Integer)state.getValue(BlockSnow.LAYERS) + 1, 3)));
                        affectedPositions.add(choice);
                        break;
                    }
                }

                positions = Util.getBlocksWithinRadius(this.world, this.getPosition(), (float)this.ritual.radius_x, (float)this.ritual.radius_y, (float)this.ritual.radius_z, (posx) -> {
                    return (GeneralConfig.getWaterBlocks().contains(this.world.getBlockState(posx).getBlock()) || this.world.getBlockState(posx).getBlock() == Blocks.LAVA) && this.world.isAirBlock(posx.up());
                });
                if (!positions.isEmpty()) {
                    choice = (BlockPos)positions.get(this.rand.nextInt(positions.size()));
                    state = this.world.getBlockState(choice);
                    if (GeneralConfig.getWaterBlocks().contains(state.getBlock())) {
                        if ((Integer)state.getValue(BlockLiquid.LEVEL) == 0) {
                            this.world.setBlockState(choice, BlockRegistry.BLACK_ICE.getDefaultState());
                            affectedPositions.add(choice);
                        }
                    }
                }

                positions = Util.getBlocksWithinRadius(this.world, this.getPosition(), (float)this.ritual.radius_x, (float)this.ritual.radius_y, (float)this.ritual.radius_z, Blocks.FIRE);

                for (BlockPos position : positions) {
                    this.world.setBlockToAir(position);
                    affectedPositions.add(position);
                }
            }

            if (!affectedPositions.isEmpty()) {
                MessageFrostLandsProgressFX progress = new MessageFrostLandsProgressFX(affectedPositions);
                PacketHandler.sendToAllTracking(progress, this);
            }
        }

        ci.cancel();
    }
}
