package com.seriouscreeper.bladditions.client;

import com.seriouscreeper.bladditions.tiles.TileCrucibleSwamp;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.*;

import java.awt.*;

public class BLAdditionFX {
    public static BLAdditionFX INSTANCE = new BLAdditionFX();
    static int q = 0;

    public BLAdditionFX() {
    }

    public World getWorld() {
        return FMLClientHandler.instance().getClient().world;
    }

    public void crucibleBoil(BlockPos pos, TileCrucibleSwamp tile, int j) {
        for(int a = 0; a < 2; ++a) {
            FXGeneric fb = new FXGeneric(this.getWorld(), (double)((float)pos.getX() + 0.2F + this.getWorld().rand.nextFloat() * 0.6F), (double)((float)pos.getY() + 0.1F + tile.getFluidHeight()), (double)((float)pos.getZ() + 0.2F + this.getWorld().rand.nextFloat() * 0.6F), 0.0D, 0.002D, 0.0D);
            fb.setMaxAge((int)(7.0D + 8.0D / (Math.random() * 0.8D + 0.2D)));
            fb.setScale(new float[]{this.getWorld().rand.nextFloat() * 0.3F + 0.2F});
            if (tile.aspects.size() == 0) {
                fb.setRBGColorF(1.0F, 1.0F, 1.0F);
            } else {
                Color color = new Color(tile.aspects.getAspects()[this.getWorld().rand.nextInt(tile.aspects.getAspects().length)].getColor());
                fb.setRBGColorF((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F);
            }

            fb.setRandomMovementScale(0.001F, 0.001F, 0.001F);
            fb.setGravity(-0.025F * (float)j);
            fb.setParticle(64);
            fb.setFinalFrames(new int[]{65, 66});
            ParticleEngine.addEffect(this.getWorld(), fb);
        }
    }


    public void jarSplashFx(double x, double y, double z) {
        FXGeneric fb = new FXGeneric(this.getWorld(), x + this.getWorld().rand.nextGaussian() * 0.07500000298023224D, y, z + this.getWorld().rand.nextGaussian() * 0.07500000298023224D, this.getWorld().rand.nextGaussian() * 0.014999999664723873D, (double)(0.075F + this.getWorld().rand.nextFloat() * 0.05F), this.getWorld().rand.nextGaussian() * 0.014999999664723873D);
        fb.setMaxAge(20 + this.getWorld().rand.nextInt(10));
        Color c = new Color(11249008);
        fb.setRBGColorF((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F);
        fb.setAlphaF(0.5F);
        fb.setLoop(false);
        fb.setParticles(73, 1, 1);
        fb.setScale(new float[]{0.4F + this.getWorld().rand.nextFloat() * 0.3F, 0.0F});
        fb.setLayer(1);
        fb.setGravity(0.3F);
        fb.setRotationSpeed(0.0F);
        ParticleEngine.addEffect(this.getWorld(), fb);
    }
}
