package com.seriouscreeper.bladditions.mixins.modsupport.embers;

import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.AnvilRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.spi.TileAnvilBase;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomery;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.ModuleTechBloomeryConfig;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.block.BlockBloom;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.spi.ItemTongsEmptyBase;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.item.spi.ItemTongsFullBase;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomeryRecipe;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.recipe.BloomeryRecipeBase;
import com.codetaylor.mc.pyrotech.modules.tech.bloomery.util.BloomHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import teamroots.embers.api.tile.IBin;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageAnvilSparksFX;
import teamroots.embers.network.message.MessageStamperFX;
import teamroots.embers.recipe.DawnstoneAnvilRecipe;
import teamroots.embers.recipe.RecipeRegistry;
import teamroots.embers.tileentity.TileEntityBin;
import teamroots.embers.tileentity.TileEntityDawnstoneAnvil;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
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
        return recipe != null || stack1.getItem() instanceof BlockBloom.ItemBlockBloom || stack2.getItem() instanceof BlockBloom.ItemBlockBloom;
    }


    /**
     * @author SC
     */
    @Inject(method = "activate(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;Lnet/minecraft/util/EnumFacing;FFF)Z", at = @At("HEAD"), cancellable = true)
    public void activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> callback) {
        ItemStack heldItem = player.getHeldItem(hand);

        if (!heldItem.isEmpty() && hand == EnumHand.MAIN_HAND) {
            boolean doContinue = true;

            if(heldItem.getItem() instanceof ItemTongsFullBase) {
                NBTTagCompound tagCompound = heldItem.getTagCompound();

                if(tagCompound == null) {
                    callback.setReturnValue(false);
                }

                NBTTagCompound tileTag = tagCompound.getCompoundTag("BlockEntityTag");
                ItemStack bloomStack = BloomHelper.createBloomAsItemStack(new ItemStack(ModuleTechBloomery.Blocks.BLOOM), tileTag);

                for (int i = 0; i < 2 && doContinue; ++i) {
                    if (this.inventory.getStackInSlot(i).isEmpty()) {
                        this.inventory.insertItem(i, bloomStack, false);
                        ItemStack emptyTongsStack = BloomHelper.createItemTongsEmpty(heldItem, !player.isCreative());
                        heldItem.shrink(1);
                        if (!emptyTongsStack.isEmpty()) {
                            ItemHandlerHelper.giveItemToPlayer(player, emptyTongsStack, player.inventory.currentItem);
                        }

                        doContinue = false;
                        this.progress = 0;
                        this.markDirty();
                        callback.setReturnValue(true);
                    }
                }
            } else if(heldItem.getItem() instanceof ItemTongsEmptyBase) {
                for(int i = 1; i >= 0 && doContinue; --i) {
                    if (!this.inventory.getStackInSlot(i).isEmpty() && !world.isRemote && this.inventory.getStackInSlot(i).getItem() == ModuleTechBloomery.Items.BLOOM) {
                        ItemStack bloomStack = this.inventory.extractItem(i, 1, false);
                        ItemStack tongsFull = BloomHelper.createItemTongsFull(heldItem, bloomStack);
                        heldItem.shrink(1);
                        ItemHandlerHelper.giveItemToPlayer(player, tongsFull, player.inventory.currentItem);

                        doContinue = false;
                        this.progress = 0;
                        this.markDirty();
                        callback.setReturnValue(true);
                    }
                }
            }
        }
    }


    /**
     * @author SC
     */
    @Inject(method = "onHit()V", at = @At("HEAD"), cancellable = true)
    public void onHit(CallbackInfo ci) {
        // TODO:
        // - maybe prevent tinker hammer from working
        if(this.inventory.getStackInSlot(0).getItem() instanceof BlockBloom.ItemBlockBloom || this.inventory.getStackInSlot(1).getItem() instanceof BlockBloom.ItemBlockBloom) {
            ++this.progress;

            this.world.playSound((double) this.pos.getX(), (double) this.pos.getY(), (double) this.pos.getZ(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 0.25F, 2.0F + this.random.nextFloat(), false);

            if (this.progress > MAX_HITS) {
                this.progress = 0;

                int inventorySlot;

                if(this.inventory.getStackInSlot(0).getItem() instanceof BlockBloom.ItemBlockBloom) {
                    inventorySlot = 0;
                } else if (this.inventory.getStackInSlot(1).getItem() instanceof BlockBloom.ItemBlockBloom) {
                    inventorySlot = 1;
                } else {
                    ci.cancel();
                    return;
                }

                ItemStack bloomStack = this.inventory.getStackInSlot(inventorySlot);
                BlockBloom.ItemBlockBloom bloom = (BlockBloom.ItemBlockBloom)bloomStack.getItem();

                //BloomHelper.trySpawnFire(world, this.pos, RandomHelper.random(), ModuleTechBloomeryConfig.BLOOM.FIRE_SPAWN_CHANCE_ON_HIT_RAW);

                AnvilRecipe recipe = AnvilRecipe.getRecipe(bloomStack, AnvilRecipe.EnumTier.IRONCLAD, AnvilRecipe.EnumType.HAMMER);

                if(recipe == null) {
                    ci.cancel();
                    return;
                }

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

                int integrity = bloom.getIntegrity(bloomStack);

                // check integrity of bloom
                if (!this.world.isRemote) {
                    integrity -= 1;
                    bloom.setIntegrity(bloomStack, integrity);
                }

                if (integrity <= 0) {
                    this.inventory.setStackInSlot(inventorySlot, ItemStack.EMPTY);
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
