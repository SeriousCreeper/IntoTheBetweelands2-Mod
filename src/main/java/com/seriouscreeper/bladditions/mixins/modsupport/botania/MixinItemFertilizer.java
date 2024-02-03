package com.seriouscreeper.bladditions.mixins.modsupport.botania;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ItemFertilizer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Mixin(value = ItemFertilizer.class, remap = false)
public class MixinItemFertilizer {
    /**
     * @author
     * @reason
     */
    @Overwrite
    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) {
        if (!world.isRemote) {
            List<BlockPos> validCoords = new ArrayList();

            int i;
            int j;
            for(i = -4; i < 3; ++i) {
                for(j = -4; j < 3; ++j) {
                    for(int k = 2; k >= -2; --k) {
                        BlockPos pos_ = pos.add(i + 1, k + 1, j + 1);
                        if (world.isAirBlock(pos_) && (!world.provider.isNether() || pos_.getY() < 255) && ModBlocks.mushroom.canPlaceBlockAt(world, pos_)) {
                            validCoords.add(pos_);
                        }
                    }
                }
            }

            i = Math.min(validCoords.size(), world.rand.nextBoolean() ? 3 : 4);

            for(j = 0; j < i; ++j) {
                BlockPos coords = (BlockPos)validCoords.get(world.rand.nextInt(validCoords.size()));
                validCoords.remove(coords);
                world.setBlockState(coords, ModBlocks.mushroom.getDefaultState().withProperty(BotaniaStateProps.COLOR, EnumDyeColor.byMetadata(world.rand.nextInt(16))), 3);
            }

            player.getHeldItem(hand).shrink(1);
        } else {
            for(int i = 0; i < 15; ++i) {
                double x = (double)(pos.getX() - 3 + world.rand.nextInt(7)) + Math.random();
                double y = (double)(pos.getY() + 1);
                double z = (double)(pos.getZ() - 3 + world.rand.nextInt(7)) + Math.random();
                float red = (float)Math.random();
                float green = (float)Math.random();
                float blue = (float)Math.random();
                Botania.proxy.wispFX(x, y, z, red, green, blue, 0.15F + (float)Math.random() * 0.25F, -((float)Math.random()) * 0.1F - 0.05F);
            }
        }

        return EnumActionResult.SUCCESS;
    }
}
