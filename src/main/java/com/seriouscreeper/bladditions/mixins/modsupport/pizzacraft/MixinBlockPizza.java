package com.seriouscreeper.bladditions.mixins.modsupport.pizzacraft;

import com.tiviacz.pizzacraft.blocks.BlockPizza;
import com.tiviacz.pizzacraft.handlers.ConfigHandler;
import com.tiviacz.pizzacraft.init.ModItems;
import com.tiviacz.pizzacraft.items.BlockBase;
import growthcraft.core.shared.item.ItemTest;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.api.capability.IDecayCapability;
import thebetweenlands.api.item.IDecayFood;
import thebetweenlands.api.item.IFoodSicknessItem;
import thebetweenlands.common.registries.CapabilityRegistry;

import java.util.Objects;

import static com.tiviacz.pizzacraft.blocks.BlockPizza.BITES;

@Mixin(value = BlockPizza.class, remap = false)
public class MixinBlockPizza extends BlockBase implements IDecayFood, IFoodSicknessItem {
    @Final
    @Shadow public static final PropertyInteger BITES = PropertyInteger.create("bites", 0, 5);
    @Shadow
    private int foodstats;
    @Shadow
    private float saturation;
    @Shadow
    private Item pizzaslice;

    public MixinBlockPizza(String name, Material material) {
        super(name, material);
    }

    /**
     * @author SC
     * @reason
     */
    @Overwrite
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack helditem = playerIn.getHeldItem(hand);
        int i = (Integer)state.getValue(BITES);
        if (worldIn.isRemote) {
            return true;
        } else {
            if (helditem.getItem() == ModItems.PEEL && i == 0 && (!playerIn.capabilities.isCreativeMode || playerIn.capabilities.isCreativeMode)) {
                spawnAsEntity(worldIn, pos, new ItemStack(this));
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

            return this.eatCake(worldIn, pos, state, playerIn);
        }
    }


    @Override
    public int getDecayHealAmount(ItemStack itemStack) {
        return 1;
    }

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
}
