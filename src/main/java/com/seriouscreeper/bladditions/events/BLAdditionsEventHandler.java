package com.seriouscreeper.bladditions.events;

import com.mrbysco.anotherliquidmilkmod.init.MilkRegistry;
import com.seriouscreeper.bladditions.proxy.CommonProxy;
import growthcraft.cellar.common.tileentity.TileEntityBrewKettle;
import growthcraft.core.shared.tileentity.GrowthcraftTileDeviceBase;
import growthcraft.core.shared.tileentity.feature.IFluidTankOperable;
import growthcraft.milk.shared.init.GrowthcraftMilkFluids;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thebetweenlands.common.entity.mobs.EntityLurker;
import thebetweenlands.common.entity.projectiles.EntityBetweenstonePebble;
import thebetweenlands.common.entity.projectiles.EntityPyradFlame;
import thebetweenlands.common.entity.projectiles.EntitySapSpit;
import thebetweenlands.common.item.tools.ItemBLBucket;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.ItemRegistry;
import thebetweenlands.common.registries.SoundRegistry;
import thebetweenlands.common.tile.TileEntityBarrel;

import java.util.Map;

@Mod.EventBusSubscriber
public class BLAdditionsEventHandler {

    @SubscribeEvent
    public static void entityHurt(LivingHurtEvent event) {
        IPlayerKnowledge knowledge;

        if (event.getSource().getImmediateSource() != null && event.getEntity() instanceof EntityPlayer && ThaumcraftCapabilities.knowsResearchStrict((EntityPlayer)event.getEntity(), new String[]{"FOCUSPROJECTILE@2"})) {
            knowledge = ThaumcraftCapabilities.getKnowledge((EntityPlayer)event.getEntity());
            if (!ThaumcraftCapabilities.knowsResearch((EntityPlayer)event.getEntity(), new String[]{"f_arrow"}) && event.getSource().getImmediateSource() instanceof EntityBetweenstonePebble) {
                knowledge.addResearch("f_arrow");
                knowledge.sync((EntityPlayerMP)event.getEntity());
                ((EntityPlayer)event.getEntity()).sendStatusMessage(new TextComponentString(TextFormatting.DARK_PURPLE + I18n.translateToLocal("got.projectile")), true);
            }

            if (!ThaumcraftCapabilities.knowsResearch((EntityPlayer)event.getEntity(), new String[]{"f_fireball"}) && event.getSource().getImmediateSource() instanceof EntityPyradFlame) {
                knowledge.addResearch("f_fireball");
                knowledge.sync((EntityPlayerMP)event.getEntity());
                ((EntityPlayer)event.getEntity()).sendStatusMessage(new TextComponentString(TextFormatting.DARK_PURPLE + I18n.translateToLocal("got.projectile")), true);
            }

            if (!ThaumcraftCapabilities.knowsResearch((EntityPlayer)event.getEntity(), new String[]{"f_spit"}) && event.getSource().getImmediateSource() instanceof EntitySapSpit) {
                knowledge.addResearch("f_spit");
                knowledge.sync((EntityPlayerMP)event.getEntity());
                ((EntityPlayer)event.getEntity()).sendStatusMessage(new TextComponentString(TextFormatting.DARK_PURPLE + I18n.translateToLocal("got.projectile")), true);
            }
        }
    }

    @SubscribeEvent
    public static void BonemealEvent(BonemealEvent event) {
        EntityPlayer player = event.getEntityPlayer();

        if(player != null && player.getHeldItem(event.getHand()) != ItemStack.EMPTY && player.getHeldItem(event.getHand()).getItem() == new ItemStack(Items.DYE, 1, 15).getItem()) {
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if(event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;

            IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);
            ResearchCategory researchBasics = ResearchCategories.getResearchCategory("BASICS");

            int value = knowledge.getKnowledgeRaw(IPlayerKnowledge.EnumKnowledgeType.THEORY, researchBasics);

            if (value < 1600) {
                for(Map.Entry<String, ResearchCategory> entry : ResearchCategories.researchCategories.entrySet()) {
                    ResearchCategory tempCategory = entry.getValue();

                    knowledge.addKnowledge(IPlayerKnowledge.EnumKnowledgeType.THEORY, tempCategory, Math.max(0, 1600 - value));

                    value = knowledge.getKnowledgeRaw(IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, tempCategory);
                    knowledge.addKnowledge(IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, tempCategory, Math.max(0, 3200 - value));
                }
            }
        }
    }


    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.NeighborNotifyEvent event) {
        Block block = event.getState().getBlock();

        if(block == BlockRegistry.SPREADING_SLUDGY_DIRT) {
            if(event.getWorld().rand.nextFloat() > 0.8) {
                AuraHelper.polluteAura(event.getWorld(), event.getPos(), 1, true);
            }
        } else if(block == Blocks.DIRT) {
            event.getWorld().setBlockState(event.getPos(), BlockRegistry.SWAMP_DIRT.getDefaultState());
        } else if(block == Blocks.ICE) {
            event.getWorld().setBlockState(event.getPos(), BlockRegistry.BLACK_ICE.getDefaultState());
        } else if(block == Blocks.SNOW_LAYER) {
            event.getWorld().setBlockState(event.getPos(), BlockRegistry.SNOW.getDefaultState());
        }
    }


    @SubscribeEvent
    public static void onBottleUsed(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();

        if(world.isRemote)
            return;

        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = event.getItemStack();

        TileEntity te = world.getTileEntity(event.getPos());

        if(stack != ItemStack.EMPTY && (te instanceof GrowthcraftTileDeviceBase || te instanceof TileEntityBarrel)) {
            int itemDamage = stack.getItemDamage();
            IFluidHandler fluidHandler = FluidUtil.getFluidHandler(world, event.getPos(), null);
            ItemStack singleStack = stack.copy();
            singleStack.setCount(1);
            IFluidHandlerItem fluidItem = FluidUtil.getFluidHandler(singleStack);

            // this is only for draining
            if(stack.getItem() == ItemRegistry.DENTROTHYST_VIAL && (itemDamage == 0 || itemDamage == 2)) {
                ItemStack newBottle = itemDamage == 0 ? new ItemStack(CommonProxy.DENTROTHYST_FLUID_VIAL) : new ItemStack(CommonProxy.DENTROTHYST_FLUID_VIAL, 1, 1);

                // has no content, check output side of TE
                FluidStack fluidStack = fluidHandler.drain(250, false);
                FluidActionResult result = fluidStack != null ? FluidUtil.tryFillContainer(newBottle, fluidHandler, 250, player, false) : FluidActionResult.FAILURE;

                if(result.isSuccess()) {
                    FluidUtil.tryFillContainer(newBottle, fluidHandler, 250, player, true);
                    newBottle = result.getResult();

                    stack.shrink(1);

                    if (!player.inventory.addItemStackToInventory(newBottle)) {
                        world.spawnEntity(new EntityItem(world, (double) event.getPos().getX() + 0.5D, (double) event.getPos().getY() + 1.5D, (double) event.getPos().getZ() + 0.5D, newBottle));
                    } else if (player instanceof EntityPlayerMP) {
                        ((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
                    }

                    event.setCanceled(true);
                }
            } else if(stack.getItem() == CommonProxy.DENTROTHYST_FLUID_VIAL && (stack.getItemDamage() == 0 || stack.getItemDamage() == 1)) {
                // for filling the kettle
                FluidStack fluidStack = fluidItem.drain(250, false);
                FluidActionResult result = fluidStack != null ? FluidUtil.tryEmptyContainer(singleStack, fluidHandler, 250, player, false) : FluidActionResult.FAILURE;

                if(result.isSuccess()) {
                    FluidUtil.tryEmptyContainer(singleStack, fluidHandler, 250, player, true);

                    ItemStack newBottle;

                    if(stack.getItemDamage() == 0) {
                        newBottle = new ItemStack(ItemRegistry.DENTROTHYST_VIAL, 1, 1);
                    } else {
                        newBottle = new ItemStack(ItemRegistry.DENTROTHYST_VIAL, 1, 2);
                    }

                    stack.shrink(1);

                    if (!player.inventory.addItemStackToInventory(newBottle)) {
                        world.spawnEntity(new EntityItem(world, (double) event.getPos().getX() + 0.5D, (double) event.getPos().getY() + 1.5D, (double) event.getPos().getZ() + 0.5D, newBottle));
                    } else if (player instanceof EntityPlayerMP) {
                        ((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
                    }

                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract e) {
        World world = e.getWorld();

        if(world.isRemote)
            return;

        ItemStack itemstack = e.getItemStack();

        if (itemstack != ItemStack.EMPTY && itemstack.getItem() == ItemRegistry.BL_BUCKET && e.getHand() == EnumHand.MAIN_HAND) {
            ItemStack copy = ItemHandlerHelper.copyStackWithSize(itemstack, 1);
            IFluidHandlerItem fluidItem = FluidUtil.getFluidHandler(copy);

            if (fluidItem != null) {
                int fill = fluidItem.fill(new FluidStack(MilkRegistry.liquid_milk, Fluid.BUCKET_VOLUME), true);

                if (fill == Fluid.BUCKET_VOLUME) {
                    EntityPlayer player = e.getEntityPlayer();
                    player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
                    world.playSound(null, player.posX, player.posY + 0.5D, player.posZ, SoundEvents.ENTITY_COW_MILK, SoundCategory.BLOCKS, 1.0F, 1.0F);

                    copy = fluidItem.getContainer().copy();
                    itemstack.shrink(1);

                    if (itemstack.isEmpty()) {
                        player.inventory.addItemStackToInventory(copy);
                    } else if (!player.inventory.addItemStackToInventory(copy)) {
                        player.dropItem(copy, false);
                    }

                    e.setCanceled(true);
                }
            }
        } else {
            e.setCanceled(true);
        }
    }
}
