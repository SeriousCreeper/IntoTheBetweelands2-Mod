package com.seriouscreeper.bladditions.mixins.modsupport.botania;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.corporea.InvWithLocation;
import vazkii.botania.common.entity.EntityCorporeaSpark;
import vazkii.botania.common.item.ModItems;

import java.util.List;

@Mixin(value = EntityCorporeaSpark.class, remap = false)
public class MixinEntityCorporeaSpark extends Entity implements ICorporeaSpark {
    public MixinEntityCorporeaSpark(World worldIn) {
        super(worldIn);
    }


    @Shadow
    public void setNetwork(EnumDyeColor network) {
    }


    @Shadow
    private void restartNetwork() {
    }

    @Shadow
    private void findNetwork() {
    }

    @Inject(method = "processInitialInteract", at = @At("HEAD"), cancellable = true)
    public void processInitialInteract(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = player.getHeldItem(hand);
        if (!this.isDead && !stack.isEmpty()) {
            if (stack.getItem() == Items.DYE) {
                int color = 15 - stack.getItemDamage();

                if (color != this.getNetwork().getMetadata()) {
                    this.setNetwork(EnumDyeColor.byMetadata(color));
                    if (this.isMaster()) {
                        this.restartNetwork();
                    } else {
                        this.findNetwork();
                    }

                    stack.shrink(1);
                    cir.setReturnValue(true);
                }
            }
        }
    }

    @Shadow
    protected void entityInit() {

    }

    @Shadow
    protected void readEntityFromNBT(NBTTagCompound nbtTagCompound) {

    }

    @Shadow
    protected void writeEntityToNBT(NBTTagCompound nbtTagCompound) {

    }

    @Shadow
    public void registerConnections(ICorporeaSpark iCorporeaSpark, ICorporeaSpark iCorporeaSpark1, List<ICorporeaSpark> list) {

    }

    @Shadow
    public InvWithLocation getSparkInventory() {
        return null;
    }

    @Shadow
    public List<ICorporeaSpark> getConnections() {
        return null;
    }

    @Shadow
    public List<ICorporeaSpark> getRelatives() {
        return null;
    }

    @Shadow
    public ICorporeaSpark getMaster() {
        return null;
    }

    @Shadow
    public void onItemExtracted(ItemStack itemStack) {

    }

    @Shadow
    public void onItemsRequested(List<ItemStack> list) {

    }

    @Shadow
    public boolean isMaster() {
        return false;
    }

    @Shadow
    public EnumDyeColor getNetwork() {
        return null;
    }
}
