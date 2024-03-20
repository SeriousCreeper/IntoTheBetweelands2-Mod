package com.seriouscreeper.bladditions.mixins.modsupport.roots.spells;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.RayCastUtil;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageLifeInfusionFX;
import epicsquid.roots.network.fx.MessageRampantLifeInfusionFX;
import epicsquid.roots.spell.SpellGrowthInfusion;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thebetweenlands.api.block.IFarmablePlant;
import thebetweenlands.common.block.farming.BlockGenericDugSoil;
import thebetweenlands.common.tile.TileEntityDugSoil;
import vazkii.botania.common.block.decor.BlockModMushroom;

@Mixin(value = SpellGrowthInfusion.class, remap = false)
public class MixinSpellGrowthInfusion {
    @Shadow public static SpellGrowthInfusion instance;

    @Inject(method = "cast", at = @At("HEAD"), cancellable = true)
    private void injectCast(EntityPlayer player, StaffModifierInstanceList info, int ticks, CallbackInfoReturnable<Boolean> cir) {
        RayTraceResult result = RayCastUtil.rayTraceBlocksSight(player.world, player, 8.0F);

        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos pos = result.getBlockPos();
            IBlockState state = player.world.getBlockState(pos);
            Block block = state.getBlock();

            if (block instanceof IFarmablePlant && (!(block instanceof IGrowable) || block instanceof BlockModMushroom)) {
                TileEntityDugSoil te = BlockGenericDugSoil.getTile(player.world, pos.down());
                IFarmablePlant plant = (IFarmablePlant)block;

                if (te != null && te.isComposted() && plant.isFarmable(player.world, pos, state)) {
                    BlockPos offset = pos;

                    switch (player.world.rand.nextInt(4)) {
                        case 0:
                            offset = offset.north();
                            break;
                        case 1:
                            offset = offset.south();
                            break;
                        case 2:
                            offset = offset.east();
                            break;
                        case 3:
                            offset = offset.west();
                    }

                    float spreadChance = plant.getSpreadChance(player.world, pos, state, offset, player.world.rand) / 10f;

                    if (player.world.rand.nextFloat() <= spreadChance && plant.canSpreadTo(player.world, pos, state, offset, player.world.rand)) {
                        plant.spreadTo(player.world, pos, state, offset, player.world.rand);
                        te.setCompost(Math.max(te.getCompost() - plant.getCompostCost(player.world, pos, state, player.world.rand), 0));
                    }

                    if (player.world.rand.nextInt(3) == 0) {
                        PacketHandler.sendToAllTracking(new MessageLifeInfusionFX((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), player);
                    }

                    cir.setReturnValue(true);
                }
            }
        }
    }
}
