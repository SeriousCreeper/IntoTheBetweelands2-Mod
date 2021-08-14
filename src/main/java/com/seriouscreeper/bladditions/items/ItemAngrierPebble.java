package com.seriouscreeper.bladditions.items;

import com.seriouscreeper.bladditions.BLAdditions;
import com.seriouscreeper.bladditions.entities.EntityAngrierPebble;
import com.seriouscreeper.bladditions.proxy.CommonProxy;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.client.tab.BLCreativeTabs;
import thebetweenlands.common.entity.EntityAngryPebble;
import thebetweenlands.common.item.misc.ItemAngryPebble;
import thebetweenlands.common.registries.SoundRegistry;

public class ItemAngrierPebble extends ItemAngryPebble {
    public ItemAngrierPebble() {
        this.setHasSubtypes(false);
        setRegistryName("angrier_pebble");
        setTranslationKey(BLAdditions.MODID + ".angrier_pebble");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (!worldIn.isRemote && entityLiving instanceof EntityPlayer) {
            int useTime = this.getMaxItemUseDuration(stack) - timeLeft;
            if (useTime > 20) {
                worldIn.playSound((EntityPlayer)null, entityLiving.posX, entityLiving.posY, entityLiving.posZ, CommonProxy.SORRY_CHARGED, SoundCategory.PLAYERS, 0.7F, 0.8F);
                EntityAngrierPebble pebble = new EntityAngrierPebble(worldIn, entityLiving);
                pebble.shoot(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, -10.0F, 1.2F, 3.5F);
                worldIn.spawnEntity(pebble);
                if (!((EntityPlayer)entityLiving).isCreative()) {
                    stack.shrink(1);
                }
            }
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}
