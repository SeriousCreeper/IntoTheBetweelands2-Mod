package com.seriouscreeper.bladditions.items.tools;

import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.items.IWarpingGear;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.IThaumcraftItems;
import thebetweenlands.api.item.CorrosionHelper;
import thebetweenlands.api.item.ICorrodible;
import thebetweenlands.common.capability.circlegem.CircleGemHelper;

import javax.annotation.Nullable;
import java.util.List;

public class PatchedItemVoidSword extends ItemSword implements IWarpingGear, IThaumcraftItems, ICorrodible {
    public PatchedItemVoidSword(ToolMaterial enumtoolmaterial) {
        super(enumtoolmaterial);
        this.setCreativeTab(ConfigItems.TABTC);
        this.setRegistryName("thaumcraft:void_sword");
        this.setTranslationKey("void_sword");
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

    public void onUpdate(ItemStack stack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {
        super.onUpdate(stack, world, entity, p_77663_4_, p_77663_5_);
        if (stack.isItemDamaged() && entity != null && entity.ticksExisted % 20 == 0 && entity instanceof EntityLivingBase) {
            stack.damageItem(-1, (EntityLivingBase)entity);
        }

        CorrosionHelper.updateCorrosion(stack, world, entity, p_77663_4_, p_77663_5_);
    }

    public boolean hitEntity(ItemStack is, EntityLivingBase target, EntityLivingBase hitter) {
        if (!target.world.isRemote && (!(target instanceof EntityPlayer) || !(hitter instanceof EntityPlayer) || FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())) {
            try {
                target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 60));
            } catch (Exception var5) {
            }
        }

        return super.hitEntity(is, target, hitter);
    }

    public int getWarp(ItemStack itemstack, EntityPlayer player) {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GOLD + I18n.translateToLocal("enchantment.special.sapless"));
        CorrosionHelper.addCorrosionTooltips(stack, tooltip, flagIn.isAdvanced());
        super.addInformation(stack, worldIn, tooltip, flagIn);
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
