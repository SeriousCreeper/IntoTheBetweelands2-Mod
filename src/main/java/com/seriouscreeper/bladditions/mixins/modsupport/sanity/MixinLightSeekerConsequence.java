package com.seriouscreeper.bladditions.mixins.modsupport.sanity;

import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCampfire;
import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tiffit.sanity.SanityCapability;
import net.tiffit.sanity.consequences.LightSeekerConsequence;
import net.tiffit.sanity.entity.LightSeekerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thaumcraft.common.blocks.world.ore.BlockCrystal;
import thebetweenlands.common.block.misc.BlockOctine;
import thebetweenlands.common.block.terrain.BlockLifeCrystalStalactite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Mixin(value = LightSeekerConsequence.class, remap = false)
public class MixinLightSeekerConsequence {
    /**
     * @author SC
     */
    @Overwrite
    public void run(EntityPlayerMP player, SanityCapability.SanityLevel level) {
        BlockPos pos = this.findLight(player);
        if (pos != null) {
            BlockPos spawn = pos.add(this.getOffset());
            LightSeekerEntity ent = new LightSeekerEntity(player.world);
            ent.setTarget(pos);
            ent.setPosition(spawn.getX() + .5, spawn.getY() + .5, spawn.getZ() + .5);
            player.world.spawnEntity(ent);
        }
    }


    /**
     * @author SC
     */
    @Overwrite
    private BlockPos findLight(EntityPlayerMP p) {
        int searchRadius = 3;
        World w = p.getEntityWorld();
        List<BlockPos> allowed = new ArrayList();

        for(int x = -searchRadius; x <= searchRadius; ++x) {
            for(int y = -2; y <= 2; ++y) {
                for(int z = -searchRadius; z <= searchRadius; ++z) {
                    BlockPos pos = (new BlockPos(x, y, z)).add(p.getPosition());
                    IBlockState state = w.getBlockState(pos);
                    Block block = state.getBlock();
                    TileEntity te = w.getTileEntity(pos);
                    if ((te == null || te instanceof TileCampfire) && !(block instanceof BlockLiquid) && (double)state.getLightValue(w, pos) > 0.2D) {
                        List<String> blackList = Arrays.asList(ConfigBLAdditions.configSanity.sanity_lightseeker_blacklist);

                        if(blackList.contains(block.getRegistryName().toString())) {
                            continue;
                        }

                        allowed.add(pos);
                    }
                }
            }
        }

        if (!allowed.isEmpty()) {
            Random rand = new Random();
            return (BlockPos)allowed.get(rand.nextInt(allowed.size()));
        } else {
            return null;
        }
    }

    @Shadow
    private BlockPos getOffset() { return null; }
}
