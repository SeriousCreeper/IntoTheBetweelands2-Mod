package com.seriouscreeper.bladditions.items.tools;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.IThaumcraftItems;
import thebetweenlands.api.item.CorrosionHelper;
import thebetweenlands.api.item.ICorrodible;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class PatchedItemThaumiumPickaxe extends ItemPickaxe implements IThaumcraftItems, ICorrodible {
    public PatchedItemThaumiumPickaxe(ToolMaterial enumtoolmaterial) {
        super(enumtoolmaterial);
        this.setCreativeTab(ConfigItems.TABTC);
        this.setRegistryName("thaumcraft:thaumium_pick");
        this.setTranslationKey("thaumium_pick");
        ConfigItems.ITEM_VARIANT_HOLDERS.add(this);

        CorrosionHelper.addCorrosionPropertyOverrides(this);
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

    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("pickaxe");
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
        Material material = state.getMaterial();
        float str = material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getDestroySpeed(stack, state) : this.efficiency;
        str = CorrosionHelper.getDestroySpeed(str, stack, state);
        return str;
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
