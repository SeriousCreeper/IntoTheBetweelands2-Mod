package com.seriouscreeper.bladditions.events;

import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataFluidTank;
import com.codetaylor.mc.athenaeum.util.SoundHelper;
import com.codetaylor.mc.pyrotech.library.spi.block.IBlockIgnitableWithIgniterItem;
import com.codetaylor.mc.pyrotech.library.spi.tile.TileCombustionWorkerBase;
import com.codetaylor.mc.pyrotech.library.spi.tile.TileEntityDataWorkerBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.potion.PotionFocused;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCampfire;
import com.mrbysco.anotherliquidmilkmod.init.MilkRegistry;
import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import com.seriouscreeper.bladditions.potion.PotionThaumcraftResearch;
import com.seriouscreeper.bladditions.proxy.CommonProxy;
import crafttweaker.api.event.BlockPlaceEvent;
import growthcraft.core.shared.tileentity.GrowthcraftTileDeviceBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.terraingen.ChunkGeneratorEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.common.lib.utils.EntityUtils;
import thebetweenlands.api.environment.IEnvironmentEvent;
import thebetweenlands.common.block.farming.BlockFungusCrop;
import thebetweenlands.common.block.farming.BlockGenericDugSoil;
import thebetweenlands.common.block.structure.BlockFenceBetweenlands;
import thebetweenlands.common.entity.mobs.EntityGreebling;
import thebetweenlands.common.entity.mobs.EntityLurker;
import thebetweenlands.common.entity.projectiles.EntityBetweenstonePebble;
import thebetweenlands.common.entity.projectiles.EntityPyradFlame;
import thebetweenlands.common.entity.projectiles.EntitySapSpit;
import thebetweenlands.common.item.misc.ItemMisc;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.FluidRegistry;
import thebetweenlands.common.registries.ItemRegistry;
import thebetweenlands.common.tile.TileEntityBarrel;
import thebetweenlands.common.tile.TileEntityDugSoil;
import thebetweenlands.common.world.storage.BetweenlandsWorldStorage;
import vazkii.quark.decoration.entity.EntityLeashKnot2TheKnotting;
import vazkii.quark.decoration.feature.IronLadders;
import vazkii.quark.tweaks.base.BlockStack;
import vazkii.quark.tweaks.feature.HoeSickle;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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

            // TODO: Skip the addons ones

            if(!player.getEntityData().hasKey("free_thaumcraft_research")) {
                IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);

                for (Map.Entry<String, ResearchCategory> entry : ResearchCategories.researchCategories.entrySet()) {
                    ResearchCategory tempCategory = entry.getValue();

                    int value = knowledge.getKnowledgeRaw(IPlayerKnowledge.EnumKnowledgeType.THEORY, tempCategory);
                    knowledge.addKnowledge(IPlayerKnowledge.EnumKnowledgeType.THEORY, tempCategory, Math.max(0, 320 - value));

                    value = knowledge.getKnowledgeRaw(IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, tempCategory);
                    knowledge.addKnowledge(IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, tempCategory, Math.max(0, 160 - value));
                }

                player.getEntityData().setBoolean("free_thaumcraft_research", true);
            }
        }
    }


    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.NeighborNotifyEvent event) {
        Block block = event.getState().getBlock();

        if(block == BlockRegistry.SPREADING_SLUDGY_DIRT) {
            if(event.getWorld().rand.nextFloat() > 0.8) {
                AuraHelper.polluteAura(event.getWorld(), event.getPos(), 2, true);
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
    public static void onBottleUsed(PlayerInteractEvent.RightClickBlock event) throws NoSuchFieldException, IllegalAccessException {
        World world = event.getWorld();

        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = event.getItemStack();

        TileEntity te = world.getTileEntity(event.getPos());

        if(te instanceof TileCampfire && stack != ItemStack.EMPTY && stack.getItem() == ItemRegistry.BL_BUCKET && FluidUtil.getFluidContained(stack) != null && FluidUtil.getFluidContained(stack).getFluid() == FluidRegistry.SWAMP_WATER) {
            TileCampfire campfire = (TileCampfire) te;

            if(campfire.workerIsActive()) {
                Field field = TileCampfire.class.getDeclaredField("extinguishedByRain");
                field.setAccessible(true);
                boolean b = field.getBoolean(campfire);
                b = true;

                campfire.workerSetActive(false);

                if (!world.isRemote) {
                    SoundHelper.playSoundServer(world, te.getPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS);
                }
            }
        }

        if(stack != ItemStack.EMPTY && (te instanceof GrowthcraftTileDeviceBase || te instanceof TileEntityBarrel)) {
            if(world.isRemote)
                return;

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
            }
            else if(stack.getItem() == CommonProxy.DENTROTHYST_FLUID_VIAL && (stack.getItemDamage() == 0 || stack.getItemDamage() == 1)) {
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
        } else if(stack != ItemStack.EMPTY && (stack.getItem() == CommonProxy.DENTROTHYST_VIAL || stack.getItem() == CommonProxy.DENTROTHYST_FLUID_VIAL)) {
            event.setCanceled(true);
        }
    }

    /*
    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract e) {
        World world = e.getWorld();

        ItemStack itemstack = e.getItemStack();

        System.out.println(e.getTarget().getName());

        if (e.getTarget() instanceof EntityLurker && itemstack != ItemStack.EMPTY && itemstack.getItem() == ItemRegistry.BL_BUCKET && e.getHand() == EnumHand.MAIN_HAND) {
            ItemStack copy = ItemHandlerHelper.copyStackWithSize(itemstack, 1);
            IFluidHandlerItem fluidItem = FluidUtil.getFluidHandler(copy);

            if (fluidItem != null) {
                int fill = fluidItem.fill(new FluidStack(MilkRegistry.liquid_milk, Fluid.BUCKET_VOLUME), true);

                if (fill == Fluid.BUCKET_VOLUME) {
                    EntityPlayer player = e.getEntityPlayer();
                    player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
                    world.playSound(null, player.posX, player.posY + 0.5D, player.posZ, SoundEvents.ENTITY_COW_MILK, SoundCategory.BLOCKS, 1.0F, 1.0F);

                    if(!world.isRemote) {
                        copy = fluidItem.getContainer().copy();
                        itemstack.shrink(1);

                        if (itemstack.isEmpty()) {
                            player.setHeldItem(e.getHand(), copy);
                        } else if (!player.inventory.addItemStackToInventory(copy)) {
                            player.dropItem(copy, false);
                        }
                    }

                    e.setCanceled(true);
                }
            }
        }
    }
     */


    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        if(world.isRemote)
            return;

        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = player.getHeldItem(event.getHand());
        BlockPos pos = event.getPos();
        IBlockState state = world.getBlockState(pos);

        if(stack.getItem() == Items.LEAD && state.getBlock() instanceof BlockFenceBetweenlands) {
            for(EntityLiving entityliving : world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(player.posX - 7, player.posY - 7, player.posZ - 7, player.posX + 7, player.posY + 7, player.posZ + 7))) {
                if(entityliving.getLeashHolder() == player)
                    return;
            }

            EntityLeashKnot2TheKnotting knot = new EntityLeashKnot2TheKnotting(world);
            knot.setPosition(pos.getX() + 0.5, pos.getY() + 0.5 - 1F / 8F, pos.getZ() + 0.5);
            world.spawnEntity(knot);
            knot.setLeashHolder(player, true);

            if(!player.isCreative())
                stack.shrink(1);
            world.playSound(null, pos, SoundEvents.ENTITY_LEASHKNOT_PLACE, SoundCategory.BLOCKS, 1F, 1F);
            event.setCanceled(true);
        }
    }


    private static Method getSeed;

    private static void replant(World world, BlockPos pos, BlockStack inWorld, EntityPlayer player) {
        ItemStack mainHand = player.getHeldItemMainhand();
        boolean isHoe = !mainHand.isEmpty() && mainHand.getItem() instanceof ItemHoe;

        BlockStack newBlock = CommonProxy.CROPS.get(inWorld);
        NonNullList<ItemStack> drops = NonNullList.create();
        int fortune = HoeSickle.canFortuneApply(Enchantments.FORTUNE, mainHand) && isHoe ?
                EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, mainHand) : 0;

        if(inWorld.getBlock() instanceof BlockFungusCrop) {
            drops.add(((BlockFungusCrop) inWorld.getBlock()).getCropDrop(world, pos, world.rand));

            if(world.rand.nextInt(2) >= 1) {
                drops.add(new ItemStack(ItemRegistry.SPORES));
            }
        } else {
            inWorld.getBlock().getDrops(drops, world, pos, inWorld.getState(), fortune);
        }

        for (ItemStack stack : drops) {
            if (stack.isEmpty())
                continue;

            if (stack.getItem() instanceof IPlantable || (stack.getItem() == Items.DYE && stack.getMetadata() == EnumDyeColor.BROWN.getDyeDamage())) {
                stack.shrink(1);
                break;
            }
        }

        ForgeEventFactory.fireBlockHarvesting(drops, world, pos, inWorld.getState(), fortune, 1.0F, false, player);
        boolean expired = updateSoil(world, pos, 10);

        boolean seedNotNull = true;
        if (inWorld.getBlock() instanceof BlockCrops) {
            try {
                if (getSeed == null)
                    getSeed = ObfuscationReflectionHelper.findMethod(BlockCrops.class, "func_149866_i", Item.class);
                Item seed = (Item) getSeed.invoke(inWorld.getBlock());
                seedNotNull = seed != null && seed != Items.AIR;
            } catch (IllegalAccessException | InvocationTargetException e) {
            }
        }

        if (seedNotNull) {
            if (!world.isRemote) {
                world.playEvent(2001, pos, Block.getStateId(newBlock.getState()));

                if(!expired) {
                    world.setBlockState(pos, newBlock.getState());

                    for (ItemStack stack : drops) {
                        EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
                        entityItem.setPickupDelay(10);
                        world.spawnEntity(entityItem);
                    }
                }
            }
        }
    }


    protected static boolean updateSoil(World world, BlockPos pos, int compost) {
        IBlockState stateDown = world.getBlockState(pos.down());

        if (stateDown.getBlock() instanceof BlockGenericDugSoil) {
            TileEntityDugSoil te = BlockGenericDugSoil.getTile(world, pos.down());

            if (te != null && te.isComposted()) {
                te.setCompost(Math.max(te.getCompost() - compost, 0));

                if (((BlockGenericDugSoil)stateDown.getBlock()).isPurified(world, pos.down(), stateDown)) {
                    te.setPurifiedHarvests(te.getPurifiedHarvests() + 1);
                }

                return !te.isComposted();
            } else {
                return false;
            }
        }

        return false;
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onCropClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != EnumHand.MAIN_HAND)
            return;

        BlockStack worldBlock = BlockStack.getStackFromPos(event.getWorld(), event.getPos());

        int harvests = 0;

        if (CommonProxy.CROPS.containsKey(worldBlock)) {
            replant(event.getWorld(), event.getPos(), worldBlock, event.getEntityPlayer());
            harvests++;
        }

        if (harvests > 0) {
            event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
            event.setCanceled(true);
            event.setCancellationResult(EnumActionResult.SUCCESS);
        }
    }


    private static Method canAttachTo;

    private static boolean canAttachTo(Block ladder, World world, BlockPos pos, EnumFacing facing) {
        if (ladder == IronLadders.iron_ladder) {
            return IronLadders.iron_ladder.canBlockStay(world, pos, facing);
        } else {
            if (ladder instanceof BlockLadder) {
                BlockPos attachPos = pos.offset(facing, -1);
                if (canAttachTo == null) {
                    canAttachTo = ObfuscationReflectionHelper.findMethod(BlockLadder.class, "func_193392_c", Boolean.TYPE, new Class[]{World.class, BlockPos.class, EnumFacing.class});
                }

                try {
                    return (Boolean)canAttachTo.invoke(ladder, world, attachPos, facing);
                } catch (InvocationTargetException | IllegalAccessException var6) {
                }
            }

            return false;
        }
    }


    @SubscribeEvent
    public void onInteract(PlayerInteractEvent.RightClickBlock event) {
        EntityPlayer player = event.getEntityPlayer();
        EnumHand hand = event.getHand();
        ItemStack stack = player.getHeldItem(hand);
        List<Item> items = new ArrayList();
        items.add(Item.getItemFromBlock(Blocks.LADDER));
        items.add(Item.getItemFromBlock(BlockRegistry.WEEDWOOD_LADDER));

        if (!stack.isEmpty() && items.contains(stack.getItem())) {
            Block block = Block.getBlockFromItem(stack.getItem());
            World world = event.getWorld();

            BlockPos posDown;
            for(BlockPos pos = event.getPos(); world.getBlockState(pos).getBlock() == block; pos = posDown) {
                event.setCanceled(true);
                posDown = pos.down();
                if (world.isOutsideBuildHeight(posDown)) {
                    break;
                }

                IBlockState stateDown = world.getBlockState(posDown);

                if (stateDown.getBlock() != block) {
                    if (stateDown.getBlock().isAir(stateDown, world, posDown)) {
                        IBlockState copyState = world.getBlockState(pos);
                        EnumFacing facing = (EnumFacing)copyState.getValue(BlockLadder.FACING);

                        if (canAttachTo(block, world, posDown, facing)) {
                            world.setBlockState(posDown, copyState);
                            world.playSound((EntityPlayer)null, (double)posDown.getX(), (double)posDown.getY(), (double)posDown.getZ(), SoundEvents.BLOCK_LADDER_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);

                            if (world.isRemote) {
                                player.swingArm(hand);
                            }

                            if (!player.capabilities.isCreativeMode) {
                                stack.shrink(1);
                                if (stack.getCount() <= 0) {
                                    player.setHeldItem(hand, ItemStack.EMPTY);
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
    }


    @SubscribeEvent
    public void onPotionApplied(PotionEvent.PotionAddedEvent event) {
        EntityLivingBase entity = event.getEntityLiving();

        if(entity == null)
            return;

        Collection<PotionEffect> effects = entity.getActivePotionEffects();

        boolean hasResearchPotion = false;

        for(PotionEffect effect : effects) {
            if(effect.getPotion() instanceof PotionThaumcraftResearch && effect != event.getPotionEffect()) {
                if(hasResearchPotion) {
                    entity.removePotionEffect(effect.getPotion());
                    continue;
                }

                hasResearchPotion = true;
            }
        }
    }


    @SubscribeEvent
    public void onPotionRemoved(PotionEvent.PotionRemoveEvent event) {
        EntityLivingBase entity = event.getEntityLiving();

        if(event.getPotion() instanceof PotionThaumcraftResearch) {
            if(entity.getEntityData().hasKey("wellnessBonus")) {
                entity.getEntityData().removeTag("wellnessBonus");
            }
        }
    }


    private float checkPlayerWellness(World world, EntityPlayer player, Collection<PotionEffect> effects, PotionThaumcraftResearch.RESEARCH_CATEGORY category) {
        float wellnessBonus = 1;

        // greebling nearby
        // well rested buff from pyrotech
        // bl events affecting it in different ways?
        // bookshelf
        // maybe certain blocks should affect different research?
            // like cauldron nearby improves the alchemy one

        if(player.isRiding()) {
            wellnessBonus *= ConfigBLAdditions.configTea.SittingBonus;
        }

        // Find greebling nearby
        List<Entity> l = EntityUtils.getEntitiesInRange(world, player.getPosition(), (Entity)null, Entity.class, 10.0D);

        if (!l.isEmpty()) {
            for (Entity e : l) {
                if(e instanceof EntityGreebling) {
                    wellnessBonus *= ConfigBLAdditions.configTea.GreeblingBonus;
                }
            }
        }

        int blockSearchRadius = 4;

        List<Block> blocksApplied = new ArrayList<>();

        for(int y = -1; y <= 1; ++y) {
            for(int x = -blockSearchRadius; x <= blockSearchRadius; ++x) {
                for(int z = -blockSearchRadius; z <= blockSearchRadius; ++z) {
                    IBlockState state = world.getBlockState(player.getPosition().add(x, y, z));
                    Block block = state.getBlock();

                    for(Object wellnessBlock : CommonProxy.WELLNESS_BLOCKS.keySet()) {
                        if(wellnessBlock == block) {
                            List<PotionThaumcraftResearch.RESEARCH_CATEGORY> researchBonuses = CommonProxy.WELLNESS_BLOCKS.get(block);

                            if(researchBonuses != null && researchBonuses.contains(category)) {
                                if(!blocksApplied.contains(block)) {
                                    wellnessBonus *= ConfigBLAdditions.configTea.NearbyBlocksBonus;
                                    blocksApplied.add(block);
                                }

                                break;
                            }
                        }
                    }
                }
            }
        }

        // Pyrotech buff bonus
        for(PotionEffect effect : effects) {
            if(effect.getPotion() instanceof PotionFocused) {
                wellnessBonus *= ConfigBLAdditions.configTea.FocusedBuffBonus;
                break;
            }
        }

        // BL Events
        BetweenlandsWorldStorage storage = BetweenlandsWorldStorage.forWorld(world);

        if (storage != null) {
            Map<String, Float> eventBonuses = ConfigBLAdditions.parseBLEvents();

            List<IEnvironmentEvent> activeEvents = storage.getEnvironmentEventRegistry().getActiveEvents();

            for(IEnvironmentEvent event : activeEvents) {
                String eventName = event.getEventName().getPath();

                if(eventBonuses.containsKey(eventName)) {
                    wellnessBonus *= eventBonuses.get(eventName);
                }
            }
        }

        return wellnessBonus;
    }


    @SubscribeEvent
    public void onPlayerIgnitePyrotech(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();

        if(world.isRemote)
            return;

        BlockPos pos = event.getPos();
        IBlockState state = event.getWorld().getBlockState(pos);

        if (state.getBlock() instanceof IBlockIgnitableWithIgniterItem) {
            if(event.getItemStack() != ItemStack.EMPTY && event.getItemStack().getItem() == ItemRegistry.OCTINE_INGOT) {
                ((IBlockIgnitableWithIgniterItem) state.getBlock()).igniteWithIgniterItem(world, pos, state, event.getFace());
                world.playSound(null, event.getPos().getX(), event.getPos().getY() + 0.5D, event.getPos().getZ(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
        }
    }


    private boolean hasScribingTools(EntityPlayer player) {
        for (ItemStack itemstack : player.inventory.mainInventory) {
            if (!itemstack.isEmpty() && itemstack.getItem() == ItemsTC.scribingTools) {
                itemstack.damageItem(1, player);
                return true;
            }
        }

        return false;
    }


    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        World world = player.world;

        // check if we have a bonus saved
        // if not, run it once and save it
        // if we do, only check it if check interval passed + we triggered the next chance
        // remove wellness nbt from player when potion runs out

        if(!world.isRemote && player.ticksExisted % 20 == 0) {
            Collection<PotionEffect> effects = player.getActivePotionEffects();

            for(PotionEffect effect : effects) {
                if(effect.getPotion() instanceof PotionThaumcraftResearch) {
                    PotionThaumcraftResearch researchPotion = (PotionThaumcraftResearch) effect.getPotion();

                    int wellnessInterval = ConfigBLAdditions.configTea.TCPotionChance;

                    if(!player.getEntityData().hasKey("wellnessBonus")) {
                        wellnessInterval = Math.round(wellnessInterval * checkPlayerWellness(world, player, effects, researchPotion.Category));
                    }

                    if(world.rand.nextInt(wellnessInterval) != 0) {
                        return;
                    }

                    if(!ConfigBLAdditions.configTea.RequiresBookAndQuill || player.inventory.hasItemStack(new ItemStack(ItemsTC.thaumonomicon)) && hasScribingTools(player)) {
                        int oProg = IPlayerKnowledge.EnumKnowledgeType.OBSERVATION.getProgression();
                        int tProg = IPlayerKnowledge.EnumKnowledgeType.THEORY.getProgression();

                        switch(researchPotion.Category) {
                            case ARCANE:
                                ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, ResearchCategories.getResearchCategory("INFUSION"), MathHelper.getInt(player.getRNG(), oProg / 2, oProg));
                                ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.THEORY, ResearchCategories.getResearchCategory("INFUSION"), MathHelper.getInt(player.getRNG(), tProg / 3, tProg / 2));
                                break;
                            case AUROMANCY:
                                ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, ResearchCategories.getResearchCategory("AUROMANCY"), MathHelper.getInt(player.getRNG(), oProg / 2, oProg));
                                ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.THEORY, ResearchCategories.getResearchCategory("AUROMANCY"), MathHelper.getInt(player.getRNG(), tProg / 3, tProg / 2));
                                break;
                            case GOLEMANCY:
                                ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, ResearchCategories.getResearchCategory("GOLEMANCY"), MathHelper.getInt(player.getRNG(), oProg / 2, oProg));
                                ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.THEORY, ResearchCategories.getResearchCategory("GOLEMANCY"), MathHelper.getInt(player.getRNG(), tProg / 3, tProg / 2));
                                break;
                            case ELDRITCH:
                                ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, ResearchCategories.getResearchCategory("ELDRITCH"), MathHelper.getInt(player.getRNG(), oProg / 2, oProg));
                                ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.THEORY, ResearchCategories.getResearchCategory("ELDRITCH"), MathHelper.getInt(player.getRNG(), tProg / 3, tProg / 2));
                                ThaumcraftApi.internalMethods.addWarpToPlayer(player, 1, IPlayerWarp.EnumWarpType.NORMAL);
                                ThaumcraftApi.internalMethods.addWarpToPlayer(player, 5, IPlayerWarp.EnumWarpType.TEMPORARY);
                                break;
                            case ARTIFICE:
                                ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, ResearchCategories.getResearchCategory("ARTIFICE"), MathHelper.getInt(player.getRNG(), oProg / 2, oProg));
                                ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.THEORY, ResearchCategories.getResearchCategory("ARTIFICE"), MathHelper.getInt(player.getRNG(), tProg / 3, tProg / 2));
                                break;
                            case ALCHEMY:
                                ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, ResearchCategories.getResearchCategory("ALCHEMY"), MathHelper.getInt(player.getRNG(), oProg / 2, oProg));
                                ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.THEORY, ResearchCategories.getResearchCategory("ALCHEMY"), MathHelper.getInt(player.getRNG(), tProg / 3, tProg / 2));
                                break;
                        }
                    }

                    break;
                }
            }
        }
    }


    @SubscribeEvent
    public void handleTorchInWater(TickEvent.PlayerTickEvent event) {
        if(event.player == null || event.player.world.isRemote) {
            return;
        }

        if(event.player.isInWater()) {
            IBlockState blockState = event.player.world.getBlockState(new BlockPos(event.player.posX, event.player.getEntityBoundingBox().maxY + 0.1D, event.player.posZ));
            if(blockState.getMaterial().isLiquid()) {
                for(EnumHand hand : EnumHand.values()) {
                    ItemStack torches = event.player.getHeldItem(hand);

                    if(torches != ItemStack.EMPTY && torches.getItem() == Item.getItemFromBlock(BlockRegistry.SULFUR_TORCH)) {
                        event.player.setHeldItem(hand, new ItemStack(BlockRegistry.SULFUR_TORCH_EXTINGUISHED, torches.getCount()));
                        event.player.world.playSound((EntityPlayer)null, event.player.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT, 1.0F, 1.0F);
                    }
                }
            }
        }
    }
}
