package com.seriouscreeper.bladditions.mixins.modsupport.roots;

import com.codetaylor.mc.pyrotech.modules.storage.block.spi.BlockShelfBase;
import com.seriouscreeper.bladditions.proxy.CommonProxy;
import epicsquid.mysticallib.item.ItemShearsBase;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.particle.particles.ParticleGlitter;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.capability.runic_shears.RunicShearsCapability;
import epicsquid.roots.capability.runic_shears.RunicShearsCapabilityProvider;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.config.MossConfig;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.item.ItemRunicShears;
import epicsquid.roots.network.fx.MessageRunicShearsAOEFX;
import epicsquid.roots.network.fx.MessageRunicShearsFX;
import epicsquid.roots.recipe.RunicShearEntityRecipe;
import epicsquid.roots.recipe.RunicShearRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.entity.mobs.EntityPyrad;
import thebetweenlands.common.entity.mobs.EntitySwarm;
import thebetweenlands.common.registries.BlockRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

@Mixin(value = ItemRunicShears.class, remap = false)
public class MixinRunicShears extends ItemShearsBase {
    public MixinRunicShears(String name, Supplier<Ingredient> repairIngredient) {
        super(name, repairIngredient);
    }

    @Shadow
    public static AxisAlignedBB bounding = new AxisAlignedBB(-2.0, -2.0, -2.0, 2.0, 2.0, 2.0);

    @Shadow
    private Random random;

    /**
     * @author
     * @reason
     */
    @Overwrite
    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block == BlockRegistry.ITEM_SHELF || block instanceof BlockShelfBase) {
            return EnumActionResult.PASS;
        }

        if (block == ModBlocks.imbuer) {
            return EnumActionResult.PASS;
        } else {
            if (!MossConfig.getBlacklistDimensions().contains(player.world.provider.getDimension())) {
                IBlockState moss = MossConfig.scrapeResult(state);
                IBlockState moss2 = MossConfig.mossConversion(state);
                if (moss != null || moss2 != null) {
                    if (!world.isRemote) {
                        AxisAlignedBB bounds = bounding.offset(pos);
                        BlockPos start = new BlockPos(bounds.minX, bounds.minY, bounds.minZ);
                        BlockPos stop = new BlockPos(bounds.maxX, bounds.maxY, bounds.maxZ);
                        List<BlockPos> affectedBlocks = new ArrayList();
                        Iterator var17 = BlockPos.getAllInBoxMutable(start, stop).iterator();

                        while(var17.hasNext()) {
                            BlockPos.MutableBlockPos p = (BlockPos.MutableBlockPos)var17.next();
                            IBlockState pState = world.getBlockState(p);
                            IBlockState m = MossConfig.scrapeResult(pState);
                            if (m != null) {
                                affectedBlocks.add(p.toImmutable());
                                world.setBlockState(p, m);
                                world.scheduleBlockUpdate(p, m.getBlock(), 1, m.getBlock().tickRate(world));
                                ItemUtil.spawnItem(world, player.getPosition().add(0, 1, 0), new ItemStack(ModItems.terra_moss));
                            }
                        }

                        if (!affectedBlocks.isEmpty()) {
                            if (!player.capabilities.isCreativeMode) {
                                player.getHeldItem(hand).damageItem(1 + Math.min(6, this.random.nextInt(affectedBlocks.size())), player);
                            }

                            MessageRunicShearsAOEFX message = new MessageRunicShearsAOEFX(affectedBlocks);
                            PacketHandler.sendToAllTracking(message, world.provider.getDimension(), pos);
                        }
                    }

                    player.swingArm(hand);
                    world.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    return EnumActionResult.SUCCESS;
                }
            }

            RunicShearRecipe recipe = ModRecipes.getRunicShearRecipe(state);
            if (recipe != null) {
                if (!world.isRemote) {
                    world.setBlockState(pos, recipe.getReplacementState());
                    ItemUtil.spawnItem(world, player.getPosition().add(0, 1, 0), recipe.getDrop().copy());
                    if (!player.capabilities.isCreativeMode) {
                        player.getHeldItem(hand).damageItem(1, player);
                    }

                    player.swingArm(hand);
                    world.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
                } else {
                    for(int i = 0; i < 50; ++i) {
                        ClientProxy.particleRenderer.spawnParticle(world, ParticleGlitter.class, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, this.random.nextDouble() * 0.1 * (double)(this.random.nextDouble() > 0.5 ? -1 : 1), this.random.nextDouble() * 0.1 * (double)(this.random.nextDouble() > 0.5 ? -1 : 1), this.random.nextDouble() * 0.1 * (double)(this.random.nextDouble() > 0.5 ? -1 : 1), new double[]{120.0, 0.855 + this.random.nextDouble() * 0.05, 0.71, 0.943 - this.random.nextDouble() * 0.05, 1.0, this.random.nextDouble() + 0.5, this.random.nextDouble() * 2.0});
                    }
                }
            }

            return EnumActionResult.SUCCESS;
        }
    }

    /**
     * @author SC
     */
    @Overwrite
    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
        World world = player.world;
        Random rand = itemRand;

        if (entity.isChild()) {
            return true;
        } else {
            if (!player.isSneaking()) {
                RunicShearEntityRecipe recipe = ModRecipes.getRunicShearRecipe(entity);
                if (recipe != null) {
                    player.swingArm(hand);

                    if (!world.isRemote) {
                        RunicShearsCapability cap = (RunicShearsCapability)entity.getCapability(RunicShearsCapabilityProvider.RUNIC_SHEARS_CAPABILITY, (EnumFacing)null);
                        if (cap != null) {
                            if (cap.canHarvest()) {
                                long cooldown = (long)recipe.getCooldown();

                                boolean isPacifist = CommonProxy.IsPacifist((EntityPlayerMP) player);

                                if(isPacifist) {
                                    cooldown /= 4;
                                } else if(entity instanceof EntitySwarm || entity instanceof EntityPyrad) {
                                    player.sendStatusMessage((new TextComponentTranslation("Only Pacifists can shear this entity", new Object[0])).setStyle((new Style()).setColor(TextFormatting.DARK_PURPLE)), true);
                                    return true;
                                }

                                cap.setCooldown(cooldown);
                                EntityItem ent = entity.entityDropItem(recipe.getDrop(entity).copy(), 1.0F);

                                ent.motionY += (double)(rand.nextFloat() * 0.05F);
                                ent.motionX += (double)((rand.nextFloat() - rand.nextFloat()) * 0.1F);
                                ent.motionZ += (double)((rand.nextFloat() - rand.nextFloat()) * 0.1F);
                                if (!player.capabilities.isCreativeMode) {
                                    itemstack.damageItem(1, entity);
                                }

                                world.playSound((EntityPlayer)null, entity.getPosition(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
                                IMessage packet = new MessageRunicShearsFX(entity);
                                PacketHandler.sendToAllTracking(packet, entity);
                                return true;
                            }

                            player.sendStatusMessage((new TextComponentTranslation("roots.runic_shears.cooldown", new Object[0])).setStyle((new Style()).setColor(TextFormatting.DARK_PURPLE)), true);
                        }
                    }
                }
            }

            if (entity instanceof IShearable) {
                int count = 0;
                if (Items.SHEARS.itemInteractionForEntity(itemstack, player, entity, hand)) {
                    ++count;
                }

                float radius = (float) GeneralConfig.RunicShearsRadius;
                List<EntityLiving> entities = Util.getEntitiesWithinRadius(entity.world, (ex) -> {
                    return ex instanceof IShearable;
                }, entity.getPosition(), radius, radius / 2.0F, radius);
                Iterator var21 = entities.iterator();

                while(true) {
                    EntityLiving e;
                    do {
                        if (!var21.hasNext()) {
                            if (count > 0) {
                                player.swingArm(hand);
                                return true;
                            }

                            return false;
                        }

                        e = (EntityLiving)var21.next();
                        e.captureDrops = true;
                        if (Items.SHEARS.itemInteractionForEntity(itemstack, player, e, hand)) {
                            ++count;
                        }

                        e.captureDrops = false;
                    } while(world.isRemote);

                    Iterator var12 = e.capturedDrops.iterator();

                    while(var12.hasNext()) {
                        EntityItem ent = (EntityItem)var12.next();
                        ent.setPosition(entity.posX, entity.posY, entity.posZ);
                        ent.motionY = 0.0D;
                        ent.motionX = 0.0D;
                        ent.motionZ = 0.0D;
                        ent.world.spawnEntity(ent);
                    }
                }
            } else {
                return false;
            }
        }
    }
}
