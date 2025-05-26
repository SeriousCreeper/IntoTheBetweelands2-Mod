package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.world.taint.BlockFluxGoo;
import thaumcraft.common.entities.monster.EntityThaumicSlime;
import thaumcraft.common.lib.SoundsTC;

import java.util.Random;

@Mixin(value = BlockFluxGoo.class, remap = false)
public class MixinBlockFluxGoo extends BlockFluidFinite {
    public MixinBlockFluxGoo(Fluid fluid, Material material, MapColor mapColor) {
        super(fluid, material, mapColor);
    }

    /**
     * @author SC
     * @reason reduce amount of slimes and flux this produces
     */
    @Overwrite
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        int meta = (Integer)state.getValue(LEVEL);
        EntityThaumicSlime slime;
        if (meta >= 2 && meta < 6 && world.isAirBlock(pos.up()) && rand.nextInt(ConfigBLAdditions.configThaumcraft.taintSlimeSpawnChance) == 0) {
            world.setBlockToAir(pos);
            slime = new EntityThaumicSlime(world);
            slime.setLocationAndAngles((double)((float)pos.getX() + 0.5F), (double)pos.getY(), (double)((float)pos.getZ() + 0.5F), 0.0F, 0.0F);
            slime.setSlimeSize(1, true);
            world.spawnEntity(slime);
            slime.playSound(SoundsTC.gore, 1.0F, 1.0F);
        } else if (meta >= 6 && world.isAirBlock(pos.up()) && rand.nextInt(ConfigBLAdditions.configThaumcraft.taintSlimeSpawnChance) == 0) {
            world.setBlockToAir(pos);
            slime = new EntityThaumicSlime(world);
            slime.setLocationAndAngles((double)((float)pos.getX() + 0.5F), (double)pos.getY(), (double)((float)pos.getZ() + 0.5F), 0.0F, 0.0F);
            if(rand.nextBoolean()) {
                slime.setSlimeSize(2, true);
            } else {
                slime.setSlimeSize(1, true);
            }
            world.spawnEntity(slime);
            slime.playSound(SoundsTC.gore, 1.0F, 1.0F);
        } else if (rand.nextInt(4) == 0) {
            if (meta == 0) {
                if (rand.nextBoolean()) {
                    AuraHelper.polluteAura(world, pos, 1.0F, true);
                    world.setBlockToAir(pos);
                } else {
                    world.setBlockState(pos, BlocksTC.taintFibre.getDefaultState());
                }
            } else {
                world.setBlockState(pos, state.withProperty(LEVEL, meta - 1), 2);

                if(rand.nextInt(10) == 0) {
                    AuraHelper.polluteAura(world, pos, 1.0F, true);
                }
            }
        } else {
            super.updateTick(world, pos, state, rand);
        }
    }
}
