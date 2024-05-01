package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import com.google.common.base.Predicate;
import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import gigaherz.eyes.entity.EntityEyes;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import thebetweenlands.common.item.misc.ItemBarkAmulet;

import java.util.Arrays;
import java.util.List;

@Mixin(value = ItemBarkAmulet.class, remap = false)
public class MixinItemBarkAmulet {
    /**
     * @author SC
     * @reason make indestructible
     */
    @Overwrite
    public void onEquipmentTick(ItemStack stack, Entity entity, IInventory inventory) {
    }


    @SideOnly(Side.CLIENT)
    @Redirect(method = "onClientTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"))
    private static List<EntityLivingBase>  redirectClientTick(World instance, Class aClass, AxisAlignedBB axisAlignedBB, Predicate predicate) {
        Entity view = Minecraft.getMinecraft().getRenderViewEntity();

        return view.world.getEntitiesWithinAABB(EntityLivingBase.class, view.getEntityBoundingBox().grow(12.0), (e) -> {
            return e.getDistanceSq(view) <= 144.0 && !(Arrays.asList(ConfigBLAdditions.configGeneral.BarkAmuletBlocklist).contains(e.getClass().getName()));
        });
    }
}
