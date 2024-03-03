package com.seriouscreeper.bladditions.mixins.modsupport.bloodmagic;

import WayofTime.bloodmagic.item.soul.ItemSentientPickaxe;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.util.helper.NBTHelper;
import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thebetweenlands.api.item.CorrosionHelper;
import thebetweenlands.api.item.ICorrodible;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(value = ItemSentientPickaxe.class, remap = false)
public class MixinItemSentientPickaxe extends ItemPickaxe implements ICorrodible {
    protected MixinItemSentientPickaxe(ToolMaterial material) {
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
                    return ConfigBLAdditions.configBloodMagic.configPickaxe.defaultDamageAdded[willBracket];
                case DESTRUCTIVE:
                    return ConfigBLAdditions.configBloodMagic.configPickaxe.destructiveDamageAdded[willBracket];
                case VENGEFUL:
                    return ConfigBLAdditions.configBloodMagic.configPickaxe.vengefulDamageAdded[willBracket];
                case STEADFAST:
                    return ConfigBLAdditions.configBloodMagic.configPickaxe.steadfastDamageAdded[willBracket];
                default:
                    return 0.0;
            }
        }
    }

    @Redirect(method = "recalculatePowers", at = @At(value = "INVOKE", target = "LWayofTime/bloodmagic/item/soul/ItemSentientPickaxe;setDamageOfActivatedSword(Lnet/minecraft/item/ItemStack;D)V"))
    private void redirectRecalculatePowers(ItemSentientPickaxe instance, ItemStack stack, double damage) {
        instance.setDamageOfActivatedSword(stack, damage - 3 + ConfigBLAdditions.configBloodMagic.configPickaxe.defaultDamage);
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

    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    public void getDestroySpeed(ItemStack stack, IBlockState state, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(CorrosionHelper.getDestroySpeed(cir.getReturnValue(), stack, state));
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
