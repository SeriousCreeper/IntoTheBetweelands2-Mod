package com.seriouscreeper.bladditions.mixins.modsupport.arcaneworld;

import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import party.lemons.arcaneworld.block.BlockReturnPortal;
import party.lemons.arcaneworld.config.ArcaneWorldConfig;
import party.lemons.arcaneworld.gen.dungeon.dimension.TeleporterDungeonReturn;
import party.lemons.arcaneworld.util.capabilities.IRitualCoordinate;
import party.lemons.arcaneworld.util.capabilities.RitualCoordinateProvider;

@Mixin(value = BlockReturnPortal.class)
public class MixinBlockReturnPortal {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return false;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
        if (worldIn.provider.getDimension() == ConfigBLAdditions.configGeneral.DungeonDimensionID) {
            if (!worldIn.isRemote && entity instanceof EntityPlayer) {
                entity.timeUntilPortal = entity.getPortalCooldown();
                int returnDim = 20;
                entity.changeDimension(returnDim, new TeleporterDungeonReturn((WorldServer)worldIn));
            }
        }
    }
}
