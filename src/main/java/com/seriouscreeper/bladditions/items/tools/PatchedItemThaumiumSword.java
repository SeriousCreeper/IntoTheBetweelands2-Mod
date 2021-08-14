package com.seriouscreeper.bladditions.items.tools;

import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.IThaumcraftItems;
import thebetweenlands.api.item.CorrosionHelper;
import thebetweenlands.api.item.ICorrodible;
import thebetweenlands.common.capability.circlegem.CircleGemHelper;

import javax.annotation.Nullable;
import java.util.List;

public class PatchedItemThaumiumSword extends ItemSword implements IThaumcraftItems, ICorrodible {
    public PatchedItemThaumiumSword(ToolMaterial enumtoolmaterial) {
        super(enumtoolmaterial);
        this.setCreativeTab(ConfigItems.TABTC);
        this.setRegistryName("thaumcraft:thaumium_sword");
        this.setTranslationKey("thaumium_sword");
        ConfigItems.ITEM_VARIANT_HOLDERS.add(this);

        CorrosionHelper.addCorrosionPropertyOverrides(this);
        CircleGemHelper.addGemPropertyOverrides(this);
    }

    public Item getItem() {
        return this;
    }

    public String[] getVariantNames() {
        return new String[]{"normal"};
    }

    public int[] getVariantMeta() {
        return new int[]{0};
    }

    public ItemMeshDefinition getCustomMesh() {
        return null;
    }

    public ModelResourceLocation getCustomModelResourceLocation(String variant) {
        return new ModelResourceLocation("thaumcraft:" + variant);
    }

    public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {
        return stack2.isItemEqual(new ItemStack(ItemsTC.ingots, 1, 0)) ? true : super.getIsRepairable(stack1, stack2);
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

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        return CorrosionHelper.getAttributeModifiers(super.getAttributeModifiers(slot, stack), slot, stack, ATTACK_DAMAGE_MODIFIER, 3.0F + this.getAttackDamage());
    }
}
