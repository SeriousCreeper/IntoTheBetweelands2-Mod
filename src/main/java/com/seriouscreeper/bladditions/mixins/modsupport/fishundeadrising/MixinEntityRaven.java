package com.seriouscreeper.bladditions.mixins.modsupport.fishundeadrising;

import com.Fishmod.mod_LavaCow.client.Modconfig;
import com.Fishmod.mod_LavaCow.entities.tameable.EntityFishTameable;
import com.Fishmod.mod_LavaCow.entities.tameable.EntityRaven;
import com.Fishmod.mod_LavaCow.init.FishItems;
import com.Fishmod.mod_LavaCow.util.LootTableHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.Map;

@Mixin(value = EntityRaven.class, remap = false)
public class MixinEntityRaven extends EntityFishTameable {
    @Shadow
    public int callTimer;
    @Shadow
    private BlockPos jukeboxPosition;
    @Shadow
    private boolean partyParrot;
    @Shadow
    private int ridingCooldown;

    public MixinEntityRaven(World worldIn) {
        super(worldIn);
    }

    @Shadow
    private boolean isFetching() {
        return false;
    }

    @Shadow
    private void SetDismount(Entity ridden) {
    }

    @Shadow
    public int getSkin() {
        return 0;
    }

    @Shadow
    private void calculateFlapping() {}

    /**
     * @author SC
     * @reason
     */
    @Overwrite
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.callTimer > 0) {
            --this.callTimer;
        }

        if (this.jukeboxPosition == null || this.jukeboxPosition.distanceSq(this.posX, this.posY, this.posZ) > 12.0 || this.world.getBlockState(this.jukeboxPosition).getBlock() != Blocks.JUKEBOX) {
            this.partyParrot = false;
            this.jukeboxPosition = null;
        }

        if (!this.isFetching() && this.isTamed()) {
            if (this.ridingCooldown > 0) {
                --this.ridingCooldown;
            }

            if (this.getRidingEntity() != null && this.getRidingEntity() instanceof EntityPlayer) {
                this.setRotation(this.getRidingEntity().rotationYaw, 0.0F);
                if (Modconfig.Raven_Slowfall && !this.getRidingEntity().onGround && this.getRidingEntity().motionY < 0.0 && !this.getRidingEntity().hasNoGravity() && !((EntityPlayer)this.getRidingEntity()).isElytraFlying()) {
                    Entity var10000 = this.getRidingEntity();
                    var10000.motionY *= 0.5;
                }

                if (this.ridingCooldown == 0 && (this.getRidingEntity().isSneaking() || this.getRidingEntity().isInWater())) {
                    this.SetDismount(this.getRidingEntity());
                }
            }

            if (!this.isSitting() && !this.isRiding() && this.getHeldItemMainhand().isEmpty() && this.ticksExisted % 200 == 0 && this.rand.nextFloat() < 0.02F) {
                ItemStack chosenDrop = null;
                Map lootTable;
                switch (this.getSkin()) {
                    case 2:
                        lootTable = LootTableHandler.LOOT_SEAGULL;
                        break;
                    case 3:
                        lootTable = LootTableHandler.LOOT_SPECTRAL_RAVEN;
                        break;
                    default:
                        lootTable = LootTableHandler.LOOT_RAVEN;
                }

                Iterator var3 = lootTable.entrySet().iterator();

                while(var3.hasNext()) {
                    Map.Entry<ItemStack, Float> entry = (Map.Entry)var3.next();
                    if (this.rand.nextFloat() < (Float)entry.getValue()) {
                        chosenDrop = (ItemStack)entry.getKey();
                        break;
                    }
                }

                if (chosenDrop == null) {
                    chosenDrop = new ItemStack(FishItems.FEATHER_BLACK, 1);
                }

                this.setHeldItem(this.getActiveHand(), new ItemStack(chosenDrop.getItem(), this.rand.nextInt(chosenDrop.getCount()) + 1, chosenDrop.getMetadata()));
            }
        }

        if (this.canPickUpLoot() && !this.getHeldItemMainhand().isEmpty()) {
            this.setCanPickUpLoot(false);
        } else if (!this.canPickUpLoot() && this.getHeldItemMainhand().isEmpty()) {
            this.setCanPickUpLoot(true);
        }

        this.calculateFlapping();
    }
}
