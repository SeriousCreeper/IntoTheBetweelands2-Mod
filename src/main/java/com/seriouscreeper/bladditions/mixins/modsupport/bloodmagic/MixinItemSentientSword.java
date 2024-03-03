package com.seriouscreeper.bladditions.mixins.modsupport.bloodmagic;

import WayofTime.bloodmagic.item.soul.ItemSentientSword;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.util.helper.NBTHelper;
import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import thebetweenlands.api.item.CorrosionHelper;
import thebetweenlands.api.item.ICorrodible;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(value = ItemSentientSword.class, remap = false)
public class MixinItemSentientSword extends ItemSword implements ICorrodible {
    public MixinItemSentientSword(ToolMaterial material) {
        super(material);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public double getExtraDamage(EnumDemonWillType type, int willBracket) {
        if (willBracket < 0) {
            return 0.0;
        } else {
            switch (type) {
                case CORROSIVE:
                case DEFAULT:
                    return ConfigBLAdditions.configBloodMagic.configSword.defaultDamageAdded[willBracket];
                case DESTRUCTIVE:
                    return ConfigBLAdditions.configBloodMagic.configSword.destructiveDamageAdded[willBracket];
                case VENGEFUL:
                    return ConfigBLAdditions.configBloodMagic.configSword.vengefulDamageAdded[willBracket];
                case STEADFAST:
                    return ConfigBLAdditions.configBloodMagic.configSword.steadfastDamageAdded[willBracket];
                default:
                    return 0.0;
            }
        }
    }

    @Redirect(method = "recalculatePowers(Lnet/minecraft/item/ItemStack;LWayofTime/bloodmagic/soul/EnumDemonWillType;D)V", at = @At(value = "INVOKE", target = "LWayofTime/bloodmagic/item/soul/ItemSentientSword;setDamageOfActivatedSword(Lnet/minecraft/item/ItemStack;D)V"))
    private void redirectRecalculatePowers(ItemSentientSword instance, ItemStack stack, double damage) {
        instance.setDamageOfActivatedSword(stack, damage - 5 + ConfigBLAdditions.configBloodMagic.configSword.defaultDamage);
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
    public double getDamageOfActivatedSword(ItemStack stack) {
        NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();
        return tag.getDouble("soulSwordDamage") * CorrosionHelper.getModifier(stack);
    }
}
