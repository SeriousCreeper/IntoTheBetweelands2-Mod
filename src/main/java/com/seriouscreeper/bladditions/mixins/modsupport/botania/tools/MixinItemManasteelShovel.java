package com.seriouscreeper.bladditions.mixins.modsupport.botania.tools;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thebetweenlands.api.item.CorrosionHelper;
import thebetweenlands.api.item.ICorrodible;
import thebetweenlands.common.entity.mobs.EntityTinySludgeWorm;
import thebetweenlands.common.registries.AdvancementCriterionRegistry;
import thebetweenlands.common.registries.BlockRegistry;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelShovel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mixin(value = ItemManasteelShovel.class, remap = false)
public class MixinItemManasteelShovel extends ItemSpade implements ICorrodible {
    public MixinItemManasteelShovel(ToolMaterial material) {
        super(material);
    }

    /**
     * @author
     * @reason
     */
    @Nonnull
    @Overwrite
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);

        if (facing == EnumFacing.UP) {
            boolean dug = false;
            IBlockState blockState = world.getBlockState(pos);
            if (blockState.getBlock() == BlockRegistry.COARSE_SWAMP_DIRT) {
                world.setBlockState(pos, BlockRegistry.DUG_SWAMP_DIRT.getDefaultState());
                dug = true;
            }

            if (blockState.getBlock() == BlockRegistry.SWAMP_DIRT) {
                world.setBlockState(pos, BlockRegistry.DUG_SWAMP_DIRT.getDefaultState());
                dug = true;
                this.checkForWormSpawn(world, pos, player);
            }

            if (blockState.getBlock() == BlockRegistry.SWAMP_GRASS) {
                world.setBlockState(pos, BlockRegistry.DUG_SWAMP_GRASS.getDefaultState());
                dug = true;
                this.checkForWormSpawn(world, pos, player);
            }

            if (blockState.getBlock() == BlockRegistry.PURIFIED_SWAMP_DIRT) {
                world.setBlockState(pos, BlockRegistry.DUG_PURIFIED_SWAMP_DIRT.getDefaultState());
                dug = true;
            }

            if (dug) {
                if (world.isRemote) {
                    for(int i = 0; i < 80; ++i) {
                        world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, (double)((float)pos.getX() + 0.5F), (double)(pos.getY() + 1), (double)((float)pos.getZ() + 0.5F), (double)((world.rand.nextFloat() - 0.5F) * 0.1F), (double)(world.rand.nextFloat() * 0.3F), (double)((world.rand.nextFloat() - 0.5F) * 0.1F), new int[]{Block.getStateId(blockState)});
                    }
                }

                SoundType sound = blockState.getBlock().getSoundType(blockState, world, pos, player);

                for(int i = 0; i < 3; ++i) {
                    world.playSound((EntityPlayer)null, (double)((float)pos.getX() + hitX), (double)((float)pos.getY() + hitY), (double)((float)pos.getZ() + hitZ), sound.getBreakSound(), SoundCategory.PLAYERS, 1.0F, 0.5F + world.rand.nextFloat() * 0.5F);
                }

                player.getHeldItem(hand).damageItem(1, player);
                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.PASS;
    }


    @Unique
    public void checkForWormSpawn(World world, BlockPos pos, EntityPlayer player) {
        if (!world.isRemote && world.getDifficulty() != EnumDifficulty.PEACEFUL && world.rand.nextInt(12) == 0) {
            EntityTinySludgeWorm entity = new EntityTinySludgeWorm(world);
            entity.setLocationAndAngles((double)pos.getX() + 0.5D, (double)pos.getY() + 1.0D, (double)pos.getZ() + 0.5D, 0.0F, 0.0F);
            world.spawnEntity(entity);
            if (player instanceof EntityPlayerMP) {
                AdvancementCriterionRegistry.WORM_FROM_DIRT.trigger((EntityPlayerMP)player);
            }
        }
    }


    @Inject(method = "onUpdate", at = @At("HEAD"))
    public void injectOnUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected, CallbackInfo ci) {
        CorrosionHelper.updateCorrosion(stack, worldIn, entityIn, itemSlot, isSelected);
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

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        return CorrosionHelper.getAttributeModifiers(super.getAttributeModifiers(slot, stack), slot, stack, ATTACK_DAMAGE_MODIFIER, this.attackDamage);
    }
}
