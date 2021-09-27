package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.client.render.particle.BLParticles;
import thebetweenlands.client.render.particle.ParticleFactory;
import thebetweenlands.common.entity.mobs.EntityDreadfulMummy;
import thebetweenlands.common.entity.mobs.EntitySwampHag;
import thebetweenlands.common.item.misc.ItemMummyBait;
import thebetweenlands.common.registries.BiomeRegistry;

@Mixin(value = ItemMummyBait.class, remap = false)
public class MixinItemMummyBait {
    /**
     * @author SC
     */
    @Overwrite
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        if (entityItem.ticksExisted % 10 == 0 && entityItem.onGround) {
            int bx = MathHelper.floor(entityItem.posX);
            int by = MathHelper.floor(entityItem.posY);
            int bz = MathHelper.floor(entityItem.posZ);
            BlockPos.PooledMutableBlockPos pos = BlockPos.PooledMutableBlockPos.retain();
            Biome biome = entityItem.world.getBiome(pos.setPos(bx, by, bz));
            if (biome == BiomeRegistry.SLUDGE_PLAINS || biome == BiomeRegistry.MARSH_0 || biome == BiomeRegistry.MARSH_1) {
                boolean canSpawn = true;
                int xo = -3;

                label92:
                while(true) {
                    int zo;
                    if (xo > -1) {
                        if (!canSpawn) {
                            break;
                        }

                        if (!entityItem.world.isRemote) {
                            EntityDreadfulMummy boss = new EntityDreadfulMummy(entityItem.world);
                            boss.setLocationAndAngles(entityItem.posX, entityItem.posY, entityItem.posZ, 0.0F, 0.0F);

                            if (boss.getCanSpawnHere()) {
                                if(entityItem.getItem().getDisplayName().equals("Pete Mummy")) {
                                    boss.setCustomNameTag("pete mummy");
                                }

                                entityItem.world.spawnEntity(boss);
                                entityItem.setDead();

                                if(entityItem.getItem().getDisplayName().equals("Pete Mummy")) {
                                    EntitySwampHag hag = new EntitySwampHag(entityItem.world);
                                    hag.setLocationAndAngles(entityItem.posX, entityItem.posY, entityItem.posZ, entityItem.rotationYaw, 0.0F);
                                    hag.onInitialSpawn(entityItem.world.getDifficultyForLocation(entityItem.getPosition()), (IEntityLivingData)null);
                                    entityItem.world.spawnEntity(hag);
                                    hag.startRiding(boss);
                                }

                                pos.release();
                                return true;
                            }
                            break;
                        }

                        xo = -1;

                        while(true) {
                            if (xo > 1 || !canSpawn) {
                                break label92;
                            }

                            for(zo = -1; zo <= 1 && canSpawn; ++zo) {
                                IBlockState state = entityItem.world.getBlockState(pos.setPos(bx + xo, by - 1, bz + zo));
                                int stateId = Block.getStateId(state);
                                int i = 0;

                                for(int amount = 12 + entityItem.world.rand.nextInt(20); i < amount; ++i) {
                                    double ox = entityItem.world.rand.nextDouble();
                                    double oy = entityItem.world.rand.nextDouble() * 3.0D;
                                    double oz = entityItem.world.rand.nextDouble();
                                    double motionX = entityItem.world.rand.nextDouble() * 0.2D - 0.1D;
                                    double motionY = entityItem.world.rand.nextDouble() * 0.1D + 0.1D;
                                    double motionZ = entityItem.world.rand.nextDouble() * 0.2D - 0.1D;
                                    entityItem.world.spawnParticle(EnumParticleTypes.BLOCK_DUST, (double)(bx + xo) + ox, (double)by, (double)(bz + zo) + oz, motionX, motionY, motionZ, new int[]{stateId});
                                    BLParticles.SMOKE.spawn(entityItem.world, (double)(bx + xo) + ox, (double)by + oy, (double)(bz + zo) + oz, ParticleFactory.ParticleArgs.get().withColor(-1.0F, 57005.0F, 49374.0F, 1.0F).withMotion(0.0D, 0.25D, 0.0D).withScale(1.0F));
                                }
                            }

                            ++xo;
                        }
                    }

                    for(zo = -1; zo <= 1 && canSpawn; ++zo) {
                        for(zo = -1; zo <= 1 && canSpawn; ++zo) {
                            IBlockState state = entityItem.world.getBlockState(pos.setPos(bx + zo, by + xo, bz + zo));
                            if (!state.isNormalCube() && !state.isSideSolid(entityItem.world, pos, EnumFacing.UP)) {
                                canSpawn = false;
                            }
                        }
                    }

                    ++xo;
                }
            }

            pos.release();
        }

        return false;
    }
}
