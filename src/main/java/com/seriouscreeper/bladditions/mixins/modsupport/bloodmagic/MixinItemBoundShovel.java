package com.seriouscreeper.bladditions.mixins.modsupport.bloodmagic;

import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.iface.IActivatable;
import WayofTime.bloodmagic.item.ItemBoundShovel;
import WayofTime.bloodmagic.item.ItemBoundTool;
import WayofTime.bloodmagic.util.ItemStackWrapper;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.*;
import thebetweenlands.api.item.CorrosionHelper;
import thebetweenlands.api.item.IAnimatorRepairable;
import thebetweenlands.api.item.ICorrodible;
import thebetweenlands.common.entity.mobs.EntityTinySludgeWorm;
import thebetweenlands.common.item.BLMaterialRegistry;
import thebetweenlands.common.registries.AdvancementCriterionRegistry;
import thebetweenlands.common.registries.BlockRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

@Mixin(value = ItemBoundShovel.class, remap = false)
public class MixinItemBoundShovel extends ItemBoundTool implements IActivatable, ICorrodible, IAnimatorRepairable {
    @Final
    @Shadow
    private static Set<Block> EFFECTIVE_ON;

    public MixinItemBoundShovel(String name, float damage, Set<Block> effectiveBlocks) {
        super(name, damage, effectiveBlocks);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        CorrosionHelper.updateCorrosion(stack, world, entity, itemSlot, isSelected);
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

    /**
     * @author
     * @reason
     */
    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);

        if(!this.getActivated(player.getHeldItem(hand))) {
            return EnumActionResult.PASS;
        }

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


    /**
     * @author
     * @reason
     */
    @Overwrite
    protected void onBoundRelease(ItemStack stack, World world, EntityPlayer player, int charge) {
        if (!world.isRemote) {
            int fortuneLvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
            boolean silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
            int range = Math.round(CorrosionHelper.getModifier(stack) * (charge / 15f));
            HashMultiset<ItemStackWrapper> drops = HashMultiset.create();
            BlockPos playerPos = player.getPosition();

            for(int i = -range; i <= range; ++i) {
                for(int j = 0; j <= 2 * range; ++j) {
                    for(int k = -range; k <= range; ++k) {
                        BlockPos blockPos = playerPos.add(i, j, k);
                        IBlockState blockState = world.getBlockState(blockPos);
                        if (!world.isAirBlock(blockPos)) {
                            Material material = blockState.getMaterial();
                            if (material == Material.GROUND || material == Material.CLAY || material == Material.GRASS || EFFECTIVE_ON.contains(blockState.getBlock())) {
                                BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, blockPos, blockState, player);
                                if (!MinecraftForge.EVENT_BUS.post(event) && event.getResult() != Event.Result.DENY) {
                                    this.sharedHarvest(stack, world, player, blockPos, blockState, silkTouch, fortuneLvl);
                                }
                            }
                        }
                    }
                }
            }

            NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, SoulTicket.item(stack, world, player, (int)((double)(charge * charge * charge) / 2.7)));
            world.createExplosion(player, (double)playerPos.getX(), (double)playerPos.getY(), (double)playerPos.getZ(), 0.5F, false);
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        return ArrayListMultimap.create();
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(slot);
        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", CorrosionHelper.getModifier(stack) * 3.5, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", -2.5, 0));
        }

        return multimap;
    }

    public int getMinRepairFuelCost(ItemStack stack) {
        return BLMaterialRegistry.getMinRepairFuelCost(BLMaterialRegistry.TOOL_VALONITE);
    }

    public int getFullRepairFuelCost(ItemStack stack) {
        return BLMaterialRegistry.getFullRepairFuelCost(BLMaterialRegistry.TOOL_VALONITE);
    }

    public int getMinRepairLifeCost(ItemStack stack) {
        return BLMaterialRegistry.getMinRepairLifeCost(BLMaterialRegistry.TOOL_VALONITE);
    }

    public int getFullRepairLifeCost(ItemStack stack) {
        return BLMaterialRegistry.getFullRepairLifeCost(BLMaterialRegistry.TOOL_VALONITE);
    }
}
