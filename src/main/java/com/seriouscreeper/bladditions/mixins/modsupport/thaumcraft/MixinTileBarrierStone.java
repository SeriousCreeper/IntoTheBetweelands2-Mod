package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.tiles.misc.TileBarrierStone;
import thebetweenlands.common.entity.EntityAnimalBL;

import java.util.Iterator;
import java.util.List;

@Mixin(value = TileBarrierStone.class, remap = false)
public class MixinTileBarrierStone extends TileEntity implements ITickable {
    @Shadow int count = 0;


    @Shadow
    public boolean gettingPower() {
        return this.world.getRedstonePowerFromNeighbors(this.pos) > 0;
    }


    /**
     * @author SC
     */
    @Overwrite
    public void update() {
        if (!this.world.isRemote) {
            if (this.count == 0) {
                this.count = this.world.rand.nextInt(100);
            }

            if (this.count % 5 == 0 && !this.gettingPower()) {
                List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, (new AxisAlignedBB((double)this.pos.getX(), (double)this.pos.getY(), (double)this.pos.getZ(), (double)(this.pos.getX() + 1), (double)(this.pos.getY() + 3), (double)(this.pos.getZ() + 1))).grow(0.1D, 0.1D, 0.1D));
                if (targets.size() > 0) {
                    for (EntityLivingBase e : targets) {
                        if (!e.onGround && !(e instanceof EntityMob) && !(e instanceof EntityPlayer)) {
                            e.addVelocity((double) (-MathHelper.sin((e.rotationYaw + 180.0F) * 3.1415927F / 180.0F) * 0.2F), -0.1D, (double) (MathHelper.cos((e.rotationYaw + 180.0F) * 3.1415927F / 180.0F) * 0.2F));
                        }
                    }
                }
            }

            if (++this.count % 100 == 0) {
                if (this.world.getBlockState(this.pos.up(1)) != BlocksTC.barrier.getDefaultState() && this.world.isAirBlock(this.pos.up(1))) {
                    this.world.setBlockState(this.pos.up(1), BlocksTC.barrier.getDefaultState(), 3);
                }

                if (this.world.getBlockState(this.pos.up(2)) != BlocksTC.barrier.getDefaultState() && this.world.isAirBlock(this.pos.up(2))) {
                    this.world.setBlockState(this.pos.up(2), BlocksTC.barrier.getDefaultState(), 3);
                }
            }
        }
    }
}
