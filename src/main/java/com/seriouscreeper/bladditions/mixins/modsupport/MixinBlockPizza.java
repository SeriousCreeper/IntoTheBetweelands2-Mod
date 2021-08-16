package com.seriouscreeper.bladditions.mixins.modsupport;

import com.tiviacz.pizzacraft.blocks.BlockPizza;
import com.tiviacz.pizzacraft.handlers.ConfigHandler;
import com.tiviacz.pizzacraft.init.ModItems;
import growthcraft.core.shared.item.ItemTest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thebetweenlands.api.capability.IDecayCapability;
import thebetweenlands.api.item.IDecayFood;
import thebetweenlands.api.item.IFoodSicknessItem;
import thebetweenlands.common.registries.CapabilityRegistry;

import java.util.Objects;

import static com.tiviacz.pizzacraft.blocks.BlockPizza.BITES;
import static net.minecraft.block.Block.spawnAsEntity;

@Mixin(value = BlockPizza.class, remap = false)
public class MixinBlockPizza implements IDecayFood, IFoodSicknessItem {
    @Shadow
    private int foodstats;
    @Shadow
    private float saturation;
    @Shadow
    private Item pizzaslice;

    /**
     * @author SC
     */
    @Overwrite
    private boolean eatCake(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        ItemStack helditem = player.getHeldItem(player.getActiveHand());
        int i = (Integer)state.getValue(BITES);
        if (player.canEat(false) && helditem.getItem() != ModItems.PEEL && !ItemTest.itemMatchesOre(helditem, "toolKnife") && !player.isSneaking()) {
            player.getFoodStats().addStats(this.foodstats, this.saturation);

            IDecayCapability cap = player.getCapability(CapabilityRegistry.CAPABILITY_DECAY, null);
            if(cap != null && cap.getDecayStats().getDecayLevel() > 0) {
                int decayValue = 0;

                switch(Objects.requireNonNull(worldIn.getBlockState(pos).getBlock().getRegistryName()).toString()) {
                    case "pizzacraft:pizza_5":
                        decayValue = 4;
                        break;

                    case "pizzacraft:pizza_9":
                        decayValue = 10;
                        break;
                }

                cap.getDecayStats().addStats(-decayValue, 0);
            }

            if (i < 5) {
                worldIn.setBlockState(pos, state.withProperty(BITES, i + 1), 3);
            } else {
                worldIn.setBlockToAir(pos);
            }

            return true;
        } else {
            return false;
        }
    }


    @Inject(method = "Lcom/tiviacz/pizzacraft/blocks/BlockPizza;func_180639_a(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;Lnet/minecraft/util/EnumFacing;FFF)Z", at = @At("HEAD"), cancellable = true)
    public void func_180639_a(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> cir) {
        ItemStack helditem = playerIn.getHeldItem(hand);
        int i = (Integer)state.getValue(BITES);
        if (worldIn.isRemote) {
            cir.setReturnValue(false);
            return;
        } else {
            if (helditem.getItem() == ModItems.PEEL && i == 0 && (!playerIn.capabilities.isCreativeMode || playerIn.capabilities.isCreativeMode)) {
                spawnAsEntity(worldIn, pos, new ItemStack(worldIn.getBlockState(pos).getBlock()));
                worldIn.setBlockToAir(pos);
                playerIn.getHeldItem(hand).damageItem(1, playerIn);
            }

            if (ConfigHandler.isKnifeNeeded) {
                if (ItemTest.itemMatchesOre(helditem, "toolKnife")) {
                    spawnAsEntity(worldIn, pos, new ItemStack(this.pizzaslice));
                    playerIn.getHeldItem(hand).damageItem(1, playerIn);
                    if (i < 5) {
                        worldIn.setBlockState(pos, state.withProperty(BITES, i + 1), 3);
                    } else {
                        worldIn.setBlockToAir(pos);
                    }
                }
            } else if (playerIn.isSneaking()) {
                spawnAsEntity(worldIn, pos, new ItemStack(this.pizzaslice));
                if (i < 5) {
                    worldIn.setBlockState(pos, state.withProperty(BITES, i + 1), 3);
                } else {
                    worldIn.setBlockToAir(pos);
                }
            }

            cir.setReturnValue(this.eatCake(worldIn, pos, state, playerIn));
            return;
        }
    }

    @Override
    public int getDecayHealAmount(ItemStack itemStack) {
        return 1;
    }
}
