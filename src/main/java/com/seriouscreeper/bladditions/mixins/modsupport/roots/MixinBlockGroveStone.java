package com.seriouscreeper.bladditions.mixins.modsupport.roots;

import epicsquid.mysticallib.block.BlockBase;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.block.groves.BlockGroveStone;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.network.fx.MessageOvergrowthEffectFX;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.IChunkGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.world.gen.biome.decorator.DecorationHelper;
import thebetweenlands.common.world.gen.biome.decorator.DecoratorPositionProvider;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Mixin(value = BlockGroveStone.class)
public class MixinBlockGroveStone extends BlockBase {
    @Final
    @Shadow public static final PropertyBool VALID = PropertyBool.create("valid");

    public MixinBlockGroveStone(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name) {
        super(mat, type, hardness, name);
        this.setTickRandomly(true);
    }

    /**
     * @author SC
     */
    @Overwrite
    public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
        super.randomTick(world, pos, state, random);

        if (GeneralConfig.EnableGroveStoneEnvironment) {
            if (!world.isRemote) {
                if ((Boolean)state.getValue(VALID)) {
                    if (random.nextInt(GeneralConfig.GroveStoneChance) == 1) {
                        DecoratorPositionProvider provider = new DecoratorPositionProvider();
                        provider.init(world, world.getBiome(pos), (IChunkGenerator)null, world.rand, pos.getX(), pos.getY(), pos.getZ());
                        provider.setOffsetXZ(-4, 4);
                        provider.setOffsetY(-2, 2);

                        DecorationHelper.generateSwampDoubleTallgrass(provider);
                        DecorationHelper.generateTallCattail(provider);
                        DecorationHelper.generateSwampTallgrassCluster(provider);
                        if (world.rand.nextInt(5) == 0) {
                            DecorationHelper.generateCattailCluster(provider);
                        }

                        if (world.rand.nextInt(3) == 0) {
                            DecorationHelper.generateShootsCluster(provider);
                        }
                    }
                }
            }
        }
    }
}
