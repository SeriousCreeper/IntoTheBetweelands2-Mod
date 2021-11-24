package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.api.storage.LocalRegion;
import thebetweenlands.api.storage.StorageUUID;
import thebetweenlands.common.block.structure.BlockSlabBetweenlands;
import thebetweenlands.common.block.terrain.BlockCragrock;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.world.gen.feature.structure.WorldGenCragrockTower;
import thebetweenlands.common.world.storage.BetweenlandsWorldStorage;
import thebetweenlands.common.world.storage.SharedLootPoolStorage;
import thebetweenlands.common.world.storage.location.LocationCragrockTower;
import thebetweenlands.common.world.storage.location.guard.ILocationGuard;

import java.util.Random;
import java.util.UUID;

@Mixin(value = WorldGenCragrockTower.class, remap = false)
public class MixinWorldGenCragrockTower {
    @Final
    @Shadow
    private static final ThreadLocal<Boolean> CASCADING_GEN_MUTEX = null;

    @Shadow
    private static IBlockState CRAGROCK;
    @Shadow
    private static IBlockState MOSSY_CRAGROCK_TOP;
    @Shadow
    private static IBlockState MOSSY_CRAGROCK_BOTTOM;
    @Shadow
    private static IBlockState CRAGROCK_BRICKS;
    @Shadow
    private static IBlockState SMOOTH_CRAGROCK_STAIRS;
    @Shadow
    private static IBlockState CRAGROCK_BRICK_SLAB;
    @Shadow
    private static IBlockState CRAGROCK_BRICK_SLAB_UPSIDEDOWN;
    @Shadow
    private static IBlockState SMOOTH_CRAGROCK_SLAB;
    @Shadow
    private static IBlockState SMOOTH_CRAGROCK_SLAB_UPSIDEDOWN;
    @Shadow
    private static IBlockState CRAGROCK_BRICK_STAIRS;
    @Shadow
    private static IBlockState CRAGROCK_PILLAR;
    @Shadow
    private static IBlockState SMOOTH_CRAGROCK;
    @Shadow
    private static IBlockState CHISELED_CRAGROCK;
    @Shadow
    private static IBlockState ROOT;
    @Shadow
    private static IBlockState SMOOTH_BETWEENSTONE_WALL;
    @Shadow
    private static IBlockState CRAGROCK_BRICK_WALL;
    @Shadow
    private static IBlockState SMOOTH_CRAGROCK_WALL;
    @Shadow
    private static IBlockState INACTIVE_GLOWING_SMOOTH_CRAGROCK;
    @Shadow
    private static IBlockState AIR;

    @Shadow
    private Random lootRng;
    @Shadow
    private SharedLootPoolStorage lootStorage;
    @Shadow
    private ILocationGuard guard;
    @Shadow
    private LocationCragrockTower towerLocation;
    @Shadow
    private BetweenlandsWorldStorage worldStorage;

    @Shadow
    private boolean tower(World world, Random random, int x, int y, int z) { return true; }


    /**
     * @author SC
     */
    @Overwrite
    public boolean generate(World worldIn, Random rand, BlockPos pos) {
        if ((Boolean)CASCADING_GEN_MUTEX.get()) {
            return false;
        } else {
            CASCADING_GEN_MUTEX.set(true);

            try {
                CRAGROCK = BlockRegistry.CRAGROCK.getDefaultState();
                MOSSY_CRAGROCK_TOP = BlockRegistry.CRAGROCK.getDefaultState().withProperty(BlockCragrock.VARIANT, BlockCragrock.EnumCragrockType.MOSSY_1);
                MOSSY_CRAGROCK_BOTTOM = BlockRegistry.CRAGROCK.getDefaultState().withProperty(BlockCragrock.VARIANT, BlockCragrock.EnumCragrockType.MOSSY_2);
                CRAGROCK_BRICKS = BlockRegistry.CRAGROCK_BRICKS.getDefaultState();
                SMOOTH_CRAGROCK_STAIRS = BlockRegistry.SMOOTH_CRAGROCK_STAIRS.getDefaultState();
                CRAGROCK_BRICK_SLAB = BlockRegistry.CRAGROCK_BRICK_SLAB.getDefaultState();
                CRAGROCK_BRICK_SLAB_UPSIDEDOWN = BlockRegistry.CRAGROCK_BRICK_SLAB.getDefaultState().withProperty(BlockSlabBetweenlands.HALF, BlockSlabBetweenlands.EnumBlockHalfBL.TOP);
                SMOOTH_CRAGROCK_SLAB = BlockRegistry.SMOOTH_CRAGROCK_SLAB.getDefaultState();
                SMOOTH_CRAGROCK_SLAB_UPSIDEDOWN = BlockRegistry.SMOOTH_CRAGROCK_SLAB.getDefaultState().withProperty(BlockSlabBetweenlands.HALF, BlockSlabBetweenlands.EnumBlockHalfBL.TOP);
                CRAGROCK_BRICK_STAIRS = BlockRegistry.CRAGROCK_BRICK_STAIRS.getDefaultState();
                CRAGROCK_PILLAR = BlockRegistry.CRAGROCK_PILLAR.getDefaultState();
                SMOOTH_CRAGROCK = BlockRegistry.SMOOTH_CRAGROCK.getDefaultState();
                ROOT = BlockRegistry.ROOT.getDefaultState();
                SMOOTH_BETWEENSTONE_WALL = BlockRegistry.SMOOTH_BETWEENSTONE_WALL.getDefaultState();
                CRAGROCK_BRICK_WALL = BlockRegistry.SMOOTH_CRAGROCK_WALL.getDefaultState();
                SMOOTH_CRAGROCK_WALL = BlockRegistry.SMOOTH_CRAGROCK_WALL.getDefaultState();
                INACTIVE_GLOWING_SMOOTH_CRAGROCK = BlockRegistry.INACTIVE_GLOWING_SMOOTH_CRAGROCK.getDefaultState();
                CHISELED_CRAGROCK = BlockRegistry.CRAGROCK_CHISELED.getDefaultState();

                for(AIR = Blocks.AIR.getDefaultState(); worldIn.isAirBlock(pos) && pos.getY() > 120; pos = pos.add(0, -1, 0)) {
                }

                this.worldStorage = BetweenlandsWorldStorage.forWorld(worldIn);
                this.towerLocation = new LocationCragrockTower(this.worldStorage, new StorageUUID(UUID.randomUUID()), LocalRegion.getFromBlockPos(pos));
                this.guard = this.towerLocation.getGuard();
                this.lootRng = new Random(worldIn.rand.nextLong());
                this.lootStorage = new SharedLootPoolStorage(this.worldStorage, new StorageUUID(UUID.randomUUID()), LocalRegion.getFromBlockPos(pos), rand.nextLong());
                this.worldStorage.getLocalStorageHandler().addLocalStorage(this.lootStorage);
                boolean var4 = this.tower(worldIn, rand, pos.getX(), pos.getY(), pos.getZ());
                return var4;
            } finally {
                CASCADING_GEN_MUTEX.set(false);
            }
        }
    }
}
