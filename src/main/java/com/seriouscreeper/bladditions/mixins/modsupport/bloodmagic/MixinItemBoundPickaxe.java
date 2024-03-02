package com.seriouscreeper.bladditions.mixins.modsupport.bloodmagic;

import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.iface.IActivatable;
import WayofTime.bloodmagic.item.ItemBoundPickaxe;
import WayofTime.bloodmagic.item.ItemBoundTool;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import com.google.common.collect.Multimap;
import com.seriouscreeper.bladditions.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thebetweenlands.api.item.CorrosionHelper;
import thebetweenlands.api.item.ICorrodible;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

@Mixin(value = ItemBoundPickaxe.class, remap = false)
public class MixinItemBoundPickaxe extends ItemBoundTool implements IActivatable, ICorrodible {
    public MixinItemBoundPickaxe(String name, float damage, Set<Block> effectiveBlocks) {
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
    @Overwrite
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot, stack);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", CorrosionHelper.getModifier(stack) * (this.getActivated(stack) ? 3.0 : 2.0), 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", -2.5, 0));
        }

        return multimap;
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
            BlockPos playerPos = player.getPosition();

            for(int i = -range; i <= range; ++i) {
                for(int j = 0; j <= 2 * range; ++j) {
                    for(int k = -range; k <= range; ++k) {
                        BlockPos blockPos = playerPos.add(i, j, k);
                        IBlockState blockState = world.getBlockState(blockPos);
                        if (!CommonProxy.IsWithinLocation(world, blockPos) && !world.isAirBlock(blockPos) && (blockState.getMaterial() == Material.ROCK || EFFECTIVE_ON.contains(blockState.getBlock()))) {
                            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, blockPos, blockState, player);
                            if (!MinecraftForge.EVENT_BUS.post(event) && event.getResult() != Event.Result.DENY) {
                                this.sharedHarvest(stack, world, player, blockPos, blockState, silkTouch, fortuneLvl);
                            }
                        }
                    }
                }
            }

            NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, SoulTicket.item(stack, world, player, (int)((double)(charge * charge * charge) / 2.7)));
            world.createExplosion(player, (double)playerPos.getX(), (double)playerPos.getY(), (double)playerPos.getZ(), 0.5F, false);
        }
    }


    @Final
    @Shadow
    private static Set<Block> EFFECTIVE_ON;
}
