package com.seriouscreeper.bladditions.mixins.modsupport.roots;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.entity.ritual.EntityRitualAnimalHarvest;
import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.network.fx.MessageRampantLifeInfusionFX;
import epicsquid.roots.ritual.RitualAnimalHarvest;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.entity.mobs.EntityAnadia;

import java.util.Iterator;
import java.util.List;

@Mixin(value = EntityRitualAnimalHarvest.class, remap = false)
public class MixinEntityRitualAnimalHarvest extends EntityRitualBase {
    @Shadow private RitualAnimalHarvest ritual;

    public MixinEntityRitualAnimalHarvest(World worldIn) {
        super(worldIn);
    }

    /**
     * @author SC
     * @reason
     */
    @Overwrite
    private boolean doHarvest() {
        List<BlockPos> waterSourceBlocks = Util.getBlocksWithinRadius(this.world, this.getPosition(), (float)this.ritual.radius_x, (float)this.ritual.radius_y, (float)this.ritual.radius_z, (p) -> {
            IBlockState state = this.world.getBlockState(p);
            return state.getMaterial() == Material.WATER && state.getPropertyKeys().contains(BlockLiquid.LEVEL) && (Integer)state.getValue(BlockLiquid.LEVEL) == 0;
        });

        if (!waterSourceBlocks.isEmpty() && this.rand.nextFloat() <= this.ritual.fish_chance) {
            // make sure there are only X many fish nearby
            AxisAlignedBB aabb = (new AxisAlignedBB(getPosition())).grow((float)this.ritual.radius_x);

            List<EntityAnadia> anadias = world.getEntitiesWithinAABB(EntityAnadia.class, aabb, (a) -> a.getDistanceSq((double)((float)getPosition().getX() + 0.5F), (double)((float)getPosition().getY() + 0.5F), (double)((float)getPosition().getZ() + 0.5F)) <= (double)((float)this.ritual.radius_x * (float)this.ritual.radius_x));

            if(anadias.size() >= this.ritual.fish_count) {
                return false;
            }

            BlockPos pos = (BlockPos)waterSourceBlocks.get(this.rand.nextInt(waterSourceBlocks.size()));

            if (!this.world.isRemote) {
                EntityAnadia anadia = new EntityAnadia(world);
                anadia.setLocationAndAngles((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5, rand.nextFloat() * 360.0F, 0.0F);
                world.spawnEntity(anadia);
            }

            PacketHandler.sendToAllTracking(new MessageRampantLifeInfusionFX((double)pos.getX(), (double)(pos.getY() + 1), (double)pos.getZ()), this);

            return true;
        }

        return false;
    }
}
