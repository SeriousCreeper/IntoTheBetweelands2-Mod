package com.seriouscreeper.bladditions.mixins.modsupport.roots;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.capability.runic_shears.RunicShearsCapability;
import epicsquid.roots.capability.runic_shears.RunicShearsCapabilityProvider;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.item.dispenser.DispenseRunicShears;
import epicsquid.roots.network.fx.MessageRunicShearsFX;
import epicsquid.roots.recipe.RunicShearEntityRecipe;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thebetweenlands.common.entity.mobs.EntitySwarm;

import java.util.List;

@Mixin(value = DispenseRunicShears.class, remap = false)
public class MixinDispenseRunicShears {
    @Inject(method = "dispense", at = @At("HEAD"), cancellable = true)
    private void injectDispense(IBlockSource source, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        World world = source.getWorld();
        EnumFacing facing = (EnumFacing)source.getBlockState().getValue(BlockDispenser.FACING);
        BlockPos pos = source.getBlockPos().offset(facing);
        List closeEntities;
        EntityLivingBase entity;
        closeEntities = world.getEntitiesInAABBexcluding((Entity)null, new AxisAlignedBB(pos), (ex) -> {
            return !(ex instanceof EntityPlayer) && ex instanceof EntityLivingBase;
        });
        if (!closeEntities.isEmpty()) {
            entity = null;
            RunicShearEntityRecipe recipe = null;
            RunicShearsCapability cap = null;

            while(!closeEntities.isEmpty()) {
                entity = (EntityLivingBase)closeEntities.remove(world.rand.nextInt(closeEntities.size()));
                recipe = ModRecipes.getRunicShearRecipe((EntityLivingBase)entity);
                if (recipe != null) {
                    cap = (RunicShearsCapability)entity.getCapability(RunicShearsCapabilityProvider.RUNIC_SHEARS_CAPABILITY, (EnumFacing)null);
                    if (cap != null) {
                        if (cap.canHarvest()) {
                            break;
                        }

                        recipe = null;
                        cap = null;
                    }
                }
            }

            if (entity != null && recipe != null && cap != null) {
                cap.setCooldown((long)recipe.getCooldown());
                EntityItem ent = entity.entityDropItem(recipe.getDrop((EntityLivingBase)entity).copy(), 1.0F);
                ent.motionY += (double)(Util.rand.nextFloat() * 0.05F);
                ent.motionX += (double)((Util.rand.nextFloat() - Util.rand.nextFloat()) * 0.1F);
                ent.motionZ += (double)((Util.rand.nextFloat() - Util.rand.nextFloat()) * 0.1F);
                if (stack.attemptDamageItem(1, world.rand, FakePlayerFactory.getMinecraft((WorldServer)world))) {
                    stack.setCount(0);
                }

                if(entity instanceof EntitySwarm) {
                    entity.setHealth(entity.getHealth() - 5);
                }

                IMessage packet = new MessageRunicShearsFX(entity);
                PacketHandler.sendToAllTracking(packet, entity);

                source.getWorld().playSound((EntityPlayer)null, source.getBlockPos(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);

                cir.setReturnValue(stack);
            }
        }
    }
}
