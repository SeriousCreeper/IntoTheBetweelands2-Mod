package com.seriouscreeper.bladditions.mixins.modsupport.simpledifficulty;

import com.charles445.simpledifficulty.potion.PotionBase;
import com.charles445.simpledifficulty.potion.PotionParasites;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = PotionParasites.class, remap = false)
public class MixinPotionParasites extends PotionBase {
    @Shadow
    @SideOnly(Side.CLIENT)
    @Override
    public ResourceLocation getTexture() {
        return null;
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
