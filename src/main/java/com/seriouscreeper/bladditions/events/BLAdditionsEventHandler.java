package com.seriouscreeper.bladditions.events;

import baubles.api.BaublesApi;
import com.Fishmod.mod_LavaCow.client.Modconfig;
import com.Fishmod.mod_LavaCow.entities.EntityParasite;
import com.Fishmod.mod_LavaCow.entities.flying.EntityVespa;
import com.Fishmod.mod_LavaCow.init.ModMobEffects;
import com.Fishmod.mod_LavaCow.util.LootTableHandler;
import com.charles445.simpledifficulty.api.SDCapabilities;
import com.charles445.simpledifficulty.api.SDItems;
import com.charles445.simpledifficulty.api.thirst.IThirstCapability;
import com.charles445.simpledifficulty.api.thirst.ThirstEnum;
import com.charles445.simpledifficulty.api.thirst.ThirstUtil;
import com.charles445.simpledifficulty.item.ItemCanteen;
import com.codetaylor.mc.athenaeum.util.SoundHelper;
import com.codetaylor.mc.pyrotech.library.spi.block.IBlockIgnitableWithIgniterItem;
import com.codetaylor.mc.pyrotech.modules.tech.basic.ModuleTechBasic;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockKilnPit;
import com.codetaylor.mc.pyrotech.modules.tech.basic.potion.PotionFocused;
import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCampfire;
import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import com.seriouscreeper.bladditions.interfaces.ISanityExtraInfo;
import com.seriouscreeper.bladditions.potion.PotionThaumcraftResearch;
import com.seriouscreeper.bladditions.proxy.CommonProxy;
import epicsquid.roots.init.ModItems;
import hunternif.mc.atlas.api.AtlasAPI;
import mcp.mobius.waila.api.event.WailaRenderEvent;
import mcp.mobius.waila.api.event.WailaTooltipEvent;
import net.darkhax.gamestages.event.GameStageEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagInt;
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
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.tiffit.sanity.Sanity;
import net.tiffit.sanity.SanityCapability;
import net.tiffit.sanity.SanityModifier;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.FocusPackage;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.common.blocks.world.ore.BlockCrystal;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.config.ModConfig;
import thaumcraft.common.items.armor.ItemGoggles;
import thaumcraft.common.items.casters.ItemCaster;
import thaumcraft.common.items.casters.ItemFocus;
import thaumcraft.common.items.casters.foci.FocusEffectFire;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.utils.InventoryUtils;
import thebetweenlands.api.capability.IRotSmellCapability;
import thebetweenlands.api.environment.IEnvironmentEvent;
import thebetweenlands.common.block.farming.BlockFungusCrop;
import thebetweenlands.common.block.farming.BlockGenericDugSoil;
import thebetweenlands.common.block.misc.BlockSulfurTorchExtinguished;
import thebetweenlands.common.block.structure.BlockFenceBetweenlands;
import thebetweenlands.common.block.structure.BlockWaystone;
import thebetweenlands.common.entity.mobs.EntityAnadia;
import thebetweenlands.common.entity.mobs.EntityGreebling;
import thebetweenlands.common.entity.projectiles.EntityBetweenstonePebble;
import thebetweenlands.common.entity.projectiles.EntityFishingSpear;
import thebetweenlands.common.entity.projectiles.EntityPyradFlame;
import thebetweenlands.common.entity.projectiles.EntitySapSpit;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.CapabilityRegistry;
import thebetweenlands.common.registries.FluidRegistry;
import thebetweenlands.common.registries.ItemRegistry;
import thebetweenlands.common.tile.TileEntityDugSoil;
import thebetweenlands.common.world.storage.BetweenlandsWorldStorage;
import thecodex6824.thaumicaugmentation.common.item.ItemTieredCasterGauntlet;
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
    public void onPlayerRespawnBegin(net.minecraftforge.event.entity.player.PlayerEvent.Clone e) {
        if (e.isWasDeath() && !e.getEntityPlayer().world.isRemote) {
            restoreThirst(e.getEntityPlayer(), e.getOriginal());
        }
    }


    private static void restoreThirst(EntityPlayer player, EntityPlayer oldPlayer) {
        IThirstCapability oldThirstCap = SDCapabilities.getThirstData(oldPlayer);
        IThirstCapability newThirstCap = SDCapabilities.getThirstData(player);

        newThirstCap.setThirstLevel(Math.max(6, oldThirstCap.getThirstLevel()));
        newThirstCap.setThirstSaturation(Math.max(6, oldThirstCap.getThirstSaturation()));
    }


    @SubscribeEvent
    public static void entityHurt(LivingHurtEvent event) {
        IPlayerKnowledge knowledge;

        if (event.getSource().getImmediateSource() != null && event.getEntity() instanceof EntityPlayer && ThaumcraftCapabilities.knowsResearchStrict((EntityPlayer)event.getEntity(), "FOCUSPROJECTILE@2")) {
            knowledge = ThaumcraftCapabilities.getKnowledge((EntityPlayer)event.getEntity());
            if (!ThaumcraftCapabilities.knowsResearch((EntityPlayer)event.getEntity(), "f_arrow") && event.getSource().getImmediateSource() instanceof EntityBetweenstonePebble) {
                knowledge.addResearch("f_arrow");
                knowledge.sync((EntityPlayerMP)event.getEntity());
                ((EntityPlayer)event.getEntity()).sendStatusMessage(new TextComponentString(TextFormatting.DARK_PURPLE + I18n.translateToLocal("got.projectile")), true);
            }

            if (!ThaumcraftCapabilities.knowsResearch((EntityPlayer)event.getEntity(), "f_fireball") && event.getSource().getImmediateSource() instanceof EntityPyradFlame) {
                knowledge.addResearch("f_fireball");
                knowledge.sync((EntityPlayerMP)event.getEntity());
                ((EntityPlayer)event.getEntity()).sendStatusMessage(new TextComponentString(TextFormatting.DARK_PURPLE + I18n.translateToLocal("got.projectile")), true);
            }

            if (!ThaumcraftCapabilities.knowsResearch((EntityPlayer)event.getEntity(), "f_spit") && event.getSource().getImmediateSource() instanceof EntitySapSpit) {
                knowledge.addResearch("f_spit");
                knowledge.sync((EntityPlayerMP)event.getEntity());
                ((EntityPlayer)event.getEntity()).sendStatusMessage(new TextComponentString(TextFormatting.DARK_PURPLE + I18n.translateToLocal("got.projectile")), true);
            }
        }
    }


    private static void giveDreamJournal(EntityPlayer player) {
        IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);
        knowledge.addResearch("!gotdream");
        knowledge.sync((EntityPlayerMP)player);
        ItemStack book = ConfigItems.startBook.copy();
        book.getTagCompound().setString("author", player.getName());
        if (!player.inventory.addItemStackToInventory(book)) {
            InventoryUtils.dropItemAtEntity(player.world, book, player);
        }

        try {
            player.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE + I18n.translateToLocal("got.dream")));
        } catch (Exception var4) {
        }
    }


    @SubscribeEvent
    public void onStageUnlocked(GameStageEvent.Added event) {
        if(event.getStageName().equals("knowledge_of_decay")) {
            IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(event.getEntityPlayer());

            knowledge.addResearch("!gotcrystals");
            knowledge.sync((EntityPlayerMP)event.getEntityPlayer());

            if (ModConfig.CONFIG_MISC.noSleep && !knowledge.isResearchKnown("!gotdream")) {
                giveDreamJournal(event.getEntityPlayer());
            }
        } else if(event.getStageName().equals("knowledge_of_technology")) {
            IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(event.getEntityPlayer());

            knowledge.addResearch("!unlockedta");
            knowledge.sync((EntityPlayerMP)event.getEntityPlayer());
        }
    }


    @SubscribeEvent
    public static void BonemealEvent(BonemealEvent event) {
        EntityPlayer player = event.getEntityPlayer();

        if(player != null && event.getHand() != null && player.getHeldItem(event.getHand()) != ItemStack.EMPTY && player.getHeldItem(event.getHand()).getItem() == new ItemStack(Items.DYE, 1, 15).getItem()) {
            event.setCanceled(true);
        }
    }


    /*
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if(event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;

            if(!player.getEntityData().hasKey("free_thaumcraft_research")) {
                IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);

                for (Map.Entry<String, ResearchCategory> entry : ResearchCategories.researchCategories.entrySet()) {
                    if(entry.getKey().equals("THAUMIC_AUGMENTATION") || entry.getKey().equals("MECHANICS") || entry.getKey().equals("PERIPHERY")) {
                        continue;
                    }

                    ResearchCategory tempCategory = entry.getValue();

                    int value = knowledge.getKnowledgeRaw(IPlayerKnowledge.EnumKnowledgeType.THEORY, tempCategory);
                    knowledge.addKnowledge(IPlayerKnowledge.EnumKnowledgeType.THEORY, tempCategory, Math.max(0, 40 - value));

                    value = knowledge.getKnowledgeRaw(IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, tempCategory);
                    knowledge.addKnowledge(IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, tempCategory, Math.max(0, 20 - value));
                }

                player.getEntityData().setBoolean("free_thaumcraft_research", true);
            }
        }
    }

     */


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
        } else if(block == BlockRegistry.WAYSTONE && ConfigBLAdditions.configAutoMapping.AutoMapMenhir && event.getState() == event.getState().withProperty(BlockWaystone.PART, BlockWaystone.Part.BOTTOM)) {
            if(!event.getWorld().isRemote) {
                AtlasAPI.getMarkerAPI().putGlobalMarker(event.getWorld(), false, CommonProxy.MARKER_MENHIR.toString(), "Menhir", event.getPos().getX(), event.getPos().getZ());
            }
        }
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBottleUsed(PlayerInteractEvent.RightClickBlock event) throws NoSuchFieldException, IllegalAccessException {
        World world = event.getWorld();

        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = event.getItemStack();

        if(stack == ItemStack.EMPTY) {
            return;
        }

        TileEntity te = world.getTileEntity(event.getPos());

        // to prevent emptying bottles with water into containers
        if(FluidUtil.getFluidContained(stack) != null && FluidUtil.getFluidContained(stack).getFluid() != null) {
            if (FluidUtil.getFluidContained(stack).getFluid() == net.minecraftforge.fluids.FluidRegistry.WATER && !(te instanceof TileCampfire)) {
                event.setCanceled(true);
            }
        }

        if (stack.getItem() instanceof ItemGlassBottle) {
            IFluidHandler fluidHandler = FluidUtil.getFluidHandler(world, event.getPos(), null);

            if(fluidHandler == null) {
                return;
            }

            FluidStack tankContents = fluidHandler.drain(250, false);

            if (tankContents != null && (tankContents.getFluid() == FluidRegistry.CLEAN_WATER) && tankContents.amount >= 250) {
                if (!world.isRemote) {
                    IBlockState state = world.getBlockState(event.getPos());
                    ItemStack filledBottle = new ItemStack(SDItems.purifiedWaterBottle);

                    if (!player.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }

                    if (!player.inventory.addItemStackToInventory(filledBottle)) {
                        ForgeHooks.onPlayerTossEvent(player, filledBottle, false);
                    }

                    fluidHandler.drain(250, true);
                    world.notifyBlockUpdate(event.getPos(), state, state, 3);
                }

                world.playSound(null, event.getPos(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 0.75F, 2.0F);

                return;
            }
        } else if(stack.getItem() instanceof ItemCanteen) {
            IFluidHandler fluidHandler = FluidUtil.getFluidHandler(world, event.getPos(), null);

            if(fluidHandler == null) {
                return;
            }

            ItemCanteen canteen = ((ItemCanteen)stack.getItem());
            int doses = canteen.getDoses(stack);
            int maxDoses = canteen.getMaxDoses((stack));
            int additionalDoses = 0;

            if(canteen.getThirstEnum(stack) != ThirstEnum.PURIFIED && doses > 0 || doses == maxDoses) {
                return;
            }

            boolean filled = false;

            for(int i = 0; i < maxDoses - doses; i++) {
                FluidStack tankContents = fluidHandler.drain(250 * (i + 1), false);

                if (tankContents != null && (tankContents.getFluid() == FluidRegistry.CLEAN_WATER) && tankContents.amount >= 250 * (i + 1)) {
                    filled = true;
                    additionalDoses++;
                }
            }

            if(filled) {
                if (!world.isRemote) {
                    IBlockState state = world.getBlockState(event.getPos());

                    if (canteen.getThirstEnum(stack) != ThirstEnum.PURIFIED) {
                        canteen.setCanteenEmpty(stack);
                        stack.setTagInfo("CanteenType", new NBTTagInt(ThirstEnum.PURIFIED.ordinal()));
                    }

                    ((ItemCanteen)stack.getItem()).setDoses(stack, doses + additionalDoses);

                    fluidHandler.drain(250 * additionalDoses, true);
                    world.notifyBlockUpdate(event.getPos(), state, state, 3);
                }

                world.playSound(null, event.getPos(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 0.75F, 2.0F);
            }

            return;
        }

        if(te instanceof TileCampfire && stack.getItem() == ItemRegistry.BL_BUCKET && FluidUtil.getFluidContained(stack) != null && FluidUtil.getFluidContained(stack).getFluid() == FluidRegistry.SWAMP_WATER) {
            TileCampfire campfire = (TileCampfire) te;

            if(campfire.workerIsActive()) {
                Field field = TileCampfire.class.getDeclaredField("extinguishedByRain");
                field.setAccessible(true);
                boolean b = field.getBoolean(campfire);
                b = true;

                campfire.workerSetActive(false);

                player.setHeldItem(event.getHand(), new ItemStack(ItemRegistry.BL_BUCKET, 1, stack.getMetadata()));

                if (!world.isRemote) {
                    SoundHelper.playSoundServer(world, te.getPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS);
                }
            }
        }

        /*
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
         */
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
                    canAttachTo = ObfuscationReflectionHelper.findMethod(BlockLadder.class, "func_193392_c", Boolean.TYPE, World.class, BlockPos.class, EnumFacing.class);
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
    public void onItemUse(PlayerInteractEvent.RightClickItem event) {
        if(!event.getWorld().isRemote) {
            addSanityForItemUse(event.getEntityPlayer(), event.getWorld(), event.getItemStack());
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
                        EnumFacing facing = copyState.getValue(BlockLadder.FACING);

                        if (canAttachTo(block, world, posDown, facing)) {
                            world.setBlockState(posDown, copyState);
                            world.playSound(null, posDown.getX(), posDown.getY(), posDown.getZ(), SoundEvents.BLOCK_LADDER_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);

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

        if(entity == null || !(event.getPotionEffect().getPotion() instanceof PotionThaumcraftResearch))
            return;

        Collection<PotionEffect> effects = entity.getActivePotionEffects();

        for(PotionEffect effect : effects) {
            if(effect.getPotion() instanceof PotionThaumcraftResearch && effect.getPotion().getRegistryName() != event.getPotionEffect().getPotion().getRegistryName()) {
                entity.removePotionEffect(effect.getPotion());
                return;
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
        List<Entity> l = EntityUtils.getEntitiesInRange(world, player.getPosition(), null, Entity.class, 10.0D);

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

                    for(Block wellnessBlock : CommonProxy.WELLNESS_BLOCKS.keySet()) {
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

        if (state.getBlock() instanceof IBlockIgnitableWithIgniterItem && event.getItemStack() != ItemStack.EMPTY) {
            if(event.getItemStack().getItem() == ItemRegistry.OCTINE_INGOT) {
                ((IBlockIgnitableWithIgniterItem) state.getBlock()).igniteWithIgniterItem(world, pos, state, event.getFace());
                world.playSound(null, event.getPos().getX(), event.getPos().getY() + 0.5D, event.getPos().getZ(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
            } else if(event.getItemStack().getItem() instanceof ItemCaster || event.getItemStack().getItem() instanceof ItemTieredCasterGauntlet) {
                FocusPackage focusPackage;

                if(event.getItemStack().getItem() instanceof ItemCaster) {
                    ItemCaster caster = (ItemCaster) event.getItemStack().getItem();
                    ItemStack focusStack = caster.getFocusStack(event.getItemStack());
                    focusPackage = ItemFocus.getPackage(focusStack);

                    if(!caster.consumeVis(event.getItemStack(), event.getEntityPlayer(), caster.getFocus(event.getItemStack()).getVisCost(focusStack), false, true)) {
                        return;
                    }
                } else {
                    ItemTieredCasterGauntlet caster = (ItemTieredCasterGauntlet) event.getItemStack().getItem();
                    ItemStack focusStack = caster.getFocusStack(event.getItemStack());
                    focusPackage = ItemFocus.getPackage(focusStack);

                    if(!caster.consumeVis(event.getItemStack(), event.getEntityPlayer(), ((ItemFocus)caster.getFocus(event.getItemStack())).getVisCost(focusStack), false, true)) {
                        return;
                    }
                }

                if(focusPackage == null) {
                    return;
                }

                FocusEffect[] focusEffects = focusPackage.getFocusEffects();

                for(FocusEffect effect : focusEffects) {
                    if(effect instanceof FocusEffectFire) {
                        ((IBlockIgnitableWithIgniterItem) state.getBlock()).igniteWithIgniterItem(world, pos, state, event.getFace());
                        world.playSound(null, event.getPos().getX(), event.getPos().getY() + 0.5D, event.getPos().getZ(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                        break;
                    }
                }
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
    public void onPlayerDeath(PlayerEvent.PlayerRespawnEvent event) {
        if(event.player == null) {
            return;
        }

        SanityCapability cap = event.player.getCapability(SanityCapability.INSTANCE, null);

        if(cap == null) {
            return;
        }

        cap.increaseSanity(-20);
    }


    private void addSanityForItemUse(EntityPlayer player, World world, ItemStack stack) {
        SanityCapability cap = player.getCapability(SanityCapability.INSTANCE, (EnumFacing)null);

        if(cap == null) {
            return;
        }

        List<SanityModifier> mods = Sanity.getModifierValues("items");

        for (SanityModifier mod : mods) {
            if (stack.getItem().getRegistryName().toString().equals(mod.value)) {
                cap.increaseSanity(mod.amount);
                return;
            }
        }
    }


    private void addSanityForPotions(EntityPlayer player, World world, PotionEffect potion) {
        SanityCapability cap = player.getCapability(SanityCapability.INSTANCE, (EnumFacing)null);

        if(cap == null) {
            return;
        }

        List<SanityModifier> mods = Sanity.getModifierValues("potions");

        for (SanityModifier mod : mods) {
            if (potion.getPotion().getRegistryName().toString().equals(mod.value)) {
                cap.increaseSanity(mod.amount);
                return;
            }
        }
    }


    private void addSanityForEvents(EntityPlayer player, World world) {
        List<SanityModifier> mods = Sanity.getModifierValues("bl_events");

        if(mods.isEmpty()) {
            return;
        }

        SanityCapability cap = player.getCapability(SanityCapability.INSTANCE, (EnumFacing)null);

        if(cap == null) {
            return;
        }

        BetweenlandsWorldStorage storage = BetweenlandsWorldStorage.forWorld(world);
        List<IEnvironmentEvent> activeEvents = storage.getEnvironmentEventRegistry().getActiveEvents();

        for(IEnvironmentEvent activeEvent : activeEvents) {
            String eventName = activeEvent.getEventName().getPath();

            for (SanityModifier mod : mods) {
                if (eventName.equals(mod.value)) {
                    switch(eventName) {
                        case "auroras":
                        case "rift":
                            if(!world.canSeeSky(player.getPosition())) {
                                continue;
                            }
                            break;
                    }

                    if(mod.amount > 0 && cap.getSanityExact() + mod.amount > 10) {
                        continue;
                    }

                    cap.increaseSanity(mod.amount);
                }
            }
        }
    }


    private void addSanityForCaves(EntityPlayer player, World world) {
        float y = player.getPosition().getY();
        float multiplier = 1f - Math.max(0, Math.min(1.1f, y / (float)80));

        List<SanityModifier> mods = Sanity.getModifierValues("misc");

        if(mods.isEmpty()) {
            return;
        }

        SanityCapability cap = player.getCapability(SanityCapability.INSTANCE, (EnumFacing)null);

        if(cap == null) {
            return;
        }

        for (SanityModifier mod : mods) {
            if (mod.value.equals("depth")) {
                float newVal = mod.amount * multiplier;

                if(newVal > 0 && newVal + cap.getSanityExact() >= 0) {
                    continue;
                }

                cap.increaseSanity(newVal);
            }
        }
    }


    private void addSanityForWellness(EntityPlayer player, float wellness) {
        List<SanityModifier> mods = Sanity.getModifierValues("misc");

        if(mods.isEmpty()) {
            return;
        }

        SanityCapability cap = player.getCapability(SanityCapability.INSTANCE, (EnumFacing)null);

        if(cap == null) {
            return;
        }

        for (SanityModifier mod : mods) {
            if (mod.value.equals("wellness")) {
                cap.increaseSanity(mod.amount * (1f - wellness));
            }
        }
    }


    private void addSanityForBeingSmelly(EntityPlayer player) {
        IRotSmellCapability smellyCap = (IRotSmellCapability)player.getCapability(CapabilityRegistry.CAPABILITY_ROT_SMELL, (EnumFacing)null);

        if(smellyCap == null || !smellyCap.isSmellingBad()) {
            return;
        }

        List<SanityModifier> mods = Sanity.getModifierValues("misc");

        if(mods.isEmpty()) {
            return;
        }

        SanityCapability cap = player.getCapability(SanityCapability.INSTANCE, (EnumFacing)null);

        if(cap == null) {
            return;
        }

        for (SanityModifier mod : mods) {
            if (mod.value.equals("smelly")) {
                cap.increaseSanity(mod.amount);
            }
        }
    }


    @SubscribeEvent
    public static void adjustFoodExhaustingForSanity(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.getEntityWorld().isRemote) {
            EntityPlayer player = event.player;

            SanityCapability cap = player.getCapability(SanityCapability.INSTANCE, (EnumFacing)null);

            if(cap == null) {
                return;
            }

            float foodReduction = cap.getSanityExact() / 10000f;
            foodReduction = MathHelper.clamp(foodReduction, -0.005f, 0.005f);

            if (player.getFoodStats().foodExhaustionLevel > -20 && player.getFoodStats().foodExhaustionLevel < 20) {
                player.getFoodStats().addExhaustion(-foodReduction);
            }
        }
    }


    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            return;
        }

        EntityPlayer player = event.player;
        World world = player.world;

        // check if we have a bonus saved
        // if not, run it once and save it
        // if we do, only check it if check interval passed + we triggered the next chance
        // remove wellness nbt from player when potion runs out

        if(!world.isRemote && player.ticksExisted % 40 == 0) {
            addSanityForEvents(player, world);
            addSanityForCaves(player, world);
            addSanityForBeingSmelly(player);

            // When facing up in the rain, player slowly recovers thirst.
            final double angle = player.getLookVec().y;

            if (angle >= 0.8) {
                IThirstCapability capability = SDCapabilities.getThirstData(player);
                BetweenlandsWorldStorage storage = BetweenlandsWorldStorage.forWorld(world);

                if (storage != null && capability.getThirstLevel() < 20) {
                    List<IEnvironmentEvent> activeEvents = storage.getEnvironmentEventRegistry().getActiveEvents();

                    for(IEnvironmentEvent activeEvent : activeEvents) {
                        String eventName = activeEvent.getEventName().getPath();

                        if(eventName.equals("heavy_rain")) {
                            ThirstUtil.takeDrink(player, 1, 1, 0);
                        }
                    }
                }
            }

            Collection<PotionEffect> effects = player.getActivePotionEffects();

            float totalWellnessBonus = 0;
            float activeWellnessBonuses = 0;

            for(PotionEffect effect : effects) {
                addSanityForPotions(player, world, effect);

                if(effect.getPotion() instanceof PotionThaumcraftResearch) {
                    PotionThaumcraftResearch researchPotion = (PotionThaumcraftResearch) effect.getPotion();

                    int wellnessInterval = ConfigBLAdditions.configTea.TCPotionChance;

                    float wellness = checkPlayerWellness(world, player, effects, researchPotion.Category);

                    if(!player.getEntityData().hasKey("wellnessBonus")) {
                        wellnessInterval = Math.round(wellnessInterval * wellness);
                    }

                    int wellnessChanceRequired = (effect.getAmplifier() + 1) * Math.round((float)ConfigBLAdditions.configTea.TCPotionChance * 0.03f);
                    int nextCheck = world.rand.nextInt(wellnessInterval);

                    if(nextCheck > wellnessChanceRequired) {
                        continue;
                    }

                    if(!ConfigBLAdditions.configTea.RequiresBookAndQuill || player.inventory.hasItemStack(new ItemStack(ItemsTC.thaumonomicon)) && hasScribingTools(player)) {
                        int oProg = IPlayerKnowledge.EnumKnowledgeType.OBSERVATION.getProgression();
                        int tProg = IPlayerKnowledge.EnumKnowledgeType.THEORY.getProgression();

                        if(world.rand.nextInt(100) <= 25) {
                            ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, ResearchCategories.getResearchCategory("BASICS"), MathHelper.getInt(player.getRNG(), oProg / 2, oProg));
                            ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.THEORY, ResearchCategories.getResearchCategory("BASICS"), MathHelper.getInt(player.getRNG(), tProg / 3, tProg / 2));
                        }

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

            if(activeWellnessBonuses > 0) {
                totalWellnessBonus /= activeWellnessBonuses;
                addSanityForWellness(player, Math.min(1, totalWellnessBonus));
            }
        }
    }


    @SubscribeEvent
    public void explodySulfur(BlockEvent.BreakEvent event) {
        if(event.getWorld().isRemote || event.getPlayer() == null || ConfigBLAdditions.configGeneral.SulfurExplosionDamage == -1)
            return;

        IBlockState state = event.getState();

        if(state.getBlock() == BlockRegistry.SULFUR_ORE) {
            ItemStack pick = event.getPlayer().getHeldItem(EnumHand.MAIN_HAND);

            if(pick != ItemStack.EMPTY && pick.getItem() == ItemRegistry.OCTINE_PICKAXE && event.getWorld().rand.nextInt(ConfigBLAdditions.configGeneral.SulfurExplosionChance) == 0) {
                event.getWorld().createExplosion(null, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), ConfigBLAdditions.configGeneral.SulfurExplosionDamage, true);
            }
        }
    }


    @SubscribeEvent
    public void placedTorch(BlockEvent.PlaceEvent event) {
        if(event.getPlacedBlock().getBlock() instanceof BlockSulfurTorchExtinguished) {
            if(event.getPlayer().inventory.hasItemStack(new ItemStack(ItemRegistry.OCTINE_INGOT))) {
                event.getWorld().setBlockState(event.getPos(), BlockRegistry.SULFUR_TORCH.getStateFromMeta(event.getPlacedBlock().getBlock().getMetaFromState(event.getPlacedBlock())));
            }
        }
    }


    @SubscribeEvent
    public void onEDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        World world = event.getEntity().getEntityWorld();

        int ItemPos;
        int i;
        if (!world.isRemote && (LootTableHandler.PARASITE_HOSTLIST.contains(EntityList.getKey(entity)) && ((new Random()).nextInt(100) < Modconfig.pSpawnRate_Parasite || EntityParasite.gotParasite(entity.getPassengers()) != null) || event.getEntityLiving().isPotionActive(ModMobEffects.INFESTED))) {
            ItemPos = 3 + (new Random()).nextInt(3);
            i = 0;
            EntityParasite passenger = EntityParasite.gotParasite(entity.getPassengers());
            if (event.getEntityLiving().isPotionActive(ModMobEffects.INFESTED)) {
                i = event.getEntityLiving().getActivePotionEffect(ModMobEffects.INFESTED).getAmplifier();
            }

            for(int var3 = 0; var3 < ItemPos + (i - 1) * (1 + (new Random()).nextInt(3)); ++var3) {
                float var4 = ((float)(var3 % 2) - 0.5F) / 4.0F;
                float var5 = ((float)(var3 / 2) - 0.5F) / 4.0F;
                EntityParasite entityparasite = new EntityParasite(world);
                if (passenger != null) {
                    entityparasite.setSkin(passenger.getSkin());
                } else if (BiomeDictionary.hasType(world.getBiome(entity.getPosition()), BiomeDictionary.Type.DRY)) {
                    entityparasite.setSkin(1);
                } else if (!BiomeDictionary.hasType(world.getBiome(entity.getPosition()), BiomeDictionary.Type.JUNGLE) && !(event.getSource().getTrueSource() instanceof EntityVespa)) {
                    entityparasite.setSkin(0);
                } else {
                    entityparasite.setSkin(2);
                }

                entityparasite.setLocationAndAngles(entity.posX + (double)var4, entity.posY + 1.0, entity.posZ + (double)var5, entity.rotationYaw, entity.rotationPitch);
                world.spawnEntity(entityparasite);
            }
        }
    }


    @SubscribeEvent
    public void tcCrystal(BlockEvent.BreakEvent event) {
        if(event.getWorld().isRemote || event.getPlayer() == null)
            return;

        IBlockState state = event.getState();

        if(state.getBlock() instanceof BlockCrystal) {
            ItemStack pick = event.getPlayer().getHeldItem(EnumHand.MAIN_HAND);

            if(pick != ItemStack.EMPTY &&
                    pick.getItem() != ItemRegistry.BONE_PICKAXE &&
                    pick.getItem() != ItemRegistry.WEEDWOOD_PICKAXE &&
                    pick.getItem() != ModItems.living_pickaxe) {
                BlockCrystal crystalBlock = (BlockCrystal)state.getBlock();
                int count = crystalBlock.getGrowth(state) + 1;

                for(int i = 0; i < count; ++i) {
                    InventoryHelper.spawnItemStack(event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), ThaumcraftApiHelper.makeCrystal(crystalBlock.aspect));
                }
            }
        }
    }


    @SubscribeEvent()
    public static void onEvent(BlockEvent event) {
        if(event.getWorld().provider.getDimension() != 0) {
            if (event.getState().getBlock() == Blocks.WATER) {
                event.getWorld().setBlockState(event.getPos(), BlockRegistry.SWAMP_WATER.getDefaultState());
            } else if (event.getState().getBlock() == Blocks.LAVA) {
                event.getWorld().setBlockState(event.getPos(), BlockRegistry.TAR.getDefaultState());
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
                        event.player.world.playSound(null, event.player.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT, 1.0F, 1.0F);
                    }
                }
            }
        }
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onAnadiaHit(LivingHurtEvent event) {
        if(event.getEntity().world.isRemote || !(event.getEntityLiving() instanceof EntityAnadia)) {
            return;
        }

        Entity trueSource = event.getSource().getTrueSource();
        Entity damageSource = event.getSource().getImmediateSource();

        if(trueSource instanceof EntityPlayer && !(damageSource instanceof EntityFishingSpear)) {
            ((EntityPlayer)trueSource).sendStatusMessage(new TextComponentString("This fish is too slippery to hit without throwing a fishing spear!"), true);
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public void onRightClickKiln(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos hitPos = event.getPos();
        ItemStack itemHeld = event.getItemStack();

        if(itemHeld == ItemStack.EMPTY || itemHeld.getItem() != Item.getItemFromBlock(BlockRegistry.THATCH) || !(world.getBlockState(hitPos).getBlock() instanceof BlockKilnPit)) {
            return;
        }

        if (!world.isRemote && world.getBlockState(hitPos).getValue(BlockKilnPit.VARIANT) == BlockKilnPit.EnumType.EMPTY) {
            itemHeld.setCount(itemHeld.getCount() - 1);
            world.setBlockState(hitPos, ModuleTechBasic.Blocks.KILN_PIT.getDefaultState().withProperty(BlockKilnPit.VARIANT, BlockKilnPit.EnumType.THATCH));
            world.playSound(null, hitPos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            event.setCanceled(true);
        }
    }


    /*
    @SubscribeEvent
    public void onRightClickCampfire(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos hitPos = event.getPos();
        EntityPlayer player = event.getEntityPlayer();
        ItemStack itemHeld = event.getItemStack();

        if(itemHeld == ItemStack.EMPTY || itemHeld.getItem() != Item.getItemFromBlock(BlockRegistry.SULFUR_TORCH_EXTINGUISHED) || !(world.getBlockState(hitPos).getBlock() instanceof BlockCampfire)) {
            return;
        }

        if (!world.isRemote && world.getBlockState(hitPos).getValue(BlockCampfire.VARIANT) == BlockCampfire.EnumType.LIT) {
            itemHeld.setCount(itemHeld.getCount() - 1);
            player.addItemStackToInventory(new ItemStack(Item.getItemFromBlock(BlockRegistry.SULFUR_TORCH)));
            world.playSound((EntityPlayer)null, hitPos, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.BLOCKS, 1.0F, 1.0F);
            event.setCanceled(true);
        }
    }
     */


    @SubscribeEvent
    public void preTooltipRender (WailaRenderEvent.Pre event) {
        EntityPlayer player = event.getAccessor().getPlayer();

        if(player.world.provider.getDimension() == -2) {
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public void getTooltipText (WailaTooltipEvent event) {
        EntityPlayer player = event.getAccessor().getPlayer();
        boolean hasGogglesOfRevealing = false;

        for (ItemStack armor : player.getArmorInventoryList()) {
            if (armor.getItem() instanceof ItemGoggles) {
                hasGogglesOfRevealing = true;
                break;
            }
        }

        // check for baubles too
        if(!hasGogglesOfRevealing && BaublesApi.isBaubleEquipped(player, ItemsTC.goggles) > -1) {
            hasGogglesOfRevealing = true;
        }

        if(!hasGogglesOfRevealing) {
            boolean isFirst = false;

            for (final Iterator<String> iterator = event.getCurrentTip().iterator(); iterator.hasNext(); ) {
                final String line = iterator.next();

                if(!isFirst) {
                    isFirst = true;
                    continue;
                }

                iterator.remove();
            }
        }
    }
}
