package com.seriouscreeper.bladditions.tiles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.*;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.config.ConfigAspects;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.TileThaumcraftInventory;
import thebetweenlands.common.herblore.elixir.effects.ElixirEffect;
import thebetweenlands.common.item.herblore.ItemElixir;
import thebetweenlands.common.registries.ItemRegistry;

import java.util.Iterator;
import java.util.List;

public class PatchedTilePotionSprayer extends TileThaumcraftInventory implements IAspectContainer, IEssentiaTransport {
    public AspectList recipe = new AspectList();
    public AspectList recipeProgress = new AspectList();
    public int charges = 0;
    public int color = 0;
    int counter = 0;
    boolean activated = false;
    int venting = 0;
    Aspect currentSuction = null;

    public PatchedTilePotionSprayer() {
        super(1);
    }

    public void update() {
        super.update();
        ++this.counter;
        EnumFacing facing = BlockStateUtils.getFacing(this.getBlockMetadata());
        if (this.world.isRemote) {
            if (this.venting > 0) {
                --this.venting;

                for(int a = 0; a < this.venting / 2; ++a) {
                    float fx = 0.1F - this.world.rand.nextFloat() * 0.2F;
                    float fz = 0.1F - this.world.rand.nextFloat() * 0.2F;
                    float fy = 0.1F - this.world.rand.nextFloat() * 0.2F;
                    float fx2 = (float)(this.world.rand.nextGaussian() * 0.06D);
                    float fz2 = (float)(this.world.rand.nextGaussian() * 0.06D);
                    float fy2 = (float)(this.world.rand.nextGaussian() * 0.06D);
                    FXDispatcher.INSTANCE.drawVentParticles2((double)((float)this.pos.getX() + 0.5F + fx + (float)facing.getXOffset() / 2.0F), (double)((float)this.pos.getY() + 0.5F + fy + (float)facing.getYOffset() / 2.0F), (double)((float)this.pos.getZ() + 0.5F + fz + (float)facing.getZOffset() / 2.0F), (double)fx2 + (double)facing.getXOffset() * 0.25D, (double)fy2 + (double)facing.getYOffset() * 0.25D, (double)fz2 + (double)facing.getZOffset() * 0.25D, this.color, 4.0F);
                }
            }
        } else {
            if (this.counter % 5 == 0) {
                this.currentSuction = null;
                if (this.getStackInSlot(0).isEmpty() || this.charges >= 8) {
                    return;
                }

                boolean done = true;
                Aspect[] var3 = this.recipe.getAspectsSortedByName();
                int var4 = var3.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    Aspect aspect = var3[var5];
                    if (this.recipeProgress.getAmount(aspect) < this.recipe.getAmount(aspect)) {
                        this.currentSuction = aspect;
                        done = false;
                        break;
                    }
                }

                if (done) {
                    this.recipeProgress = new AspectList();
                    ++this.charges;
                    this.syncTile(false);
                    this.markDirty();
                } else if (this.currentSuction != null) {
                    this.fill();
                }
            }

            if (!BlockStateUtils.isEnabled(this.getBlockMetadata())) {
                if (!this.activated && this.charges > 0) {
                    --this.charges;

                    ItemElixir elixir = (ItemElixir)this.getStackInSlot(0).getItem();
                    ElixirEffect effect = elixir.getElixirFromItem(this.getStackInSlot(0));

                    if (effect != null) {
                        int area = 1;
                        BlockPos p = this.pos.offset(facing, 2);
                        List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB((double)(p.getX() - area), (double)(p.getY() - area), (double)(p.getZ() - area), (double)(p.getX() + 1 + area), (double)(p.getY() + 1 + area), (double)(p.getZ() + 1 + area)));
                        boolean lifted = false;
                        if (targets.size() > 0) {
                            Iterator var7 = targets.iterator();

                            label75:
                            while(true) {
                                EntityLivingBase e;

                                do {
                                    do {
                                        if (!var7.hasNext()) {
                                            break label75;
                                        }

                                        e = (EntityLivingBase)var7.next();
                                    } while(e.isDead);
                                } while(!e.canBeHitWithPotion());

                                int duration = elixir.getElixirDuration(this.getStackInSlot(0));
                                int strength = elixir.getElixirStrength(this.getStackInSlot(0));
                                e.addPotionEffect(effect.createEffect(duration == -1 ? 1200 : duration, strength == -1 ? 0 : strength));
                            }
                        }
                    }

                    this.world.playSound((EntityPlayer)null, this.pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.25F, 2.6F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.8F);
                    this.world.addBlockEvent(this.getPos(), this.getBlockType(), 0, 0);
                    this.syncTile(false);
                    this.markDirty();
                }

                this.activated = true;
            } else if (this.activated) {
                this.activated = false;
            }
        }

    }

    private void drawFX(EnumFacing facing, double c) {
    }

    public boolean receiveClientEvent(int i, int j) {
        if (i >= 0) {
            if (this.world.isRemote) {
                this.venting = 15;
            }

            return true;
        } else {
            return super.receiveClientEvent(i, j);
        }
    }

    public void readSyncNBT(NBTTagCompound nbt) {
        this.recipe = new AspectList();
        this.recipe.readFromNBT(nbt, "recipe");
        this.recipeProgress = new AspectList();
        this.recipeProgress.readFromNBT(nbt, "progress");
        this.charges = nbt.getInteger("charges");
        this.color = nbt.getInteger("color");
    }

    public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {
        this.recipe.writeToNBT(nbt, "recipe");
        this.recipeProgress.writeToNBT(nbt, "progress");
        nbt.setInteger("charges", this.charges);
        nbt.setInteger("color", this.color);
        return nbt;
    }

    @Override
    public boolean isItemValidForSlot(int par1, ItemStack stack) {
//        return stack != null && !stack.isEmpty() && stack.getItem() == ItemRegistry.ELIXIR;
        return stack != null && !stack.isEmpty() && stack.getItem() == ItemRegistry.ELIXIR;
    }

    public void setInventorySlotContents(int par1, ItemStack stack) {
        super.setInventorySlotContents(par1, stack);
        this.recalcAspects();
    }

    public ItemStack decrStackSize(int par1, int par2) {
        ItemStack stack = super.decrStackSize(par1, par2);
        this.recalcAspects();
        return stack;
    }

    private void recalcAspects() {
        if (!this.world.isRemote) {
            ItemStack stack = this.getStackInSlot(0);
            this.color = 3355443;
            if (!this.world.isRemote) {
                if (stack != null && !stack.isEmpty()) {
                    this.recipe = ConfigAspects.getPotionAspects(stack);
                    this.color = this.getPotionColor(stack);
                } else {
                    this.recipe = new AspectList();
                }

                this.charges = 0;
                this.recipe = AspectHelper.cullTags(this.recipe, 10);
                this.recipeProgress = new AspectList();
                this.syncTile(false);
                this.markDirty();
            }

        }
    }

    public int getPotionColor(ItemStack itemstack) {
        ItemElixir elixir = (ItemElixir)itemstack.getItem();
        ElixirEffect effect = elixir.getElixirFromItem(itemstack);

        if(effect != null) {
            return elixir.getColorMultiplier(itemstack, 0);
        }

        return 3355443;
    }

    void fill() {
        EnumFacing facing = BlockStateUtils.getFacing(this.getBlockMetadata());
        TileEntity te = null;
        IEssentiaTransport ic = null;

        for(int y = 0; y <= 1; ++y) {
            EnumFacing[] var5 = EnumFacing.VALUES;
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                EnumFacing dir = var5[var7];
                if (dir != facing) {
                    te = ThaumcraftApiHelper.getConnectableTile(this.world, this.pos.up(y), dir);
                    if (te != null) {
                        ic = (IEssentiaTransport)te;
                        if (ic.getEssentiaAmount(dir.getOpposite()) > 0 && ic.getSuctionAmount(dir.getOpposite()) < this.getSuctionAmount((EnumFacing)null) && this.getSuctionAmount((EnumFacing)null) >= ic.getMinimumSuction()) {
                            int ess = ic.takeEssentia(this.currentSuction, 1, dir.getOpposite());
                            if (ess > 0) {
                                this.addToContainer(this.currentSuction, ess);
                                return;
                            }
                        }
                    }
                }
            }
        }

    }

    public int addToContainer(Aspect tt, int am) {
        int ce = this.recipe.getAmount(tt) - this.recipeProgress.getAmount(tt);
        if (ce <= 0) {
            return am;
        } else {
            int add = Math.min(ce, am);
            this.recipeProgress.add(tt, add);
            this.syncTile(false);
            this.markDirty();
            return am - add;
        }
    }

    public boolean takeFromContainer(Aspect tt, int am) {
        return false;
    }

    public boolean takeFromContainer(AspectList ot) {
        return false;
    }

    public boolean doesContainerContain(AspectList ot) {
        return false;
    }

    public boolean doesContainerContainAmount(Aspect tt, int am) {
        return this.recipeProgress.getAmount(tt) >= am;
    }

    public int containerContains(Aspect tt) {
        return this.recipeProgress.getAmount(tt);
    }

    public boolean doesContainerAccept(Aspect tag) {
        return true;
    }

    public boolean isConnectable(EnumFacing face) {
        return face != BlockStateUtils.getFacing(this.getBlockMetadata());
    }

    public boolean canInputFrom(EnumFacing face) {
        return face != BlockStateUtils.getFacing(this.getBlockMetadata());
    }

    public boolean canOutputTo(EnumFacing face) {
        return false;
    }

    public void setSuction(Aspect aspect, int amount) {
        this.currentSuction = aspect;
    }

    public Aspect getSuctionType(EnumFacing loc) {
        return this.currentSuction;
    }

    public int getSuctionAmount(EnumFacing loc) {
        return this.currentSuction != null ? 128 : 0;
    }

    public Aspect getEssentiaType(EnumFacing loc) {
        return null;
    }

    public int getEssentiaAmount(EnumFacing loc) {
        return 0;
    }

    public int takeEssentia(Aspect aspect, int amount, EnumFacing face) {
        return this.canOutputTo(face) && this.takeFromContainer(aspect, amount) ? amount : 0;
    }

    public int addEssentia(Aspect aspect, int amount, EnumFacing face) {
        return this.canInputFrom(face) ? amount - this.addToContainer(aspect, amount) : 0;
    }

    public int getMinimumSuction() {
        return 0;
    }

    public AspectList getAspects() {
        return this.recipeProgress;
    }

    public void setAspects(AspectList aspects) {
        this.recipeProgress = aspects;
    }
}

