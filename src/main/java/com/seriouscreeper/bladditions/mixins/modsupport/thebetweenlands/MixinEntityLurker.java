package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import com.mrbysco.anotherliquidmilkmod.init.MilkRegistry;
import growthcraft.core.shared.fluids.FluidDictionary;
import growthcraft.milk.shared.init.GrowthcraftMilkFluids;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemHandlerHelper;
import org.spongepowered.asm.mixin.Mixin;
import thebetweenlands.common.entity.mobs.EntityLurker;
import thebetweenlands.common.item.misc.ItemMisc;
import thebetweenlands.common.registries.ItemRegistry;

@Mixin(value = EntityLurker.class)
public class MixinEntityLurker extends EntityCreature {
    public long milk_cooldown;
    private final int timeBetweenMilking = 6000;

    public MixinEntityLurker(World worldIn) {
        super(worldIn);
    }

    private boolean getCanMilk() {
        return getEntityWorld().getTotalWorldTime() >= getMilkCooldown();
    }

    public void setMilkCooldown(long cooldownTime) {
        milk_cooldown = cooldownTime;
    }

    public long getMilkCooldown() {
        return milk_cooldown;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        player.swingArm(hand);

        if (!getEntityWorld().isRemote && !stack.isEmpty() && stack.getItem() == ItemRegistry.BL_BUCKET) {
            if(!getCanMilk()) {
                player.sendStatusMessage(new TextComponentString("Can't milk lurker for another " + Math.round((getMilkCooldown() - getEntityWorld().getTotalWorldTime()) / 20f) + " seconds"), true);
                return false;
            }

            ItemStack copy = ItemHandlerHelper.copyStackWithSize(stack, 1);
            IFluidHandlerItem fluidItem = FluidUtil.getFluidHandler(copy);

            if (fluidItem != null) {
                int fill = fluidItem.fill(new FluidStack(MilkRegistry.liquid_milk, Fluid.BUCKET_VOLUME), true);

                if (fill == Fluid.BUCKET_VOLUME) {
                    player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
                    world.playSound(null, player.posX, player.posY + 0.5D, player.posZ, SoundEvents.ENTITY_COW_MILK, SoundCategory.BLOCKS, 1.0F, 1.0F);

                    if(!world.isRemote) {
                        copy = fluidItem.getContainer().copy();

                        if (!player.inventory.addItemStackToInventory(copy)) {
                            player.dropItem(copy, false);
                        }

                        stack.shrink(1);

                        setMilkCooldown(getEntityWorld().getTotalWorldTime() + timeBetweenMilking);
                    }

                    return false;
                }
            }

        }

        return super.processInteract(player, hand);
    }


    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if(compound.hasKey("milk_cooldown")) {
            setMilkCooldown(compound.getLong("milk_cooldown"));
        }
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setLong("milk_cooldown", getMilkCooldown());

        return super.writeToNBT(compound);
    }
}
