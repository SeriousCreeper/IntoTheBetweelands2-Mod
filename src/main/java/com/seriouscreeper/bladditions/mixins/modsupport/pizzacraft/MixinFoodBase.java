package com.seriouscreeper.bladditions.mixins.modsupport.pizzacraft;

import com.tiviacz.pizzacraft.items.FoodBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import thebetweenlands.api.item.IDecayFood;
import thebetweenlands.api.item.IFoodSicknessItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Mixin(value = FoodBase.class)
public class MixinFoodBase extends ItemFood implements IDecayFood, IFoodSicknessItem {
    public MixinFoodBase(int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
    }

    @Override
    public int getDecayHealAmount(ItemStack itemStack) {
        switch(Objects.requireNonNull(itemStack.getItem().getRegistryName()).toString()) {
            case "pizzacraft:slice_5":
                return 4;

            case "pizzacraft:slice_9":
                return 10;
        }

        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return Objects.requireNonNull(stack.getItem().getRegistryName()).toString().equals("pizzacraft:slice_9");
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        if (!worldIn.isRemote && Objects.requireNonNull(stack.getItem().getRegistryName()).toString().equals("pizzacraft:slice_9")) {
            player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 50, 1));
            player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 1200, 0));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getDecayFoodTooltip(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
        switch(Objects.requireNonNull(stack.getItem().getRegistryName()).toString()) {
            case "pizzacraft:slice_5":
            case "pizzacraft:slice_9":
                list.add(I18n.format("tooltip.bl.decay_food", new Object[]{stack.getDisplayName()}));
                break;
        }
    }
}
