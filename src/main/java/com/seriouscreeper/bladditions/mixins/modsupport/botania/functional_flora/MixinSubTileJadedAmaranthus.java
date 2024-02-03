package com.seriouscreeper.bladditions.mixins.modsupport.botania.functional_flora;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.subtile.functional.SubTileJadedAmaranthus;
import vazkii.botania.common.core.handler.ConfigHandler;

@Mixin(value = SubTileJadedAmaranthus.class, remap = false)
public class MixinSubTileJadedAmaranthus extends SubTileFunctional {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onUpdate() {
        super.onUpdate();
        if (!this.supertile.getWorld().isRemote && this.redstoneSignal <= 0) {
            if (this.ticksExisted % 30 == 0 && this.mana >= 100) {
                BlockPos pos = new BlockPos(this.supertile.getPos().getX() - 4 + this.supertile.getWorld().rand.nextInt(9), this.supertile.getPos().getY() + 4, this.supertile.getPos().getZ() - 4 + this.supertile.getWorld().rand.nextInt(9));
                BlockPos up = pos.up();

                for(int i = 0; i < 8; ++i) {
                    IBlockState stateAbove = this.supertile.getWorld().getBlockState(up);
                    Block blockAbove = stateAbove.getBlock();
                    if ((this.supertile.getWorld().isAirBlock(up) || blockAbove.isReplaceable(this.supertile.getWorld(), up)) && stateAbove.getMaterial() != Material.WATER && ModBlocks.mushroom.canPlaceBlockAt(this.supertile.getWorld(), up)) {
                        EnumDyeColor color = EnumDyeColor.byMetadata(this.supertile.getWorld().rand.nextInt(16));
                        IBlockState state = ModBlocks.mushroom.getDefaultState().withProperty(BotaniaStateProps.COLOR, color);
                        if (ConfigHandler.blockBreakParticles) {
                            this.supertile.getWorld().playEvent(2001, up, Block.getStateId(state));
                        }

                        this.supertile.getWorld().setBlockState(up, state, 3);
                        this.mana -= 100;
                        this.sync();
                        break;
                    }

                    up = pos;
                    pos = pos.down();
                }
            }

        }
    }
}
