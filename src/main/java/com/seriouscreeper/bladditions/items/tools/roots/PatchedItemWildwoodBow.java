package com.seriouscreeper.bladditions.items.tools.roots;

import com.google.common.collect.Multimap;
import epicsquid.roots.item.wildwood.ItemWildwoodBow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.api.item.CorrosionHelper;
import thebetweenlands.api.item.ICorrodible;
import thebetweenlands.common.registries.ItemRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class PatchedItemWildwoodBow extends ItemWildwoodBow implements ICorrodible {
    public PatchedItemWildwoodBow(String name) {
        super(name);
        CorrosionHelper.addCorrosionPropertyOverrides(this);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            boolean infiniteBow = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack arrow = this.findAmmo(player);

            int usedTicks = this.getMaxItemUseDuration(stack) - timeLeft;
            usedTicks = ForgeEventFactory.onArrowLoose(stack, world, (EntityPlayer) entityLiving, usedTicks, !arrow.isEmpty() || infiniteBow);

            if (usedTicks < 0) {
                return;
            }

            if (!arrow.isEmpty() || infiniteBow) {
                if (arrow.isEmpty()) {
                    arrow = new ItemStack(ItemRegistry.ANGLER_TOOTH_ARROW);
                }

                float strength = getArrowVelocity(usedTicks);

                strength *= CorrosionHelper.getModifier(stack);

                if (strength >= 0.1F) {

                    boolean infiniteArrows = player.capabilities.isCreativeMode || (arrow.getItem() instanceof ItemArrow && ((ItemArrow) arrow.getItem()).isInfinite(arrow, stack, player));

                    if (!world.isRemote) {
                        ItemArrow itemArrow = (ItemArrow)arrow.getItem();
                        EntityArrow entityArrow = itemArrow.createArrow(world, arrow, player);
                        entityArrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, strength * 3.0F, 1.0F);

                        if (strength == 1.0F) {
                            entityArrow.setIsCritical(true);
                        }

                        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

                        if (j > 0) {
                            entityArrow.setDamage(entityArrow.getDamage() + (double) j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

                        if (k > 0) {
                            entityArrow.setKnockbackStrength(k);
                        }

                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
                            entityArrow.setFire(100);
                        }

                        stack.damageItem(1, player);

                        if (infiniteArrows || player.capabilities.isCreativeMode && (arrow.getItem() == Items.SPECTRAL_ARROW || arrow.getItem() == Items.TIPPED_ARROW)) {
                            entityArrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                        }

                        player.world.spawnEntity(entityArrow);
                    }

                    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + strength * 0.5F);

                    if (!infiniteArrows) {
                        arrow.shrink(1);

                        if (arrow.getCount() == 0) {
                            player.inventory.deleteStack(arrow);
                        }
                    }

                    player.addStat(StatList.getObjectUseStats(this));
                }
            }
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        CorrosionHelper.updateCorrosion(stack, worldIn, entityIn, itemSlot, isSelected);
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        CorrosionHelper.addCorrosionTooltips(stack, tooltip, flagIn.isAdvanced());
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return CorrosionHelper.getDestroySpeed(super.getDestroySpeed(stack, state), stack, state);
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return CorrosionHelper.shouldCauseBlockBreakReset(oldStack, newStack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return CorrosionHelper.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }
}
