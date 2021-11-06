package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import growthcraft.core.shared.fluids.FluidDictionary;
import growthcraft.milk.shared.init.GrowthcraftMilkFluids;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
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
        if (!getEntityWorld().isRemote && !stack.isEmpty() && stack.getItem() == ItemRegistry.BL_BUCKET && getCanMilk()) {
            stack.shrink(1);

            if (stack.getCount() <= 0)
                player.setHeldItem(hand, ItemStack.EMPTY);

            ItemStack filledBucket = FluidUtil.getFilledBucket(GrowthcraftMilkFluids.milk.asFluidStack(1000));

            if (stack.isEmpty()) {
                player.setHeldItem(hand, filledBucket);
            } else if (!player.inventory.addItemStackToInventory(filledBucket)) {
                player.dropItem(filledBucket, false);
            }

            setMilkCooldown(getEntityWorld().getTotalWorldTime() + 24);

            player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
            world.playSound(null, player.posX, player.posY + 0.5D, player.posZ, SoundEvents.ENTITY_COW_MILK, SoundCategory.BLOCKS, 1.0F, 1.0F);

            return true;
        }
        return super.processInteract(player, hand);
    }
}
