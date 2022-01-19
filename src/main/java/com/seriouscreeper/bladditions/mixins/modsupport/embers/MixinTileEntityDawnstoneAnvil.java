package com.seriouscreeper.bladditions.mixins.modsupport.embers;

import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockBloom;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomeryRecipeBase;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.util.BloomHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import teamroots.embers.api.tile.IBin;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageAnvilSparksFX;
import teamroots.embers.network.message.MessageStamperFX;
import teamroots.embers.recipe.DawnstoneAnvilRecipe;
import teamroots.embers.recipe.RecipeRegistry;
import teamroots.embers.tileentity.TileEntityBin;
import teamroots.embers.tileentity.TileEntityDawnstoneAnvil;

import javax.annotation.Nonnull;
import java.util.Random;

@Mixin(value = TileEntityDawnstoneAnvil.class, remap = false)
public class MixinTileEntityDawnstoneAnvil extends TileEntity {
    @Shadow
    Random random = new Random();

    @Shadow
    int progress = 0;

    @Shadow
    public static int MAX_HITS = 40;

    @Shadow
    public ItemStackHandler inventory = new ItemStackHandler(2) {
        protected void onContentsChanged(int slot) {
            MixinTileEntityDawnstoneAnvil.this.markDirty();
        }

        protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
            return 1;
        }
    };


    /**
     * @author SC
     */
    @Overwrite
    public boolean isValid(ItemStack stack1, ItemStack stack2) {
        DawnstoneAnvilRecipe recipe = RecipeRegistry.getDawnstoneAnvilRecipe(stack1, stack2);
        return recipe != null || stack1.getItem() instanceof BlockBloom.ItemBlockBloom;
    }


    /**
     * @author SC
     */
    @Inject(method = "onHit()V", at = @At("HEAD"), cancellable = true)
    public void onHit(CallbackInfo ci) {
        // TODO:
        // - maybe prevnet tinker hammer from working
        if(this.inventory.getStackInSlot(0).getItem() instanceof BlockBloom.ItemBlockBloom || this.inventory.getStackInSlot(1).getItem() instanceof BlockBloom.ItemBlockBloom) {
            ++this.progress;

            this.world.playSound((double) this.pos.getX(), (double) this.pos.getY(), (double) this.pos.getZ(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 0.25F, 2.0F + this.random.nextFloat(), false);

            if (this.progress > MAX_HITS) {
                this.progress = 0;

                int inventorySlot = 0;

                if(this.inventory.getStackInSlot(0).getItem() instanceof BlockBloom.ItemBlockBloom) {
                    inventorySlot = 0;
                } else if (this.inventory.getStackInSlot(1).getItem() instanceof BlockBloom.ItemBlockBloom) {
                    inventorySlot = 1;
                } else {
                    ci.cancel();
                }

                BlockBloom.ItemBlockBloom bloom = (BlockBloom.ItemBlockBloom)this.inventory.getStackInSlot(inventorySlot).getItem();

                // check integrity of bloom
                int integrity = bloom.getIntegrity(this.inventory.getStackInSlot(0));
                integrity -= 1;
                bloom.setIntegrity(this.inventory.getStackInSlot(inventorySlot), integrity);

                BloomHelper.trySpawnFire(world, this.pos, RandomHelper.random(), ModuleTechBloomeryConfig.BLOOM.FIRE_SPAWN_CHANCE_ON_HIT_RAW);

                BloomeryRecipeBase recipe = ModuleTechBloomery.Registries.BLOOMERY_RECIPE.getValue(new ResourceLocation(bloom.getRecipeId(this.inventory.getStackInSlot(inventorySlot))));

                if (recipe != null) {
                    TileEntity bin = this.getWorld().getTileEntity(this.getPos().down());
                    if (bin instanceof IBin) {
                        ItemStack remainder = ((TileEntityBin) bin).getInventory().insertItem(0, recipe.getOutput(), false);
                        if (!remainder.isEmpty() && !this.getWorld().isRemote) {
                            EntityItem item = new EntityItem(this.getWorld(), (double) this.getPos().getX() + 0.5D, (double) ((float) this.getPos().getY() + 1.0625F), (double) this.getPos().getZ() + 0.5D, remainder);
                            this.getWorld().spawnEntity(item);
                        }

                        bin.markDirty();
                        this.markDirty();
                    } else if (!this.world.isRemote) {
                        EntityItem item = new EntityItem(this.getWorld(), (double) this.getPos().getX() + 0.5D, (double) ((float) this.getPos().getY() + 1.0625F), (double) this.getPos().getZ() + 0.5D, recipe.getOutput());
                        this.getWorld().spawnEntity(item);
                    }
                }

                if(integrity <= 0) {
                    this.inventory.setStackInSlot(0, ItemStack.EMPTY);
                }

                if (!this.getWorld().isRemote) {
                    PacketHandler.INSTANCE.sendToAll(new MessageStamperFX((double) this.getPos().getX() + 0.5D, (double) this.getPos().getY() + 1.0625D, (double) this.getPos().getZ() + 0.5D));
                }

                this.world.playSound((double) this.pos.getX(), (double) this.pos.getY(), (double) this.pos.getZ(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 1.0F, 0.95F + this.random.nextFloat() * 0.1F, false);

                this.markDirty();
                if (!this.getWorld().isRemote) {
                    PacketHandler.INSTANCE.sendToAll(new MessageAnvilSparksFX((double) this.getPos().getX() + 0.5D, (double) this.getPos().getY() + 1.0625D, (double) this.getPos().getZ() + 0.5D));
                }
            }

            ci.cancel();
        }
    }
}
