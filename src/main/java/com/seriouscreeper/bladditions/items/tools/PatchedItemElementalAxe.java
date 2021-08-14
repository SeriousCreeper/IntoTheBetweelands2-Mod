package com.seriouscreeper.bladditions.items.tools;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.IThaumcraftItems;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;
import thaumcraft.common.lib.utils.EntityUtils;
import thebetweenlands.api.item.CorrosionHelper;
import thebetweenlands.api.item.ICorrodible;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PatchedItemElementalAxe extends ItemAxe implements IThaumcraftItems, ICorrodible {
    public PatchedItemElementalAxe(ToolMaterial enumtoolmaterial) {
        super(enumtoolmaterial, 8.0F, -3.0F);
        this.setCreativeTab(ConfigItems.TABTC);
        this.setRegistryName("thaumcraft:elemental_axe");
        this.setTranslationKey("elemental_axe");
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
        return ImmutableSet.of("axe");
    }

    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.RARE;
    }

    public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {
        return stack2.isItemEqual(new ItemStack(ItemsTC.ingots, 1, 0)) ? true : super.getIsRepairable(stack1, stack2);
    }

    public EnumAction getItemUseAction(ItemStack itemstack) {
        return EnumAction.BOW;
    }

    public int getMaxItemUseDuration(ItemStack p_77626_1_) {
        return 72000;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        playerIn.setActiveHand(hand);
        return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
    }

    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        List<EntityItem> stuff = EntityUtils.getEntitiesInRange(player.world, player.posX, player.posY, player.posZ, player, EntityItem.class, 10.0D);
        if (stuff != null && stuff.size() > 0) {
            Iterator var5 = stuff.iterator();

            while (var5.hasNext()) {
                EntityItem e = (EntityItem) var5.next();
                if (!e.isDead) {
                    double d6 = e.posX - player.posX;
                    double d8 = e.posY - player.posY + (double) (player.height / 2.0F);
                    double d10 = e.posZ - player.posZ;
                    double d11 = (double) MathHelper.sqrt(d6 * d6 + d8 * d8 + d10 * d10);
                    d6 /= d11;
                    d8 /= d11;
                    d10 /= d11;
                    double d13 = 0.3D;
                    e.motionX -= d6 * d13;
                    e.motionY -= d8 * d13 - 0.1D;
                    e.motionZ -= d10 * d13;
                    if (e.motionX > 0.25D) {
                        e.motionX = 0.25D;
                    }

                    if (e.motionX < -0.25D) {
                        e.motionX = -0.25D;
                    }

                    if (e.motionY > 0.25D) {
                        e.motionY = 0.25D;
                    }

                    if (e.motionY < -0.25D) {
                        e.motionY = -0.25D;
                    }

                    if (e.motionZ > 0.25D) {
                        e.motionZ = 0.25D;
                    }

                    if (e.motionZ < -0.25D) {
                        e.motionZ = -0.25D;
                    }

                    if (player.world.isRemote) {
                        FXDispatcher.INSTANCE.crucibleBubble((float) e.posX + (player.world.rand.nextFloat() - player.world.rand.nextFloat()) * 0.2F, (float) e.posY + e.height + (player.world.rand.nextFloat() - player.world.rand.nextFloat()) * 0.2F, (float) e.posZ + (player.world.rand.nextFloat() - player.world.rand.nextFloat()) * 0.2F, 0.33F, 0.33F, 1.0F);
                    }
                }
            }
        }

    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab != ConfigItems.TABTC && tab != CreativeTabs.SEARCH) {
            super.getSubItems(tab, items);
        } else {
            ItemStack w1 = new ItemStack(this);
            EnumInfusionEnchantment.addInfusionEnchantment(w1, EnumInfusionEnchantment.BURROWING, 1);
            EnumInfusionEnchantment.addInfusionEnchantment(w1, EnumInfusionEnchantment.COLLECTOR, 1);
            items.add(w1);
        }

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