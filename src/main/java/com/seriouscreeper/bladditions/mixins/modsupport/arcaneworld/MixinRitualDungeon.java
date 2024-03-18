package com.seriouscreeper.bladditions.mixins.modsupport.arcaneworld;

import com.seriouscreeper.bladditions.compat.arcaneworld.TeleporterDungeonCustom;
import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.arcaneworld.config.ArcaneWorldConfig;
import party.lemons.arcaneworld.crafting.ritual.impl.RitualDungeon;
import party.lemons.arcaneworld.gen.dungeon.dimension.TeleporterDungeon;
import party.lemons.arcaneworld.util.capabilities.IRitualCoordinate;
import party.lemons.arcaneworld.util.capabilities.RitualCoordinateProvider;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;

@Mixin(value = RitualDungeon.class, remap = false)
public class MixinRitualDungeon {
    @Inject(method = "onActivate", at = @At("HEAD"), cancellable = true)
    public void injectOnActivate(World world, BlockPos pos, EntityPlayer player, ItemStack[] items, CallbackInfo ci) {
        WorldServer ws = (WorldServer)world;
        TeleporterDungeon teleporter = new TeleporterDungeonCustom(ws);
        List<EntityLivingBase> players = world.getEntitiesWithinAABB(EntityLivingBase.class, (new AxisAlignedBB(pos)).grow(5.0, 5.0, 5.0));
        Iterator var8 = players.iterator();

        while(var8.hasNext()) {
            EntityLivingBase p = (EntityLivingBase)var8.next();
            ((IRitualCoordinate)p.getCapability(RitualCoordinateProvider.RITUAL_COORDINATE_CAPABILITY, (EnumFacing)null)).setPos(new BlockPos(p.posX, p.posY, p.posZ));
            ((IRitualCoordinate)p.getCapability(RitualCoordinateProvider.RITUAL_COORDINATE_CAPABILITY, (EnumFacing)null)).setDim(p.dimension);
            p.changeDimension(ConfigBLAdditions.configGeneral.DungeonDimensionID, teleporter);
        }

        ci.cancel();
    }
}
